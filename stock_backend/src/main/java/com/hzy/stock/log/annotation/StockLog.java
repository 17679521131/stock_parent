package com.hzy.stock.log.annotation;

import java.lang.annotation.*;

/**
 * @author daocaoaren
 * @date 2024/7/23 21:03
 * @description :
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StockLog {
    String value() default "";
}
