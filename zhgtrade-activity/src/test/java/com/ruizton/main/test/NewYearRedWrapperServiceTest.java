package com.ruizton.main.test;

import com.ruizton.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/12/28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-redis.xml"})
public class NewYearRedWrapperServiceTest {
    @Autowired
    private JedisPool jedisPool;

    private String buildLuaScript() {
        StringBuilder scriptBuf = new StringBuilder();

        scriptBuf.append("local amount\n");
        scriptBuf.append("local cachePrefix = 'cache:redWrapper:' .. ARGV[1]\n");

        // 用户抽取次数限制(10次)
        scriptBuf.append("local count = redis.call('hget', cachePrefix , 'users:' .. ARGV[4])\n");
        scriptBuf.append("count = count and count * 1 or 0\n");
        scriptBuf.append("if count >= 10 then\n");
        scriptBuf.append("  return '0'\n");
        scriptBuf.append("end\n");

        // 剩余金额判断
        scriptBuf.append("local remainAmount = redis.call('hget', cachePrefix, 'amount')\n");
        scriptBuf.append("remainAmount = remainAmount and remainAmount * 1 or 0\n");
        scriptBuf.append("if remainAmount <= 0 then\n");
        scriptBuf.append("  return '0'\n");
        scriptBuf.append("end\n");

        // 剩余份数判断
        scriptBuf.append("local remainCount = redis.call('hget', cachePrefix, 'count')\n");
        scriptBuf.append("remainCount = remainCount and remainCount * 1 or 0\n");
        scriptBuf.append("if remainCount <= 0 then\n");
        scriptBuf.append("  return '0'\n");
        scriptBuf.append("end\n");

        // 红包分配算法
        scriptBuf.append("if 1 == remainCount then\n");
        scriptBuf.append("  amount = remainAmount\n");
        scriptBuf.append("else\n");
        scriptBuf.append("  local min = 0.01\n");
        scriptBuf.append("  local randomRate = tonumber(ARGV[3])\n");

        scriptBuf.append("  if 'true' == ARGV[6] then\n");
        scriptBuf.append("      amount = 100 * randomRate\n");
        scriptBuf.append("  else\n");
        scriptBuf.append("      amount = remainAmount / remainCount * randomRate * 2\n");
        scriptBuf.append("  end\n");

        scriptBuf.append("  local numStr = tostring(amount * 100)\n");
        scriptBuf.append("  local _, index = string.find(numStr, '%.')\n");
        scriptBuf.append("  amount = string.sub(numStr, 0, index - 1) / 100\n");
        scriptBuf.append("  amount = amount < min and min or amount\n");
        scriptBuf.append("  amount = amount > remainAmount and remainAmount or amount\n");
        scriptBuf.append("end\n");

        // 用户数据保存
        scriptBuf.append("redis.call('hset', cachePrefix, 'users:' .. ARGV[4], count + 1)\n");
        scriptBuf.append("redis.call('zadd', cachePrefix .. ':list', tostring(amount), cjson.encode({userId = ARGV[4], type = ARGV[5], amount = amount, drawTime = ARGV[2]}))\n");

        // 剩余金额及份数保存
        scriptBuf.append("redis.call('hset', cachePrefix, 'amount', remainAmount - amount)\n");
        scriptBuf.append("redis.call('hset', cachePrefix, 'count', remainCount - 1)\n");
        scriptBuf.append("return tostring(amount)\n");

        return scriptBuf.toString();
    }

    @Test
    public void drawRedWrapperTest() {
        String amount;
        long st = System.currentTimeMillis();

        try (Jedis jedis = jedisPool.getResource()) {
//            do {
                Date curTime = new Date();
                Object obj = jedis.eval(buildLuaScript(), new ArrayList<>(), new ArrayList<String>(Arrays.asList(new String[]{DateUtils.formatDate(curTime), String.valueOf(curTime.getTime()), String.valueOf(new SecureRandom().nextDouble()), String.valueOf(39310), String.valueOf(1), String.valueOf(true)})));
                System.out.println(obj);
                amount = (String) obj;
//            } while (!amount.equals("0"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("共耗时：" + ((System.currentTimeMillis() - st) / 1000));
    }

}
