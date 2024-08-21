/*
 * Copyright (c) 2015-2023 BiliBili Inc.
 */

package org.buy.life.utils;

/**
 * @Auther: kavin
 * @Date: 2023/3/13 - 03 - 13 - 13:36
 * @Description: com.bilibili.ops.ehr.bcomm.base.util
 * @version: 1.0
 */
@FunctionalInterface
public interface FutureFunction<T> {
    void apply(T t) throws Exception;
}
