package org.buy.life.service;

import org.buy.life.entity.BuyCartEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.buy.life.entity.req.BuyCartReq;
import org.buy.life.entity.resp.BuyCartResp;

import java.util.List;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
public interface IBuyCartService extends IService<BuyCartEntity> {

    BuyCartResp cartList();


    void joinCart(BuyCartReq buyCartReq);

    void removeSku(List<String> skus);
}
