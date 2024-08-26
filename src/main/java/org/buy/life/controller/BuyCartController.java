package org.buy.life.controller;


import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.req.BuyCartReq;
import org.buy.life.entity.req.BuySkuReq;
import org.buy.life.entity.req.PageBasicReq;
import org.buy.life.entity.resp.BuyCartResp;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.service.IBuyCartService;
import org.buy.life.utils.JSONData;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@RestController
@RequestMapping("/buyCart")
public class BuyCartController {

    @Resource
    private IBuyCartService buyCartService;

    @PostMapping("/joinCart")
    public JSONData joinCart(@RequestBody BuyCartReq req){
        buyCartService.joinCart(req);
        return JSONData.success();
    }

    @GetMapping("/cartList")
    public JSONData<BuyCartResp> cartList(){
        return JSONData.success(buyCartService.cartList());
    }
}

