package com.zhgtrade.deal.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-05-16 23:21
 */
public class RedisTemplate {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private JedisPool jedisPool;

    public RedisTemplate(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void execute(RedisCallback action) {
        try (Jedis jedis = jedisPool.getResource()) {
            action.doInRedis(jedis);
        } catch (Exception e) {
            log.error("error " + e.getLocalizedMessage());
        }
    }

}
