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
    CONFIRM("CONFIRM", "{\"zh_cn\": \"确认订单\",\"en\": \"确认订单\",\"es\": \"确认订单\",\"fr\": \"确认订单\",\"de\": \"确认订单\"}"),
    CONFIRM_PAY("CONFIRM_PAY", "{\"zh_cn\": \"确认收款\",\"en\": \"确认收款\",\"es\": \"确认收款\",\"fr\": \"确认收款\",\"de\": \"确认收款\"}"),
    CONFIRM_DELIVERY("CONFIRM_DELIVERY", "{\"zh_cn\": \"确认发货\",\"en\": \"确认发货\",\"es\": \"确认发货\",\"fr\": \"确认发货\",\"de\": \"确认发货\"}"),
    CONFIRM_TAKE_GOODS("CONFIRM_TAKE_GOODS", "{\"zh_cn\": \"确认收货\",\"en\": \"确认收货\",\"es\": \"确认收货\",\"fr\": \"确认收货\",\"de\": \"确认收货\"}"),
    CANCEL("CANCEL","{\"zh_cn\": \"取消订单\",\"en\": \"取消订单\",\"es\": \"取消订单\",\"fr\": \"取消订单\",\"de\": \"取消订单\"}");

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
