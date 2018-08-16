package me.wuwenbin.noteblogv4.util;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * 本项目使用的是springboot默认的ConcurrentMapCacheManager
 * created by Wuwenbin on 2018/8/16 at 11:33
 *
 * @author wuwenbin
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

    /**
     * 获取权限缓存
     *
     * @return
     */
    public static Cache getAuthCache() {
        return getCache(AUTH_CACHE);
    }


    public static void putIntoParamCache(Object key, Object value) {
        getParamCache().put(key, value);
    }

    public static <T> T fetchFromParamCache(Object key, Class<T> clazz) {
        return getParamCache().get(key, clazz);
    }

    public static void removeParamCache(Object key) {
        getParamCache().evict(key);
    }

    public static void clearAllParamCache() {
        getParamCache().clear();
    }

    public static void putIntoAuthCache(Object key, Object value) {
        getAuthCache().put(key, value);
    }

    public static <T> T fetchFromAuthCache(Object key, Class<T> clazz) {
        return getAuthCache().get(key, clazz);
    }

    public static void removeAuthCache(Object key) {
        getAuthCache().evict(key);
    }

    public static void clearAllAuthCache() {
        getAuthCache().clear();
    }
}
