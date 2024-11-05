package org.buy.life.controller;

import org.buy.life.entity.resp.SimplePage;
import org.buy.life.model.request.AdminSeriesRequest;
import org.buy.life.model.response.AdminSeriesResponse;
import org.buy.life.service.IAdminSeriesService;
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
@RequestMapping("/admin/series")
public class AdminSeriesController {

    @Resource
    private IAdminSeriesService iAdminSeriesService;

    /**
     * 系列列表
     *
     * @param adminSeriesRequest
     * @return
     */
    @PostMapping("/queryPage")
    public JSONData<SimplePage<AdminSeriesResponse>> querySeriesPage(@RequestBody AdminSeriesRequest adminSeriesRequest) {
        SimplePage<AdminSeriesResponse> pageInfo = iAdminSeriesService.querySeriesPage(adminSeriesRequest);
        return JSONData.success(pageInfo);
    }

    /**
     * 导入系列
     *
     * @param file
     * @return
     */
    @PostMapping("/import")
    public JSONData<Boolean> importSeriesInfo(@RequestBody MultipartFile file) {
        iAdminSeriesService.importSeriesInfo(file);
        return JSONData.success(true);
    }

    /**
     * 下载系列导入模版
     *
     * @return
     */
    @GetMapping("/downloadCategoryTemplate")
    public void downloadCategoryTemplate(HttpServletResponse response) {
        iAdminSeriesService.downloadSeriesTemplate(response);
    }
}
