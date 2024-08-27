package org.buy.life.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.buy.life.constant.OrderStatusEnum;
import org.buy.life.entity.BuyOrderDetailEntity;
import org.buy.life.entity.BuyOrderEntity;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.BuyUserEntity;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.mapper.BuyOrderMapper;
import org.buy.life.model.enums.ActionEnum;
import org.buy.life.model.request.AdminOrderConfirmRequest;
import org.buy.life.model.request.AdminOrderRequest;
import org.buy.life.model.request.UpdateOrderRequest;
import org.buy.life.model.response.AdminOrderDetailResponse;
import org.buy.life.model.response.AdminOrderResponse;
import org.buy.life.model.response.AdminSkuResponse;
import org.buy.life.model.response.OrderDetailInfoResponse;
import org.buy.life.service.IAdminOrderService;
import org.buy.life.service.IAdminSkuService;
import org.buy.life.service.IBuyOrderDetailService;
import org.buy.life.service.IBuyUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 2:56 PM
 * I am a code man ^_^ !!
 */
@Slf4j
@Service
public class AdminOrderServiceImpl extends ServiceImpl<BuyOrderMapper, BuyOrderEntity> implements IAdminOrderService {

    @Resource
    private IBuyUserService buyUserService;
    @Resource
    private IBuyOrderDetailService buyOrderDetailService;
    @Resource
    private IAdminSkuService adminSkuService;

    @Override
    public SimplePage<AdminOrderResponse> queryOrderPage(AdminOrderRequest adminOrderRequest) {
        Page<BuyOrderEntity> orderPage = getOrderPage(adminOrderRequest);
        if (orderPage == null || CollectionUtils.isEmpty(orderPage.getRecords())) {
            return SimplePage.emptyPage();
        }
        SimplePage<AdminOrderResponse> pageInfo = BeanUtil.copyProperties(orderPage, SimplePage.class);
        List<AdminOrderResponse> responses = new ArrayList<>();
        List<BuyOrderEntity> records = orderPage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<String> userIds = records.stream().map(BuyOrderEntity::getUserId).distinct().collect(Collectors.toList());
            List<BuyUserEntity> userList = buyUserService.getUserListByUserId(userIds);
            Map<String, BuyUserEntity> userMap = userList.stream().collect(Collectors.toMap(BuyUserEntity::getUserId, contract -> contract, (a, b) -> a));
            orderPage.getRecords().forEach(r -> {
                AdminOrderResponse adminOrderResponse = BeanUtil.copyProperties(r, AdminOrderResponse.class);
                BuyUserEntity buyUserEntity = userMap.get(r.getUserId());
                adminOrderResponse.setMail(buyUserEntity.getMail());
                responses.add(adminOrderResponse);
            });
        }
        pageInfo.setList(responses);
        return pageInfo;
    }

    @Override
    public AdminOrderDetailResponse queryDetail(String orderId) {
        BuyOrderEntity orderEntity = getOrderByOrderId(orderId);
        BuyUserEntity user = buyUserService.getUserByUserId(orderEntity.getUserId());
        AdminOrderDetailResponse adminOrderDetailResponse = BeanUtil.copyProperties(orderEntity, AdminOrderDetailResponse.class);
        adminOrderDetailResponse.setMail(user.getMail());
        adminOrderDetailResponse.setSubmitTime(orderEntity.getLstSubmitTime());
        //获取详情
        List<BuyOrderDetailEntity> orderDetailList = buyOrderDetailService.getDetailByOrderId(orderId);
        List<String> skuIds = orderDetailList.stream().map(BuyOrderDetailEntity::getSkuId).distinct().collect(Collectors.toList());
        List<BuySkuEntity> skuList = adminSkuService.getSkuBySkuIdList(skuIds);
        Map<String, BuySkuEntity> skuMap = skuList.stream().collect(Collectors.toMap(BuySkuEntity::getSkuId, contract -> contract, (a, b) -> a));
        List<OrderDetailInfoResponse> detailInfoResponses = new ArrayList<>();
        orderDetailList.forEach(d -> {
            OrderDetailInfoResponse orderDetailInfoResponse = BeanUtil.copyProperties(d, OrderDetailInfoResponse.class);
            BuySkuEntity buySkuEntity = skuMap.get(d.getSkuId());
            orderDetailInfoResponse.setBatchKey(buySkuEntity.getBatchKey());
            orderDetailInfoResponse.setSkuName(buySkuEntity.getSkuName());
            orderDetailInfoResponse.setSkuType(buySkuEntity.getSkuType());
            orderDetailInfoResponse.setSkuCategory(buySkuEntity.getSkuCategory());
            detailInfoResponses.add(orderDetailInfoResponse);
        });
        adminOrderDetailResponse.setOrderDetails(detailInfoResponses);
        return adminOrderDetailResponse;
    }

    @Override
    public void confirm(AdminOrderConfirmRequest adminOrderConfirmRequest) {
        ActionEnum actionEnum = ActionEnum.getByAction(adminOrderConfirmRequest.getAction());
        String status = null;
        switch (actionEnum) {
            case CONFIRM:
                status = OrderStatusEnum.NEED_PAY.getCode();
                break;
            case CONFIRM_PAY:
                status = OrderStatusEnum.NEED_DELIVERY.getCode();
                break;
            case CONFIRM_DELIVERY:
                status = OrderStatusEnum.HAS_DELIVERY.getCode();
                break;
            case CONFIRM_TAKE_GOODS:
                status = OrderStatusEnum.END.getCode();
                break;
            case CANCEL:
                status = OrderStatusEnum.CANCEL.getCode();
                break;
            default:
                return;
        }
        if (status != null) {
            lambdaUpdate().set(BuyOrderEntity::getStatus, status)
                    .eq(BuyOrderEntity::getOrderId, adminOrderConfirmRequest.getOrderId())
                    .update();
        }
    }

    public Page<BuyOrderEntity> getOrderPage(AdminOrderRequest adminOrderRequest) {
        Page<BuyOrderEntity> page = new Page<>(adminOrderRequest.getPageNum(), adminOrderRequest.getPageSize());
        return lambdaQuery()
                .eq(BuyOrderEntity::getStatus, adminOrderRequest.getStatus())
                .eq(BuyOrderEntity::getIsDeleted, false)
                .orderByDesc(BuyOrderEntity::getMtime)
                .page(page);
    }

    public BuyOrderEntity getOrderByOrderId(String orderId) {
        return lambdaQuery()
                .eq(BuyOrderEntity::getOrderId, orderId)
                .eq(BuyOrderEntity::getIsDeleted, false)
                .one();
    }

    @Override
    public void importOrder(MultipartFile file) {

    }

    @Override
    public void update(UpdateOrderRequest updateOrderRequest) {

    }
}
