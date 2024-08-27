package org.buy.life.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @menu 导入订单明细
 * @Author YourJustin
 * @Date 2024/8/26 4:54 PM
 * I am a code man ^_^ !!
 */
@Data
public class ImportOrderDto {

    @ExcelProperty(value = "订单号",index = 0)
    private String orderId;

    @ExcelProperty(value = "商品编码",index = 1)
    private String skuId;

    @ExcelProperty(value = "零售价", index = 2)
    private String price;

    @ExcelProperty(value = "币种", index = 3)
    private String currency;

    @ExcelProperty(value = "数量",index = 4)
    private Long skuNum;

}
