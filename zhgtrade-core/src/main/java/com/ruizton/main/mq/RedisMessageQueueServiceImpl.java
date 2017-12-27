package com.ruizton.main.mq;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

/**
 * Redis 消息队列实现
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-03-23 10:02
 */
public class RedisMessageQueueServiceImpl implements MessageQueueService {

    @Autowired
    JedisPool jedisPool;

    @Override
    public void publish(String channel, Object message) {
        Jedis jedis = jedisPool.getResource();
        jedis.publish(channel, JSON.toJSONString(message));
    }

    @Override
    public <T> void subscribe(final String channel, final MessageListener<T> listener, final Class<T> clazz) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Jedis jedis = jedisPool.getResource();
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        T msg = JSON.parseObject(message, clazz);
                        listener.onMessage(msg);
                    }
                }, channel);
            }
        }).start();
    }
}
