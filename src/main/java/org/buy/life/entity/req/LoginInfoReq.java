package org.buy.life.entity.req;

import lombok.Data;

/**
 * @Auther: zhaoss
 * @Date: 2023/3/22 - 03 - 22 - 14:55
 * @Description: com.fire.data.model.entity.req
 * @version: 1.0
 */
@Data
public class LoginInfoReq {
    private String account;
    private String password;
}
