package org.buy.life.model.request;

import lombok.Data;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/27 1:23 PM
 * I am a code man ^_^ !!
 */
@Data
public class AdminOrderConfirmRequest {

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 动作
     */
    private String action;

    /**
     * 发货凭证
     */
    private String receiptCertificate;
}
