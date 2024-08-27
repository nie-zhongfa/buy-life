package org.buy.life.model.request;

import com.alibaba.fastjson2.JSON;
import lombok.Data;

import java.util.List;

@Data
public class SkuPrice {
    private String currency;

    private String skuPrice;

    public static String getSkuPrice(String json, String currency) {
        List<SkuPrice> skuPrices = JSON.parseArray(json, SkuPrice.class);
        SkuPrice skuPrice = skuPrices.stream().filter(s -> currency.equals(s.getCurrency())).findFirst().get();
        return skuPrice.getSkuPrice();
    }
}
