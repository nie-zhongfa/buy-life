package org.buy.life.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.buy.life.constant.SkuStatusEnum;
import org.buy.life.entity.BuySkuDictEntity;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.exception.BusinessException;
import org.buy.life.filter.CurrentAdminUser;
import org.buy.life.mapper.BuySkuMapper;
import org.buy.life.model.dto.ExportOrderDetailInfoDto;
import org.buy.life.model.dto.ImportCategoryDto;
import org.buy.life.model.dto.ImportSkuDto;
import org.buy.life.model.enums.ClassificationEnum;
import org.buy.life.model.enums.CurrencyEnum;
import org.buy.life.model.enums.LangEnum;
import org.buy.life.model.request.*;
import org.buy.life.model.response.AdminSkuResponse;
import org.buy.life.service.IAdminFileService;
import org.buy.life.service.IAdminSkuService;
import org.buy.life.service.IBuySkuDictService;
import org.buy.life.utils.excel.ExcelReadImageUtil;
import org.buy.life.utils.excel.ExcelUtil;
import org.springframework.mock.web.MockMultipartFile;
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

    @Override
    public SimplePage<AdminSkuResponse> querySkuPage(AdminSkuRequest adminSkuRequest) {
        Page<BuySkuEntity> adminSkuPage = getSkuPage(adminSkuRequest);
        if (adminSkuPage == null || CollectionUtils.isEmpty(adminSkuPage.getRecords())) {
            return SimplePage.emptyPage();
        }
        SimplePage<AdminSkuResponse> pageInfo = BeanUtil.copyProperties(adminSkuPage, SimplePage.class);

        List<AdminSkuResponse> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(adminSkuPage.getRecords())) {
            adminSkuPage.getRecords().forEach(r -> {
                AdminSkuResponse adminSkuResponse = BeanUtil.copyProperties(r, AdminSkuResponse.class);

                String skuName = SkuName.getSkuName(r.getSkuName(), LangEnum.ZH_CN.getCode());

                String skuType = SkuType.getSkuType(r.getSkuType(), LangEnum.ZH_CN.getCode());

                adminSkuResponse.setSkuName(skuName);
                adminSkuResponse.setSkuCategory(r.getSkuCategory());
                adminSkuResponse.setSkuType(skuType);
                adminSkuResponse.setPriceCNY(SkuPrice.getSkuPrice(r.getPrice(), CurrencyEnum.CNY.getCode()));
                adminSkuResponse.setPriceUSD(SkuPrice.getSkuPrice(r.getPrice(), CurrencyEnum.USD.getCode()));
                adminSkuResponse.setPriceEUR(SkuPrice.getSkuPrice(r.getPrice(), CurrencyEnum.EUR.getCode()));

                responses.add(adminSkuResponse);
            });
        }
        pageInfo.setList(responses);
        return pageInfo;
    }

    @Override
    public void importSku(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            List<ImportSkuDto> doReadSync = EasyExcelFactory.read(file.getInputStream()).head(ImportSkuDto.class).sheet().doReadSync();
            ExcelReadImageUtil.readImage(inputStream, doReadSync);
            List<BuySkuEntity> buySkuEntities = new ArrayList<>();

            for (ImportSkuDto importSkuDto : doReadSync) {
                String fileUrl = uploadSkuImg(importSkuDto);

                List<SkuPrice> prices = new ArrayList<>();
                SkuPrice.buildPriceList(importSkuDto, prices);

                List<SkuType> skuTypes = new ArrayList<>();
                SkuType.buildSkuTypeList(importSkuDto, skuTypes);

                List<SkuName> skuNames = new ArrayList<>();
                SkuName.buildSkuNameList(importSkuDto, skuNames);

                BuySkuEntity buySkuEntity = BeanUtil.copyProperties(importSkuDto, BuySkuEntity.class);
                buySkuEntity.setSkuName(JSON.toJSONString(skuNames));
                buySkuEntity.setPrice(JSON.toJSONString(prices));
                buySkuEntity.setSkuType(JSON.toJSONString(skuTypes));
                buySkuEntity.setBatchKey(fileUrl);
                buySkuEntity.setStatus(importSkuDto.getSkuStatus());
                buySkuEntity.setCreator(CurrentAdminUser.getUserId());
                buySkuEntity.setUpdater(CurrentAdminUser.getUserId());
                buySkuEntity.setClassification(importSkuDto.getClassification());

                List<BuySkuEntity> list = lambdaQuery().eq(BuySkuEntity::getSkuId, importSkuDto.getSkuId()).eq(BuySkuEntity::getIsDeleted, false).list();
                if (!CollectionUtils.isEmpty(list)) {
                    buySkuEntity.setId(list.get(0).getId());
                    buySkuEntity.setCreator(list.get(0).getCreator());
                }
                buySkuEntities.add(buySkuEntity);
            }
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
                .eq(BuySkuEntity::getIsDeleted, false)
                .orderByDesc(BuySkuEntity::getMtime)
                .page(page);
    }

    public String uploadSkuImg(ImportSkuDto importSkuDto) {
        try {
            String fileName = importSkuDto.getSkuNameZh_cn() + importSkuDto.getImgSuffix();
            MultipartFile imgFile = new MockMultipartFile(fileName, fileName, "application/octet-stream", importSkuDto.getFile());
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
                .classification("StarRail、GenshinImpact、ZenlessZoneZero(选择其中一个)")
                .skuCategory("2007(需提前导入品类，传品类编码)")
                .skuTypeZh_cn("多人")
                .skuTypeEn("Multiple people")
                .skuNameZh_cn("发车倒计时系列镭射卡套组7枚入")
                .skuNameEn("Departure countdown series laser card set with 7 pieces")
                .costPrice("1.00")
                .priceCNY("1.00")
                .priceUSD("1.00")
                .priceEUR("1.00")
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
                .ip("StarRail、GenshinImpact、ZenlessZoneZero(选择其中一个)")
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

}
