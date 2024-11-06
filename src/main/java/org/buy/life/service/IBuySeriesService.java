package org.buy.life.service;

import org.buy.life.entity.BuySeriesEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.buy.life.entity.req.BuySkuReq;
import org.buy.life.entity.req.PageBasicReq;
import org.buy.life.entity.resp.SimplePage;

import java.util.List;

/**
 * <p>
 * 系列 服务类
 * </p>
 *
 * @author MrWu
 * @since 2024-11-04
 */
public interface IBuySeriesService extends IService<BuySeriesEntity> {

    SimplePage<BuySeriesEntity> getSeriesList(PageBasicReq<BuySkuReq> buySkuReqPageBasicReq);
}
