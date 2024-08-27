package org.buy.life.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/26 10:26 PM
 * I am a code man ^_^ !!
 */
@Data
public class AdminOrderRequest extends BaseRequest implements Serializable {

    /**
     * 状态
     */
    private String status;

    /**
     * 订单号
     */
    private String orderId;
}
