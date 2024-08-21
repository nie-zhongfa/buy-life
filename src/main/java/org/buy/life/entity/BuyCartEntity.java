package org.buy.life.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 购物车
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("buy_cart")
public class BuyCartEntity extends Model<BuyCartEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 最后修改时间
     */
    private LocalDateTime mtime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String updater;

    /**
     * 用户id
     */
    private String userId;

    /**
     * sku_id
     */
    private String skuId;

    /**
     * 购物车sku数量
     */
    private Long skuNum;

    /**
     * 状态
     */
    private String status;

    /**
     * 保留域
     */
    private String ext;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
