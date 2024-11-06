package org.buy.life.model.request;

import lombok.Data;

import java.util.List;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/11/6 17:27
 * I am a code man ^_^ !!
 */
@Data
public class UpdateAdminAccountRequest {

    private String userId;

    private String oldPwd;

    private String pwd;

    private List<String> filedList;
}
