package org.buy.life.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.buy.life.constant.SkuStatusEnum;
import org.buy.life.entity.BuyCategoryEntity;
import org.buy.life.entity.BuySeriesEntity;
import org.buy.life.entity.BuySkuDictEntity;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.exception.BusinessException;
import org.buy.life.filter.CurrentAdminUser;
import org.buy.life.mapper.BuySkuMapper;
import org.buy.life.model.dto.ImportCategoryDto;
import org.buy.life.model.dto.ImportSkuDto;
import org.buy.life.model.enums.CurrencyEnum;
import org.buy.life.model.enums.LangEnum;
import org.buy.life.model.request.*;
import org.buy.life.model.response.AdminSkuResponse;
import org.buy.life.service.*;
import org.buy.life.utils.excel.ExcelReadImageUtil;
import org.buy.life.utils.excel.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 2:56 PM
 * I am a code man ^_^ !!
 */
@Slf4j
@Service
public class AdminSkuServiceImpl extends ServiceImpl<BuySkuMapper, BuySkuEntity> implements IAdminSkuService {

    @Resource
    private IAdminFileService adminFileService;
    @Resource
    private IBuySkuDictService buySkuDictService;
    @Resource
    private BuySkuMapper buySkuMapper;
    @Autowired
    @Qualifier("thirdThreadPoolExecutor")
    private ThreadPoolTaskExecutor thirdThreadPoolExecutor;

    @Autowired
    @Qualifier("uploadThirdThreadPoolExecutor")
    private ThreadPoolTaskExecutor uploadThirdThreadPoolExecutor;

    @Resource
    private IAdminCategoryService iAdminCategoryService;
    @Resource
    private IAdminSeriesService iAdminSeriesService;

