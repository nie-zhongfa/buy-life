package org.buy.life.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @menu 账号信息
 * @Author YourJustin
 * @Date 2024/8/24 11:55 PM
 * I am a code man ^_^ !!
 */
@Data
public class AccountResponse implements Serializable {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 国家
     */
    private String country;
    /**
     * 币种
     */
    private String currency;
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 用户名
     */
    private String userId;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 状态
     */
    private String status;
}
