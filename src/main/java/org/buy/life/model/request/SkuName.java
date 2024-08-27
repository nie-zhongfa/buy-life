package org.buy.life.model.request;

import com.alibaba.fastjson2.JSON;
import lombok.Data;

import java.util.List;

@Data
public class SkuName
{

    private String lang;

    private String skuName;


    public static String getSkuName(String json, String lang) {
        List<SkuName> skuNames = JSON.parseArray(json, SkuName.class);
        SkuName skuName = skuNames.stream().filter(s -> lang.equals(s.getLang())).findFirst().get();
        return skuName.getSkuName();
    }
}
