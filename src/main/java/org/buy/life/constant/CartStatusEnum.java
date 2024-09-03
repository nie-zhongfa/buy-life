package org.buy.life.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CartStatusEnum {
    REMOVED("REMOVED", "{\"zh_cn\": \"已提交\",\"en\": \"submitted\",\"es\": \"Presentado\",\"fr\": \"Soumis\",\"de\": \"Eingereicht\"}"),

    JOINED("JOINED", "{\"zh_cn\": \"已添加\",\"en\": \"added\",\"es\": \"Se ha añadido\",\"fr\": \"A été ajouté\",\"de\": \"Hinzugefügt\"}");
    /**
     * 后端code
     */
    private final String code;

    /**
     * desc 后端节点翻译
     */
    private final String desc;
}
