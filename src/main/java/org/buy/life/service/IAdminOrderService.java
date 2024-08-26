package org.buy.life.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.buy.life.entity.BuyOrderEntity;
import org.buy.life.model.request.AdminOrderRequest;
import org.buy.life.model.response.AdminOrderResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 2:56 PM
 * I am a code man ^_^ !!
 */
public interface IAdminOrderService  extends IService<BuyOrderEntity> {

    PageInfo<AdminOrderResponse> queryOrderPage(AdminOrderRequest adminOrderRequest);

    void importOrder(MultipartFile file);
}
