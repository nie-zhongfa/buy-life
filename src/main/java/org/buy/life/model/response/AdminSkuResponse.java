package org.buy.life.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 9:07 PM
 * I am a code man ^_^ !!
 */
@Data
public class AdminSkuResponse implements Serializable {

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
     * 基准成本价
     */
    private String costPrice;
    /**
     * 人民币单价
     */
    private String priceCNY;
    /**
     * 美元单价
     */
    private String priceUSD;
    /**
     * 欧元单价
     */
    private String priceEUR;
    /**
     * 人民币零售价
     */
    private String retailPriceCNY;
    /**
     * 美元零售价
     */
    private String retailPriceUSD;
    /**
     * 欧元零售价
     */
    private String retailPriceEUR;
    /**
     * 库存
     */
    private String stock;
    /**
     * 分类：genshin_impact、star_rail、zenless_zone_zero
     */
    private String classification;

    /**
     * LISTED 已上下，REMOVED 已下架
     */
    private String status;
}
