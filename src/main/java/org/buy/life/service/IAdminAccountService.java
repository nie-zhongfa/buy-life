package org.buy.life.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.buy.life.entity.BuyUserEntity;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.model.request.QueryAccountRequest;
import org.buy.life.model.request.UpdateAccountRequest;
import org.buy.life.model.response.AccountResponse;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 2:55 PM
 * I am a code man ^_^ !!
 */
public interface IAdminAccountService extends IService<BuyUserEntity> {

    SimplePage<AccountResponse> queryAccountPage(QueryAccountRequest queryAccountRequest);

    void updateAccount(UpdateAccountRequest updateAccountRequest);
}
