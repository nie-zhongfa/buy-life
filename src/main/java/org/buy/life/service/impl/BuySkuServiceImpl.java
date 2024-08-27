package org.buy.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.buy.life.constant.SkuStatusEnum;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.req.BuySkuReq;
import org.buy.life.entity.req.PageBasicReq;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.mapper.BuySkuMapper;
import org.buy.life.service.IBuySkuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;

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


    @Override
    public SimplePage<BuySkuEntity> pageList(PageBasicReq<BuySkuReq> buySkuReq){
        LambdaQueryWrapper<BuySkuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BuySkuEntity::getIsDeleted,0)
                .eq(StringUtils.isNoneBlank(buySkuReq.getCondition().getSkuCategory()),BuySkuEntity::getSkuCategory,buySkuReq.getCondition().getSkuCategory())
                .eq(StringUtils.isNoneBlank(buySkuReq.getCondition().getSkuType()),BuySkuEntity::getSkuType,buySkuReq.getCondition().getSkuType())
                .in(BuySkuEntity::getStatus, Lists.newArrayList(SkuStatusEnum.LISTED))
                .and(StringUtils.isNoneBlank(buySkuReq.getCondition().getKeyWord()),r->r.like(BuySkuEntity::getSkuId,buySkuReq.getCondition().getKeyWord()).
                        or().like(BuySkuEntity::getSkuName,buySkuReq.getCondition().getKeyWord()));

        Page<BuySkuEntity> page = this.page(new Page<>(buySkuReq.getPageNum(), buySkuReq.getPageSize()), wrapper);
        return  getSimplePage(buySkuReq,page);
    }


    private SimplePage<BuySkuEntity> getSimplePage(PageBasicReq<BuySkuReq> buySkuReq,Page<BuySkuEntity> page ) {
        if (page == null) {
            return new SimplePage<>(Collections.emptyList(), buySkuReq.getPageNum(), buySkuReq.getPageSize(), 0L);
        }
        return new SimplePage<>(page.getRecords(), buySkuReq.getPageNum(), buySkuReq.getPageSize(), page.getTotal());
    }
}
