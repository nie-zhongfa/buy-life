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
public class ImportSkuDto {

    @ColumnWidth(30)
    @ExcelProperty(value = "商品编码",index = 0)
    private String skuId;

    @ColumnWidth(30)
    @ExcelProperty(value = "IP",index = 1)
    private String classification;

    @ColumnWidth(30)
    @ExcelProperty(value = "材质品类",index = 2)
    private String skuCategory;

    @ColumnWidth(30)
    @ExcelProperty(value = "人物款式中文",index = 3)
    private String skuTypeZh_cn;
    @ColumnWidth(30)
    @ExcelProperty(value = "人物款式英文",index = 4)
    private String skuTypeEn;

    @ColumnWidth(30)
    @ExcelProperty(value = "商品名称中文",index = 5)
    private String skuNameZh_cn;
    @ColumnWidth(30)
    @ExcelProperty(value = "商品名称英文",index = 6)
    private String skuNameEn;

    @ColumnWidth(30)
    @ExcelProperty(value = "成本价",index = 7)
    private String costPrice;

    @ColumnWidth(30)
    @ExcelProperty(value = "人民币单价",index = 8)
    private String priceCNY;
    @ColumnWidth(30)
    @ExcelProperty(value = "美元单价",index = 9)
    private String priceUSD;
    @ColumnWidth(30)
    @ExcelProperty(value = "欧元单价",index = 10)
    private String priceEUR;

    @ColumnWidth(30)
    @ExcelProperty(value = "人民币零售价",index = 11)
    private String retailPriceCNY;
    @ColumnWidth(30)
    @ExcelProperty(value = "美元零售价",index = 12)
    private String retailPriceUSD;
    @ColumnWidth(30)
    @ExcelProperty(value = "欧元零售价",index = 13)
    private String retailPriceEUR;

    @ColumnWidth(30)
    @ExcelProperty(value = "图片",index = 14)
    @ExcelImageProperty(value = "表明这是一个图片字段")
    private InputStream file;
    @ExcelIgnore
    @ExcelSuffixProperty(value = "后缀名（.png）")
    private String imgSuffix;

    @ColumnWidth(30)
    @ExcelProperty(value = "数量",index = 15)
    private String stock;

    @ColumnWidth(30)
    @ExcelProperty(value = "上下架",index = 16)
    private String skuStatus;

}
