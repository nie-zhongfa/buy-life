package org.buy.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.Objects;

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
        return  getSimplePage(buySkuReq,page);
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
        return list(wrapper);
    }

}
