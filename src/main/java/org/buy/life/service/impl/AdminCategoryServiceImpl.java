package org.buy.life.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.buy.life.entity.BuyCategoryEntity;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.exception.BusinessException;
import org.buy.life.filter.CurrentAdminUser;
import org.buy.life.mapper.BuyCategoryMapper;
import org.buy.life.model.dto.ImportCategoryDto;
import org.buy.life.model.dto.ImportCategoryInfoDto;
import org.buy.life.model.enums.LangEnum;
import org.buy.life.model.request.*;
import org.buy.life.model.response.AdminCategoryResponse;
import org.buy.life.service.IAdminCategoryService;
import org.buy.life.service.IAdminSkuService;
import org.buy.life.utils.excel.ExcelReadImageUtil;
import org.buy.life.utils.excel.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * <p>
 * 类别 服务实现类
 * </p>
 *
 * @author MrWu
 * @since 2024-11-04
 */
@Service
public class AdminCategoryServiceImpl extends ServiceImpl<BuyCategoryMapper, BuyCategoryEntity> implements IAdminCategoryService {

    @Resource
    private IAdminSkuService iAdminSkuService;
    @Autowired
    @Qualifier("thirdThreadPoolExecutor")
    private ThreadPoolTaskExecutor thirdThreadPoolExecutor;

    /**
     * 查询类别列表
     * @param adminCategoryRequest
     * @return
     */
    @Override
    public SimplePage<AdminCategoryResponse> queryCategoryPage(AdminCategoryRequest adminCategoryRequest) {
        Page<BuyCategoryEntity> categoryPage = getCategoryPage(adminCategoryRequest);
        if (categoryPage == null || CollectionUtils.isEmpty(categoryPage.getRecords())) {
            return SimplePage.emptyPage();
        }
        SimplePage<AdminCategoryResponse> pageInfo = BeanUtil.copyProperties(categoryPage, SimplePage.class);
        List<AdminCategoryResponse> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(categoryPage.getRecords())) {
            categoryPage.getRecords().forEach(r -> {
                AdminCategoryResponse adminCategoryResponse = BeanUtil.copyProperties(r, AdminCategoryResponse.class);
                String categoryName = CategoryName.getCategoryName(r.getCategoryName(), LangEnum.ZH_CN.getCode());
                adminCategoryResponse.setCategoryName(categoryName);
                responses.add(adminCategoryResponse);
            });
        }
        pageInfo.setList(responses);
        return pageInfo;

    }

    @Override
    public List<BuyCategoryEntity> getCategoryListByCode(List<String> categoryCodeList) {
        List<BuyCategoryEntity> list = lambdaQuery()
                .in(BuyCategoryEntity::getCategoryCode, categoryCodeList)
                .eq(BuyCategoryEntity::getIsDeleted, false)
                .orderByDesc(BuyCategoryEntity::getMtime)
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list;
    }

    public Page<BuyCategoryEntity> getCategoryPage(AdminCategoryRequest adminCategoryRequest) {
        Page<BuyCategoryEntity> page = new Page<>(adminCategoryRequest.getPageNum(), adminCategoryRequest.getPageSize());
        return lambdaQuery()
                .eq(BuyCategoryEntity::getClassification, adminCategoryRequest.getClassification())
                .eq(BuyCategoryEntity::getIsDeleted, false)
                .orderByDesc(BuyCategoryEntity::getMtime)
                .page(page);
    }

    @Override
    public void importCategoryInfo(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            List<ImportCategoryInfoDto> doReadSync = EasyExcelFactory.read(file.getInputStream()).head(ImportCategoryInfoDto.class).sheet().doReadSync();
            if (CollectionUtils.isEmpty(doReadSync)) {
                return;
            }
            ExcelReadImageUtil.readImage(inputStream, doReadSync);

            List<String> categoryCodeList = doReadSync.stream().map(ImportCategoryInfoDto::getCategoryCode).collect(Collectors.toList());
            List<BuyCategoryEntity> categoryListByCode = getCategoryListByCode(categoryCodeList);
            Map<String, BuyCategoryEntity> categoryEntityMap = categoryListByCode.stream().collect(Collectors.toMap(BuyCategoryEntity::getCategoryCode, c -> c, (a, b) -> a));
            List<BuyCategoryEntity> buyCategoryEntities = new ArrayList<>();

            CountDownLatch latch = new CountDownLatch(doReadSync.size());

            for (ImportCategoryInfoDto categoryDto : doReadSync) {

                CompletableFuture.runAsync(() -> {
                    try {
                        String img = iAdminSkuService.uploadImg(categoryDto.getZh_cn() + categoryDto.getImgSuffix(), categoryDto.getFile());

                        BuyCategoryEntity buyCategoryEntity = new BuyCategoryEntity();
                        buyCategoryEntity.setClassification(categoryDto.getIp());
                        buyCategoryEntity.setCategoryCode(categoryDto.getCategoryCode());
                        buyCategoryEntity.setCover(img);

                        List<CategoryName> categoryNames = new ArrayList<>();
                        CategoryName.buildCategoryNameList(categoryDto, categoryNames);
                        buyCategoryEntity.setCategoryName(JSON.toJSONString(categoryNames));

                        if (categoryEntityMap.get(categoryDto.getCategoryCode()) != null) {
                            BuyCategoryEntity buyCategory = categoryEntityMap.get(categoryDto.getCategoryCode());
                            buyCategoryEntity.setId(buyCategory.getId());
                        }
                        buyCategoryEntities.add(buyCategoryEntity);
                    } finally {
                        latch.countDown();
                    }
                }, thirdThreadPoolExecutor);
            }
            latch.await();
            this.saveOrUpdateBatch(buyCategoryEntities);
        } catch (Exception ex) {
            log.error("importCategoryInfo fail", ex);
            throw new BusinessException(9999, "导入失败");
        }
    }

    @Override
    public void downloadCategoryInfoTemplate(HttpServletResponse response) {
        ImportCategoryInfoDto categoryInfoDto = ImportCategoryInfoDto.builder()
                .ip("star_rail、genshin_impact、zenless_zone_zero、tears_of_themis(选择其中一个)")
                .categoryCode("1001")
                .zh_cn("金属徽章")
                .en("Badges")
                .file(null)
                .build();
        ExcelUtil.writeExcel(response, "sku_category_template", ImportCategoryInfoDto.class, Arrays.asList(categoryInfoDto));
    }
}
