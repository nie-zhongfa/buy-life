package org.buy.life.controller;

import com.github.pagehelper.PageInfo;
import org.buy.life.model.request.QueryAccountRequest;
import org.buy.life.model.request.UpdateAccountRequest;
import org.buy.life.model.response.AccountResponse;
import org.buy.life.service.IAdminAccountService;
import org.buy.life.utils.JSONData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @menu 账号管理
 * @Author YourJustin
 * @Date 2024/8/24 12:28 PM
 * I am a code man ^_^ !!
 */
@RestController
@RequestMapping("/admin/account")
public class AdminAccountController {

    @Resource
    private IAdminAccountService iAdminAccountService;


    @PostMapping("/queryPage")
    public JSONData<PageInfo<AccountResponse>> queryAccountPage(@RequestBody QueryAccountRequest queryAccountRequest) {
        PageInfo<AccountResponse> pageInfo = iAdminAccountService.queryAccountPage(queryAccountRequest);
        return JSONData.success(pageInfo);
    }

    @PostMapping("/update")
    public JSONData<Boolean> updateAccount(@RequestBody UpdateAccountRequest updateAccountRequest) {
        iAdminAccountService.updateAccount(updateAccountRequest);
        return JSONData.success(true);
    }
}
