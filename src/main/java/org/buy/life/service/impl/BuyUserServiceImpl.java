package org.buy.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.buy.life.constant.UserStatusEnum;
import org.buy.life.entity.BuyUserEntity;
import org.buy.life.entity.req.BuyUserReq;
import org.buy.life.entity.req.LoginInfoReq;
import org.buy.life.exception.BusinessException;
import org.buy.life.exception.ServerCodeEnum;
import org.buy.life.mapper.BuyUserMapper;
import org.buy.life.service.IBuyUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.buy.life.utils.BeanCopiesUtils;
import org.buy.life.utils.TtlUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@Service
@Slf4j
public class BuyUserServiceImpl extends ServiceImpl<BuyUserMapper, BuyUserEntity> implements IBuyUserService {


    @Override
    public  BuyUserEntity findByToken(String token){
        LambdaQueryWrapper<BuyUserEntity> queryWrapper=new QueryWrapper<BuyUserEntity>().lambda();
        queryWrapper.eq(BuyUserEntity::getIsDeleted,0).eq(BuyUserEntity::getToken,token);
        return getOne(queryWrapper);
    }

    @Override
    public  BuyUserEntity findByAccount(){
        String token = TtlUtils.getSPCtx().getToken();
        LambdaQueryWrapper<BuyUserEntity> queryWrapper=new QueryWrapper<BuyUserEntity>().lambda();
        queryWrapper.eq(BuyUserEntity::getIsDeleted,0).eq(BuyUserEntity::getToken,token);
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
    public  void create(BuyUserReq buyUserReq){
        BuyUserEntity buyUser = BeanCopiesUtils.copy(buyUserReq, BuyUserEntity.class);
        buyUser.setUserId(random()+"");
        buyUser.setPwd(randomPwd(8));
        buyUser.setStatus(UserStatusEnum.CREATE.getCode());
        save(buyUser);
    }


    @Override
    public  void update(BuyUserReq buyUserReq){
        LambdaQueryWrapper<BuyUserEntity> queryWrapper=new QueryWrapper<BuyUserEntity>().lambda();
        queryWrapper.eq(BuyUserEntity::getIsDeleted,0).eq(BuyUserEntity::getUserId,buyUserReq.getUserId());
        BuyUserEntity user = getOne(queryWrapper);
        if(Objects.isNull(user)){
            throw  new BusinessException(ServerCodeEnum.NO_ACCOUNT);
        }

        BuyUserEntity buyUser = BeanCopiesUtils.copy(buyUserReq, BuyUserEntity.class);
        buyUser.setId(user.getId());
        updateById(buyUser);
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

    @Override
    public void resendPwd(BuyUserReq buyUserReq) {
        LambdaQueryWrapper<BuyUserEntity> queryWrapper=new QueryWrapper<BuyUserEntity>().lambda();
        queryWrapper.eq(BuyUserEntity::getIsDeleted,0).eq(BuyUserEntity::getMail,buyUserReq.getMail());
        List<BuyUserEntity> user = list(queryWrapper);
        if(CollectionUtils.isEmpty(user)){
            throw  new BusinessException(ServerCodeEnum.NO_ACCOUNT);
        }
        for (BuyUserEntity buyUserEntity : user) {
            buyUserEntity.setStatus(UserStatusEnum.CREATE.getCode());
        }
        updateBatchById(user);
    }


    private int random(){
        int min = 100000;
        int max = 999999;
        int randomNumber = ThreadLocalRandom.current().nextInt(min, max + 1);
        LambdaQueryWrapper<BuyUserEntity> queryWrapper=new QueryWrapper<BuyUserEntity>().lambda();
        queryWrapper.eq(BuyUserEntity::getIsDeleted,0).eq(BuyUserEntity::getUserId,randomNumber+"");
        BuyUserEntity user = getOne(queryWrapper);
        if(Objects.nonNull(user)){
            return random();
        }
        return randomNumber;
    }


    private String randomPwd(int length){
        String chars = "ABCDEFGHJKMNOPQRSTUVWXYZabcdefghjkmnopqrstuvwxyz023456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
}
