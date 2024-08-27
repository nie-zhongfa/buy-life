package org.buy.life.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.buy.life.entity.BuyAdminEntity;
import org.buy.life.exception.BusinessException;
import org.buy.life.exception.ServerCodeEnum;
import org.buy.life.mapper.BuyAdminMapper;
import org.buy.life.service.IBuyAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.buy.life.utils.MD5Utils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}
