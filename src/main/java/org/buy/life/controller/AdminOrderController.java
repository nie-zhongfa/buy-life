package org.buy.life.controller;

import com.github.pagehelper.PageInfo;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.model.request.AdminOrderConfirmRequest;
import org.buy.life.model.request.AdminOrderRequest;
import org.buy.life.model.request.AdminSkuRequest;
import org.buy.life.model.request.UpdateOrderRequest;
import org.buy.life.model.response.AdminOrderDetailResponse;
import org.buy.life.model.response.AdminOrderResponse;
import org.buy.life.model.response.AdminSkuResponse;
import org.buy.life.service.IAdminOrderService;
import org.buy.life.utils.JSONData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public JSONData<SimplePage<AdminOrderResponse>> queryOrderPage(@RequestBody AdminOrderRequest adminOrderRequest) {
        SimplePage<AdminOrderResponse> pageInfo = adminOrderService.queryOrderPage(adminOrderRequest);
        return JSONData.success(pageInfo);
    }

    @PostMapping("/import")
    public JSONData<Boolean> export(@RequestBody MultipartFile file) {
        adminOrderService.importOrder(file);
        return JSONData.success(true);
    }

    @GetMapping("/detail")
    public JSONData<AdminOrderDetailResponse> detail(@RequestParam("orderId") String orderId) {
        AdminOrderDetailResponse adminOrderDetailResponse = adminOrderService.queryDetail(orderId);
        return JSONData.success(adminOrderDetailResponse);
    }

    @PostMapping("/confirm")
    public JSONData<Boolean> confirm(@RequestBody AdminOrderConfirmRequest adminOrderConfirmRequest) {
        adminOrderService.confirm(adminOrderConfirmRequest);
        return JSONData.success(true);
    }

    @PostMapping("/update")
    public JSONData<Boolean> update(@RequestBody UpdateOrderRequest updateOrderRequest) {
        adminOrderService.update(updateOrderRequest);
        return JSONData.success(true);
    }
}
