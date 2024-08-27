package org.buy.life.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.BuyUserEntity;
import org.buy.life.entity.req.BuySkuReq;
import org.buy.life.entity.req.PageBasicReq;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.mapper.BuyUserMapper;
import org.buy.life.model.request.QueryAccountRequest;
import org.buy.life.model.request.UpdateAccountRequest;
import org.buy.life.model.response.AccountResponse;
import org.buy.life.service.IAdminAccountService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
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
        Page<BuyUserEntity> accountPage = getAccountPage(queryAccountRequest);
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
        this.updateById(buyUserEntity);
    }

    public Page<BuyUserEntity> getAccountPage(QueryAccountRequest queryAccountRequest) {
        Page<BuyUserEntity> page = new Page<>(queryAccountRequest.getPageNum(), queryAccountRequest.getPageSize());
        return lambdaQuery()
                .eq(BuyUserEntity::getIsDeleted, false)
                .orderByDesc(BuyUserEntity::getMtime)
                .page(page);
    }
}
