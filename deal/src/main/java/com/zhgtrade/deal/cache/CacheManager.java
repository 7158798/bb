package com.zhgtrade.deal.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-05-12 10:13
 */
public class CacheManager {

    private Map<String, Cache> cacheMap = new ConcurrentHashMap<>();

    public Object get(String key) {
        Cache cache = cacheMap.get(key);
        if (cache != null && System.currentTimeMillis() > cache.expireTime) {
            cacheMap.remove(key);
            cache = null;
        }
        return cache == null ? null : cache.value;
    }

    public void put(String key, Object value, long expire) {
        Cache cache = new Cache();
        cache.value = value;
        cache.expireTime = System.currentTimeMillis() + expire;
        cacheMap.put(key, cache);
    }

    class Cache {
        Object value;
        long expireTime;
    }

}




