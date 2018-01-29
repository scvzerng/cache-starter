package com.zero.scvzerng.autoconfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zero.scvzerng.cache.NameSpaceRedisCachePrefix;
import com.zero.scvzerng.cache.RedisCacheProperties;
import com.zero.scvzerng.cache.annotation.EnableTimeCaching;
import com.zero.scvzerng.cache.annotation.Time;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Configuration
@ConditionalOnProperty(name = "spring.cache.type",havingValue = "redis")
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnClass({EnableTimeCaching.class,RedisCacheManager.class, RedisCache.class,Time.class})
@EnableConfigurationProperties(RedisCacheProperties.class)
public class RedisCacheAutoConfiguration {

    @Resource
    RedisCacheProperties redisCacheProperties;

    @PostConstruct
    public void init(){
        //支持自动类型 否则fast json会报错
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    //fast json 序列化与反序列化器
    private static final RedisSerializer FAST_JSON_REDIS_SERIALIZER = new RedisSerializer() {

        @Override
        public byte[] serialize(Object o) throws SerializationException {
            //对SimpleKey的兼容
            if(o instanceof SimpleKey){
                return o.toString().getBytes();
            }
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
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        if(redisCacheProperties.isUsePrefix()){
            redisCacheManager.setCachePrefix(new NameSpaceRedisCachePrefix(redisCacheProperties.getNamespace()));
            redisCacheManager.setUsePrefix(redisCacheProperties.isUsePrefix());
        }
        redisCacheManager.setDefaultExpiration(redisCacheProperties.getDefaultExpiration());
        return  redisCacheManager;
    }

}
