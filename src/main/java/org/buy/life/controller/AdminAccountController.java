package org.buy.life.controller;

import org.buy.life.entity.BuyAdminEntity;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.model.request.QueryAccountRequest;
import org.buy.life.model.request.UpdateAccountRequest;
import org.buy.life.model.request.UpdateAdminAccountRequest;
import org.buy.life.model.response.AccountResponse;
import org.buy.life.model.response.AdminAccountResponse;
import org.buy.life.service.IAdminAccountService;
import org.buy.life.service.IBuyAdminService;
import org.buy.life.utils.JSONData;
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
    @Resource
    private IBuyAdminService iBuyAdminService;

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

    /**
     * 查询系统后台账号列表
     *
     * @param queryAccountRequest
     * @return
     */
    @PostMapping("/queryAdminPage")
    public JSONData<SimplePage<AdminAccountResponse>> queryAdminAccountPage(@RequestBody QueryAccountRequest queryAccountRequest) {
        SimplePage<AdminAccountResponse> pageInfo = iBuyAdminService.queryAdminAccountPage(queryAccountRequest);
        return JSONData.success(pageInfo);
    }

    /**
     * 修改密码
     *
     * @param updateAdminAccountRequest
     * @return
     */
    @PostMapping("/updatePwd")
    public JSONData<Void> updatePwd(@RequestBody UpdateAdminAccountRequest updateAdminAccountRequest) {
        iBuyAdminService.updatePwd(updateAdminAccountRequest);
        return JSONData.success();
    }

    /**
     * 配置表头
     *
     * @param updateAdminAccountRequest
     * @return
     */
    @PostMapping("/updateField")
    public JSONData<Void> updateField(@RequestBody UpdateAdminAccountRequest updateAdminAccountRequest) {
        iBuyAdminService.updateField(updateAdminAccountRequest);
        return JSONData.success();
    }

    /**
     * 添加系统账号
     *
     * @param adminEntity
     * @return
     */
    @PostMapping("/addAdminAccount")
    public JSONData<Void> addAdminAccount(@RequestBody BuyAdminEntity adminEntity) {
        iBuyAdminService.addAdminAccount(adminEntity);
        return JSONData.success();
    }
}
