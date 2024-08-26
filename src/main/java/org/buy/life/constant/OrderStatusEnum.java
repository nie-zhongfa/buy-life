package org.buy.life.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatusEnum {
    NEED_CONFIRM("NEED_CONFIRM", "待确认"),

    NEED_PAY("NEED_PAY", "待付款"),

    NEED_DELIVERY("NEED_DELIVERY", "待发货"),

    HAS_DELIVERY("HAS_DELIVERY", "已发货"),

    END("END", "已结束"),
    ;
    /**
     * 后端code
     */
    private final String code;

    /**
     * desc 后端节点翻译
     */
    private final String desc;
}
