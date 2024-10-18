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
 * @menu 订单明细
 * @Author YourJustin
 * @Date 2024/8/27 12:56 PM
 * I am a code man ^_^ !!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@HeadRowHeight(30)
@ContentRowHeight(20)
public class ExportOrderDetailInfoDto {

    /**
     * 订单号
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "订单号",index = 0)
    private String orderId;

    /**
     * 用户id
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "用户名",index = 1)
    private String userId;

    /**
     * 邮箱
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "邮箱",index = 2)
    private String mail;

    /**
     * 商品id
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "商品编码",index = 3)
    private String skuId;

    /**
     * 商品名称
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "商品名称",index = 4)
    private String skuName;

    /**
     * 材质品类
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "材质品类",index = 5)
    private String skuCategory;

    /**
     * 人物款式
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "人物款式",index = 6)
    private String skuType;

    /**
     * 单价
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "单价",index = 7)
    private String price;

    /**
     * 金额
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "金额",index = 8)
    private String totalAmt;

    /**
     * 币种
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "币种",index = 9)
    private String currency;

    /**
     * 数量
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "数量",index = 10)
    private Long skuNum;
}
