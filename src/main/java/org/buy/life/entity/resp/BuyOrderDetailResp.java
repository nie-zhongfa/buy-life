package org.buy.life.entity.resp;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@Data
public class BuyOrderDetailResp {
    /**
     * 创建时间
     */
    private LocalDateTime submitTime;
    /**
     * 金额
     */
    private String orderAmt;

    List<OrderDetail> orderDetails;

    private String orderId;


    /**
     * 币种
     */
    private String currency;

    private String adminRemark;

    @Data
    public static class OrderDetail{
        /**
         * sku_id
         */
        private String skuId;

        private String skuName;

        /**
         * 购物车sku数量
         */
        private Long skuNum;

        /**
         * 单价
         */
        private String price;

        /**
         * 金额
         */
        private String totalAmt;


        /**
         * 币种
         */
        private String currency;

        /**
         * 文件地址
         */
        private String batchKey;

        /**
         * sku类型
         */
        private String skuType;

        /**
         * sku品类
         */
        private String skuCategory;
    }


}
