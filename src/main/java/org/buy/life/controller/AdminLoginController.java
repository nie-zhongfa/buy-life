package org.buy.life.controller;

import org.buy.life.model.request.AdminLoginRequest;
import org.buy.life.service.IBuyAdminService;
import org.buy.life.utils.JSONData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 9:53 PM
 * I am a code man ^_^ !!
 */
@RestController
@RequestMapping("/admin")
public class AdminLoginController {

    @Resource
    private IBuyAdminService buyAdminService;

    /**
     * 管理端登录
     *
     * @param adminLoginRequest
     * @return
     */
    @PostMapping("/login")
    public JSONData<String> login(@RequestBody AdminLoginRequest adminLoginRequest) {
        String token = buyAdminService.login(adminLoginRequest.getUsername(), adminLoginRequest.getPassword());
        return JSONData.success(token);
    }
}
