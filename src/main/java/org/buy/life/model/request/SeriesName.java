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
public class SeriesName
{

    private String lang;

    private String seriesName;


    public static String getSeriesName(String json, String lang) {
        List<SeriesName> seriesNames = JSON.parseArray(json, SeriesName.class);
        SeriesName seriesName = seriesNames.stream().filter(s -> lang.equals(s.getLang())).findFirst().get();
        return seriesName.getSeriesName();
    }

    public static void buildSeriesName(String seriesName,
                                    String lang,
                                    List<SeriesName> seriesNames) {
        SeriesName seriesNameDto = SeriesName.builder()
                .seriesName(seriesName)
                .lang(lang)
                .build();
        seriesNames.add(seriesNameDto);
    }

    public static void buildSeriesNameList(ImportSkuDto importSkuDto, List<SeriesName> seriesNames) {
        SeriesName.buildSeriesName(importSkuDto.getSkuNameZh_cn(), LangEnum.ZH_CN.getCode(), seriesNames);
        SeriesName.buildSeriesName(importSkuDto.getSkuNameEn(), LangEnum.EN.getCode(), seriesNames);
    }
}
