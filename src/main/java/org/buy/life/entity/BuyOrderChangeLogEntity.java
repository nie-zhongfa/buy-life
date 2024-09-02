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
 * 订单修改记录表
 * </p>
 *
 * @author MrWu
 * @since 2024-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("buy_order_change_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyOrderChangeLogEntity extends Model<BuyOrderChangeLogEntity> {

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
     * order_id
     */
    private String orderId;

    /**
     * sku_id
     */
    private String skuId;

    /**
     * 变更json
     */
    private String changeLog;

    /**
     * 变更类型：ADD、UPDATE、DELETE
     */
    private String changeType;

    /**
     * 保留域
     */
    private String ext;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
