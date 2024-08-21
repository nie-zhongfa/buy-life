package org.buy.life.utils;

import java.lang.annotation.*;

/**
 * @Auther: kavin
 * @Date: 2023/2/14 - 02 - 14 - 17:59
 * @Description: com.bilibili.sutra.annotation
 * @version: 1.0
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CopySourceName {
    String value();
}
