package org.buy.life.service;

import org.buy.life.entity.BuyUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.buy.life.entity.req.BuyUserReq;
import org.buy.life.entity.req.LoginInfoReq;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
public interface IBuyUserService extends IService<BuyUserEntity> {

    BuyUserEntity findByToken(String token);

    BuyUserEntity findByAccount();

    BuyUserEntity doLogin(LoginInfoReq loginInfoReq);

    void create(BuyUserReq buyUserReq);

    void update(BuyUserReq buyUserReq);

    BuyUserEntity reset(LoginInfoReq loginInfoReq);

    void delToken(LoginInfoReq loginInfoReq);

    List<BuyUserEntity> getUserListByUserId(List<String> userIds);

    BuyUserEntity getUserByUserId(String userId);

    void resendPwd(BuyUserReq buyUserReq);
}
