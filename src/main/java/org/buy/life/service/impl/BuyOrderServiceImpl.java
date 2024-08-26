package org.buy.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.buy.life.constant.OrderStatusEnum;
import org.buy.life.entity.BuyOrderDetailEntity;
import org.buy.life.entity.BuyOrderEntity;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.req.BuyOrderDetailReq;
import org.buy.life.entity.resp.BuyOrderDetailResp;
import org.buy.life.mapper.BuyOrderMapper;
import org.buy.life.service.IBuyOrderDetailService;
import org.buy.life.service.IBuyOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.buy.life.service.IBuySkuService;
import org.buy.life.utils.BeanCopiesUtils;
import org.buy.life.utils.TtlUtils;
import org.springframework.stereotype.Service;

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

    @Override
    public void joinOrder(BuyOrderDetailReq req){
        DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
        String format = LocalDateTime.now().format(yyyyMM);
        String orderId = format+random();
        BuyOrderEntity buyOrder = BuyOrderEntity.builder().orderId(orderId).orderAmt(req.getOrderAmt()).isDeleted(0)
                .ctime(LocalDateTime.now()).lstSubmitTime(req.getSubmitTime()).creator(TtlUtils.getSPCtx().getUserId()).updater(TtlUtils.getSPCtx().getUserId())
                .userId(TtlUtils.getSPCtx().getUserId()).status(OrderStatusEnum.NEED_CONFIRM.getCode()).build();
        
        List<BuyOrderDetailEntity> buyOrderDetailEntityList=new ArrayList<>();

        for (BuyOrderDetailReq.OrderDetail orderDetail:req.getOrderDetails()){
            BuyOrderDetailEntity orderDetailEntity = BuyOrderDetailEntity.builder().orderId(orderId).skuId(orderDetail.getSkuId())
                    .price(orderDetail.getPrice()).skuNum(orderDetail.getSkuNum()).isDeleted(0)
                    .status(OrderStatusEnum.NEED_CONFIRM.getCode()).totalAmt(orderDetail.getTotalAmt())
                    .ctime(LocalDateTime.now()).creator(TtlUtils.getSPCtx().getUserId()).build();
            buyOrderDetailEntityList.add(orderDetailEntity);
        }
        save(buyOrder);
        buyOrderDetailService.saveBatch(buyOrderDetailEntityList);
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
            buyOrderDetailResp.setSubmitTime(buyOrderDetailResp.getSubmitTime());

            List<BuyOrderDetailEntity> buyOrderDetailEntities = detailMap.get(buyOrderEntity.getOrderId());
            List<BuyOrderDetailResp.OrderDetail> orderDetails1 = BeanCopiesUtils.copyList(buyOrderDetailEntities, BuyOrderDetailResp.OrderDetail.class);
            orderDetails1.stream().filter(o->skuMap.containsKey(o.getSkuId())).map(o -> {
                o.setSkuName(skuMap.get(o.getSkuId()).getSkuName());
                return o;
            }).collect(Collectors.toList());
            buyOrderDetailResp.setOrderDetails(orderDetails1);
            buyOrderDetailResps.add(buyOrderDetailResp);
        }
        return buyOrderDetailResps;
    }


    private int random(){
        int min = 100000;
        int max = 999999;

        int randomNumber = ThreadLocalRandom.current().nextInt(min, max + 1);

        log.info("生成的随机数字为：" + randomNumber);

        return randomNumber;
    }

}
