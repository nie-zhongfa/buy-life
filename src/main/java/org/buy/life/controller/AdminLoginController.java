package org.buy.life.controller;

import org.buy.life.model.request.AdminLoginRequest;
import org.buy.life.model.request.AdminSkuRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 9:53 PM
 * I am a code man ^_^ !!
 */
@RestController
@RequestMapping("/admin")
public class AdminLoginController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AdminLoginRequest adminLoginRequest) {
        return ResponseEntity.ok("");
    }
}
