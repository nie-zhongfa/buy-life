package org.buy.life.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.buy.life.constant.OrderStatusEnum;
import org.buy.life.entity.*;
import org.buy.life.entity.req.BuyOrderDetailReq;
import org.buy.life.entity.resp.BuyOrderDetailResp;
import org.buy.life.exception.BusinessException;
import org.buy.life.mapper.BuyOrderMapper;
import org.buy.life.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.buy.life.utils.BeanCopiesUtils;
import org.buy.life.utils.TtlUtils;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@Service
@Slf4j
public class BuyOrderServiceImpl extends ServiceImpl<BuyOrderMapper, BuyOrderEntity> implements IBuyOrderService {

    @Resource
    private IBuyOrderDetailService buyOrderDetailService;


    @Resource
    private IBuySkuService skuService;


    @Resource
    private IAdminSkuService adminSkuService;

    @Resource
    private IBuyCartService buyCartService;

    @Resource
    private IBuyOrderChangeLogService buyOrderChangeLogService;



    @Override
    @Transactional
    public String joinOrder(BuyOrderDetailReq req){

        List<String> skus = req.getOrderDetails().stream().map(BuyOrderDetailReq.OrderDetail::getSkuId).collect(Collectors.toList());
        LambdaQueryWrapper<BuySkuEntity> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.in(BuySkuEntity::getSkuId,skus);
        List<BuySkuEntity> skuEntitys = skuService.list(skuWrapper);
        Map<String, BuySkuEntity> skuMap = skuEntitys.stream().collect(Collectors.toMap(BuySkuEntity::getSkuId, Function.identity(), (key1, key2) -> key2));



        DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
        String format = LocalDateTime.now().format(yyyyMM);
        String orderId = format+random();
        BuyOrderEntity buyOrder = BuyOrderEntity.builder().orderId(orderId).orderAmt(req.getOrderAmt()).isDeleted(0)
                .ctime(LocalDateTime.now()).lstSubmitTime(req.getSubmitTime()).creator(TtlUtils.getSPCtx().getUserId()).updater(TtlUtils.getSPCtx().getUserId())
                .userId(TtlUtils.getSPCtx().getUserId()).status(OrderStatusEnum.NEED_CONFIRM.getCode()).
                userRemark(req.getUserRemark()).currency(TtlUtils.getSPCtx().getCurrency()).build();
        
        List<BuyOrderDetailEntity> buyOrderDetailEntityList=new ArrayList<>();

        for (BuyOrderDetailReq.OrderDetail orderDetail:req.getOrderDetails()){
            BuyOrderDetailEntity orderDetailEntity = BuyOrderDetailEntity.builder().orderId(orderId).skuId(orderDetail.getSkuId())
                    .price(orderDetail.getPrice()).skuNum(orderDetail.getSkuNum()).isDeleted(0)
                    .status(OrderStatusEnum.NEED_CONFIRM.getCode()).totalAmt(orderDetail.getTotalAmt())
                    .ctime(LocalDateTime.now()).creator(TtlUtils.getSPCtx().getUserId()).currency(TtlUtils.getSPCtx().getCurrency()).build();
            buyOrderDetailEntityList.add(orderDetailEntity);
            //校验库存是否满足
            BuySkuEntity buySkuEntity = skuMap.get(orderDetail.getSkuId());
            if (Long.parseLong(buySkuEntity.getStock())<orderDetail.getSkuNum()) {
                throw new BusinessException(9999, "【" + buySkuEntity.getSkuName() + "】" + "库存不足");
            }
            //更新sku库存
            boolean b = adminSkuService.updateStock(buySkuEntity.getId(), orderDetail.getSkuNum());
            if (!b) {
                throw new BusinessException(9999, "【" + buySkuEntity.getSkuName() + "】" + "库存不足");
            }
        }
        //更新库存
        save(buyOrder);
        buyOrderDetailService.saveBatch(buyOrderDetailEntityList);
        //购物车移除订单
        buyCartService.removeSku(skus);
        return orderId;
    }


    @Override
    public List<BuyOrderDetailResp> orderList(){
        String userId = TtlUtils.getSPCtx().getUserId();
        LambdaQueryWrapper<BuyOrderEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BuyOrderEntity::getUserId,userId)
                .eq(BuyOrderEntity::getIsDeleted,0);
        List<BuyOrderEntity> orderEntityList = list(wrapper);

        List<String> orderIds = orderEntityList.stream().map(BuyOrderEntity::getOrderId).collect(Collectors.toList());

        LambdaQueryWrapper<BuyOrderDetailEntity> orderDetalWrapper = new LambdaQueryWrapper<>();
        orderDetalWrapper.in(BuyOrderDetailEntity::getOrderId,orderIds)
                .eq(BuyOrderDetailEntity::getIsDeleted,0);
        List<BuyOrderDetailEntity> orderDetails = buyOrderDetailService.list(orderDetalWrapper);

        List<String> skus = orderDetails.stream().map(BuyOrderDetailEntity::getSkuId).collect(Collectors.toList());
        LambdaQueryWrapper<BuySkuEntity> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.in(BuySkuEntity::getSkuId,skus);
        List<BuySkuEntity> skuEntitys = skuService.list(skuWrapper);
        Map<String, BuySkuEntity> skuMap = skuEntitys.stream().collect(Collectors.toMap(BuySkuEntity::getSkuId, Function.identity(), (key1, key2) -> key2));


        Map<String, List<BuyOrderDetailEntity>> detailMap = orderDetails.stream().collect(Collectors.groupingBy(BuyOrderDetailEntity::getOrderId));

