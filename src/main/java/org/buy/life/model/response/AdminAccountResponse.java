package org.buy.life.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/11/6 16:30
 * I am a code man ^_^ !!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAccountResponse {

    private String role;

    /**
     * 添加账号
     */
    private String roleDesc;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户密码
     */
    private String pwd;

    /**
     * 可见字段
     */
    private List<String> filedList;
}
