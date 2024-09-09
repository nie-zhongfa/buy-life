package org.buy.life.model.request;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.buy.life.model.dto.ImportSkuDto;
import org.buy.life.model.enums.LangEnum;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuName
{

    private String lang;

    private String skuName;


    public static String getSkuName(String json, String lang) {
        List<SkuName> skuNames = JSON.parseArray(json, SkuName.class);
        SkuName skuName = skuNames.stream().filter(s -> lang.equals(s.getLang())).findFirst().get();
        return skuName.getSkuName();
    }

    public static void buildSkuName(String skuName,
                                    String lang,
                                    List<SkuName> skuNames) {
        SkuName skuNameDto = SkuName.builder()
                .skuName(skuName)
                .lang(lang)
                .build();
        skuNames.add(skuNameDto);
    }

    public static void buildSkuNameList(ImportSkuDto importSkuDto, List<SkuName> skuNames) {
        SkuName.buildSkuName(importSkuDto.getSkuNameZh_cn(), LangEnum.ZH_CN.getCode(), skuNames);
        SkuName.buildSkuName(importSkuDto.getSkuNameEn(), LangEnum.EN.getCode(), skuNames);
//        SkuName.buildSkuName(importSkuDto.getSkuNameEs(), LangEnum.ES.getCode(), skuNames);
//        SkuName.buildSkuName(importSkuDto.getSkuNameFr(), LangEnum.FR.getCode(), skuNames);
//        SkuName.buildSkuName(importSkuDto.getSkuNameDe(), LangEnum.DE.getCode(), skuNames);
    }
}
