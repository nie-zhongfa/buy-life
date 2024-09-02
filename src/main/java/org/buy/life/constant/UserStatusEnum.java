package org.buy.life.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatusEnum {
    CREATE("CREATE", "已申请注册/未发送密码"),

    COMPLETE("COMPLETE", "注册完成/已发送密码");
    /**
     * 后端code
     */
    private final String code;

    /**
     * desc 后端节点翻译
     */
    private final String desc;

    public static String getCodeByDesc(String desc) {
        for (UserStatusEnum skuStatusEnum : values()) {
            if (skuStatusEnum.desc.equals(desc)) {
                return skuStatusEnum.code;
            }
        }
        return null;
    }
}
