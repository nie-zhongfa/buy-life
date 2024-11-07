package org.buy.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.buy.life.constant.SkuStatusEnum;
import org.buy.life.entity.BuyCategoryEntity;
import org.buy.life.entity.BuySeriesEntity;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.req.BuySkuReq;
import org.buy.life.entity.req.PageBasicReq;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.mapper.BuySkuMapper;
import org.buy.life.service.IBuyCategoryService;
import org.buy.life.service.IBuySeriesService;
import org.buy.life.service.IBuySkuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * sku 服务实现类
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@Service
public class BuySkuServiceImpl extends ServiceImpl<BuySkuMapper, BuySkuEntity> implements IBuySkuService {


    @Resource
    private IBuySeriesService buySeriesService;

    @Resource
    private IBuyCategoryService buyCategoryService;

    @Override
    public SimplePage<BuySkuEntity> pageList(PageBasicReq<BuySkuReq> buySkuReq){
        LambdaQueryWrapper<BuySkuEntity> skuWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(buySkuReq.getCondition().getKeyWord())){
            LambdaQueryWrapper<BuyCategoryEntity> categoryWrapper = new LambdaQueryWrapper<>();
            categoryWrapper.eq(BuyCategoryEntity::getIsDeleted, 0).
                    and(StringUtils.isNoneBlank(buySkuReq.getCondition().getKeyWord()), r -> r.like(BuyCategoryEntity::getCategoryCode, buySkuReq.getCondition().getKeyWord()).
                            or().like(BuyCategoryEntity::getCategoryName, buySkuReq.getCondition().getKeyWord()));
            List<BuyCategoryEntity> categoryEntityList = buyCategoryService.list(categoryWrapper);

            LambdaQueryWrapper<BuySeriesEntity> seriesWrapper = new LambdaQueryWrapper<>();

            seriesWrapper.eq(BuySeriesEntity::getIsDeleted, 0).
                    and(StringUtils.isNoneBlank(buySkuReq.getCondition().getKeyWord()), r -> r.like(BuySeriesEntity::getSeriesCode, buySkuReq.getCondition().getKeyWord()).
                            or().like(BuySeriesEntity::getSeriesName, buySkuReq.getCondition().getKeyWord()));
            List<BuySeriesEntity> buySeriesEntities = buySeriesService.list(seriesWrapper);
            skuWrapper.eq(BuySkuEntity::getIsDeleted, 0).and(r -> r.like(BuySkuEntity::getSkuName, buySkuReq.getCondition().getKeyWord()).
                    or().like(BuySkuEntity::getSkuId, buySkuReq.getCondition().getKeyWord()).or().like(BuySkuEntity::getClassification,buySkuReq.getCondition().getKeyWord()).
                    or().like(BuySkuEntity::getSkuType, buySkuReq.getCondition().getKeyWord()).or().like(BuySkuEntity::getSkuCategory, buySkuReq.getCondition().getKeyWord()).or().
                    in(CollectionUtils.isNotEmpty(categoryEntityList),BuySkuEntity::getCategoryCode,categoryEntityList).or().
                    in(CollectionUtils.isNotEmpty(buySeriesEntities),BuySkuEntity::getSeriesCode,buySeriesEntities));
            Page<BuySkuEntity> page = this.page(new Page<>(buySkuReq.getPageNum(), buySkuReq.getPageSize()), skuWrapper);
            return  getSimplePage(buySkuReq,page);
        }else {
            skuWrapper.eq(BuySkuEntity::getIsDeleted, 0);
        }
        Page<BuySkuEntity> page = this.page(new Page<>(buySkuReq.getPageNum(), buySkuReq.getPageSize()), skuWrapper);
        buildResp(page.getRecords());
        return  getSimplePage(buySkuReq,page);
    }


    private void buildResp(List<BuySkuEntity> buySkuEntities){
        List<String> categoryCode = buySkuEntities.stream().map(BuySkuEntity::getCategoryCode).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(categoryCode)){
            LambdaQueryWrapper<BuyCategoryEntity> categoryWrapper = new LambdaQueryWrapper<>();
            categoryWrapper.eq(BuyCategoryEntity::getIsDeleted, 0).
                    in(BuyCategoryEntity::getCategoryCode,categoryCode);
            List<BuyCategoryEntity> categoryEntityList = buyCategoryService.list(categoryWrapper);
            Map<String, BuyCategoryEntity> categoryMap = ListUtils.emptyIfNull(categoryEntityList).stream().collect(Collectors.toMap(BuyCategoryEntity::getCategoryCode, Function.identity(), (k1, k2) -> k2));
            buySkuEntities.forEach(b->{
                if(!categoryMap.containsKey(b.getCategoryCode())){
                    return;
                }
                b.setCategoryName(categoryMap.get(b.getCategoryCode()).getCategoryName());
                b.setCategoryCover(categoryMap.get(b.getCategoryCode()).getCover());
            });
        }

        List<String> seriesCode = buySkuEntities.stream().map(BuySkuEntity::getSeriesCode).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(seriesCode)){
            LambdaQueryWrapper<BuySeriesEntity> seriesWrapper = new LambdaQueryWrapper<>();
            seriesWrapper.eq(BuySeriesEntity::getIsDeleted, 0).
                    in(BuySeriesEntity::getSeriesCode,seriesCode);
            List<BuySeriesEntity> seriesEntityList = buySeriesService.list(seriesWrapper);
            Map<String, BuySeriesEntity> seriesMap = ListUtils.emptyIfNull(seriesEntityList).stream().collect(Collectors.toMap(BuySeriesEntity::getSeriesCode, Function.identity(), (k1, k2) -> k2));
            buySkuEntities.forEach(b->{
                if(!seriesMap.containsKey(b.getSeriesCode())){
                    return;
                }
                b.setSeriesName(seriesMap.get(b.getSeriesCode()).getSeriesName());
                b.setSeriesCover(seriesMap.get(b.getSeriesCode()).getCover());
            });
        }


    }


    private SimplePage<BuySkuEntity> getSimplePage(PageBasicReq<BuySkuReq> buySkuReq,Page<BuySkuEntity> page ) {
        if (page == null) {
            return new SimplePage<>(Collections.emptyList(), buySkuReq.getPageNum(), buySkuReq.getPageSize(), 0L);
        }
        return new SimplePage<>(page.getRecords(), buySkuReq.getPageNum(), buySkuReq.getPageSize(), page.getTotal());
    }




    @Override
    public List<BuySkuEntity> pageSeriesList(BuySkuReq buySkuReq){
        LambdaQueryWrapper<BuySkuEntity> wrapper = new LambdaQueryWrapper<>();
        if(Objects.isNull(buySkuReq)){
            wrapper.eq(BuySkuEntity::getIsDeleted,0).in(BuySkuEntity::getStatus, Lists.newArrayList(SkuStatusEnum.LISTED.getCode()));
        }else {
            wrapper.eq(BuySkuEntity::getIsDeleted,0)
                    .eq(StringUtils.isNoneBlank(buySkuReq.getSeriesCode()),BuySkuEntity::getSeriesCode,buySkuReq.getSeriesCode())
                    .in(BuySkuEntity::getStatus, Lists.newArrayList(SkuStatusEnum.LISTED.getCode()));
        }
        List<BuySkuEntity> list = list(wrapper);
        buildResp(list);
        return list;
    }


    @Override
    public List<BuySkuEntity>  getBySeriesCodes(List<String> seriesCodes){
        LambdaQueryWrapper<BuySkuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BuySkuEntity::getIsDeleted,0)
                .in(CollectionUtils.isNotEmpty(seriesCodes),BuySkuEntity::getSeriesCode,seriesCodes)
                .in(BuySkuEntity::getStatus, Lists.newArrayList(SkuStatusEnum.LISTED.getCode()));
        return  list(wrapper);
    }

}
