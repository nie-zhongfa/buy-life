package org.buy.life.model.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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

    @ExcelProperty(value = "人物款式",index = 2)
    private String skuType;

    @ExcelProperty(value = "商品名称",index = 3)
    private String skuName;

    @ExcelProperty(value = "零售价",index = 4)
    private String price;

    @ExcelProperty(value = "图片",index = 5)
    @ExcelImageProperty(index = 5)
    private InputStream file;

    @ExcelIgnore
    @ExcelSuffixProperty(index = 5)
    private String imgSuffix;

    @ExcelProperty(value = "单价",index = 6)
    private String costPrice;

    @ExcelProperty(value = "数量",index = 7)
    private String stock;

}
