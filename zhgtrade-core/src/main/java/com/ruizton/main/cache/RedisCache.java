package com.ruizton.main.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-14 10:02
 */
public class RedisCache  implements Cache {

    @Autowired
    private RedisTemplate redisTemplate;

    private RedisSerializer defaultSerializer;
    private RedisSerializer stringSerializer;
    private String name;

    @PostConstruct
    private void init() {
        defaultSerializer = redisTemplate.getDefaultSerializer();
        stringSerializer = redisTemplate.getStringSerializer();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        System.out.println("getNativeCache");
        return null;
    }

    @Override
    public ValueWrapper get(final Object key) {
        Object value = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] bytes = connection.get(((String) key).getBytes());
                if (bytes == null) {
                    return null;
                }
                return defaultSerializer.deserialize(bytes);
            }
        });

        if (value == null) {
            return null;
        }
        return new SimpleValueWrapper(value);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return null;
    }

    @Override
    public void put(final Object key, final Object value) {
//        System.out.println("put key=" + key + ",value=" + value);
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(stringSerializer.serialize(key), defaultSerializer.serialize(value));
                return null;
            }
        });
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return null;
    }

    @Override
    public void evict(final Object key) {
//        System.out.println("evict key=" + key);
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.del(stringSerializer.serialize(key));
                return null;
            }
        });
    }

    @Override
    public void clear() {
        System.out.println("clear");
    }
}