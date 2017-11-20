package com.yazuo.intelligent.cache;

import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class NameSpaceRedisCachePrefix implements RedisCachePrefix {
    private String namespace;
    private final RedisSerializer serializer = new StringRedisSerializer();
    private final String delimiter;

    public NameSpaceRedisCachePrefix(String namespace) {
        this.delimiter = ":";
        this.namespace = namespace;
    }

    @Override
    public byte[] prefix(String cacheName) {
        return serializer.serialize(namespace.concat(delimiter).concat(cacheName).concat(delimiter));
    }
}
