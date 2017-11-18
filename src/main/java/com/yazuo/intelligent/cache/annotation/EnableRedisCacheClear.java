package com.yazuo.intelligent.cache.annotation;

import com.yazuo.intelligent.autoconfig.RedisCacheAutoConfiguration;
import com.yazuo.intelligent.cache.ClearTimeBeanPostProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Year: 2017-2017/11/18-20:36
 * Project:xycrm_intelligent_service
 * Package:com.yazuo.intelligent.cache
 * To change this template use File | Settings | File Templates.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ClearTimeBeanPostProcessor.class,RedisCacheAutoConfiguration.class})
public @interface EnableRedisCacheClear {
}
