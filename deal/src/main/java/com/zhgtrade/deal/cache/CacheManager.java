package com.zhgtrade.deal.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
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




