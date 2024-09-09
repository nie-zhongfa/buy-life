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
public class SkuType
{

    private String lang;

    private String skuType;

    public static String getSkuType(String json, String lang) {
        List<SkuType> skuTypes = JSON.parseArray(json, SkuType.class);
        SkuType skuType = skuTypes.stream().filter(s -> lang.equals(s.getLang())).findFirst().get();
        return skuType.getSkuType();
    }

    public static void buildSkuType(String skuType,
                              String lang,
                              List<SkuType> skuTypes) {
        SkuType skuTypeDto = SkuType.builder()
                .skuType(skuType)
                .lang(lang)
                .build();
        skuTypes.add(skuTypeDto);
    }

    public static void buildSkuTypeList(ImportSkuDto importSkuDto, List<SkuType> skuTypes) {
        SkuType.buildSkuType(importSkuDto.getSkuTypeZh_cn(), LangEnum.ZH_CN.getCode(), skuTypes);
        SkuType.buildSkuType(importSkuDto.getSkuTypeEn(), LangEnum.EN.getCode(), skuTypes);
//        SkuType.buildSkuType(importSkuDto.getSkuTypeEs(), LangEnum.ES.getCode(), skuTypes);
//        SkuType.buildSkuType(importSkuDto.getSkuTypeFr(), LangEnum.FR.getCode(), skuTypes);
//        SkuType.buildSkuType(importSkuDto.getSkuTypeDe(), LangEnum.DE.getCode(), skuTypes);
    }


}
