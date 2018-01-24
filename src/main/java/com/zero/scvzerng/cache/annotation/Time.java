package com.zero.scvzerng.cache.annotation;

import com.zero.scvzerng.cache.enums.TimeType;

import java.lang.annotation.*;


@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Time {
    long value() default 0;
    TimeType type() default TimeType.SECONDS;
}
