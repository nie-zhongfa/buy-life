package org.buy.life.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.buy.life.constant.UserStatusEnum;
import org.buy.life.entity.BuyUserEntity;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.filter.CurrentAdminUser;
import org.buy.life.mapper.BuyUserMapper;
import org.buy.life.model.request.QueryAccountRequest;
import org.buy.life.model.request.UpdateAccountRequest;
import org.buy.life.model.response.AccountResponse;
import org.buy.life.service.IAdminAccountService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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
    public SimplePage<AccountResponse> queryAccountPage(QueryAccountRequest queryAccountRequest) {
        IPage<BuyUserEntity> accountPage = getAccountPage(queryAccountRequest);
        if (accountPage == null || CollectionUtils.isEmpty(accountPage.getRecords())) {
            return SimplePage.emptyPage();
        }
        SimplePage<AccountResponse> pageInfo = BeanUtil.copyProperties(accountPage, SimplePage.class);
        List<AccountResponse> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(accountPage.getRecords())) {
            accountPage.getRecords().forEach(r -> {
                AccountResponse accountResponse = BeanUtil.copyProperties(r, AccountResponse.class);
                responses.add(accountResponse);
            });
        }
        pageInfo.setList(responses);
        return pageInfo;
    }

    @Override
    public void updateAccount(UpdateAccountRequest updateAccountRequest) {
        BuyUserEntity buyUserEntity = BeanUtil.copyProperties(updateAccountRequest, BuyUserEntity.class);
        buyUserEntity.setUpdater(CurrentAdminUser.getUserId());
        this.updateById(buyUserEntity);
    }

    @Override
    public void confirmRegister(String userId) {
       lambdaUpdate()
               .set(BuyUserEntity::getStatus, UserStatusEnum.COMPLETE.getCode())
               .eq(BuyUserEntity::getUserId, userId)
               .update();
    }

    public IPage<BuyUserEntity> getAccountPage(QueryAccountRequest queryAccountRequest) {
        return lambdaQuery()
                .eq(BuyUserEntity::getIsDeleted, false)
                .orderByDesc(BuyUserEntity::getMtime)
                .page(new Page<>(queryAccountRequest.getPageNum(), queryAccountRequest.getPageSize()));
    }
}
