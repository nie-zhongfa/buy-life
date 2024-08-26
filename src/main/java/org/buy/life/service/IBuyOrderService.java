package org.buy.life.service;

import org.buy.life.entity.BuyOrderEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.buy.life.entity.req.BuyOrderDetailReq;
import org.buy.life.entity.resp.BuyOrderDetailResp;

import java.util.List;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
public interface IBuyOrderService extends IService<BuyOrderEntity> {

    String joinOrder(BuyOrderDetailReq req);

    List<BuyOrderDetailResp> orderList();

    BuyOrderDetailResp orderDetail(String orderId);
}
