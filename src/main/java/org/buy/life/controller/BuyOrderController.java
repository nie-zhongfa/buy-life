package org.buy.life.controller;


import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.req.BuyCartReq;
import org.buy.life.entity.req.BuyOrderDetailReq;
import org.buy.life.entity.resp.BuyCartResp;
import org.buy.life.entity.resp.BuyOrderDetailResp;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.service.IBuyOrderService;
import org.buy.life.utils.JSONData;
import org.buy.life.utils.TtlUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@RestController
@RequestMapping("/buyOrder")
public class BuyOrderController {

    @Resource
    private IBuyOrderService buyOrderService;


    @PostMapping("/joinOrder")
    public JSONData<String> joinOrder(@RequestBody BuyOrderDetailReq req){
        return JSONData.success(buyOrderService.joinOrder(req));
    }

    @GetMapping("/orderList")
    public JSONData<List<BuyOrderDetailResp>> orderList(){
        return JSONData.success(buyOrderService.orderList());
    }


    @GetMapping("/orderDetail")
    public JSONData<BuyOrderDetailResp> orderDetail(@RequestParam("orderId") String orderId){
        String userId = TtlUtils.getSPCtx().getUserId();
        return JSONData.success(buyOrderService.orderDetail(orderId, userId));
    }
}

