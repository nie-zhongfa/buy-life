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
public enum CountryEnum {

    CHN("CHN", "中国"),
    USA("USA", "美国"),
    FRA("FRA", "法国"),
    ESP("ESP", "西班牙"),
    DE("DE", "德国");


    private final String code;
    private final String desc;
}
