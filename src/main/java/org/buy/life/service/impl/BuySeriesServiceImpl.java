package org.buy.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.buy.life.entity.BuyCategoryEntity;
import org.buy.life.entity.BuySeriesEntity;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.req.BuySkuReq;
import org.buy.life.entity.req.PageBasicReq;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.mapper.BuySeriesMapper;
import org.buy.life.service.IBuySeriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 系列 服务实现类
 * </p>
 *
 * @author MrWu
 * @since 2024-11-04
 */
@Service
public class BuySeriesServiceImpl extends ServiceImpl<BuySeriesMapper, BuySeriesEntity> implements IBuySeriesService {

    @Override
    public SimplePage<BuySeriesEntity> getSeriesList(PageBasicReq<BuySkuReq> buySkuReq) {
        LambdaQueryWrapper<BuySeriesEntity> seriesWrapper = new LambdaQueryWrapper<>();
        seriesWrapper.eq(BuySeriesEntity::getIsDeleted, false);
        if(Objects.nonNull(buySkuReq.getCondition())&&StringUtils.isNotEmpty(buySkuReq.getCondition().getCategoryCode())){
            seriesWrapper.eq(BuySeriesEntity::getCategoryCode,buySkuReq.getCondition().getCategoryCode());
        }
        Page<BuySeriesEntity> page = this.page(new Page<>(buySkuReq.getPageNum(), buySkuReq.getPageSize()), seriesWrapper);
        return getSimplePage(buySkuReq,page);
    }

    private SimplePage<BuySeriesEntity> getSimplePage(PageBasicReq<BuySkuReq> buySkuReq,Page<BuySeriesEntity> page ) {
        if (page == null) {
            return new SimplePage<>(Collections.emptyList(), buySkuReq.getPageNum(), buySkuReq.getPageSize(), 0L);
        }
        return new SimplePage<>(page.getRecords(), buySkuReq.getPageNum(), buySkuReq.getPageSize(), page.getTotal());
    }
}
