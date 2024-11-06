package org.buy.life.controller;


import org.buy.life.entity.BuyCategoryEntity;
import org.buy.life.entity.BuySeriesEntity;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.req.BuySkuReq;
import org.buy.life.entity.req.PageBasicReq;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.service.IBuyCategoryService;
import org.buy.life.service.IBuySeriesService;
import org.buy.life.utils.JSONData;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 系列 前端控制器
 * </p>
 *
 * @author MrWu
 * @since 2024-11-04
 */
@RestController
@RequestMapping("/buySeries")
public class BuySeriesController {

    @Resource
    private IBuySeriesService iBuySeriesService;

    //查询类别下的所有系列，如果系列为空则查询所有系列
    @PostMapping("/seriesList")
    public JSONData<SimplePage<BuySeriesEntity>> seriesList(@RequestBody PageBasicReq<BuySkuReq> buySkuReq){
        return JSONData.success(iBuySeriesService.getSeriesList(buySkuReq));
    }
}

