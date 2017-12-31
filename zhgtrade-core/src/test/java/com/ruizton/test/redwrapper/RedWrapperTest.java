package com.ruizton.test.redwrapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.Set;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/12/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-redis.xml"})
public class RedWrapperTest {
    @Autowired
    private JedisPool jedisPool;

    @Test
    public void cleanRedWrapper(){
        try(Jedis jedis = jedisPool.getResource()){
            Set<String> set = jedis.keys("cache:redWrapper:*");
            set.forEach(e -> {
                jedis.del(e);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
