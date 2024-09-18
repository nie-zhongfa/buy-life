package org.buy.life.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @menu 导入
 * @Author YourJustin
 * @Date 2024/8/26 4:54 PM
 * I am a code man ^_^ !!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportCategoryDto {

    @ExcelProperty(value = "品类编码",index = 0)
    private String categoryCode;

    @ExcelProperty(value = "IP",index = 1)
    private String ip;

    @ExcelProperty(value = "中文",index = 2)
    private String zh_cn;

    @ExcelProperty(value = "英文",index = 3)
    private String en;


}
