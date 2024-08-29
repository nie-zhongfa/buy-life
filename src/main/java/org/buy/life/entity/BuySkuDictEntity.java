package org.buy.life.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buy.life.model.request.SkuName;

/**
 * <p>
 * 枚举表
 * </p>
 *
 * @author MrWu
 * @since 2024-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("buy_sku_dict")
public class BuySkuDictEntity extends Model<BuySkuDictEntity> {

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
     * 修改人
     */
    private String code;

    /**
     * 品类名称
     */
    private String skuCategory;

    /**
     * 语言
     */
    private String lang;

    /**
     * 保留域
     */
    private String ext;

    /**
     * 主题
     */
    private String title;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public static String getSkuCategoryName(List<BuySkuDictEntity> skuCategoryList, String lang) {
        BuySkuDictEntity buySkuDictEntity = skuCategoryList.stream().filter(s -> lang.equals(s.getLang())).findFirst().get();
        return buySkuDictEntity.getSkuCategory();
    }
}
