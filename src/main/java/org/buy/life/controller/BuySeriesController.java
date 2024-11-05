package org.buy.life.controller;


import org.buy.life.entity.BuyCategoryEntity;
import org.buy.life.entity.BuySeriesEntity;
import org.buy.life.service.IBuyCategoryService;
import org.buy.life.service.IBuySeriesService;
import org.buy.life.utils.JSONData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 查询类别下的所有系列，如果系列为空则查询所有系列
     * @param categoryCode
     * @return
     */
    @GetMapping("/seriesList")
    public JSONData<List<BuySeriesEntity>> seriesList(@RequestParam("categoryCode") String categoryCode){
        return JSONData.success(iBuySeriesService.getSeriesList(categoryCode));
    }
}

