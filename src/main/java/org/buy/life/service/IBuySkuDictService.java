package org.buy.life.service;

import org.buy.life.entity.BuySkuDictEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.buy.life.entity.resp.BuySkuDictResp;

import java.util.List;

/**
 * <p>
 * 枚举表 服务类
 * </p>
 *
 * @author MrWu
 * @since 2024-08-27
 */
public interface IBuySkuDictService extends IService<BuySkuDictEntity> {

    List<BuySkuDictEntity> getSkuDictByCode(String code);

    List<BuySkuDictEntity> getSkuDictList();

    BuySkuDictResp getAllDict();
}
