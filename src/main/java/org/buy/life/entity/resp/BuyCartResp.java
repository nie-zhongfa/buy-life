package org.buy.life.entity.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 购物车
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyCartResp {

    private String totalAmt;

    private List<CartSku> cartSkuLists=new ArrayList<>();


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartSku{

        /**
         * sku_id
         */
        private String skuId;

        /**
         * 购物车sku数量
         */
        private Long skuNum;

        private String price;

        private String skuName;

        private String skuAmt;

        /**
         * 库存
         */
        private String stock;

    }

}
