/*
 * Copyright (c) 2015-2022 BiliBili Inc.
 */

package org.buy.life.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: kavin
 * @Date: 2022/10/31 - 10 - 31 - 15:32
 * @Description: com.bilibili.offer.common.exception
 * @version: 1.0
 * @apiNote 该异常会打印错误日志，不会打印堆栈。
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class BusinessException extends RuntimeException {
    private Integer code;

    private Object[] args;

    public BusinessException(ServerCodeEnum serverCodeEnum) {
        super(serverCodeEnum.getMsg());
        this.code = serverCodeEnum.getCode();
    }

    public BusinessException(Integer code, String message, Object... args) {
        super(message);
        this.code = code;
        this.args=args;
    }

    @Override
    public String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return (message != null) ? (s + ": " + code + " " + message) : s;
    }

    public static BusinessException code(int code, String message, Object... args) {
        if (args.length == 0) {
            return new BusinessException(code, message);
        }
        return new BusinessException(code, String.format(message, args),args);
    }

    public static BusinessException code(ServerCodeEnum serverCodeEnum, Object... args) {
        return code(serverCodeEnum.getCode(), serverCodeEnum.getMsg(), args);
    }
}
