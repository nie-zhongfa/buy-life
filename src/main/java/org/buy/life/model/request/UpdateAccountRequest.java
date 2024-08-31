package org.buy.life.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/25 12:12 AM
 * I am a code man ^_^ !!
 */
@Data
public class UpdateAccountRequest implements Serializable {

    private Long id;

    /**
     * 公司名称
     */
    private String companyName;

    private String userId;

    private String pwd;

    private String country;

    private String currency;

    private String mail;
}
