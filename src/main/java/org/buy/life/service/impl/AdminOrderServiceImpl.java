package org.buy.life.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.buy.life.entity.BuyOrderEntity;
import org.buy.life.mapper.BuyOrderMapper;
import org.buy.life.model.request.AdminOrderRequest;
import org.buy.life.model.response.AdminOrderResponse;
import org.buy.life.service.IAdminOrderService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 2:56 PM
 * I am a code man ^_^ !!
 */
@Slf4j
@Service
public class AdminOrderServiceImpl extends ServiceImpl<BuyOrderMapper, BuyOrderEntity> implements IAdminOrderService {

    @Override
    public PageInfo<AdminOrderResponse> queryOrderPage(AdminOrderRequest adminOrderRequest) {

        return null;
    }

    @Override
    public void importOrder(MultipartFile file) {

    }
}
