package org.buy.life.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * sku
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("buy_sku")
public class BuySkuEntity extends Model<BuySkuEntity> {

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
     * sku_id
     */
    private String skuId;

    /**
     * 状态
     */
    private String status;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * sku类型
     */
    private String skuType;

    /**
     * sku品类
     */
    private String skuCategory;

    /**
     * 单价
     */
    private String price;

    /**
     * 零售价
     */
    private String retailPrice;

    /**
     * 多地区价格
     */
    private String langPrice;

    /**
     * 库存
     */
    private String stock;

    /**
     * 文件地址
     */
    private String batchKey;

    /**
     * 文件名称
     */
    private String imageName;

    /**
     * 保留域
     */
    private String ext;

    /**
     * 主体：genshin_impact、star_rail、zenless_zone_zero
     */
    private String classification;

    /**
     * 基准成本价
     */
    private String costPrice;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
