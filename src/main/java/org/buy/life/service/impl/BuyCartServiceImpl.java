package org.buy.life.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.assertj.core.util.Lists;
import org.buy.life.constant.CartStatusEnum;
import org.buy.life.constant.SkuStatusEnum;
import org.buy.life.entity.BuyCartEntity;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.req.BuyCartReq;
import org.buy.life.entity.resp.BuyCartResp;
import org.buy.life.mapper.BuyCartMapper;
import org.buy.life.model.request.SkuPrice;
import org.buy.life.service.IBuyCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.buy.life.service.IBuySkuService;
import org.buy.life.utils.TtlUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author kavin
 * @since 2024-08-21
 */
@Service
public class BuyCartServiceImpl extends ServiceImpl<BuyCartMapper, BuyCartEntity> implements IBuyCartService {

    @Resource
    private IBuySkuService buySkuService;

    @Override
    public BuyCartResp cartList(){
        String userId = TtlUtils.getSPCtx().getUserId();
        LambdaQueryWrapper<BuyCartEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BuyCartEntity::getUserId,userId)
                .eq(BuyCartEntity::getIsDeleted,0)
                .in(BuyCartEntity::getStatus,Lists.newArrayList(CartStatusEnum.JOINED));
        List<BuyCartEntity> list = list(wrapper);
        if(CollectionUtils.isEmpty(list)){
            return new BuyCartResp();
        }
        List<String> skuList = list.stream().map(BuyCartEntity::getSkuId).collect(Collectors.toList());

        LambdaQueryWrapper<BuySkuEntity> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.eq(BuySkuEntity::getIsDeleted,0)
                .in(BuySkuEntity::getSkuId,skuList)
                .in(BuySkuEntity::getStatus, Lists.newArrayList(SkuStatusEnum.LISTED));

        List<BuySkuEntity> skuEntities = buySkuService.list(skuWrapper);

        if(CollectionUtils.isEmpty(skuEntities)){
            return new BuyCartResp();
        }

        Map<String, BuySkuEntity> skuMap = skuEntities.stream().collect(Collectors.toMap(BuySkuEntity::getSkuId, Function.identity(), (key1, key2) -> key2));

        BuyCartResp buyCartResp=new BuyCartResp();

        BigDecimal totalAmt=new BigDecimal(0);

        for (BuyCartEntity cart:list){
            BuySkuEntity buySkuEntity=skuMap.get(cart.getSkuId());
            if(Objects.isNull(buySkuEntity)){
                continue;
            }
            List<SkuPrice> skuPrice = JSONObject.parseArray(buySkuEntity.getPrice(), SkuPrice.class);

            Map<String, SkuPrice> skuPriceMap = skuPrice.stream().collect(Collectors.toMap(SkuPrice::getCurrency,
                    Function.identity(), (key1, key2) -> key2));
            
            String price=skuPriceMap.get(TtlUtils.getSPCtx().getCurrency()).getSkuPrice();

            String skuAmt = new BigDecimal(price).multiply(new BigDecimal(cart.getSkuNum())).setScale(2, RoundingMode.HALF_UP) + "";

            BuyCartResp.CartSku build = BuyCartResp.CartSku.builder().skuId(cart.getSkuId())
                    .stock(buySkuEntity.getStock()).skuName(buySkuEntity.getSkuName()).price(buySkuEntity.getPrice())
                    .skuNum(cart.getSkuNum()).skuAmt(skuAmt)
                    .build();
            buyCartResp.getCartSkuLists().add(build);
            totalAmt=totalAmt.add(new BigDecimal(skuAmt));
        }
        buyCartResp.setTotalAmt(totalAmt.toString());
        return buyCartResp;
    }




    @Override
    public void joinCart(BuyCartReq buyCartReq){
        String userId = TtlUtils.getSPCtx().getUserId();
        LambdaQueryWrapper<BuyCartEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BuyCartEntity::getUserId,userId)
                .eq(BuyCartEntity::getIsDeleted,0)
                .eq(BuyCartEntity::getSkuId,buyCartReq.getSkuId())
                .in(BuyCartEntity::getStatus,Lists.newArrayList(CartStatusEnum.JOINED.getCode()));
        BuyCartEntity buyCartEntity = getOne(wrapper);



        if(Objects.isNull(buyCartEntity)&&buyCartReq.getSkuNum().longValue()!=0l){
            save(BuyCartEntity.builder().userId(TtlUtils.getSPCtx().getUserId()).ctime(LocalDateTime.now()).creator(TtlUtils.getSPCtx().getUserId())
                    .isDeleted(0).skuId(buyCartReq.getSkuId()).skuNum(buyCartReq.getSkuNum()).status(CartStatusEnum.JOINED.getCode()).build());
            return;
        }

        if(Objects.nonNull(buyCartEntity)){
            if(buyCartReq.getSkuNum().longValue()!=0){
                buyCartEntity.setSkuNum(buyCartReq.getSkuNum());
            }else {
                buyCartEntity.setIsDeleted(1);
            }
            updateById(buyCartEntity);
        }
    }
}
