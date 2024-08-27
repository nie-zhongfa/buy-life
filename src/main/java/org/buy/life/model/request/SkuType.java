package org.buy.life.model.request;

import com.alibaba.fastjson2.JSON;
import lombok.Data;

import java.util.List;

@Data
public class SkuType
{

    private String lang;

    private String skuType;

    public static String getSkuType(String json, String lang) {
        List<SkuType> skuTypes = JSON.parseArray(json, SkuType.class);
        SkuType skuType = skuTypes.stream().filter(s -> lang.equals(s.getLang())).findFirst().get();
        return skuType.getSkuType();
    }

}
