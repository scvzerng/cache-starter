package com.yazuo.intelligent.cache;

import com.yazuo.intelligent.cache.annotation.Time;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Arrays.stream;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Year: 2017-2017/11/18-20:32
 * Project:xycrm_intelligent_service
 * Package:com.yazuo.intelligent.cache
 * To change this template use File | Settings | File Templates.
 */

public class ClearTimeBeanPostProcessor implements BeanPostProcessor,ApplicationListener<ApplicationReadyEvent>,ApplicationContextAware {
    //key的失效时间
    private static final Map<String,Long> CLEAR_TIMES = new HashMap<>();
    private static final List<Class<? extends Annotation>> FILTER_ANNOTATIONS = new ArrayList<>();
    private ApplicationContext context;
    static {
        FILTER_ANNOTATIONS.add(RestController.class);
        FILTER_ANNOTATIONS.add(Service.class);
        FILTER_ANNOTATIONS.add(Component.class);
        FILTER_ANNOTATIONS.add(Repository.class);
    }



    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(filter(bean.getClass())){
            stream(bean.getClass().getMethods())
                    .filter(this::isFilterMethod)
                    .map(this::getMethodCacheTime)
                    .forEach(cacheClearTime ->
                            stream(cacheClearTime.getCacheNames())
                                    .forEach(name-> CLEAR_TIMES.put(name,cacheClearTime.getTime())));
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 只有Class包含注解的才会被处理
     * @param clazz
     * @return
     */
    private boolean filter(Class clazz){
        for(Class<? extends Annotation> annotation : FILTER_ANNOTATIONS){
            if(clazz.isAnnotationPresent(annotation)){
                return true;
            }
        }
        return false;
    }

    private CacheClearTime getMethodCacheTime(Method method){
        return new CacheClearTime(getCacheAnnotation(method),method.getAnnotation(Time.class));
    }

    private Annotation getCacheAnnotation(Method method){

        return method.isAnnotationPresent(Cacheable.class)?method.getAnnotation(Cacheable.class):method.getAnnotation(CachePut.class);
    }

    private boolean isFilterMethod(Method method){
        if(method.isAnnotationPresent(Time.class)){
            if(method.isAnnotationPresent(Cacheable.class)||method.isAnnotationPresent(CachePut.class)){
                return true;
            }
        }
        return false;
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Optional.ofNullable(context.getBean(RedisCacheManager.class)).ifPresent(redisCacheManager -> {
            redisCacheManager.setExpires(CLEAR_TIMES);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
