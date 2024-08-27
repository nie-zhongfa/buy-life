package org.buy.life.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.buy.life.entity.BuyOrderEntity;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.model.request.AdminOrderConfirmRequest;
import org.buy.life.model.request.AdminOrderRequest;
import org.buy.life.model.request.UpdateOrderRequest;
import org.buy.life.model.response.AdminOrderDetailResponse;
import org.buy.life.model.response.AdminOrderResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 2:56 PM
 * I am a code man ^_^ !!
 */
public interface IAdminOrderService  extends IService<BuyOrderEntity> {

    SimplePage<AdminOrderResponse> queryOrderPage(AdminOrderRequest adminOrderRequest);

    AdminOrderDetailResponse queryDetail(String orderId);

    void confirm(AdminOrderConfirmRequest adminOrderConfirmRequest);

    void importOrder(MultipartFile file);

    void update(UpdateOrderRequest updateOrderRequest);
}
