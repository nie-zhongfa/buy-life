package org.buy.life.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.buy.life.constant.RoleEnum;
import org.buy.life.entity.BuyAdminEntity;
import org.buy.life.entity.BuyUserEntity;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.exception.BusinessException;
import org.buy.life.exception.ServerCodeEnum;
import org.buy.life.filter.CurrentAdminUser;
import org.buy.life.mapper.BuyAdminMapper;
import org.buy.life.model.request.QueryAccountRequest;
import org.buy.life.model.request.UpdateAdminAccountRequest;
import org.buy.life.model.response.AccountResponse;
import org.buy.life.model.response.AdminAccountResponse;
import org.buy.life.service.IBuyAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.buy.life.utils.MD5Utils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 管理员信息表 服务实现类
 * </p>
 *
 * @author MrWu
 * @since 2024-08-26
 */
@Slf4j
@Service
public class BuyAdminServiceImpl extends ServiceImpl<BuyAdminMapper, BuyAdminEntity> implements IBuyAdminService {

    @Override
    public String login(String username, String password) {
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(ServerCodeEnum.USERNAME_IS_NULL);
        }
        if (StringUtils.isBlank(password)) {
            throw new BusinessException(ServerCodeEnum.PASSWORD_IS_NULL);
        }
        BuyAdminEntity adminEntity = lambdaQuery().eq(BuyAdminEntity::getUserId, username).one();
        if (adminEntity == null) {
            throw new BusinessException(ServerCodeEnum.USERNAME_OR_PWD_IS_VAIL);
        }
        if (!password.equals(adminEntity.getPwd())) {
            throw new BusinessException(ServerCodeEnum.USERNAME_OR_PWD_IS_VAIL);
        }
        //多人登录的场景，返回相同token
        LocalDateTime lstTokenExpire = adminEntity.getLstTokenExpire();
        String token = adminEntity.getToken();
        //生成token
        if (lstTokenExpire == null || lstTokenExpire.compareTo(LocalDateTime.now()) < 0) {
            token = MD5Utils.encryptMD5(username + password + System.currentTimeMillis());
        }
        //更新token有效期
        lstTokenExpire = LocalDateTime.now().plusMinutes(30);
        lambdaUpdate()
                .set(BuyAdminEntity::getLstTokenExpire, lstTokenExpire)
                .set(BuyAdminEntity::getLstLoginTime, LocalDateTime.now())
                .set(BuyAdminEntity::getToken, token)
                .eq(BuyAdminEntity::getId, adminEntity.getId())
                .update();
        return token;
    }

    @Override
    public BuyAdminEntity getAdmin(String token) {
        BuyAdminEntity adminEntity = lambdaQuery().eq(BuyAdminEntity::getToken, token).one();
        return adminEntity;
    }

    @Override
    public SimplePage<AdminAccountResponse> queryAdminAccountPage(QueryAccountRequest queryAccountRequest) {
        IPage<BuyAdminEntity> accountPage = getAccountPage(queryAccountRequest);
        if (accountPage == null || CollectionUtils.isEmpty(accountPage.getRecords())) {
            return SimplePage.emptyPage();
        }
        SimplePage<AdminAccountResponse> pageInfo = BeanUtil.copyProperties(accountPage, SimplePage.class);
        List<AdminAccountResponse> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(accountPage.getRecords())) {
            accountPage.getRecords().forEach(r -> {
                AdminAccountResponse accountResponse = BeanUtil.copyProperties(r, AdminAccountResponse.class);
                //角色
                String roleDesc = RoleEnum.getRoleDesc(r.getRole());
                accountResponse.setRoleDesc(roleDesc);
                //可见字段
                if (!StringUtils.isBlank(r.getShowField())) {
                    accountResponse.setFiledList(JSON.parseArray(r.getShowField(), String.class));
                } else {
                    accountResponse.setFiledList(new ArrayList<>());
                }
                responses.add(accountResponse);
            });
        }
        pageInfo.setList(responses);
        return pageInfo;
    }

    public IPage<BuyAdminEntity> getAccountPage(QueryAccountRequest queryAccountRequest) {
        return lambdaQuery()
                .eq(BuyAdminEntity::getIsDeleted, false)
                .orderByDesc(BuyAdminEntity::getMtime)
                .page(new Page<>(queryAccountRequest.getPageNum(), queryAccountRequest.getPageSize()));
    }

    /**
     * 修改密码
     *
     * @param updateAdminAccountRequest
     */
    @Override
    public void updatePwd(UpdateAdminAccountRequest updateAdminAccountRequest) {
        String userId = CurrentAdminUser.getUserId();
        BuyAdminEntity adminEntity = lambdaQuery().eq(BuyAdminEntity::getUserId, userId).one();
        if (adminEntity == null) {
            throw new BusinessException(9999, "无效账号");
        }
        if (!RoleEnum.SUPER_ADMIN.getRole().equals(adminEntity.getRole())) {
            throw new BusinessException(9999, "非超级管理员不能进行此操作");
        }
        BuyAdminEntity buyAdminEntity = lambdaQuery().eq(BuyAdminEntity::getUserId, updateAdminAccountRequest.getUserId()).one();
        if (RoleEnum.SUPER_ADMIN.getRole().equals(buyAdminEntity.getRole())) {
            if (!buyAdminEntity.getPwd().equals(updateAdminAccountRequest.getOldPwd())) {
                throw new BusinessException(9999, "请输入正确的旧密码");
            }
        }
        lambdaUpdate()
                .set(BuyAdminEntity::getPwd, JSON.toJSONString(updateAdminAccountRequest.getPwd()))
                .eq(BuyAdminEntity::getUserId, updateAdminAccountRequest.getUserId())
                .update();
    }

    /**
     * 配置表头
     *
     * @param updateAdminAccountRequest
     */
    @Override
    public void updateField(UpdateAdminAccountRequest updateAdminAccountRequest) {
        String userId = CurrentAdminUser.getUserId();
        BuyAdminEntity adminEntity = lambdaQuery().eq(BuyAdminEntity::getUserId, userId).one();
        if (adminEntity == null) {
            throw new BusinessException(9999, "无效账号");
        }
        if (!RoleEnum.SUPER_ADMIN.getRole().equals(adminEntity.getRole())) {
            throw new BusinessException(9999, "非超级管理员不能进行此操作");
        }
        lambdaUpdate()
                .set(BuyAdminEntity::getShowField, JSON.toJSONString(updateAdminAccountRequest.getFiledList()))
                .eq(BuyAdminEntity::getUserId, updateAdminAccountRequest.getUserId())
                .update();
    }

    @Override
    public void addAdminAccount(BuyAdminEntity adminEntity) {
        this.save(adminEntity);
    }

    @Override
    public BuyAdminEntity getAdminInfo(String userId) {
        return lambdaQuery().eq(BuyAdminEntity::getUserId, userId).one();
    }
}
