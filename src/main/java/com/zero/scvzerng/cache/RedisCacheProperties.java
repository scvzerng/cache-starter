package com.zero.scvzerng.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisCacheProperties {
    /**
     * 命名空间
     */
    private String namespace = "intelligent";
    /**
     * 是否追加前缀
     */
    private boolean usePrefix = true;
    /**
     * 默认失效时间
     */
    private long defaultExpiration = 0;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public boolean isUsePrefix() {
        return usePrefix;
    }

    public void setUsePrefix(boolean usePrefix) {
        this.usePrefix = usePrefix;
    }

    public long getDefaultExpiration() {
        return defaultExpiration;
    }

    public void setDefaultExpiration(long defaultExpiration) {
        this.defaultExpiration = defaultExpiration;
    }
}
