package org.buy.life.service;

import org.buy.life.entity.BuySkuEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.buy.life.entity.req.BuySkuReq;
import org.buy.life.entity.req.PageBasicReq;
import org.buy.life.entity.resp.SimplePage;

/**
 * <p>
 * sku 服务类
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
public interface IBuySkuService extends IService<BuySkuEntity> {

    SimplePage<BuySkuEntity> pageList(PageBasicReq<BuySkuReq> buySkuReq);
}
