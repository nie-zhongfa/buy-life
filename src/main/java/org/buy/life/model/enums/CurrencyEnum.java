package org.buy.life.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/27 9:49 PM
 * I am a code man ^_^ !!
 */
@Getter
@AllArgsConstructor
public enum CurrencyEnum {

    CNY("CNY", "人民币"),
    USD("USD", "美元"),
    EUR("EUR", "欧元");

    private final String code;
    private final String desc;
}