        List<BuyOrderDetailResp> buyOrderDetailResps=new ArrayList<> ();
        for (BuyOrderEntity buyOrderEntity:orderEntityList){
            BuyOrderDetailResp buyOrderDetailResp=new BuyOrderDetailResp();
            buyOrderDetailResp.setOrderAmt(buyOrderEntity.getOrderAmt());
            buyOrderDetailResp.setSubmitTime(buyOrderEntity.getLstSubmitTime());
            buyOrderDetailResp.setOrderId(buyOrderEntity.getOrderId());
            buyOrderDetailResp.setCurrency(buyOrderEntity.getCurrency());
            buyOrderDetailResp.setAdminRemark(buyOrderEntity.getAdminRemark());
            buyOrderDetailResp.setUserRemark(buyOrderEntity.getUserRemark());
            buyOrderDetailResp.setStatus(buyOrderEntity.getStatus());
            List<BuyOrderDetailEntity> buyOrderDetailEntities = detailMap.get(buyOrderEntity.getOrderId());
            List<BuyOrderDetailResp.OrderDetail> orderDetails1 = BeanCopiesUtils.copyList(buyOrderDetailEntities, BuyOrderDetailResp.OrderDetail.class);
            orderDetails1.stream().filter(o->skuMap.containsKey(o.getSkuId())).map(o -> {
                o.setSkuName(skuMap.get(o.getSkuId()).getSkuName());
                o.setSkuType(skuMap.get(o.getSkuId()).getSkuType());
                o.setBatchKey(skuMap.get(o.getSkuId()).getBatchKey());
                o.setSkuCategory(skuMap.get(o.getSkuId()).getSkuCategory());
                o.setClassification(skuMap.get(o.getSkuId()).getClassification());
                return o;
            }).collect(Collectors.toList());
            buyOrderDetailResp.setOrderDetails(orderDetails1);
            buyOrderDetailResps.add(buyOrderDetailResp);
        }
        return buyOrderDetailResps;
    }




    @Override
    public BuyOrderDetailResp orderDetail(String orderId, String userId){
        LambdaQueryWrapper<BuyOrderEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(userId), BuyOrderEntity::getUserId,userId)
                .eq(BuyOrderEntity::getIsDeleted,0)
                .eq(BuyOrderEntity::getOrderId,orderId);

        BuyOrderEntity orderEntity = getOne(wrapper);

        LambdaQueryWrapper<BuyOrderDetailEntity> orderDetailWrapper = new LambdaQueryWrapper<>();
        orderDetailWrapper.eq(BuyOrderDetailEntity::getOrderId,orderEntity.getOrderId())
                .eq(BuyOrderDetailEntity::getIsDeleted,0);
        List<BuyOrderDetailEntity> orderDetails = buyOrderDetailService.list(orderDetailWrapper);

        List<String> skus = orderDetails.stream().map(BuyOrderDetailEntity::getSkuId).collect(Collectors.toList());
        LambdaQueryWrapper<BuySkuEntity> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.in(BuySkuEntity::getSkuId,skus);
        List<BuySkuEntity> skuEntitys = skuService.list(skuWrapper);
        Map<String, BuySkuEntity> skuMap = skuEntitys.stream().collect(Collectors.toMap(BuySkuEntity::getSkuId, Function.identity(), (key1, key2) -> key2));

        BuyOrderDetailResp buyOrderDetailResp=new BuyOrderDetailResp();
        buyOrderDetailResp.setOrderAmt(orderEntity.getOrderAmt());
        buyOrderDetailResp.setSubmitTime(orderEntity.getLstSubmitTime());
        buyOrderDetailResp.setOrderId(orderEntity.getOrderId());
        buyOrderDetailResp.setCurrency(orderEntity.getCurrency());
        buyOrderDetailResp.setAdminRemark(orderEntity.getAdminRemark());
        buyOrderDetailResp.setUserRemark(orderEntity.getUserRemark());
        buyOrderDetailResp.setStatus(orderEntity.getStatus());
        List<BuyOrderDetailResp.OrderDetail> orderDetails1 = BeanCopiesUtils.copyList(orderDetails, BuyOrderDetailResp.OrderDetail.class);
        orderDetails1.stream().filter(o->skuMap.containsKey(o.getSkuId())).map(o -> {
            o.setSkuName(skuMap.get(o.getSkuId()).getSkuName());
            o.setSkuType(skuMap.get(o.getSkuId()).getSkuType());
            o.setBatchKey(skuMap.get(o.getSkuId()).getBatchKey());
            o.setSkuCategory(skuMap.get(o.getSkuId()).getSkuCategory());
            o.setClassification(skuMap.get(o.getSkuId()).getClassification());
            return o;
        }).collect(Collectors.toList());
        buyOrderDetailResp.setOrderDetails(orderDetails1);
        return buyOrderDetailResp;
    }

    @Override
    public List<BuyOrderDetailResp> orderHis(String orderId){
        LambdaQueryWrapper<BuyOrderChangeLogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BuyOrderChangeLogEntity::getOrderId,orderId)
                .eq(BuyOrderChangeLogEntity::getIsDeleted,0).orderByAsc(BuyOrderChangeLogEntity::getCtime);
        List<BuyOrderChangeLogEntity> list = buyOrderChangeLogService.list(wrapper);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }

        List<BuyOrderDetailResp> collect = list.stream().map(l -> {
            BuyOrderDetailResp buyOrderDetailResp = JSONObject.parseObject(l.getChangeLog(), BuyOrderDetailResp.class);
            buyOrderDetailResp.setChangeTime(l.getCtime());
            return buyOrderDetailResp;
        }).collect(Collectors.toList());
        return collect;
    }



    private int random(){
        int min = 100000;
        int max = 999999;

        int randomNumber = ThreadLocalRandom.current().nextInt(min, max + 1);

        log.info("生成的随机数字为：" + randomNumber);

        return randomNumber;
    }

}
