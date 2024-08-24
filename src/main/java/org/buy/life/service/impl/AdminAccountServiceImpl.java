package org.buy.life.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.buy.life.entity.BuyUserEntity;
import org.buy.life.mapper.BuyUserMapper;
import org.buy.life.model.request.AdminAccountRequest;
import org.buy.life.model.response.AccountResponse;
import org.buy.life.service.IAdminAccountService;
import org.springframework.stereotype.Service;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 2:56 PM
 * I am a code man ^_^ !!
 */
@Slf4j
@Service
public class AdminAccountServiceImpl extends ServiceImpl<BuyUserMapper, BuyUserEntity> implements IAdminAccountService {

    @Override
    public PageInfo<AccountResponse> queryAccountPage(AdminAccountRequest adminAccountRequest) {

        return null;
    }
}
