package org.buy.life.model.request;

import lombok.Data;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/27 1:46 PM
 * I am a code man ^_^ !!
 */
@Data
public class UpdateOrderDetailRequest {

    private Long id;

    private String orderId;

    private String skuId;

    private Long skuNum;
}
