package org.buy.life.service;

import org.buy.life.entity.BuyCategoryEntity;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.model.request.AdminCategoryRequest;
import org.buy.life.model.response.AdminCategoryResponse;

import java.util.List;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/11/4 20:31
 * I am a code man ^_^ !!
 */
public interface IAdminCategoryService {

    SimplePage<AdminCategoryResponse> queryCategoryPage(AdminCategoryRequest adminCategoryRequest);

    List<BuyCategoryEntity> getCategoryListByCode(List<String> categoryCodeList);
}
