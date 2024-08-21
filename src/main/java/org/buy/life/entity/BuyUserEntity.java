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
 * 用户信息表
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("buy_user")
public class BuyUserEntity extends Model<BuyUserEntity> {

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
     * 公司名称
     */
    private String companyName;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 地址
     */
    private String address;

    /**
     * 币种
     */
    private String currency;

    /**
     * 上次登录时间
     */
    private LocalDateTime lstLoginTime;

    /**
     * 登录token
     */
    private String token;

    /**
     * 上次登录语音
     */
    private String lstLang;

    /**
     * 保留域
     */
    private String ext;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
