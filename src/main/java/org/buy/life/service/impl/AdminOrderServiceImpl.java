package org.buy.life.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.buy.life.constant.OrderStatusEnum;
import org.buy.life.entity.*;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.exception.BusinessException;
import org.buy.life.filter.CurrentAdminUser;
import org.buy.life.mapper.BuyOrderMapper;
import org.buy.life.model.dto.ChangeKeyValueDto;
import org.buy.life.model.dto.ExportOrderDetailInfoDto;
import org.buy.life.model.dto.ImportOrderDto;
import org.buy.life.model.enums.ActionEnum;
import org.buy.life.model.enums.LangEnum;
import org.buy.life.model.request.*;
import org.buy.life.model.response.AdminOrderDetailResponse;
import org.buy.life.model.response.AdminOrderResponse;
import org.buy.life.model.response.OrderDetailInfoResponse;
import org.buy.life.service.*;
import org.buy.life.utils.excel.ExcelUtil;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
    @Resource
    private IBuySkuDictService buySkuDictService;
    @Resource
    private IBuyOrderChangeLogService buyOrderChangeLogService;

    @Override
    public SimplePage<AdminOrderResponse> queryOrderPage(AdminOrderRequest adminOrderRequest) {
        log.info("queryOrderPage param:{}", JSON.toJSONString(adminOrderRequest));
        Page<BuyOrderEntity> orderPage = getOrderPage(adminOrderRequest);
        if (orderPage == null || CollectionUtils.isEmpty(orderPage.getRecords())) {
            return SimplePage.emptyPage();
        }
        SimplePage<AdminOrderResponse> pageInfo = BeanUtil.copyProperties(orderPage, SimplePage.class);
        List<AdminOrderResponse> responses = new ArrayList<>();
        List<BuyOrderEntity> records = orderPage.getRecords();
        List<String> userIds = records.stream().map(BuyOrderEntity::getUserId).distinct().collect(Collectors.toList());
        List<BuyUserEntity> userList = buyUserService.getUserListByUserId(userIds);
        Map<String, BuyUserEntity> userMap = userList.stream().collect(Collectors.toMap(BuyUserEntity::getUserId, contract -> contract, (a, b) -> a));
        orderPage.getRecords().forEach(r -> {
            AdminOrderResponse adminOrderResponse = BeanUtil.copyProperties(r, AdminOrderResponse.class);
            BuyUserEntity buyUserEntity = userMap.get(r.getUserId());
            adminOrderResponse.setMail(buyUserEntity.getMail());
            responses.add(adminOrderResponse);
        });
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
        //品类
        List<String> skuCategoryCodeList = skuList.stream().map(BuySkuEntity::getSkuCategory).collect(Collectors.toList());
        List<BuySkuDictEntity> skuDictByCodes = buySkuDictService.getSkuDictByCodes(skuCategoryCodeList);
        Map<String, List<BuySkuDictEntity>> skuCategoryMap = skuDictByCodes.stream().collect(Collectors.groupingBy(BuySkuDictEntity::getCode));
        //订单明细
        orderDetailList.forEach(d -> {
            OrderDetailInfoResponse orderDetailInfoResponse = BeanUtil.copyProperties(d, OrderDetailInfoResponse.class);
            BuySkuEntity buySkuEntity = skuMap.get(d.getSkuId());
            String skuName = SkuName.getSkuName(buySkuEntity.getSkuName(), LangEnum.ZH_CN.getCode());
            List<BuySkuDictEntity> skuCategoryList = skuCategoryMap.get(buySkuEntity.getSkuCategory());
            String skuCategory = BuySkuDictEntity.getSkuCategoryName(skuCategoryList, LangEnum.ZH_CN.getCode());
            String skuType = SkuType.getSkuType(buySkuEntity.getSkuType(), LangEnum.ZH_CN.getCode());
            orderDetailInfoResponse.setBatchKey(buySkuEntity.getBatchKey());
            orderDetailInfoResponse.setSkuName(skuName);
            orderDetailInfoResponse.setSkuType(skuType);
            orderDetailInfoResponse.setSkuCategory(skuCategory);
            orderDetailInfoResponse.setStock(buySkuEntity.getStock());
            detailInfoResponses.add(orderDetailInfoResponse);
        });
        adminOrderDetailResponse.setOrderDetails(detailInfoResponses);
        log.info("queryDetail response:{}", JSON.toJSONString(JSON.toJSONString(adminOrderDetailResponse)));
        return adminOrderDetailResponse;
    }

    @Override
    public void confirm(AdminOrderConfirmRequest adminOrderConfirmRequest) {
        ActionEnum actionEnum = ActionEnum.getByAction(adminOrderConfirmRequest.getAction());
        String status;
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
        lambdaUpdate()
                .set(BuyOrderEntity::getStatus, status)
                .set(StringUtils.isNotBlank(adminOrderConfirmRequest.getReceiptCertificate()), BuyOrderEntity::getReceiptCertificate, adminOrderConfirmRequest.getReceiptCertificate())
                .set(BuyOrderEntity::getUpdater, CurrentAdminUser.getUserId())
                .eq(BuyOrderEntity::getOrderId, adminOrderConfirmRequest.getOrderId())
                .update();
    }

    public Page<BuyOrderEntity> getOrderPage(AdminOrderRequest adminOrderRequest) {
        Page<BuyOrderEntity> page = new Page<>(adminOrderRequest.getPageNum(), adminOrderRequest.getPageSize());
        return lambdaQuery()
                .eq(BuyOrderEntity::getStatus, adminOrderRequest.getStatus())
                .eq(StringUtils.isNotBlank(adminOrderRequest.getOrderId()), BuyOrderEntity::getOrderId, adminOrderRequest.getOrderId())
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
    public void addRemark(AddOrderRemarkRequest addOrderRemarkRequest) {
        lambdaUpdate()
                .set(StringUtils.isNotBlank(addOrderRemarkRequest.getAdminRemark()), BuyOrderEntity::getAdminRemark, addOrderRemarkRequest.getAdminRemark())
                .set(BuyOrderEntity::getUpdater, CurrentAdminUser.getUserId())
                .eq(BuyOrderEntity::getOrderId, addOrderRemarkRequest.getOrderId())
                .update();
    }

    @Override
    @Transactional
    public void update(List<UpdateOrderDetailRequest> updateOrderDetailRequest) {
        List<BuyOrderDetailEntity> list = new ArrayList<>();
        List<BuyOrderDetailEntity> orderDetails = buyOrderDetailService.getDetailByOrderId(updateOrderDetailRequest.get(0).getOrderId());
        if (CollectionUtils.isEmpty(orderDetails)) {
            throw new BusinessException(9999, "订单号" + updateOrderDetailRequest.get(0).getOrderId() + "未匹配到订单明细");
        }
        Map<String, BuyOrderDetailEntity> orderDetailMap = orderDetails.stream().collect(Collectors.toMap(BuyOrderDetailEntity::getSkuId, contract -> contract, (a, b) -> a));
        updateOrderDetailRequest.forEach(order -> {
            BuyOrderDetailEntity orderDetail = buyOrderDetailService.getById(order.getId());
            if (order.getSkuNum().equals(orderDetail.getSkuNum())) {
                log.info("update order 数量未发生变化, skuId:{}, orderId:{}", order.getSkuId(), order.getOrderId());
                return;
            }
            BuySkuEntity sku = adminSkuService.getSkuBySkuId(order.getSkuId());
            //为0则删除该订单明细
            if (order.getSkuNum() == 0) {
                log.info("update order 删除, skuId:{}, orderId:{}", order.getSkuId(), order.getOrderId());
                deleteOrderDetail(orderDetail.getId(), sku, orderDetail.getSkuNum());
                return;
            }
            //校验库存是否满足
            long num = order.getSkuNum() - orderDetail.getSkuNum();
            checkStock(sku.getId(), sku.getSkuName(), sku.getStock(), num);
            //单行总金额
            String totalAmount = String.valueOf(BigDecimal.valueOf(order.getSkuNum()).multiply(new BigDecimal(orderDetail.getPrice())));
            BuyOrderDetailEntity entity = BuyOrderDetailEntity.builder()
                    .id(order.getId())
                    .skuNum(order.getSkuNum())
                    .totalAmt(totalAmount)
                    .updater(CurrentAdminUser.getUserId())
                    .build();
            list.add(entity);
        });
        if (!CollectionUtils.isEmpty(list)) {
            buyOrderDetailService.updateBatchById(list);
            //更新订单总金额
            updateOrderAmt(updateOrderDetailRequest.get(0).getOrderId());
        }
        //save changeLog
        saveChangeLog(updateOrderDetailRequest, orderDetailMap);
    }

    @Override
    @Transactional
    public void importOrder(String orderId, MultipartFile file) {
        List<ImportOrderDto> doReadSync;
        try {
            doReadSync = EasyExcelFactory.read(file.getInputStream()).head(ImportOrderDto.class).sheet().doReadSync();
            if (CollectionUtils.isEmpty(doReadSync)) {
                return;
            }
        } catch (Exception ex) {
            log.error("importOrder fail", ex);
            throw new BusinessException(9999, "导入失败");
        }
        if (CollectionUtils.isEmpty(doReadSync)) {
            throw new BusinessException(9999, "excel表格内容为空");
        }
        doReadSync = doReadSync.stream().filter(d -> StringUtils.isNotBlank(d.getSkuId()) && d.getSkuNum() != null && StringUtils.isNotBlank(d.getPrice())).collect(Collectors.toList());
        List<String> orderIds = doReadSync.stream().map(ImportOrderDto::getOrderId).distinct().collect(Collectors.toList());
        if (orderIds.size() > 1) {
            throw new BusinessException(9999, "不支持不同订单号导入");
        }
        if (!orderIds.get(0).equals(orderId)) {
            throw new BusinessException(9999, "当前仅可导入的订单号为："+orderId);
        }
        List<BuyOrderDetailEntity> orderDetails = buyOrderDetailService.getDetailByOrderId(orderIds.get(0));
        if (CollectionUtils.isEmpty(orderDetails)) {
            throw new BusinessException(9999, "订单号" + orderIds.get(0) + "未匹配到订单明细");
        }
        Map<String, BuyOrderDetailEntity> orderDetailMap = orderDetails.stream().collect(Collectors.toMap(BuyOrderDetailEntity::getSkuId, contract -> contract, (a, b) -> a));
        List<BuyOrderDetailEntity> entities = new ArrayList<>();
        doReadSync.forEach(order -> {
            BuyOrderDetailEntity orderDetail = orderDetailMap.get(order.getSkuId());
            BuySkuEntity sku = adminSkuService.getSkuBySkuId(order.getSkuId());
            //过滤数量为0的数据
            if (order.getSkuNum() == 0 && orderDetail == null) {
                return;
            }
            //为0则删除该订单明细
            if (order.getSkuNum() == 0 && orderDetail != null) {
                deleteOrderDetail(orderDetail.getId(), sku, orderDetail.getSkuNum());
                return;
            }
            //校验库存是否满足
            long num = order.getSkuNum() - (orderDetail != null ? orderDetail.getSkuNum() : 0);
            checkStock(sku.getId(), sku.getSkuName(), sku.getStock(), num);
            //单行总金额
            String totalAmount = String.valueOf(BigDecimal.valueOf(order.getSkuNum()).multiply(new BigDecimal(order.getPrice())));
            BuyOrderDetailEntity entity = BuyOrderDetailEntity.builder()
                    .orderId(order.getOrderId())
                    .skuId(order.getSkuId())
                    .skuNum(order.getSkuNum())
                    .price(order.getPrice())
                    .totalAmt(totalAmount)
                    .status(OrderStatusEnum.NEED_CONFIRM.getCode())
                    .currency(order.getCurrency())
                    .creator(CurrentAdminUser.getUserId())
                    .updater(CurrentAdminUser.getUserId())
                    .build();
            if (orderDetail != null) {
                entity.setId(orderDetail.getId());
                entity.setCreator(orderDetail.getCreator());
            }
            entities.add(entity);
        });
        buyOrderDetailService.saveOrUpdateBatch(entities);
        //更新订单总金额
        updateOrderAmt(orderIds.get(0));
        //save changeLog
        saveImportChangeLog(doReadSync, orderDetailMap);
    }

    private void updateOrderAmt(String orderId) {
        //更新订单总金额
        List<BuyOrderDetailEntity> detailList = buyOrderDetailService.getDetailByOrderId(orderId);
        BigDecimal orderAmt = detailList.stream().map(BuyOrderDetailEntity::getTotalAmt).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
        lambdaUpdate()
                .set(BuyOrderEntity::getOrderAmt, orderAmt)
                .set(BuyOrderEntity::getUpdater, CurrentAdminUser.getUserId())
                .eq(BuyOrderEntity::getOrderId, orderId)
                .update();
    }

    private void deleteOrderDetail(Long id, BuySkuEntity sku, Long skuNum) {
        BuyOrderDetailEntity entity = BuyOrderDetailEntity.builder()
                .id(id)
                .updater(CurrentAdminUser.getUserId())
                .isDeleted(BooleanUtil.toInteger(true)).build();
        buyOrderDetailService.updateById(entity);
        //返回库存
        adminSkuService.updateStock(sku.getId(), -skuNum);
    }

    @Override
    public void export(String orderId, HttpServletResponse response) {
        List<BuyOrderDetailEntity> orderDetails = buyOrderDetailService.getDetailByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetails)) {
            throw new BusinessException(9999, "未查询到数据");
        }
        BuyOrderDetailEntity entity = orderDetails.get(0);
        BuyOrderEntity order = lambdaQuery().eq(BuyOrderEntity::getOrderId, entity.getOrderId()).one();
        BuyUserEntity user = buyUserService.getUserByUserId(order.getUserId());
        //sku
        List<String> skuIds = orderDetails.stream().map(BuyOrderDetailEntity::getSkuId).distinct().collect(Collectors.toList());
        List<BuySkuEntity> skuList = adminSkuService.getSkuBySkuIdList(skuIds);
        Map<String, BuySkuEntity> skuMap = skuList.stream().collect(Collectors.toMap(BuySkuEntity::getSkuId, contract -> contract, (a, b) -> a));
        //品类
        List<String> skuCategoryCodeList = skuList.stream().map(BuySkuEntity::getSkuCategory).collect(Collectors.toList());
        List<BuySkuDictEntity> skuDictByCodes = buySkuDictService.getSkuDictByCodes(skuCategoryCodeList);
        Map<String, List<BuySkuDictEntity>> skuCategoryMap = skuDictByCodes.stream().collect(Collectors.groupingBy(BuySkuDictEntity::getCode));
        List<ExportOrderDetailInfoDto> list = new ArrayList<>();
        orderDetails.forEach(o -> {
            BuySkuEntity buySkuEntity = skuMap.get(o.getSkuId());
            //商品名称
            String skuName = SkuName.getSkuName(buySkuEntity.getSkuName(), LangEnum.ZH_CN.getCode());
            //品类名称
            List<BuySkuDictEntity> skuCategoryList = skuCategoryMap.get(buySkuEntity.getSkuCategory());
            String skuCategory = BuySkuDictEntity.getSkuCategoryName(skuCategoryList, LangEnum.ZH_CN.getCode());
            //款式
            String skuType = SkuType.getSkuType(buySkuEntity.getSkuType(), LangEnum.ZH_CN.getCode());
            ExportOrderDetailInfoDto detailInfoDto = ExportOrderDetailInfoDto.builder()
                    .orderId(orderId)
                    .userId(user.getUserId())
                    .mail(user.getMail())
                    .skuId(buySkuEntity.getSkuId())
                    .skuName(skuName)
                    .skuCategory(skuCategory)
                    .skuType(skuType)
                    .price(o.getPrice())
                    .totalAmt(o.getTotalAmt())
                    .currency(o.getCurrency())
                    .skuNum(o.getSkuNum())
                    .build();
            list.add(detailInfoDto);
        });
        //导出最后一行为合计
        BigDecimal orderAmt = list.stream().map(ExportOrderDetailInfoDto::getTotalAmt).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
        Long totalSkuNum = list.stream().mapToLong(ExportOrderDetailInfoDto::getSkuNum).sum();
        ExportOrderDetailInfoDto sumDetail = ExportOrderDetailInfoDto.builder()
                .totalAmt(String.valueOf(orderAmt))
                .skuNum(totalSkuNum)
                .build();
        list.add(sumDetail);
        ExcelUtil.writeExcel(response, orderId, ExportOrderDetailInfoDto.class, list);
    }

    private void checkStock(Long id, String skuName, String stock, Long num) {
        //校验库存是否满足
        if (Long.parseLong(stock) < num) {
            throw new BusinessException(9999, "【" + skuName + "】" + "库存不足");
        }
        //更新sku库存
        boolean b = adminSkuService.updateStock(id, num);
        if (!b) {
            throw new BusinessException(9999, "【" + skuName + "】" + "库存不足");
        }
    }

    private void saveChangeLog(List<UpdateOrderDetailRequest> updateOrderDetailRequest, Map<String, BuyOrderDetailEntity> orderDetailMap) {
        List<BuyOrderChangeLogEntity> logEntityList = new ArrayList<>();
        updateOrderDetailRequest.forEach(newOrder -> {
            BuyOrderDetailEntity newOrderEntity = BeanUtil.copyProperties(newOrder, BuyOrderDetailEntity.class);
            BuyOrderDetailEntity oldOrderEntity = orderDetailMap.get(newOrder.getSkuId());
            buildChangeLog(logEntityList, oldOrderEntity, newOrderEntity);
        });
        if (!CollectionUtils.isEmpty(logEntityList)) {
            buyOrderChangeLogService.saveBatch(logEntityList);
        }
    }

    private void saveImportChangeLog(List<ImportOrderDto> doReadSync, Map<String, BuyOrderDetailEntity> orderDetailMap) {
        List<BuyOrderChangeLogEntity> logEntityList = new ArrayList<>();
        doReadSync.forEach(newOrder -> {
            BuyOrderDetailEntity newOrderEntity = BeanUtil.copyProperties(newOrder, BuyOrderDetailEntity.class);
            BuyOrderDetailEntity oldOrderEntity = orderDetailMap.get(newOrder.getSkuId());
            buildChangeLog(logEntityList, oldOrderEntity, newOrderEntity);
        });
        if (!CollectionUtils.isEmpty(logEntityList)) {
            buyOrderChangeLogService.saveBatch(logEntityList);
        }
    }

    private void buildChangeLog(List<BuyOrderChangeLogEntity> logEntityList, BuyOrderDetailEntity oldOrderEntity, BuyOrderDetailEntity newOrderEntity) {
        if (newOrderEntity.getSkuNum() == 0 && oldOrderEntity == null) {
            return;
        }
        BuyOrderChangeLogEntity changeLog = new BuyOrderChangeLogEntity();
        changeLog.setOrderId(newOrderEntity.getOrderId());
        changeLog.setSkuId(newOrderEntity.getSkuId());
        changeLog.setCreator(CurrentAdminUser.getUserId());
        //diff
        Map<String, ChangeKeyValueDto> changeLogMap = diffOrder(oldOrderEntity, newOrderEntity);
        changeLog.setChangeLog(JSON.toJSONString(changeLogMap));
        //新增
        if (oldOrderEntity == null || StringUtils.isBlank(oldOrderEntity.getSkuId())) {
            changeLog.setChangeType("ADD");
        }
        //删除
        else if (newOrderEntity.getSkuNum() == 0) {
            changeLog.setChangeType("DELETE");
        }
        //修改
        else if (changeLogMap.size() > 0) {
            changeLog.setChangeType("UPDATE");
        }
        logEntityList.add(changeLog);
    }

    private Map<String, ChangeKeyValueDto> diffOrder(BuyOrderDetailEntity oldOrder, BuyOrderDetailEntity newOrder) {
        Map<String, ChangeKeyValueDto> changeLogMap = new HashMap<>();
        if (oldOrder == null) {
            oldOrder = new BuyOrderDetailEntity();
        }
        if (StringUtils.isNotBlank(newOrder.getPrice()) && !newOrder.getPrice().equals(oldOrder.getPrice())) {
            changeLogMap.put("price", ChangeKeyValueDto.builder().oldValue(oldOrder.getPrice()).newValue(newOrder.getPrice()).build());
        }
        if (newOrder.getSkuNum() != null && !newOrder.getSkuNum().equals(oldOrder.getSkuNum())) {
            changeLogMap.put("skuNum", ChangeKeyValueDto.builder().oldValue(String.valueOf(oldOrder.getSkuNum())).newValue(String.valueOf(newOrder.getSkuNum())).build());
        }
        if (StringUtils.isNotBlank(newOrder.getCurrency()) && !newOrder.getCurrency().equals(oldOrder.getCurrency())) {
            changeLogMap.put("currency", ChangeKeyValueDto.builder().oldValue(oldOrder.getCurrency()).newValue(newOrder.getCurrency()).build());
        }
        return changeLogMap;
    }
}
