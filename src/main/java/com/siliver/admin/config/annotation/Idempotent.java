package com.siliver.admin.config.annotation;

import java.lang.annotation.*;

/**
 * 分布式锁注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {


}
