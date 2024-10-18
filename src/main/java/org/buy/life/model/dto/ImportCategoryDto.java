package org.buy.life.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
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
@HeadRowHeight(30)
@ContentRowHeight(20)
public class ImportCategoryDto {

    @ColumnWidth(30)
    @ExcelProperty(value = "品类编码",index = 0)
    private String categoryCode;

    @ColumnWidth(30)
    @ExcelProperty(value = "IP",index = 1)
    private String ip;

    @ColumnWidth(30)
    @ExcelProperty(value = "中文",index = 2)
    private String zh_cn;

    @ColumnWidth(30)
    @ExcelProperty(value = "英文",index = 3)
    private String en;


}
