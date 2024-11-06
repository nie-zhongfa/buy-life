package org.buy.life.service;

import org.buy.life.entity.BuyAdminEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.model.request.QueryAccountRequest;
import org.buy.life.model.request.UpdateAdminAccountRequest;
import org.buy.life.model.response.AdminAccountResponse;

/**
 * <p>
 * 管理员信息表 服务类
 * </p>
 *
 * @author MrWu
 * @since 2024-08-26
 */
public interface IBuyAdminService extends IService<BuyAdminEntity> {

    String login(String username, String password);

    BuyAdminEntity  getAdmin(String token);

    SimplePage<AdminAccountResponse> queryAdminAccountPage(QueryAccountRequest queryAccountRequest);

    void updatePwd(UpdateAdminAccountRequest updateAdminAccountRequest);

    void updateField(UpdateAdminAccountRequest updateAdminAccountRequest);
}
