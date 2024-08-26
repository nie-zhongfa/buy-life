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
 * 管理员信息表
 * </p>
 *
 * @author MrWu
 * @since 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("buy_admin")
public class BuyAdminEntity extends Model<BuyAdminEntity> {

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
     * 用户密码
     */
    private String pwd;

    /**
     * 状态
     */
    private String status;

    /**
     * 上次登录时间
     */
    private LocalDateTime lstLoginTime;

    /**
     * 登录token
     */
    private String token;

    /**
     * 上次登录语言
     */
    private String lstLang;

    /**
     * 保留域
     */
    private String ext;

    /**
     * token有效期
     */
    private LocalDateTime lstTokenExpire;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
