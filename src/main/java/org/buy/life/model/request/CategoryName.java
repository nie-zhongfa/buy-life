package org.buy.life.model.request;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.buy.life.model.dto.ImportCategoryInfoDto;
import org.buy.life.model.dto.ImportSkuDto;
import org.buy.life.model.enums.LangEnum;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryName
{

    private String lang;

    private String categoryName;


    public static String getCategoryName(String json, String lang) {
        if (StringUtils.isBlank(json)) {
            return "";
        }
        List<CategoryName> categoryNames = JSON.parseArray(json, CategoryName.class);
        CategoryName categoryName = categoryNames.stream().filter(s -> lang.equals(s.getLang())).findFirst().get();
        return categoryName.getCategoryName();
    }

    public static void buildCategoryName(String categoryName,
                                    String lang,
                                    List<CategoryName> categoryNames) {
        CategoryName categoryNameDto = CategoryName.builder()
                .categoryName(categoryName)
                .lang(lang)
                .build();
        categoryNames.add(categoryNameDto);
    }

    public static void buildCategoryNameList(ImportCategoryInfoDto categoryInfoDto, List<CategoryName> categoryNames) {
        CategoryName.buildCategoryName(categoryInfoDto.getZh_cn(), LangEnum.ZH_CN.getCode(), categoryNames);
        CategoryName.buildCategoryName(categoryInfoDto.getEn(), LangEnum.EN.getCode(), categoryNames);
    }
}
