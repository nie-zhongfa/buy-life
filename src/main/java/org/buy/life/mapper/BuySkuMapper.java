package org.buy.life.mapper;

import org.apache.ibatis.annotations.Param;
import org.buy.life.entity.BuySkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * sku Mapper 接口
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
public interface BuySkuMapper extends BaseMapper<BuySkuEntity> {

    int updateStock(@Param("id") Long id, @Param("stock") Long stock);
}
