package org.buy.life.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.buy.life.entity.BuyCategoryEntity;
import org.buy.life.entity.BuySeriesEntity;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.req.BuySkuReq;
import org.buy.life.entity.req.PageBasicReq;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.mapper.BuySeriesMapper;
import org.buy.life.model.enums.CurrencyEnum;
import org.buy.life.model.request.SkuPrice;
import org.buy.life.service.IBuyCategoryService;
import org.buy.life.service.IBuySeriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.buy.life.service.IBuySkuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 系列 服务实现类
 * </p>
 *
 * @author MrWu
 * @since 2024-11-04
 */
@Service
public class BuySeriesServiceImpl extends ServiceImpl<BuySeriesMapper, BuySeriesEntity> implements IBuySeriesService {

    @Resource
    private IBuySkuService buySkuService;

    @Resource
    private IBuyCategoryService buyCategoryService;

    @Override
    public SimplePage<BuySeriesEntity> getSeriesList(PageBasicReq<BuySkuReq> buySkuReq) {
        LambdaQueryWrapper<BuySeriesEntity> seriesWrapper = new LambdaQueryWrapper<>();
        seriesWrapper.eq(BuySeriesEntity::getIsDeleted, false);
        if(Objects.nonNull(buySkuReq.getCondition())&&StringUtils.isNotEmpty(buySkuReq.getCondition().getCategoryCode())){
            seriesWrapper.eq(BuySeriesEntity::getCategoryCode,buySkuReq.getCondition().getCategoryCode());
        }
        Page<BuySeriesEntity> page = this.page(new Page<>(buySkuReq.getPageNum(), buySkuReq.getPageSize()), seriesWrapper);
        return getSimplePage(buySkuReq,page);
    }

    private SimplePage<BuySeriesEntity> getSimplePage(PageBasicReq<BuySkuReq> buySkuReq,Page<BuySeriesEntity> page ) {
        if (page == null) {
            return new SimplePage<>(Collections.emptyList(), buySkuReq.getPageNum(), buySkuReq.getPageSize(), 0L);
        }
        List<String> collect = ListUtils.emptyIfNull(page.getRecords()).stream().map(BuySeriesEntity::getSeriesCode).collect(Collectors.toList());
        List<BuySkuEntity> bySeriesCodes = buySkuService.getBySeriesCodes(collect);
        Map<String, List<BuySkuEntity>> skuMap = ListUtils.emptyIfNull(bySeriesCodes).stream().filter(s -> StringUtils.isNotEmpty(s.getSeriesCode())).collect(Collectors.groupingBy(BuySkuEntity::getSeriesCode));

        List<String> categoryCode = ListUtils.emptyIfNull(page.getRecords()).stream().map(BuySeriesEntity::getCategoryCode).collect(Collectors.toList());
        List<BuyCategoryEntity> categoryList = buyCategoryService.getCategoryList(categoryCode);
        Map<String, BuyCategoryEntity> categoryMap = ListUtils.emptyIfNull(categoryList).stream()
                .collect(Collectors.toMap(BuyCategoryEntity::getCategoryCode, Function.identity(),(buyCategoryEntity, buyCategoryEntity2) ->buyCategoryEntity2 ));

        page.getRecords().stream().filter(p->StringUtils.isNotEmpty(p.getSeriesCode())&&StringUtils.isNotEmpty(p.getCategoryCode())).forEach(s->{
            if(CollectionUtils.isEmpty(skuMap.get(s.getSeriesCode()))){
                return;
            }
            List<BuySkuEntity> skuEntities = skuMap.get(s.getSeriesCode());
            calculate(skuEntities,s);
            if(Objects.nonNull(categoryMap.get(s.getCategoryCode()))){
                s.setCategoryName(s.getCategoryName());
            }
        });
        return new SimplePage<>(page.getRecords(), buySkuReq.getPageNum(), buySkuReq.getPageSize(), page.getTotal());
    }


    private void calculate(List<BuySkuEntity> skuEntities,BuySeriesEntity buySeriesEntity){
        List<SkuPrice> minList=new ArrayList<>();
        List<SkuPrice> maxList=new ArrayList<>();
        List<SkuPrice> retailMinList=new ArrayList<>();
        List<SkuPrice> retailMaxList=new ArrayList<>();
        for (CurrencyEnum currencyEnum : CurrencyEnum.values()) {
            String min = skuEntities.stream().filter(k -> StringUtils.isNotEmpty(k.getPrice())).map(k -> {
                return SkuPrice.getSkuPrice(k.getPrice(), currencyEnum.getCode());
            }).min(Comparator.comparing(m -> {
                try {
                    return  Double.parseDouble(m);
                }catch (Exception e){
                    return new Double(0.00);
                }
            })).orElse("0.00");

            String max = skuEntities.stream().filter(k -> StringUtils.isNotEmpty(k.getPrice())).map(k -> {
                return SkuPrice.getSkuPrice(k.getPrice(), currencyEnum.getCode());
            }).max(Comparator.comparing(m -> {
                try {
                    return  Double.parseDouble(m);
                }catch (Exception e){
                    return new Double(0.00);
                }
            })).orElse("0.00");


            String retailMin = skuEntities.stream().filter(k -> StringUtils.isNotEmpty(k.getRetailPrice())).map(k -> {
                return SkuPrice.getSkuPrice(k.getPrice(), currencyEnum.getCode());
            }).min(Comparator.comparing(m -> {
                try {
                    return  Double.parseDouble(m);
                }catch (Exception e){
                    return new Double(0.00);
                }
            })).orElse("0.00");

            String retailMax = skuEntities.stream().filter(k -> StringUtils.isNotEmpty(k.getRetailPrice())).map(k -> {
                return SkuPrice.getSkuPrice(k.getPrice(), currencyEnum.getCode());
            }).max(Comparator.comparing(m -> {
                try {
                    return  Double.parseDouble(m);
                }catch (Exception e){
                    return new Double(0.00);
                }
            })).orElse("0.00");


            SkuPrice skuPrice1=new SkuPrice();
            skuPrice1.setCurrency(currencyEnum.getCode());
            skuPrice1.setSkuPrice(min);
            minList.add(skuPrice1);
            SkuPrice skuPrice3=new SkuPrice();
            skuPrice3.setCurrency(currencyEnum.getCode());
            skuPrice3.setSkuPrice(retailMin);
            retailMinList.add(skuPrice3);

            SkuPrice skuPrice2=new SkuPrice();
            skuPrice2.setCurrency(currencyEnum.getCode());
            skuPrice2.setSkuPrice(max);
            maxList.add(skuPrice2);
            SkuPrice skuPrice4=new SkuPrice();
            skuPrice4.setCurrency(currencyEnum.getCode());
            skuPrice4.setSkuPrice(retailMax);
            retailMaxList.add(skuPrice4);
        }
        buySeriesEntity.setMinPrice(JSONObject.toJSONString(minList));
        buySeriesEntity.setMaxPrice(JSONObject.toJSONString(maxList));
        buySeriesEntity.setRetailMinPrice(JSONObject.toJSONString(retailMinList));
        buySeriesEntity.setRetailMaxPrice(JSONObject.toJSONString(retailMaxList));

    }
}
