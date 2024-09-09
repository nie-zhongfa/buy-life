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
    EN("en", "English");
//    ES("es", "Español"),
//    FR("fr", "Français"),
//    DE("de", "Deutsch");

    private final String code;
    private final String desc;
}
