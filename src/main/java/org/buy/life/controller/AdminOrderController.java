package org.buy.life.controller;

import com.github.pagehelper.PageInfo;
import org.buy.life.model.request.AdminOrderRequest;
import org.buy.life.model.request.AdminSkuRequest;
import org.buy.life.model.response.AdminOrderResponse;
import org.buy.life.model.response.AdminSkuResponse;
import org.buy.life.service.IAdminOrderService;
import org.buy.life.utils.JSONData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 12:29 PM
 * I am a code man ^_^ !!
 */
@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Resource
    private IAdminOrderService adminOrderService;

    @PostMapping("/queryPage")
    public JSONData<PageInfo<AdminOrderResponse>> queryOrderPage(@RequestBody AdminOrderRequest adminOrderRequest) {
        PageInfo<AdminOrderResponse> pageInfo = adminOrderService.queryOrderPage(adminOrderRequest);
        return JSONData.success(pageInfo);
    }

    @PostMapping("/import")
    public JSONData<Boolean> export(@RequestBody MultipartFile file) {
        adminOrderService.importOrder(file);
        return JSONData.success(true);
    }
}
