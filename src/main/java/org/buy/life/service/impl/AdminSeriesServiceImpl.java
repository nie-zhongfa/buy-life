package org.buy.life.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.buy.life.entity.BuyCategoryEntity;
import org.buy.life.entity.BuySeriesEntity;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.mapper.BuySeriesMapper;
import org.buy.life.model.enums.LangEnum;
import org.buy.life.model.request.AdminCategoryRequest;
import org.buy.life.model.request.AdminSeriesRequest;
import org.buy.life.model.request.CategoryName;
import org.buy.life.model.request.SeriesName;
import org.buy.life.model.response.AdminCategoryResponse;
import org.buy.life.model.response.AdminSeriesResponse;
import org.buy.life.service.IAdminCategoryService;
import org.buy.life.service.IAdminSeriesService;
import org.buy.life.service.IBuySeriesService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
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
}
