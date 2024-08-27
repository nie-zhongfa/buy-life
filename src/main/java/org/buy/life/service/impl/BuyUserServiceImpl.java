package org.buy.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.buy.life.entity.BuyUserEntity;
import org.buy.life.entity.req.LoginInfoReq;
import org.buy.life.exception.BusinessException;
import org.buy.life.exception.ServerCodeEnum;
import org.buy.life.mapper.BuyUserMapper;
import org.buy.life.service.IBuyUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@Service
public class BuyUserServiceImpl extends ServiceImpl<BuyUserMapper, BuyUserEntity> implements IBuyUserService {


    @Override
    public  BuyUserEntity findByToken(String token){
        LambdaQueryWrapper<BuyUserEntity> queryWrapper=new QueryWrapper<BuyUserEntity>().lambda();
        queryWrapper.eq(BuyUserEntity::getIsDeleted,0).eq(BuyUserEntity::getToken,token);
        return getOne(queryWrapper);
    }

    @Override
    public  BuyUserEntity findByAccount(String userId){
        LambdaQueryWrapper<BuyUserEntity> queryWrapper=new QueryWrapper<BuyUserEntity>().lambda();
        queryWrapper.eq(BuyUserEntity::getIsDeleted,0).eq(BuyUserEntity::getUserId,userId);
        return getOne(queryWrapper);
    }

    @Override
    public  BuyUserEntity doLogin(LoginInfoReq loginInfoReq){
        LambdaQueryWrapper<BuyUserEntity> queryWrapper=new QueryWrapper<BuyUserEntity>().lambda();
        queryWrapper.eq(BuyUserEntity::getIsDeleted,0).eq(BuyUserEntity::getUserId,loginInfoReq.getAccount());
        BuyUserEntity user = getOne(queryWrapper);
        if(Objects.isNull(user)){
            throw  new BusinessException(ServerCodeEnum.NO_ACCOUNT);
        }
        if(StringUtils.isEmpty(loginInfoReq.getPassword())||!loginInfoReq.getPassword().equals(user.getPwd())){
            throw  new BusinessException(ServerCodeEnum.PWD_ERROR);
        }
        user.setLstLoginTime(LocalDateTime.now());
        user.setToken(UUID.randomUUID().toString().replace("-", ""));
        updateById(user);
        return user;
    }


    @Override
    public  BuyUserEntity reset(LoginInfoReq loginInfoReq){
        LambdaQueryWrapper<BuyUserEntity> queryWrapper=new QueryWrapper<BuyUserEntity>().lambda();
        queryWrapper.eq(BuyUserEntity::getIsDeleted,0).eq(BuyUserEntity::getUserId,loginInfoReq.getAccount());
        BuyUserEntity user = getOne(queryWrapper);
        if(Objects.isNull(user)){
            throw  new BusinessException(ServerCodeEnum.NO_ACCOUNT);
        }
        if(StringUtils.isEmpty(loginInfoReq.getPassword())||!loginInfoReq.getPassword().equals(user.getPwd())){
            throw  new BusinessException(ServerCodeEnum.PWD_ERROR);
        }
        user.setLstLoginTime(LocalDateTime.now());
        user.setPwd(loginInfoReq.getPassword());
        updateById(user);
        return user;
    }


    @Override
    public void delToken(LoginInfoReq loginInfoReq){
        LambdaQueryWrapper<BuyUserEntity> queryWrapper=new QueryWrapper<BuyUserEntity>().lambda();
        queryWrapper.eq(BuyUserEntity::getIsDeleted,0).eq(BuyUserEntity::getUserId,loginInfoReq.getAccount());
        BuyUserEntity user = getOne(queryWrapper);
        if(Objects.isNull(user)){
            throw  new BusinessException(ServerCodeEnum.NO_ACCOUNT);
        }
        user.setLstLoginTime(LocalDateTime.now());
        user.setToken("");
        updateById(user);
    }

    @Override
    public List<BuyUserEntity> getUserListByUserId(List<String> userIds) {
        return lambdaQuery().in(BuyUserEntity::getUserId, userIds).list();
    }

    @Override
    public BuyUserEntity getUserByUserId(String userId) {
        return lambdaQuery().eq(BuyUserEntity::getUserId, userId).one();
    }
}
