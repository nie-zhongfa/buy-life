package org.buy.life.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.buy.life.exception.BusinessException;
import org.buy.life.exception.ServerCodeEnum;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author pleiades
 * @version \: BeanCopies.java,v 0.1 2021-09-15 13:02
 */
@Slf4j
public class BeanCopiesUtils {
    /**
     * 注册BeanCopier对象缓存 key: sourceClassName + "->" + targetClassName value: BeanCopier
     */
    private static final Map<String, BeanCopier> CACHE_MAP = new ConcurrentHashMap<>();

    public static <S, T> T copy(S sourceObj, Class<T> targetClass) {
        if (sourceObj == null || targetClass == null) {
            return null;
        }
        Class<?> sourceClass = sourceObj.getClass();
        String key = getKey(sourceClass, targetClass);
        BeanCopier beanCopier = CACHE_MAP.get(key);
        if (beanCopier == null) {
            beanCopier = BeanCopier.create(sourceClass, targetClass, false);
            CACHE_MAP.put(key, beanCopier);
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            beanCopier.copy(sourceObj, target, null);
            return target;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException e) {
            log.warn("bean拷贝失败,系统异常!!", e);
            throw BusinessException.code(ServerCodeEnum.INTERNAL_SERVER_ERROR);
        }
    }

    private static <S, T> String getKey(Class<S> sourceClass, Class<T> targetClass) {
        return sourceClass.getName() + "->" + targetClass.getName();
    }

    public static <S, T> List<T> copyList(Collection<S> sourceList, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Lists.newArrayList();
        }
        return sourceList.stream().map(sourceObj -> BeanCopiesUtils.copy(sourceObj, targetClass))
                .collect(Collectors.toList());
    }

    public static <T> List<T> deepCopy(List<T> src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            List<T> dest = (List<T>) in.readObject();
            return dest;
        } catch (Exception e) {
            log.warn("深拷贝失败,系统异常!!", e);
            throw BusinessException.code(ServerCodeEnum.INTERNAL_SERVER_ERROR);
        }
    }
}
