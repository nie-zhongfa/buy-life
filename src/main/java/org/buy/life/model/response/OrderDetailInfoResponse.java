package org.buy.life.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @menu 订单明细
 * @Author YourJustin
 * @Date 2024/8/27 12:56 PM
 * I am a code man ^_^ !!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailInfoResponse {

    private Long id;

    /**
     * 商品id
     */
    private String skuId;

    /**
     * 商品名称
     */
    private String skuName;

    /**
     * 商品图片batchKey
     */
    private String batchKey;

    /**
     * 材质品类
     */
    private String skuCategory;

    /**
     * 人物款式
     */
    private String skuType;

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
     * 数量
     */
    private Long skuNum;

    /**
     * 库存
     */
    private String stock;
}
