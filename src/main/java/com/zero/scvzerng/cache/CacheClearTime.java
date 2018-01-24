package com.zero.scvzerng.cache;

import com.zero.scvzerng.cache.enums.TimeType;
import lombok.Data;
import java.lang.annotation.Annotation;
import java.util.Map;

import static org.springframework.core.annotation.AnnotationUtils.getAnnotationAttributes;


/**
 *
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Year: 2017-2017/11/18-20:53
 * Project:xycrm_intelligent_service
 * Package:com.yazuo.intelligent.cache
 * To change this template use File | Settings | File Templates.
 */
@Data
class CacheClearTime {
    public static final String VALUE_KEY = "value";
    public static final String TYPE_KEY = "type";
    public static final String CACHE_NAMES_KEY = "cacheNames";
    private static final String[] EMPTY = new String[]{};
    //缓存注解
    private String[] cacheNames;
    //失效时间注解
    private long time;

    public CacheClearTime(Annotation cache, Annotation time) {
        this.cacheNames = getAllNames(cache);
        this.time = getSeconds(time);
    }

    private long getSeconds(Annotation timeAnnotation){
        Map<String,Object> attrs = getAnnotationAttributes(timeAnnotation);
        Long time = (Long) attrs.get(VALUE_KEY);
        TimeType timeType = (TimeType) attrs.get(TYPE_KEY);
        switch (timeType){
            case SECONDS:
                break;
            case MINUTES:
                time = time*60;
                break;
            case HOURS:
                time = time*60*60;
                break;
            case DAY:
                time = time*60*60*24;
                break;

        }
        return time;
    }

    private String[] getAllNames(Annotation cache){
        return  (String[]) getAnnotationAttributes(cache).getOrDefault(CACHE_NAMES_KEY,EMPTY);
    }
}
