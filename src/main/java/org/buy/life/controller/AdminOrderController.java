package org.buy.life.controller;

import org.buy.life.entity.resp.SimplePage;
import org.buy.life.model.request.AdminOrderConfirmRequest;
import org.buy.life.model.request.AdminOrderRequest;
import org.buy.life.model.request.AddOrderRemarkRequest;
import org.buy.life.model.request.UpdateOrderDetailRequest;
import org.buy.life.model.response.AdminOrderDetailResponse;
import org.buy.life.model.response.AdminOrderResponse;
import org.buy.life.service.IAdminOrderService;
import org.buy.life.utils.JSONData;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

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

    @PostMapping("/addRemark")
    public JSONData<Boolean> addRemark(@RequestBody AddOrderRemarkRequest addOrderRemarkRequest) {
        adminOrderService.addRemark(addOrderRemarkRequest);
        return JSONData.success(true);
    }

    @PostMapping("/update")
    public JSONData<Boolean> update(@RequestBody List<UpdateOrderDetailRequest> updateOrderDetailRequest) {
        adminOrderService.update(updateOrderDetailRequest);
        return JSONData.success(true);
    }
}
