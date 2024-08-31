/*
 * Copyright (c) 2015-2020 BiliBili Inc.
 */

package org.buy.life.exception;


import java.util.Objects;

/**
 * kavin
 */
public enum ServerCodeEnum {

    /**
     * 错误码枚举
     */
    SUCCESS(0, "success"),

    //三位 服务器异常
    INVALID_PARAMETERS_ERROR(400, "请求异常"),
    UNAUTHORIZED(403, "无权限访问"),
    INTERNAL_SERVER_ERROR(500, "%s%s系统繁忙"),

    HTTP_MEDIA_TYPE_NOT_SUPPORT_ERROR(501, "MediaType 不支持"),
    REQUEST_INFO_NULL(5001, "入参为空"),
    REQUEST_INFO_FAIL(1001,"返回非成功"),
    //1xxx quafulException
    GET_PLACE_FAIL(2001,"获取单位下场所异常"),
    NO_DATA_AUTH(2002,"您暂无数据权限"),
    NO_ACCOUNT(2003,"账号不存在"),
    PWD_ERROR(2004,"密码错误"),
    ACCOUNT_NO_ROLE(2006,"账号为配置角色"),
    ACCOUNT_NO_TELANT(2007,"为配置该租户"),
    GET_DAY_FIRE_FAIL(2008,"获取事件趋势失败"),
    MEMBER_HAS_EXIST(2009,"用户已存在"),
    PROJECT_NO_EXIST(2010,"部门不存在"),
    USERNAME_IS_NULL(2050, "用户名不能为空"),
    PASSWORD_IS_NULL(2050, "密码不能为空"),
    USERNAME_OR_PWD_IS_VAIL(2051, "用户名或密码错误"),
    NO_CURRENCY(2052, "未配置该区域的币种"),

    ;
    private Integer code;
    private String msg;

    ServerCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ServerCodeEnum getByCode(Integer code) {
        for (ServerCodeEnum item : ServerCodeEnum.values()) {
            if (Objects.nonNull(item.getCode()) && item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static ServerCodeEnum getByMsg(String msg) {
        for (ServerCodeEnum item : ServerCodeEnum.values()) {
            if (Objects.nonNull(item.getMsg()) && item.getMsg().equals(msg)) {
                return item;
            }
        }
        return null;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
