package org.buy.life.controller;


import org.buy.life.entity.BuyUserEntity;
import org.buy.life.entity.req.LoginInfoReq;
import org.buy.life.service.IBuyUserService;
import org.buy.life.utils.JSONData;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@RestController
@RequestMapping("/buyUser")
public class BuyUserController {

    @Resource
    IBuyUserService buyUserService;

    @PostMapping("/doLogin")
    public JSONData<BuyUserEntity> doLogin(@RequestBody LoginInfoReq loginInfoReq){
        return JSONData.success(buyUserService.doLogin(loginInfoReq));
    }

    @PostMapping("/getTokenInfo")
    public JSONData<BuyUserEntity> getTokenInfo(@RequestBody LoginInfoReq loginInfoReq){
        return JSONData.success(buyUserService.findByAccount(loginInfoReq.getAccount()));
    }

    @PostMapping("/removeToken")
    public JSONData removeToken(@RequestBody LoginInfoReq loginInfoReq){
        buyUserService.delToken(loginInfoReq);
        return JSONData.success();
    }




}

