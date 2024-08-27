package org.buy.life.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/27 1:23 PM
 * I am a code man ^_^ !!
 */
@Getter
@AllArgsConstructor
public enum ActionEnum {

    /**
     * 订单确认枚举
     */
    CONFIRM("CONFIRM", "确认订单"),
    CONFIRM_PAY("CONFIRM_PAY", "确认收款"),
    CONFIRM_DELIVERY("CONFIRM_DELIVERY", "确认发货"),
    CONFIRM_TAKE_GOODS("CONFIRM_TAKE_GOODS", "确认收货"),
    CANCEL("CANCEL","取消订单");

    private String action;
    private String desc;

    public static ActionEnum getByAction(String action) {
        for (ActionEnum actionEnum : values()) {
            if (actionEnum.action.equals(action)) {
                return actionEnum;
            }
        }
        return null;
    }
}
