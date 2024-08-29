package org.buy.life.service.impl;

import org.buy.life.constant.OrderStatusEnum;
import org.buy.life.entity.BuySkuDictEntity;
import org.buy.life.entity.resp.BuySkuDictResp;
import org.buy.life.mapper.BuySkuDictMapper;
import org.buy.life.model.enums.ClassificationEnum;
import org.buy.life.model.enums.CountryEnum;
import org.buy.life.model.enums.CurrencyEnum;
import org.buy.life.model.enums.LangEnum;
import org.buy.life.service.IBuySkuDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.buy.life.utils.BeanCopiesUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 枚举表 服务实现类
 * </p>
 *
 * @author MrWu
 * @since 2024-08-27
 */
@Service
public class BuySkuDictServiceImpl extends ServiceImpl<BuySkuDictMapper, BuySkuDictEntity> implements IBuySkuDictService {

    @Override
    public List<BuySkuDictEntity> getSkuDictByCode(String code) {
        return lambdaQuery().eq(BuySkuDictEntity::getCode, code).eq(BuySkuDictEntity::getIsDeleted, false).list();
    }

    @Override
    public List<BuySkuDictEntity> getSkuDictByCodes(List<String> codes) {
        return lambdaQuery().in(BuySkuDictEntity::getCode, codes).eq(BuySkuDictEntity::getIsDeleted, false).list();
    }

    @Override
    public List<BuySkuDictEntity> getSkuDictList() {
        return lambdaQuery().eq(BuySkuDictEntity::getIsDeleted, false).list();
    }

    @Override
    public List<BuySkuDictEntity> getSkuDictListByLang(String lang) {
        return lambdaQuery().eq(BuySkuDictEntity::getIsDeleted, false).eq(BuySkuDictEntity::getLang, lang).list();
    }
    @Override
    public BuySkuDictResp getAllDict(){
        BuySkuDictResp buySkuDictResp=new BuySkuDictResp();
        List<BuySkuDictEntity> skuDictList = getSkuDictList();
        List<BuySkuDictResp.SkuDict> skuDicts = BeanCopiesUtils.copyList(skuDictList, BuySkuDictResp.SkuDict.class);
        List<BuySkuDictResp.OrderStatusDict> orderStatus = Arrays.stream(OrderStatusEnum.values()).map(o -> {
            BuySkuDictResp.OrderStatusDict orderStatusDict = new BuySkuDictResp.OrderStatusDict();
            orderStatusDict.setCode(o.getCode());
            orderStatusDict.setDesc(o.getDesc());
            return orderStatusDict;
        }).collect(Collectors.toList());

        List<BuySkuDictResp.TitleDict> titleDicts = Arrays.stream(ClassificationEnum.values()).map(o -> {
            BuySkuDictResp.TitleDict titleDict = new BuySkuDictResp.TitleDict();
            titleDict.setCode(o.getCode());
            titleDict.setDesc(o.getDesc());
            return titleDict;
        }).collect(Collectors.toList());

        buySkuDictResp.setTitleDicts(titleDicts);
        buySkuDictResp.setSkuDicts(skuDicts);
        buySkuDictResp.setOrderStatusDicts(orderStatus);
        buySkuDictResp.setCountryDicts(Arrays.stream(CountryEnum.values()).map(o -> {
            BuySkuDictResp.CountryDict dict = new BuySkuDictResp.CountryDict();
            dict.setCode(o.getCode());
            dict.setDesc(o.getDesc());
            return dict;
        }).collect(Collectors.toList()));

        buySkuDictResp.setLangDicts(Arrays.stream(LangEnum.values()).map(o -> {
            BuySkuDictResp.LangDict dict = new BuySkuDictResp.LangDict();
            dict.setCode(o.getCode());
            dict.setDesc(o.getDesc());
            return dict;
        }).collect(Collectors.toList()));

        buySkuDictResp.setCurrencyDicts(Arrays.stream(CurrencyEnum.values()).map(o -> {
            BuySkuDictResp.CurrencyDict dict = new BuySkuDictResp.CurrencyDict();
            dict.setCode(o.getCode());
            dict.setDesc(o.getDesc());
            return dict;
        }).collect(Collectors.toList()));
        return buySkuDictResp;
    }
}
