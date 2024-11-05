package org.buy.life.service;

import org.buy.life.entity.BuySeriesEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系列 服务类
 * </p>
 *
 * @author MrWu
 * @since 2024-11-04
 */
public interface IBuySeriesService extends IService<BuySeriesEntity> {

    List<BuySeriesEntity> getSeriesList(String categoryCode);
}
