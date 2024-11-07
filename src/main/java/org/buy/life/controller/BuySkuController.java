package org.buy.life.controller;


import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.req.BuySkuReq;
import org.buy.life.entity.req.LoginInfoReq;
import org.buy.life.entity.req.PageBasicReq;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.service.IBuySkuService;
import org.buy.life.utils.JSONData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * sku 前端控制器
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@RestController
@RequestMapping("/buySku")
public class BuySkuController {

    @Resource
    private IBuySkuService buySkuService;

    //模糊搜索
    @PostMapping("/skuList")
    public JSONData<SimplePage<BuySkuEntity>> skuList(@RequestBody PageBasicReq<BuySkuReq> req){
        return JSONData.success(buySkuService.pageList(req));
    }

    //根据seriesCode搜索，不分页
    @PostMapping("/skuBySeriesCode")
    public JSONData<List<BuySkuEntity>> skuBySeriesCode(@RequestBody BuySkuReq req){
        return JSONData.success(buySkuService.pageSeriesList(req));
    }


}

