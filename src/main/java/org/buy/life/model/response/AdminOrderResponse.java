package org.buy.life.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/26 10:24 PM
 * I am a code man ^_^ !!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderResponse implements Serializable {

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 金额
     */
    private String orderAmt;

    /**
     * 币种
     */
    private String currency;

    /**
     * 用户备注
     */
    private String userRemark;

    /**
     * 管理员备注
     */
    private String adminRemark;

    /**
     * 状态
     */
    private String status;

    /**
     * 发货凭证
     */
    private String receiptCertificate;
}
