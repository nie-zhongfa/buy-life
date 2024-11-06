package org.buy.life.mapper;

import org.buy.life.entity.BuyOrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.buy.life.model.dto.ExportOrderDetailInfoDto;
import org.buy.life.model.request.GetOrderRequest;

import java.util.List;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
public interface BuyOrderMapper extends BaseMapper<BuyOrderEntity> {

    List<ExportOrderDetailInfoDto> exportOrderInfo(GetOrderRequest getOrderRequest);
}
