package org.buy.life.model.request;

import lombok.Data;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/27 1:39 PM
 * I am a code man ^_^ !!
 */
@Data
public class UpdateOrderRequest {

    private String orderId;

    private String userRemark;

    private String adminRemark;


}
