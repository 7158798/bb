package com.zhgtrade.deal.data;

import redis.clients.jedis.Jedis;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-05-16 23:22
 */
public interface RedisCallback<T> {

    void doInRedis(Jedis connection);

}
