package org.buy.life.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系列
 * </p>
 *
 * @author MrWu
 * @since 2024-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("buy_series")
public class BuySeriesEntity extends Model<BuySeriesEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
      private Long id;

    /**
     * ip
     */
    private String classification;

    /**
     * 类别编码
     */
    private String categoryCode;
    /**
     * 系列编码
     */
    private String seriesCode;

    /**
     * 系列名称
     */
    private String seriesName;

    /**
     * 系列封面
     */
    private String cover;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 更新时间
     */
    private LocalDateTime mtime;

    /**
     * 最低价
     */
    @TableField(exist = false)
    private String minPrice;

    /**
     * 最高价
     */
    @TableField(exist = false)
    private String maxPrice;

    /**
     * 类别名称
     */
    @TableField(exist = false)
    private String categoryName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
