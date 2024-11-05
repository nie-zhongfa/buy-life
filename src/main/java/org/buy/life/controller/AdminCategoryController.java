package org.buy.life.controller;

import org.buy.life.entity.resp.SimplePage;
import org.buy.life.model.request.AdminCategoryRequest;
import org.buy.life.model.response.AdminCategoryResponse;
import org.buy.life.service.IAdminCategoryService;
import org.buy.life.utils.JSONData;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @menu 类别管理
 * @Author YourJustin
 * @Date 2024/11/4 20:27
 * I am a code man ^_^ !!
 */
@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Resource
    private IAdminCategoryService iAdminCategoryService;

    /**
     * 类别列表
     *
     * @param adminCategoryRequest
     * @return
     */
    @PostMapping("/queryPage")
    public JSONData<SimplePage<AdminCategoryResponse>> queryCategoryPage(@RequestBody AdminCategoryRequest adminCategoryRequest) {
        SimplePage<AdminCategoryResponse> pageInfo = iAdminCategoryService.queryCategoryPage(adminCategoryRequest);
        return JSONData.success(pageInfo);
    }

    /**
     * 导入类别
     *
     * @param file
     * @return
     */
    @PostMapping("/import")
    public JSONData<Boolean> importCategoryInfo(@RequestBody MultipartFile file) {
        iAdminCategoryService.importCategoryInfo(file);
        return JSONData.success(true);
    }

    /**
     * 下载类别导入模版
     *
     * @return
     */
    @GetMapping("/downloadCategoryInfoTemplate")
    public void downloadCategoryInfoTemplate(HttpServletResponse response) {
        iAdminCategoryService.downloadCategoryInfoTemplate(response);
    }
}
