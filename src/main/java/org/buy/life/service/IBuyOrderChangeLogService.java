package org.buy.life.service;

import org.buy.life.entity.BuyOrderChangeLogEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单修改记录表 服务类
 * </p>
 *
 * @author MrWu
 * @since 2024-09-02
 */
public interface IBuyOrderChangeLogService extends IService<BuyOrderChangeLogEntity> {

    List<BuyOrderChangeLogEntity> getByOrderId(String orderId);
}
