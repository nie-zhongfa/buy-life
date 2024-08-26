/*
 * Copyright (c) 2015-2020 BiliBili Inc.
 */

package org.buy.life.model.enums;

/**
 * 用户类型
 */
public enum UserTypeEnum {
    /**
     *     用户类型枚举
     */
    USER("1", "用户"),
    ADMIN("2","后台");

    private String code;
    private String desc;

    UserTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
