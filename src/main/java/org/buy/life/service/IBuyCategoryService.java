package org.buy.life.service;

import org.buy.life.entity.BuyCategoryEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 类别 服务类
 * </p>
 *
 * @author MrWu
 * @since 2024-11-04
 */
public interface IBuyCategoryService extends IService<BuyCategoryEntity> {

    List<BuyCategoryEntity> getCategoryList(String classification);
}
