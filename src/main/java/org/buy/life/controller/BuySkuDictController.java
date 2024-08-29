package org.buy.life.controller;


import org.buy.life.entity.resp.BuyOrderDetailResp;
import org.buy.life.entity.resp.BuySkuDictResp;
import org.buy.life.service.IBuySkuDictService;
import org.buy.life.utils.JSONData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 枚举表 前端控制器
 * </p>
 *
 * @author MrWu
 * @since 2024-08-27
 */
@Controller
@RequestMapping("/buySkuDict")
public class BuySkuDictController {

    @Resource
    IBuySkuDictService buySkuDictService;

    @GetMapping("/dict")
    public JSONData<BuySkuDictResp> orderList(){
        return JSONData.success(buySkuDictService.getAllDict());
    }

}


