package org.buy.life.model.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.InputStream;

/**
 * @menu 导入
 * @Author YourJustin
 * @Date 2024/8/26 4:54 PM
 * I am a code man ^_^ !!
 */
@Data
public class ImportSkuDto {

    @ExcelProperty(value = "商品编码",index = 0)
    private String skuId;

    @ExcelProperty(value = "IP",index = 1)
    private String classification;

    @ExcelProperty(value = "材质品类",index = 2)
    private String skuCategory;

    @ExcelProperty(value = "人物款式中文",index = 3)
    private String skuTypeZh_cn;
    @ExcelProperty(value = "人物款式英文",index = 4)
    private String skuTypeEn;

    @ExcelProperty(value = "商品名称中文",index = 5)
    private String skuNameZh_cn;
    @ExcelProperty(value = "商品名称英文",index = 6)
    private String skuNameEn;

    @ExcelProperty(value = "成本价",index = 7)
    private String costPrice;

    @ExcelProperty(value = "人民币零售价",index = 8)
    private String priceCNY;
    @ExcelProperty(value = "美元零售价",index = 9)
    private String priceUSD;
    @ExcelProperty(value = "欧元零售价",index = 10)
    private String priceEUR;

    @ExcelProperty(value = "图片",index = 11)
    @ExcelImageProperty(index = 11)
    private InputStream file;
    @ExcelIgnore
    @ExcelSuffixProperty(index = 11)
    private String imgSuffix;

    @ExcelProperty(value = "数量",index = 12)
    private String stock;

    @ExcelProperty(value = "上下架",index = 13)
    private String skuStatus;

}
