package org.buy.life.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * @Author derek_mao
 * @Date 2023/2/10
 */
public class BeanConvertUtils extends BeanUtils {
    /**
     * @param source   源对象
     * @param target  目标对象
     * @return {@link T}目标对象
     */
    public static <S, T> T convertTo(S source, Supplier<T> target) {
        return convertTo(source, target, null);
    }

    /**
     * 转换对象
     *
     * @param source         源对象
     * @param target 目标对象
     * @param callBack       回调方法
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @return {@link T}     目标对象
     */
    public static <S, T> T convertTo(S source, Supplier<T> target, ConvertCallBack<S, T> callBack) {
        if (null == source || null == target) {
            return null;
        }
        T targetOut = target.get();
        CustomizeBeanUtils.copyProperties(source, targetOut);
        if (callBack != null) {
            callBack.callBack(source, targetOut);
        }
        return targetOut;
    }

    /**
     * @param sources   源对象
     * @param target   目标对象
     * @return {@link List} 目标对象list
     */
    public static <S, T> List<T> convertListTo(List<S> sources, Supplier<T> target) {
        return convertListTo(sources, target, null);
    }

    /**
     * 转换对象
     *
     * @param sources        源对象list
     * @param target 目标对象供应方
     * @param callBack       回调方法
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @return {@link List}  目标对象list
     */
    public static <S, T> List<T> convertListTo(List<S> sources, Supplier<T> target, ConvertCallBack<S, T> callBack) {
        if (CollectionUtils.isEmpty(sources) || null == target) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T targetOut = target.get();
            CustomizeBeanUtils.copyProperties(source, targetOut);
            if (callBack != null) {
                callBack.callBack(source, targetOut);
            }
            list.add(targetOut);
        }
        return list;
    }

    /**
     * 回调接口
     *
     * @param <S> 源对象类型
     * @param <T> 目标对象类型
     */
    @FunctionalInterface
    public interface ConvertCallBack<S, T> {
        void callBack(S t, T s);
    }
}
