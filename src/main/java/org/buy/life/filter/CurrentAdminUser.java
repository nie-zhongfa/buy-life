package org.buy.life.filter;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/28 2:43 PM
 * I am a code man ^_^ !!
 */
public class CurrentAdminUser {

    public static TransmittableThreadLocal<String> sPCtx = new TransmittableThreadLocal<>();

    public static String getUserId() {
        return sPCtx.get();
    }

    public static void setUserId(String userId) {
        sPCtx.set(userId);
    }

    public static void remove() {
        sPCtx.remove();
    }
}