    @Override
    public SimplePage<AdminSkuResponse> querySkuPage(AdminSkuRequest adminSkuRequest) {
        Page<BuySkuEntity> adminSkuPage = getSkuPage(adminSkuRequest);
        if (adminSkuPage == null || CollectionUtils.isEmpty(adminSkuPage.getRecords())) {
            return SimplePage.emptyPage();
        }
        SimplePage<AdminSkuResponse> pageInfo = BeanUtil.copyProperties(adminSkuPage, SimplePage.class);

        List<AdminSkuResponse> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(adminSkuPage.getRecords())) {
            List<String> categoryCodeList = adminSkuPage.getRecords().stream().map(BuySkuEntity::getCategoryCode).collect(Collectors.toList());
            List<String> seriesCodeList = adminSkuPage.getRecords().stream().map(BuySkuEntity::getSeriesCode).collect(Collectors.toList());

            List<BuyCategoryEntity> categoryListByCode = iAdminCategoryService.getCategoryListByCode(categoryCodeList);
            List<BuySeriesEntity> seriesListByCode = iAdminSeriesService.getSeriesListByCode(seriesCodeList);

            Map<String, BuyCategoryEntity> categoryEntityMap = categoryListByCode.stream().collect(Collectors.toMap(BuyCategoryEntity::getCategoryCode, c -> c, (a, b) -> a));
            Map<String, BuySeriesEntity> seriesEntityMap = seriesListByCode.stream().collect(Collectors.toMap(BuySeriesEntity::getSeriesCode, c -> c, (a, b) -> a));

            adminSkuPage.getRecords().forEach(r -> {
                AdminSkuResponse adminSkuResponse = BeanUtil.copyProperties(r, AdminSkuResponse.class);

                String skuName = SkuName.getSkuName(r.getSkuName(), LangEnum.ZH_CN.getCode());

                String skuType = SkuType.getSkuType(r.getSkuType(), LangEnum.ZH_CN.getCode());

                adminSkuResponse.setSkuName(skuName);
                adminSkuResponse.setSkuCategory(r.getSkuCategory());
                adminSkuResponse.setSkuType(skuType);
                //单价
                adminSkuResponse.setPriceCNY(SkuPrice.getSkuPrice(r.getPrice(), CurrencyEnum.CNY.getCode()));
                adminSkuResponse.setPriceUSD(SkuPrice.getSkuPrice(r.getPrice(), CurrencyEnum.USD.getCode()));
                adminSkuResponse.setPriceEUR(SkuPrice.getSkuPrice(r.getPrice(), CurrencyEnum.EUR.getCode()));
                //零售价
                adminSkuResponse.setRetailPriceCNY(SkuPrice.getSkuPrice(r.getRetailPrice(), CurrencyEnum.CNY.getCode()));
                adminSkuResponse.setRetailPriceUSD(SkuPrice.getSkuPrice(r.getRetailPrice(), CurrencyEnum.USD.getCode()));
                adminSkuResponse.setRetailPriceEUR(SkuPrice.getSkuPrice(r.getRetailPrice(), CurrencyEnum.EUR.getCode()));

                BuyCategoryEntity buyCategoryEntity = categoryEntityMap.get(r.getCategoryCode());
                if (buyCategoryEntity != null) {
                    String categoryName = CategoryName.getCategoryName(buyCategoryEntity.getCategoryName(), LangEnum.ZH_CN.getCode());
                    adminSkuResponse.setCategoryName(categoryName);
                }
                BuySeriesEntity buySeriesEntity = seriesEntityMap.get(r.getSeriesCode());
                if (buySeriesEntity != null) {
                    String seriesName = SeriesName.getSeriesName(buySeriesEntity.getSeriesName(), LangEnum.ZH_CN.getCode());
                    adminSkuResponse.setSeriesName(seriesName);
                }

                responses.add(adminSkuResponse);
            });
        }
        pageInfo.setList(responses);
        return pageInfo;
    }

    @Override
    public void importSku(MultipartFile file) {
        log.info("开始导入商品信息， 文件大小：{}", file.getSize() / (1024.0 * 1024.0) + "M");
        try {
            long t = System.currentTimeMillis();
            log.info("importSku start time ~~~~");
            InputStream inputStream = file.getInputStream();
            List<ImportSkuDto> doReadSync = EasyExcelFactory.read(file.getInputStream()).head(ImportSkuDto.class).sheet().doReadSync();
            if (CollectionUtils.isEmpty(doReadSync)) {
                return;
            }
            ExcelReadImageUtil.readImage(inputStream, doReadSync);

            List<String> skuIdList = doReadSync.stream().map(ImportSkuDto::getSkuId).distinct().collect(Collectors.toList());
            List<BuySkuEntity> skuList = lambdaQuery().in(BuySkuEntity::getSkuId, skuIdList).eq(BuySkuEntity::getIsDeleted, false).list();
            Map<String, BuySkuEntity> skuEntityMap = skuList.stream().collect(Collectors.toMap(BuySkuEntity::getSkuId, c -> c, (a, b) -> a));

            syncNewUpload(doReadSync, skuEntityMap);

            log.info("importSku end time ~~~~:{}", System.currentTimeMillis() - t);
        } catch (Exception ex) {
            log.error("importSku fail", ex);
            throw new BusinessException(9999, "导入失败");
        }
    }

    private void syncNewUpload(List<ImportSkuDto> doReadSync, Map<String, BuySkuEntity> skuEntityMap) {
        List<BuySkuEntity> buySkuEntities = new ArrayList<>();
        for (ImportSkuDto importSkuDto : doReadSync) {
            List<SkuPrice> prices = new ArrayList<>();
            SkuPrice.buildPriceList(importSkuDto, prices);

            List<SkuPrice> retailPrices = new ArrayList<>();
            SkuPrice.buildRetailPriceList(importSkuDto, retailPrices);

            List<SkuType> skuTypes = new ArrayList<>();
            SkuType.buildSkuTypeList(importSkuDto, skuTypes);

            List<SkuName> skuNames = new ArrayList<>();
            SkuName.buildSkuNameList(importSkuDto, skuNames);

            BuySkuEntity buySkuEntity = BeanUtil.copyProperties(importSkuDto, BuySkuEntity.class);
            buySkuEntity.setSkuName(JSON.toJSONString(skuNames));
            buySkuEntity.setPrice(JSON.toJSONString(prices));
            buySkuEntity.setRetailPrice(JSON.toJSONString(retailPrices));
            buySkuEntity.setSkuType(JSON.toJSONString(skuTypes));
//            buySkuEntity.setBatchKey(fileUrl);
            buySkuEntity.setStatus(SkuStatusEnum.UPLOADING.getCode());
            buySkuEntity.setCreator(CurrentAdminUser.getUserId());
            buySkuEntity.setUpdater(CurrentAdminUser.getUserId());
            buySkuEntity.setClassification(importSkuDto.getClassification());
            buySkuEntity.setSeriesCode(importSkuDto.getSeriesCode());
            buySkuEntity.setCategoryCode(importSkuDto.getCategoryCode());

            BuySkuEntity buySku = skuEntityMap.get(importSkuDto.getSkuId());

            if (buySku != null) {
                buySkuEntity.setId(buySku.getId());
                buySkuEntity.setCreator(buySku.getCreator());
            }
            buySkuEntities.add(buySkuEntity);
        }
        this.saveOrUpdateBatch(buySkuEntities);

        //异步上传图片
        CompletableFuture.runAsync(() -> {
            for (ImportSkuDto importSkuDto : doReadSync) {
                CompletableFuture.runAsync(() -> {
                    try {
                        long t1 = System.currentTimeMillis();
                        log.info("img start upload ~~~");
                        //上传图片
                        String fileUrl = uploadImg(importSkuDto.getSkuNameZh_cn() + importSkuDto.getImgSuffix(), importSkuDto.getFile());
                        log.info("img end upload ~~~ :{}", System.currentTimeMillis() - t1);
                        lambdaUpdate()
                                .eq(BuySkuEntity::getSkuId, importSkuDto.getSkuId())
                                .set(BuySkuEntity::getBatchKey, fileUrl)
                                .set(BuySkuEntity::getStatus, importSkuDto.getSkuStatus())
                                .update();
                    } catch (Exception ex) {
                        log.error("上传图片失败，upload img error", ex);
                    }
                }, thirdThreadPoolExecutor);
            }
        });
    }

    private void cfUpload(List<List<ImportSkuDto>> doReadSyncs, Map<String, BuySkuEntity> skuEntityMap) {
        try {
            AtomicInteger counter = new AtomicInteger(0);
            for (List<ImportSkuDto> doReadSync : doReadSyncs) {
                long t1 = System.currentTimeMillis();
                log.info("cfUpload start upload ~~~");
                if (counter.incrementAndGet() % 2 > 0) {
                    syncUpload(doReadSync, skuEntityMap, uploadThirdThreadPoolExecutor);
                } else {
                    syncUpload(doReadSync, skuEntityMap, thirdThreadPoolExecutor);
                }
                //上传图片
                log.info("cfUpload end upload ~~~ :{}", System.currentTimeMillis() - t1);
            }
        } catch (Exception ex) {
            log.error("cfUpload importSku fail", ex);
            throw new BusinessException(9999, "导入失败");
        }
    }

    private void syncUpload(List<ImportSkuDto> doReadSync, Map<String, BuySkuEntity> skuEntityMap, ThreadPoolTaskExecutor thirdThreadPoolExecutor) {
        try {
            List<BuySkuEntity> buySkuEntities = new ArrayList<>();
            CountDownLatch latch = new CountDownLatch(doReadSync.size());
            for (ImportSkuDto importSkuDto : doReadSync) {
                CompletableFuture.runAsync(() -> {
                    try {
                        long t1 = System.currentTimeMillis();
                        log.info("start upload ~~~");
                        //上传图片
                        String fileUrl = uploadImg(importSkuDto.getSkuNameZh_cn() + importSkuDto.getImgSuffix(), importSkuDto.getFile());
                        log.info("end upload ~~~ :{}", System.currentTimeMillis() - t1);

                        List<SkuPrice> prices = new ArrayList<>();
                        SkuPrice.buildPriceList(importSkuDto, prices);

                        List<SkuPrice> retailPrices = new ArrayList<>();
                        SkuPrice.buildRetailPriceList(importSkuDto, retailPrices);

                        List<SkuType> skuTypes = new ArrayList<>();
                        SkuType.buildSkuTypeList(importSkuDto, skuTypes);

                        List<SkuName> skuNames = new ArrayList<>();
                        SkuName.buildSkuNameList(importSkuDto, skuNames);

                        BuySkuEntity buySkuEntity = BeanUtil.copyProperties(importSkuDto, BuySkuEntity.class);
                        buySkuEntity.setSkuName(JSON.toJSONString(skuNames));
                        buySkuEntity.setPrice(JSON.toJSONString(prices));
                        buySkuEntity.setRetailPrice(JSON.toJSONString(retailPrices));
                        buySkuEntity.setSkuType(JSON.toJSONString(skuTypes));
                        buySkuEntity.setBatchKey(fileUrl);
                        buySkuEntity.setStatus(importSkuDto.getSkuStatus());
                        buySkuEntity.setCreator(CurrentAdminUser.getUserId());
                        buySkuEntity.setUpdater(CurrentAdminUser.getUserId());
                        buySkuEntity.setClassification(importSkuDto.getClassification());
                        buySkuEntity.setSeriesCode(importSkuDto.getSeriesCode());
                        buySkuEntity.setCategoryCode(importSkuDto.getCategoryCode());

                        BuySkuEntity buySku = skuEntityMap.get(importSkuDto.getSkuId());

                        if (buySku != null) {
                            buySkuEntity.setId(buySku.getId());
                            buySkuEntity.setCreator(buySku.getCreator());
                        }
                        buySkuEntities.add(buySkuEntity);
                    } finally {
                        latch.countDown();
                    }
                }, thirdThreadPoolExecutor);
            }
            latch.await();
            this.saveOrUpdateBatch(buySkuEntities);
        } catch (Exception ex) {
            log.error("importSku fail", ex);
            throw new BusinessException(9999, "导入失败");
        }
    }



    public Page<BuySkuEntity> getSkuPage(AdminSkuRequest adminSkuRequest) {
        Page<BuySkuEntity> page = new Page<>(adminSkuRequest.getPageNum(), adminSkuRequest.getPageSize());
        return lambdaQuery()
                .eq(BuySkuEntity::getClassification, adminSkuRequest.getClassification())
                .in(!CollectionUtils.isEmpty(adminSkuRequest.getStatus()), BuySkuEntity::getStatus, adminSkuRequest.getStatus())
                .eq(BuySkuEntity::getIsDeleted, false)
                .orderByDesc(BuySkuEntity::getMtime)
                .page(page);
    }

    @Override
    public String uploadImg(String fileName, InputStream file) {
        if (file == null) {
            return null;
        }
        try {
            MultipartFile imgFile = new MockMultipartFile(fileName, fileName, "application/octet-stream", file);
            return adminFileService.uploadFile(imgFile);
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return null;
    }

    @Override
    public List<BuySkuEntity> getSkuBySkuIdList(List<String> skuIds) {
        return lambdaQuery()
                .in(BuySkuEntity::getSkuId, skuIds)
                .eq(BuySkuEntity::getIsDeleted, false)
                .list();
    }

    @Override
    public BuySkuEntity getSkuBySkuId(String skuId) {
        return lambdaQuery()
                .eq(BuySkuEntity::getSkuId, skuId)
                .eq(BuySkuEntity::getIsDeleted, false)
                .one();
    }

    @Override
    public boolean updateStock(Long id, Long stock) {
        int i = this.buySkuMapper.updateStock(id, stock);
        return i > 0;
    }

    @Override
    public void importCategory(MultipartFile file) {
        try {
            List<ImportCategoryDto> doReadSync = EasyExcelFactory.read(file.getInputStream()).head(ImportCategoryDto.class).sheet().doReadSync();
            if (CollectionUtils.isEmpty(doReadSync)) {
                return;
            }
            List<BuySkuDictEntity> skuDictList = buySkuDictService.getSkuDictList();
            Map<String, List<BuySkuDictEntity>> skuDictMap = skuDictList.stream().collect(Collectors.groupingBy(BuySkuDictEntity::getCode));
            List<BuySkuDictEntity> list = new ArrayList<>();
            for (ImportCategoryDto categoryDto : doReadSync) {
                buildCategory(list, LangEnum.ZH_CN.getCode(), categoryDto.getZh_cn(), categoryDto, skuDictMap);
                buildCategory(list, LangEnum.EN.getCode(), categoryDto.getEn(), categoryDto, skuDictMap);
            }
            buySkuDictService.saveOrUpdateBatch(list);
        } catch (Exception ex) {
            log.error("importCategory fail", ex);
            throw new BusinessException(9999, "导入失败");
        }
    }

    @Override
    public void upAndSownShelves(UpAndDownSkuRequest upAndDownSkuRequest) {
        lambdaUpdate()
                .set(BuySkuEntity::getStatus, upAndDownSkuRequest.getStatus())
                .eq(BuySkuEntity::getSkuId, upAndDownSkuRequest.getSkuId())
                .update();
    }

    @Override
    public void downloadSkuTemplate(HttpServletResponse response) {
        ImportSkuDto importSkuDto = ImportSkuDto.builder()
                .skuId("6976068148784")
                .classification("star_rail、genshin_impact、zenless_zone_zero、tears_of_themis(选择其中一个)")
                .skuCategory("2007(需提前导入品类，传品类编码)")
                .skuTypeZh_cn("多人")
                .skuTypeEn("Multiple people")
                .skuNameZh_cn("发车倒计时系列镭射卡套组7枚入")
                .skuNameEn("Departure countdown series laser card set with 7 pieces")
                .costPrice("1.00")
                .priceCNY("1.00")
                .priceUSD("1.00")
                .priceEUR("1.00")
                .retailPriceCNY("1.00")
                .retailPriceUSD("1.00")
                .retailPriceEUR("1.00")
                .file(null)
                .stock("1000")
                .skuStatus("LISTED、REMOVED(上架、下架选择其中一个)")
                .build();
        ExcelUtil.writeExcel(response, "sku_template", ImportSkuDto.class, Arrays.asList(importSkuDto));
    }

    @Override
    public void downloadCategoryTemplate(HttpServletResponse response) {
        ImportCategoryDto categoryDto = ImportCategoryDto.builder()
                .categoryCode("1001")
                .ip("star_rail、genshin_impact、zenless_zone_zero、tears_of_themis(选择其中一个)")
                .zh_cn("金属徽章/相卡")
                .en("Badges/Photo Cards")
                .build();
        ExcelUtil.writeExcel(response, "category_template", ImportCategoryDto.class, Arrays.asList(categoryDto));
    }

    private void buildCategory(List<BuySkuDictEntity> list,
                                           String lang,
                                           String skuCategory,
                                           ImportCategoryDto categoryDto,
                                           Map<String, List<BuySkuDictEntity>> skuDictMap) {
        BuySkuDictEntity buySkuDictEntity = new BuySkuDictEntity();
        buySkuDictEntity.setCode(categoryDto.getCategoryCode().trim());
        buySkuDictEntity.setSkuCategory(skuCategory.trim());
        buySkuDictEntity.setLang(lang);
        buySkuDictEntity.setTitle(categoryDto.getIp().trim());
        buySkuDictEntity.setCreator(CurrentAdminUser.getUserId());
        buySkuDictEntity.setUpdater(CurrentAdminUser.getUserId());
        List<BuySkuDictEntity> entity = skuDictMap.get(categoryDto.getCategoryCode().trim());
        if (entity != null) {
            List<BuySkuDictEntity> dictEntities = entity.stream().filter(e -> e.getLang().equals(lang)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(dictEntities)) {
                buySkuDictEntity.setId(dictEntities.get(0).getId());
                buySkuDictEntity.setCreator(dictEntities.get(0).getCreator());
            }
        }
        list.add(buySkuDictEntity);
    }

    public static void main(String[] args) {
        System.out.println(4%2);
    }
}
