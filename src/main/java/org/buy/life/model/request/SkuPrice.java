package org.buy.life.model.request;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.buy.life.model.dto.ImportSkuDto;
import org.buy.life.model.enums.CurrencyEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        if(StringUtils.isEmpty(skuPrice.getSkuPrice())){
            return "0.00";
        }
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
        SkuPrice.buildPrice(formatTo2Decimal(importSkuDto.getPriceCNY()), CurrencyEnum.CNY.getCode(), skuPrices);
        SkuPrice.buildPrice(formatTo2Decimal(importSkuDto.getPriceUSD()), CurrencyEnum.USD.getCode(), skuPrices);
        SkuPrice.buildPrice(formatTo2Decimal(importSkuDto.getPriceEUR()), CurrencyEnum.EUR.getCode(), skuPrices);
    }

    public static void buildRetailPriceList(ImportSkuDto importSkuDto, List<SkuPrice> skuPrices) {
        SkuPrice.buildPrice(formatTo2Decimal(importSkuDto.getRetailPriceCNY()), CurrencyEnum.CNY.getCode(), skuPrices);
        SkuPrice.buildPrice(formatTo2Decimal(importSkuDto.getRetailPriceUSD()), CurrencyEnum.USD.getCode(), skuPrices);
        SkuPrice.buildPrice(formatTo2Decimal(importSkuDto.getRetailPriceEUR()), CurrencyEnum.EUR.getCode(), skuPrices);
    }

    public static String formatTo2Decimal(String number) {
        if (number == null || number.isEmpty()) {
            return "0.00";
        }
        try {
            BigDecimal decimal = new BigDecimal(number);
            // 设置保留2位小数，四舍五入
            return decimal.setScale(2, RoundingMode.HALF_UP).toString();
        } catch (NumberFormatException e) {
            return "0.00";
        }
    }
}
