package org.buy.life.service.impl;

import org.buy.life.entity.BuyOrderChangeLogEntity;
import org.buy.life.mapper.BuyOrderChangeLogMapper;
import org.buy.life.service.IBuyOrderChangeLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单修改记录表 服务实现类
 * </p>
 *
 * @author MrWu
 * @since 2024-09-02
 */
@Service
public class BuyOrderChangeLogServiceImpl extends ServiceImpl<BuyOrderChangeLogMapper, BuyOrderChangeLogEntity> implements IBuyOrderChangeLogService {

    @Override
    public List<BuyOrderChangeLogEntity> getByOrderId(String orderId) {
        return lambdaQuery()
                .eq(BuyOrderChangeLogEntity::getOrderId, orderId)
                .orderByDesc(BuyOrderChangeLogEntity::getCtime)
                .list();
    }
}
