package org.buy.life.entity.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@Data
public class BuyOrderDetailReq{
    /**
     * 创建时间
     */
    private LocalDateTime submitTime;
    /**
     * 金额
     */
    private String orderAmt;

    private List<OrderDetail> orderDetails;

    private String userRemark;


    @Data
    public static class OrderDetail{
        /**
         * sku_id
         */
        private String skuId;

        /**
         * 购物车sku数量
         */
        private Long skuNum;

        /**
         * 单价
         */
        private String price;

        /**
         * 金额
         */
        private String totalAmt;

    }


}
