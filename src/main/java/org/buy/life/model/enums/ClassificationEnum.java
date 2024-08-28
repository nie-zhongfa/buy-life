package org.buy.life.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 9:47 PM
 * I am a code man ^_^ !!
 */
@Getter
@AllArgsConstructor
public enum ClassificationEnum {

    /**
     * 分类：genshin_impact、star_rail、zenless_zone_zero
     */
    GENSHIN_IMPACT("genshin_impact", "原神"),
    STAR_RAIL("star_rail", "崩坏星穹铁道"),
    ZENLESS_ZONE_ZERO("zenless_zone_zero", "绝区零");

    private final String code;
    private final String desc;

    public static String getCodeByDesc(String desc) {
        for (ClassificationEnum classificationEnum : values()) {
            if (classificationEnum.desc.equals(desc)) {
                return classificationEnum.code;
            }
        }
        return null;
    }
}
