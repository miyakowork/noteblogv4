package me.wuwenbin.noteblogv4.util;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * 本项目使用的是springboot默认的ConcurrentMapCacheManager
 * created by Wuwenbin on 2018/8/16 at 11:33
 */
public class CacheUtils {

    private static final String PARAM_CACHE = "paramCache";
    private static final String AUTH_CACHE = "authCache";

    private static Cache getCache(String cacheName) {
        return NBUtils.getBean(CacheManager.class).getCache(cacheName);
    }

    /**
     * 获取参数缓存对象
     *
     * @return
     */
    public static Cache getParamCache() {
        return getCache(PARAM_CACHE);
    }

    public static Cache getAuthCache() {
        return getCache(AUTH_CACHE);
    }
}
