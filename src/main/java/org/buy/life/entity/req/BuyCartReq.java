package org.buy.life.entity.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 购物车
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@Data
public class BuyCartReq {
    /**
     * sku_id
     */
    private String skuId;

    /**
     * 购物车sku数量
     */
    private Long skuNum;

}
