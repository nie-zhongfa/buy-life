package org.buy.life.controller;

import com.github.pagehelper.PageInfo;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.model.request.QueryAccountRequest;
import org.buy.life.model.request.UpdateAccountRequest;
import org.buy.life.model.response.AccountResponse;
import org.buy.life.service.IAdminAccountService;
import org.buy.life.utils.JSONData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 查询账号列表
     *
     * @param queryAccountRequest
     * @return
     */
    @PostMapping("/queryPage")
    public JSONData<SimplePage<AccountResponse>> queryAccountPage(@RequestBody QueryAccountRequest queryAccountRequest) {
        SimplePage<AccountResponse> pageInfo = iAdminAccountService.queryAccountPage(queryAccountRequest);
        return JSONData.success(pageInfo);
    }

    /**
     * 确认注册完成
     *
     * @param userId
     * @return
     */
    @GetMapping("/confirmRegister")
    public JSONData<Boolean> confirmRegister(@RequestParam("userId") String userId) {
        iAdminAccountService.confirmRegister(userId);
        return JSONData.success(true);
    }

    /**
     * 更新账号信息
     *
     * @param updateAccountRequest
     * @return
     */
    @PostMapping("/update")
    public JSONData<Boolean> updateAccount(@RequestBody UpdateAccountRequest updateAccountRequest) {
        iAdminAccountService.updateAccount(updateAccountRequest);
        return JSONData.success(true);
    }
}
