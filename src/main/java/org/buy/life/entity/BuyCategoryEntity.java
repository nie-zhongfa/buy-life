package org.buy.life.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 类别
 * </p>
 *
 * @author MrWu
 * @since 2024-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("buy_category")
public class BuyCategoryEntity extends Model<BuyCategoryEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
      private Long id;

    /**
     * IP
     */
    private String classification;

    /**
     * 类别编码
     */
    private String categoryCode;

    /**
     * 类别名称
     */
    private String categoryName;

    /**
     * 封面地址
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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
