package org.buy.life.entity.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 枚举表
 * </p>
 *
 * @author MrWu
 * @since 2024-08-27
 */
@Data
public class BuySkuDictResp{

    private List<TitleDict> titleDicts;

    private List<OrderStatusDict> orderStatusDicts;

    private List<SkuDict> skuDicts;

    private List<CountryDict> countryDicts;

    private List<LangDict> langDicts;

    private List<CurrencyDict> currencyDicts;


    @Data
    public static class TitleDict{
        private String code;

        private String desc;
    }

    @Data
    public static class OrderStatusDict{
        private String code;

        private String desc;
    }


    @Data
    public static class CountryDict{
        private String code;

        private String desc;
    }


    @Data
    public static class LangDict{
        private String code;

        private String desc;
    }


    @Data
    public static class CurrencyDict{
        private String code;

        private String desc;
    }


    @Data
    public static class SkuDict{
        /**
         * 主体
         */
        private String title;
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

    }
}
