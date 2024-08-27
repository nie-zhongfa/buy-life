package org.buy.life.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @menu 订单详情
 * @Author YourJustin
 * @Date 2024/8/27 12:55 PM
 * I am a code man ^_^ !!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderDetailResponse {

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 创建时间
     */
    private LocalDateTime submitTime;

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
     * 状态
     */
    private String status;

    /**
     * 用户备注
     */
    private String userRemark;

    /**
     * 管理员备注
     */
    private String adminRemark;

    /**
     * 订单明细
     */
    private List<OrderDetailInfoResponse> orderDetails;


}
