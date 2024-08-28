package org.buy.life.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum SkuStatusEnum {
    REMOVED("REMOVED", "已下架"),

    LISTED("LISTED", "已上架");
    /**
     * 后端code
     */
    private final String code;

    /**
     * desc 后端节点翻译
     */
    private final String desc;

    public static String getCodeByDesc(String desc) {
        for (SkuStatusEnum skuStatusEnum : values()) {
            if (skuStatusEnum.desc.equals(desc)) {
                return skuStatusEnum.code;
            }
        }
        return null;
    }
}
