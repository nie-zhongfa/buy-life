package org.buy.life.controller;


import org.buy.life.entity.BuyUserEntity;
import org.buy.life.entity.req.BuyUserReq;
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


    @PostMapping("/create")
    public JSONData<Void> create(@RequestBody BuyUserReq buyUserReq){
        buyUserService.create(buyUserReq);
        return JSONData.success();
    }


    @PostMapping("/reset")
    public JSONData<BuyUserEntity> reset(@RequestBody LoginInfoReq loginInfoReq){
        return JSONData.success(buyUserService.reset(loginInfoReq));
    }


    @GetMapping("/getTokenInfo")
    public JSONData<BuyUserEntity> getTokenInfo(){
        return JSONData.success(buyUserService.findByAccount());
    }

    @PostMapping("/removeToken")
    public JSONData removeToken(@RequestBody LoginInfoReq loginInfoReq){
        buyUserService.delToken(loginInfoReq);
        return JSONData.success();
    }


    @PostMapping("/update")
    public JSONData removeToken(@RequestBody BuyUserReq buyUserReq){
        buyUserService.update(buyUserReq);
        return JSONData.success();
    }

    @PostMapping("/resendPwd")
    public JSONData resendPwd(@RequestBody BuyUserReq buyUserReq){
        buyUserService.resendPwd(buyUserReq);
        return JSONData.success();
    }


}

