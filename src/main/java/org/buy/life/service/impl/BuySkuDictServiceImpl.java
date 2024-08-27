package org.buy.life.service.impl;

import org.buy.life.entity.BuySkuDictEntity;
import org.buy.life.mapper.BuySkuDictMapper;
import org.buy.life.service.IBuySkuDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 枚举表 服务实现类
 * </p>
 *
 * @author MrWu
 * @since 2024-08-27
 */
@Service
public class BuySkuDictServiceImpl extends ServiceImpl<BuySkuDictMapper, BuySkuDictEntity> implements IBuySkuDictService {

    @Override
    public List<BuySkuDictEntity> getSkuDictByCode(String code) {
       return lambdaQuery().eq(BuySkuDictEntity::getCode, code).eq(BuySkuDictEntity::getIsDeleted, false).list();
    }

    @Override
    public List<BuySkuDictEntity> getSkuDictList() {
        return lambdaQuery().eq(BuySkuDictEntity::getIsDeleted, false).list();
    }
}
