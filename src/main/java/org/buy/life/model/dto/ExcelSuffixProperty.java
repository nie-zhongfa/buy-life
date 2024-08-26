package org.buy.life.model.dto;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSuffixProperty {

    String[] value() default {""};

    /**
     * 图片在第几列 1开始
     * @return
     */
    int index() default -1;
}
