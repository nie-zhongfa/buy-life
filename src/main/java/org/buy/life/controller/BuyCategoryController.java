package org.buy.life.controller;


import org.buy.life.entity.BuyCategoryEntity;
import org.buy.life.entity.resp.BuyCartResp;
import org.buy.life.service.IBuyCategoryService;
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
 * 类别 前端控制器
 * </p>
 *
 * @author MrWu
 * @since 2024-11-04
 */
@RestController
@RequestMapping("/buyCategory")
public class BuyCategoryController {

    @Resource
    private IBuyCategoryService buyCategoryService;

    //查询ip下的所有类别，如果ip为空则查询所有ip的类别
    @GetMapping("/categoryList")
    public JSONData<List<BuyCategoryEntity>> categoryList(@RequestParam("classification") String classification){
        return JSONData.success(buyCategoryService.getCategoryList(classification));
    }
}

