package org.buy.life.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.buy.life.entity.BuyCategoryEntity;
import org.buy.life.entity.BuySeriesEntity;
import org.buy.life.mapper.BuySeriesMapper;
import org.buy.life.service.IBuySeriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<BuySeriesEntity> getSeriesList(String categoryCode) {
        if(StringUtils.isNotEmpty(categoryCode)){
            return lambdaQuery().eq(BuySeriesEntity::getCategoryCode,categoryCode).eq(BuySeriesEntity::getIsDeleted, false).list();
        }
        return lambdaQuery().eq(BuySeriesEntity::getIsDeleted, false).list();
    }
}
