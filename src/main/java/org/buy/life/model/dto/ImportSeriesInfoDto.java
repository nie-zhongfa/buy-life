package org.buy.life.model.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;


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
public class ImportSeriesInfoDto {


    @ColumnWidth(30)
    @ExcelProperty(value = "IP",index = 0)
    private String ip;

    @ColumnWidth(30)
    @ExcelProperty(value = "系列编码",index = 1)
    private String seriesCode;

    @ColumnWidth(30)
    @ExcelProperty(value = "类别编码",index = 2)
    private String categoryCode;

    @ColumnWidth(30)
    @ExcelProperty(value = "中文",index = 3)
    private String zh_cn;

    @ColumnWidth(30)
    @ExcelProperty(value = "英文",index = 4)
    private String en;

    @ColumnWidth(30)
    @ExcelProperty(value = "封面",index = 5)
    @ExcelImageProperty(value = "表明这是一个图片字段")
    private InputStream file;

    @ExcelIgnore
    @ExcelSuffixProperty(value = "后缀名（.png）")
    private String imgSuffix;
}
