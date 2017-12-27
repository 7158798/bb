package com.ruizton.task.queue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.util.SendMailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-08-04 13:54
 */
public class RedisEmailQueue implements Runnable {

    public static final String QUEUE = "queue:email";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private JedisPool jedisPool;

    @Autowired(required = false)
    private ConstantMap constantMap;

    private ExecutorService service = Executors.newFixedThreadPool(100);

    public void start() {
        new Thread(this).start();
    }

    public void run() {
        while (true) {
            System.out.println("listen");
            listen();
        }
    }

    public void listen() {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> list = jedis.brpop(1000 * 10, QUEUE);
            JSONObject json = JSON.parseObject(list.get(1));
            String email = json.getString("email");
            String ftitle = json.getString("ftitle");
            String fcontent = json.getString("fcontent");

            // 群发
            String[] emails = email.split(",");
            for (int i = 0; i < emails.length; i++) {
                String account = emails[i];
                service.submit(() -> {
                    if (!send(account, ftitle, fcontent)) {
                        json.put("email", account);
                        log.error("发送邮件失败", json.toJSONString());
                        jedis.rpush(QUEUE, json.toJSONString());
                    }
                });
            }


        } catch (Exception e) {
            sleep();
        }
    }

    public boolean send(String email, String ftitle, String fcontent) {
        try {
            String smtp = constantMap.getString("smtp");
            String mailName = constantMap.getString("mailName");
            String mailPassword = constantMap.getString("mailPassword");
            SendMailUtil.send(smtp, mailName, mailPassword, email, ftitle, fcontent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000 * 5);
        } catch (Exception e) {
        }
    }

}
