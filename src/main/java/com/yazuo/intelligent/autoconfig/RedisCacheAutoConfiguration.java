package com.yazuo.intelligent.autoconfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yazuo.intelligent.cache.annotation.EnableTimeCaching;
import com.yazuo.intelligent.cache.annotation.Time;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Year: 2017-2017/11/18-21:56
 * Project:xycrm_intelligent_service
 * Package:com.yazuo.intelligent.cache
 * To change this template use File | Settings | File Templates.
 */

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnClass({EnableTimeCaching.class,RedisCacheManager.class, RedisCache.class,Time.class})
public class RedisCacheAutoConfiguration {
    //fast json 序列化与反序列化器
    private static final RedisSerializer FAST_JSON_REDIS_SERIALIZER = new RedisSerializer() {
        @Override
        public byte[] serialize(Object o) throws SerializationException {
            return JSON.toJSONBytes(o, SerializerFeature.WriteClassName);
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            return JSON.parse(bytes);
        }
    };
    @Bean
    @ConditionalOnMissingBean
    public RedisCacheManager redisCacheManager(RedisTemplate redisTemplate){

        redisTemplate.setKeySerializer(FAST_JSON_REDIS_SERIALIZER);
        redisTemplate.setValueSerializer(FAST_JSON_REDIS_SERIALIZER);
        return  new RedisCacheManager(redisTemplate);
    }

}
