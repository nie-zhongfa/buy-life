/*
 * Copyright (c) 2015-2022 BiliBili Inc.
 */

package org.buy.life.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * ttl 工具类:在使用线程池等会池化复用线程的执行组件情况下
 * ,提供ThreadLocal值的传递功能，解决异步执行时上下文传递的问题
 * 具体使用场景参考:https://github.com/alibaba/transmittable-thread-local
 * </p>
 *
 * @author zhangchuanjin
 * @date 2022/12/22
 */
@Slf4j
public class TtlUtils {
    /**
     * 周期类型TTL上下文
     */
    private static final TransmittableThreadLocal<RequestData> sPCtx = new TransmittableThreadLocal<>();

    /**
     * 获取周期类型上下文
     *
     * @return
     */
    public static RequestData getSPCtx() {
        RequestData data = sPCtx.get();
        return data;
    }

    /**
     * 设置周期类型上下文
     * @param data
     */
    public static void setSPCtx(RequestData data) {
        if (log.isDebugEnabled()) {
            String name = Thread.currentThread().getName();
            log.debug("Thread name is {} okr cycle set ctx {}", name, JSON.toJSON(data));
        }
        sPCtx.set(data);
    }

    /**
     * 移除周期类型上下文
     */
    public static void removeSPCtx() {
        if (log.isDebugEnabled()) {
            String name = Thread.currentThread().getName();
            log.debug("Thread name is {} okr cycle remove ctx {}", name, JSON.toJSON(sPCtx.get()));
        }
        sPCtx.remove();
    }
}
