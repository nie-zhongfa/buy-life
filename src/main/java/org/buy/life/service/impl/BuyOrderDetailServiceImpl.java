package org.buy.life.service.impl;

import org.buy.life.entity.BuyOrderDetailEntity;
import org.buy.life.mapper.BuyOrderDetailMapper;
import org.buy.life.service.IBuyOrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@Service
public class BuyOrderDetailServiceImpl extends ServiceImpl<BuyOrderDetailMapper, BuyOrderDetailEntity> implements IBuyOrderDetailService {

    @Override
    public List<BuyOrderDetailEntity> getDetailByOrderId(String orderId) {
        return lambdaQuery().eq(BuyOrderDetailEntity::getOrderId, orderId).eq(BuyOrderDetailEntity::getIsDeleted, false).list();
    }

    @Override
    public List<BuyOrderDetailEntity> getDetailByOrderIds(List<String> orderIds) {
        return lambdaQuery()
                .in(BuyOrderDetailEntity::getOrderId, orderIds)
                .eq(BuyOrderDetailEntity::getIsDeleted, false)
                .list();
    }
}
