package org.buy.life.service;

import org.buy.life.entity.BuyOrderDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
public interface IBuyOrderDetailService extends IService<BuyOrderDetailEntity> {

    List<BuyOrderDetailEntity> getDetailByOrderId(String orderId);

}
