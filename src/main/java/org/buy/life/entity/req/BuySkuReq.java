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
 * sku
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@Data
public class BuySkuReq{

    private static final long serialVersionUID = 1L;

    /**
     * sku_id
     */
    private String keyWord;

    /**
     * sku类型
     */
    private String skuType;

    /**
     * sku品类
     */
    private String skuCategory;


}
