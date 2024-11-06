package org.buy.life.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/11/6 16:59
 * I am a code man ^_^ !!
 */
@AllArgsConstructor
@Getter
public enum RoleEnum {

    SUPER_ADMIN("SUPER_ADMIN", "超级管理员"),
    ADMIN("ADMIN", "管理员");

    private String role;

    private String desc;

    public static String getRoleDesc(String role) {
        for (RoleEnum roleEnum : values()) {
            if (role.equals(roleEnum.getRole())) {
                return roleEnum.getDesc();
            }
        }
        return ADMIN.getDesc();
    }
}
