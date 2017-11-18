package com.yazuo.intelligent.cache;

import com.yazuo.intelligent.cache.annotation.EnableTimeCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Year: 2017-2017/11/19-0:38
 * Project:cache-starter
 * Package:com.yazuo.intelligent.autoconfig
 * To change this template use File | Settings | File Templates.
 */

public class CachingConfigurationSelector extends AdviceModeImportSelector<EnableTimeCaching> {

    private static final String PROXY_JCACHE_CONFIGURATION_CLASS =
            "org.springframework.cache.jcache.config.ProxyJCacheConfiguration";

    private static final String CACHE_ASPECT_CONFIGURATION_CLASS_NAME =
            "org.springframework.cache.aspectj.AspectJCachingConfiguration";

    private static final String JCACHE_ASPECT_CONFIGURATION_CLASS_NAME =
            "org.springframework.cache.aspectj.AspectJJCacheConfiguration";


    private static final boolean jsr107Present = ClassUtils.isPresent(
            "javax.cache.Cache", org.springframework.cache.annotation.CachingConfigurationSelector.class.getClassLoader());

    private static final boolean jcacheImplPresent = ClassUtils.isPresent(
            PROXY_JCACHE_CONFIGURATION_CLASS, org.springframework.cache.annotation.CachingConfigurationSelector.class.getClassLoader());


    /**
     * {@inheritDoc}
     * @return {@link ProxyCachingConfiguration} or {@code AspectJCacheConfiguration} for
     * {@code PROXY} and {@code ASPECTJ} values of {@link EnableTimeCaching#mode()}, respectively
     */
    @Override
    public String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return getProxyImports();
            case ASPECTJ:
                return getAspectJImports();
            default:
                return null;
        }
    }

    /**
     * Return the imports to use if the {@link AdviceMode} is set to {@link AdviceMode#PROXY}.
     * <p>Take care of adding the necessary JSR-107 import if it is available.
     */
    private String[] getProxyImports() {
        List<String> result = new ArrayList<String>();
        result.add(AutoProxyRegistrar.class.getName());
        result.add(ProxyCachingConfiguration.class.getName());
        if (jsr107Present && jcacheImplPresent) {
            result.add(PROXY_JCACHE_CONFIGURATION_CLASS);
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * Return the imports to use if the {@link AdviceMode} is set to {@link AdviceMode#ASPECTJ}.
     * <p>Take care of adding the necessary JSR-107 import if it is available.
     */
    private String[] getAspectJImports() {
        List<String> result = new ArrayList<String>();
        result.add(CACHE_ASPECT_CONFIGURATION_CLASS_NAME);
        if (jsr107Present && jcacheImplPresent) {
            result.add(JCACHE_ASPECT_CONFIGURATION_CLASS_NAME);
        }
        return result.toArray(new String[result.size()]);
    }

}
