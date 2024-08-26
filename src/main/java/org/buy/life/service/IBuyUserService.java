package org.buy.life.service;

import org.buy.life.entity.BuyUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.buy.life.entity.req.LoginInfoReq;

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

    BuyUserEntity findByAccount(String userId);

    BuyUserEntity doLogin(LoginInfoReq loginInfoReq);

    void delToken(LoginInfoReq loginInfoReq);
}
