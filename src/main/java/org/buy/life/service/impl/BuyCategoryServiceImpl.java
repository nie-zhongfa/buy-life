package org.buy.life.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.buy.life.entity.BuyCategoryEntity;
import org.buy.life.mapper.BuyCategoryMapper;
import org.buy.life.service.IBuyCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 类别 服务实现类
 * </p>
 *
 * @author MrWu
 * @since 2024-11-04
 */
@Service
public class BuyCategoryServiceImpl extends ServiceImpl<BuyCategoryMapper, BuyCategoryEntity> implements IBuyCategoryService {

    @Override
    public List<BuyCategoryEntity> getCategory(String classification){
        if(StringUtils.isNotEmpty(classification)){
            return lambdaQuery().eq(BuyCategoryEntity::getClassification,classification).eq(BuyCategoryEntity::getIsDeleted, false).list();
        }
        return lambdaQuery().eq(BuyCategoryEntity::getIsDeleted, false).list();
    }


    @Override
    public List<BuyCategoryEntity> getCategoryList(List<String> categoryLists){
        if(CollectionUtils.isNotEmpty(categoryLists)){
            return lambdaQuery().in(BuyCategoryEntity::getCategoryCode,categoryLists).eq(BuyCategoryEntity::getIsDeleted, false).list();
        }
        return lambdaQuery().eq(BuyCategoryEntity::getIsDeleted, false).list();
    }
}
