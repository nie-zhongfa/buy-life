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

    @ExcelProperty(value = "材质品类",index = 1)
    private String skuCategory;

    @ExcelProperty(value = "人物款式中文",index = 2)
    private String skuTypeZh_cn;
    @ExcelProperty(value = "人物款式英文",index = 3)
    private String skuTypeEn;
    @ExcelProperty(value = "人物款式西班牙语",index = 4)
    private String skuTypeEs;
    @ExcelProperty(value = "人物款式法语",index = 5)
    private String skuTypeFr;
    @ExcelProperty(value = "人物款式德语",index = 6)
    private String skuTypeDe;

    @ExcelProperty(value = "商品名称中文",index = 7)
    private String skuNameZh_cn;
    @ExcelProperty(value = "商品名称英文",index = 8)
    private String skuNameEn;
    @ExcelProperty(value = "商品名称西班牙语",index = 9)
    private String skuNameEs;
    @ExcelProperty(value = "商品名称法语",index = 10)
    private String skuNameFr;
    @ExcelProperty(value = "商品名称德语",index = 11)
    private String skuNameDe;

    @ExcelProperty(value = "成本价",index = 12)
    private String costPrice;

    @ExcelProperty(value = "人民币零售价",index = 13)
    private String priceCNY;
    @ExcelProperty(value = "美元零售价",index = 14)
    private String priceUSD;
    @ExcelProperty(value = "欧元零售价",index = 15)
    private String priceEUR;

    @ExcelProperty(value = "图片",index = 16)
    @ExcelImageProperty(index = 16)
    private InputStream file;
    @ExcelIgnore
    @ExcelSuffixProperty(index = 16)
    private String imgSuffix;

    @ExcelProperty(value = "数量",index = 17)
    private String stock;

    @ExcelProperty(value = "上下架",index = 18)
    private String skuStatus;

}
