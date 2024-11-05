package org.buy.life.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.buy.life.entity.BuyCategoryEntity;
import org.buy.life.entity.BuySeriesEntity;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.exception.BusinessException;
import org.buy.life.mapper.BuySeriesMapper;
import org.buy.life.model.dto.ImportCategoryDto;
import org.buy.life.model.dto.ImportSeriesInfoDto;
import org.buy.life.model.enums.LangEnum;
import org.buy.life.model.request.AdminSeriesRequest;
import org.buy.life.model.request.CategoryName;
import org.buy.life.model.request.SeriesName;
import org.buy.life.model.response.AdminSeriesResponse;
import org.buy.life.service.IAdminCategoryService;
import org.buy.life.service.IAdminSeriesService;
import org.buy.life.service.IAdminSkuService;
import org.buy.life.utils.excel.ExcelReadImageUtil;
import org.buy.life.utils.excel.ExcelUtil;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 系列 服务实现类
 * </p>
 *
 * @author MrWu
 * @since 2024-11-04
 */
@Service
public class AdminSeriesServiceImpl extends ServiceImpl<BuySeriesMapper, BuySeriesEntity> implements IAdminSeriesService {

    @Resource
    private IAdminCategoryService iAdminCategoryService;
    @Resource
    private IAdminSkuService iAdminSkuService;

    /**
     * 查询类别列表
     * @param adminSeriesRequest
     * @return
     */
    @Override
    public SimplePage<AdminSeriesResponse> querySeriesPage(AdminSeriesRequest adminSeriesRequest) {
        Page<BuySeriesEntity> seriesPage = getSeriesPage(adminSeriesRequest);
        if (seriesPage == null || CollectionUtils.isEmpty(seriesPage.getRecords())) {
            return SimplePage.emptyPage();
        }
        SimplePage<AdminSeriesResponse> pageInfo = BeanUtil.copyProperties(seriesPage, SimplePage.class);
        List<AdminSeriesResponse> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(seriesPage.getRecords())) {
            List<String> cateGoryCodeList = seriesPage.getRecords().stream().map(BuySeriesEntity::getCategoryCode).collect(Collectors.toList());
            List<BuyCategoryEntity> categoryList = iAdminCategoryService.getCategoryListByCode(cateGoryCodeList);
            Map<String, BuyCategoryEntity> categoryEntityMap = categoryList.stream().collect(Collectors.toMap(BuyCategoryEntity::getCategoryCode, contract -> contract, (a, b) -> a));
            seriesPage.getRecords().forEach(r -> {
                AdminSeriesResponse adminSeriesResponse = BeanUtil.copyProperties(r, AdminSeriesResponse.class);
                BuyCategoryEntity buyCategoryEntity = categoryEntityMap.get(r.getCategoryCode());
                if (buyCategoryEntity != null) {
                    String categoryName = CategoryName.getCategoryName(buyCategoryEntity.getCategoryName(), LangEnum.ZH_CN.getCode());
                    adminSeriesResponse.setCategoryName(categoryName);
                }
                String seriesName = SeriesName.getSeriesName(r.getSeriesName(), LangEnum.ZH_CN.getCode());
                adminSeriesResponse.setSeriesName(seriesName);
                responses.add(adminSeriesResponse);
            });
        }
        pageInfo.setList(responses);
        return pageInfo;

    }

    public Page<BuySeriesEntity> getSeriesPage(AdminSeriesRequest adminSeriesRequest) {
        Page<BuySeriesEntity> page = new Page<>(adminSeriesRequest.getPageNum(), adminSeriesRequest.getPageSize());
        return lambdaQuery()
                .eq(BuySeriesEntity::getClassification, adminSeriesRequest.getClassification())
                .eq(BuySeriesEntity::getIsDeleted, false)
                .orderByDesc(BuySeriesEntity::getMtime)
                .page(page);
    }
    public List<BuySeriesEntity> getSeriesByCode(String cateGoryCode, String seriesCode) {
        return lambdaQuery()
                .eq(BuySeriesEntity::getCategoryCode, cateGoryCode)
                .eq(BuySeriesEntity::getSeriesCode, seriesCode)
                .eq(BuySeriesEntity::getIsDeleted, false)
                .orderByDesc(BuySeriesEntity::getMtime)
                .list();
    }

    @Override
    public List<BuySeriesEntity> getSeriesListByCode(List<String> seriesCodeList) {
        List<BuySeriesEntity> list = lambdaQuery()
                .in(BuySeriesEntity::getSeriesCode, seriesCodeList)
                .eq(BuySeriesEntity::getIsDeleted, false)
                .orderByDesc(BuySeriesEntity::getMtime)
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public void importSeriesInfo(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            List<ImportSeriesInfoDto> doReadSync = EasyExcelFactory.read(file.getInputStream()).head(ImportSeriesInfoDto.class).sheet().doReadSync();
            if (CollectionUtils.isEmpty(doReadSync)) {
                return;
            }
            ExcelReadImageUtil.readImage(inputStream, doReadSync);
            List<BuySeriesEntity> buySeriesEntityList = new ArrayList<>();
            for (ImportSeriesInfoDto seriesInfoDto : doReadSync) {

                String img = iAdminSkuService.uploadImg(seriesInfoDto.getZh_cn() + seriesInfoDto.getImgSuffix(), seriesInfoDto.getFile());

                BuySeriesEntity buySeriesEntity = new BuySeriesEntity();
                buySeriesEntity.setClassification(seriesInfoDto.getIp());
                buySeriesEntity.setSeriesCode(seriesInfoDto.getSeriesCode());
                buySeriesEntity.setCategoryCode(seriesInfoDto.getCategoryCode());

                buySeriesEntity.setCover(img);

                List<SeriesName> seriesNames = new ArrayList<>();
                SeriesName.buildSeriesNameList(seriesInfoDto, seriesNames);
                buySeriesEntity.setSeriesName(JSON.toJSONString(seriesNames));

                List<BuySeriesEntity> seriesEntityList = getSeriesByCode(seriesInfoDto.getSeriesCode(), seriesInfoDto.getCategoryCode());
                if (!CollectionUtils.isEmpty(seriesEntityList)) {
                    buySeriesEntity.setId(seriesEntityList.get(0).getId());
                }
                buySeriesEntityList.add(buySeriesEntity);
            }
            this.saveOrUpdateBatch(buySeriesEntityList);
        } catch (Exception ex) {
            log.error("importSeriesInfo fail", ex);
            throw new BusinessException(9999, "导入失败");
        }
    }

    @Override
    public void downloadSeriesTemplate(HttpServletResponse response) {
        ImportSeriesInfoDto seriesInfoDto = ImportSeriesInfoDto.builder()
                .ip("star_rail、genshin_impact、zenless_zone_zero、tears_of_themis(选择其中一个)")
                .seriesCode("1001")
                .categoryCode("1001")
                .zh_cn("原神系列")
                .en("Genshin Impact")
                .file(null)
                .build();
        ExcelUtil.writeExcel(response, "category_template", ImportCategoryDto.class, Arrays.asList(seriesInfoDto));
    }
}
