package com.yazuo.intelligent.cache.annotation;

import com.yazuo.intelligent.cache.enums.TimeType;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Year: 2017-2017/11/18-20:34
 * Project:xycrm_intelligent_service
 * Package:com.yazuo.intelligent.cache
 * To change this template use File | Settings | File Templates.
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Time {
    long value() default 0;
    TimeType type() default TimeType.SECONDS;
}
