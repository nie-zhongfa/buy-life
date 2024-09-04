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

    CHN("CHN", "{\"zh_cn\": \"中国\",\"en\": \"China\",\"es\": \"China\",\"fr\": \"Chine\",\"de\": \"China\"}"),
    USA("USA", "{\"zh_cn\": \"美国\",\"en\": \"America\",\"es\": \"Estados Unidos\",\"fr\": \"États - Unis\",\"de\": \"USA\"}"),
    FRA("FRA", "{\"zh_cn\": \"法国\",\"en\": \"France\",\"es\": \"Francia\",\"fr\": \"France\",\"de\": \"Frankreich\"}"),
    ESP("ESP", "{\"zh_cn\": \"西班牙\",\"en\": \"Spain\",\"es\": \"España\",\"fr\": \"Espagne\",\"de\": \"Spanien\"}"),
    DE("DE", "{\"zh_cn\": \"德国\",\"en\": \"Germany\",\"es\": \"Alemania\",\"fr\": \"Allemagne\",\"de\": \"Deutschland\"}");


    private final String code;
    private final String desc;

    public static String getDescByCode(String code) {
        for (CountryEnum countryEnum : values()) {
            if (countryEnum.getCode().equals(code)) {
                return countryEnum.getDesc();
            }
        }
        return null;
    }
}
