package org.buy.life.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatusEnum {
    NEED_CONFIRM("NEED_CONFIRM", "{\"zh_cn\": \"待确认\",\"en\": \"To be confirmed\",\"es\": \"Pendiente de confirmación\",\"fr\": \"À confirmer\",\"de\": \"Noch zu bestätigen\"}"),

    NEED_PAY("NEED_PAY", "{\"zh_cn\": \"待付款\",\"en\": \"obligation\",\"es\": \"Pendiente de pago\",\"fr\": \"À payer\",\"de\": \"Ausstehende Zahlung\"}"),

    NEED_DELIVERY("NEED_DELIVERY", "{\"zh_cn\": \"待发货\",\"en\": \"Pending shipment\",\"es\": \"Pendiente de envío\",\"fr\": \"À expédier\",\"de\": \"Ausstehende Versendung\"}"),

    HAS_DELIVERY("HAS_DELIVERY", "{\"zh_cn\": \"已发货\",\"en\": \"Shipped\",\"es\": \"Enviado\",\"fr\": \"Expédié\",\"de\": \"Bereits versandt\"}"),

    END("END", "{\"zh_cn\": \"已结束\",\"en\": \"Ended\",\"es\": \"Ha terminado\",\"fr\": \"C'est terminé\",\"de\": \"Beendet\"}"),

    CANCEL("CANCEL", "{\"zh_cn\": \"已取消\",\"en\": \"Cancelled\",\"es\": \"Cancelado\",\"fr\": \"Annulé\",\"de\": \"Abgebrochen\"}"),
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
