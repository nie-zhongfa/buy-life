package org.buy.life.model.request;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.buy.life.model.dto.ImportSkuDto;
import org.buy.life.model.enums.CurrencyEnum;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuPrice {
    private String currency;

    private String skuPrice;

    public static String getSkuPrice(String json, String currency) {
        if (StringUtils.isBlank(json)) {
            return "0.00";
        }
        List<SkuPrice> skuPrices = JSON.parseArray(json, SkuPrice.class);
        SkuPrice skuPrice = skuPrices.stream().filter(s -> currency.equals(s.getCurrency())).findFirst().get();
        return skuPrice.getSkuPrice();
    }

    public static void buildPrice(String price,
                            String currency,
                            List<SkuPrice> skuPrices) {
        SkuPrice skuPrice = SkuPrice.builder()
                .skuPrice(price)
                .currency(currency)
                .build();
        skuPrices.add(skuPrice);
    }

    public static void buildPriceList(ImportSkuDto importSkuDto, List<SkuPrice> skuPrices) {
        SkuPrice.buildPrice(importSkuDto.getPriceCNY(), CurrencyEnum.CNY.getCode(), skuPrices);
        SkuPrice.buildPrice(importSkuDto.getPriceUSD(), CurrencyEnum.USD.getCode(), skuPrices);
        SkuPrice.buildPrice(importSkuDto.getPriceEUR(), CurrencyEnum.EUR.getCode(), skuPrices);
    }

    public static void buildRetailPriceList(ImportSkuDto importSkuDto, List<SkuPrice> skuPrices) {
        SkuPrice.buildPrice(importSkuDto.getRetailPriceCNY(), CurrencyEnum.CNY.getCode(), skuPrices);
        SkuPrice.buildPrice(importSkuDto.getRetailPriceUSD(), CurrencyEnum.USD.getCode(), skuPrices);
        SkuPrice.buildPrice(importSkuDto.getRetailPriceEUR(), CurrencyEnum.EUR.getCode(), skuPrices);
    }
}
