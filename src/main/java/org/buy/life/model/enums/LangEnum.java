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
public enum LangEnum {

    ZH_CN("zh_cn", "中文"),
    EN("en", "英语"),
    ES("es", "西班牙语"),
    FR("fr", "法语"),
    DE("de", "德语");

    private final String code;
    private final String desc;
}
