package org.buy.life.controller;

import org.buy.life.entity.resp.SimplePage;
import org.buy.life.model.request.AdminSkuRequest;
import org.buy.life.model.request.UpAndDownSkuRequest;
import org.buy.life.model.response.AdminSkuResponse;
import org.buy.life.service.IAdminSkuService;
import org.buy.life.utils.JSONData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @menu 商品管理
 * @Author YourJustin
 * @Date 2024/8/24 12:27 PM
 * I am a code man ^_^ !!
 */
@RestController
@RequestMapping("/admin/sku")
public class AdminSkuController {

    @Resource
    private IAdminSkuService adminSkuService;

    /**
     * 商品列表
     *
     * @param adminSkuRequest
     * @return
     */
    @PostMapping("/queryPage")
    public JSONData<SimplePage<AdminSkuResponse>> querySkuPage(@RequestBody AdminSkuRequest adminSkuRequest) {
        SimplePage<AdminSkuResponse> pageInfo = adminSkuService.querySkuPage(adminSkuRequest);
        return JSONData.success(pageInfo);
    }

    /**
     * 导入商品
     *
     * @param file
     * @return
     */
    @PostMapping("/import")
    public JSONData<Boolean> export(@RequestBody MultipartFile file) {
        adminSkuService.importSku(file);
        return JSONData.success(true);
    }

    /**
     * 导入品类
     *
     * @param file
     * @return
     */
    @PostMapping("/importCategory")
    public JSONData<Boolean> importCategory(@RequestBody MultipartFile file) {
        adminSkuService.importCategory(file);
        return JSONData.success(true);
    }

    /**
     * 上下架商品
     * @param upAndDownSkuRequest
     * @return
     */
    @PostMapping("/upAndSownSku")
    public JSONData<Boolean> UpAndSownShelves(@RequestBody UpAndDownSkuRequest upAndDownSkuRequest) {
        adminSkuService.upAndSownShelves(upAndDownSkuRequest);
        return JSONData.success(true);
    }
}
