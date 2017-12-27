package com.ruizton.task.queue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.util.ConstantKeys;
import com.ruizton.util.MessagesUtils;
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
public class RedisMessageQueue implements Runnable {

    public static final String QUEUE = "queue:message";

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
            String fphone = json.getString("fphone");
            String fcontent = json.getString("fcontent");

            // 群发
            String[] phones = fphone.split(",");

            for (int i = 0; i < phones.length; i++) {
                String account = phones[i];
                service.submit(() -> {
                    if (!send(account, fcontent)) {
                        json.put("fphone", account);
                        log.error("发送短信失败", json.toJSONString());
                        jedis.rpush(QUEUE, json.toJSONString());
                    }
                });
            }

//            StringBuilder sb = new StringBuilder();
//
//            for (int i = 0, j = 1; i < phones.length; i++, j++) {
//                sb.append(phones[i]);
//                sb.append(",");
//                if (j % 5 == 0 || j == phones.length) {
//                    System.out.println(i + "," + j);
//                    String phoneNumbers = sb.substring(0, sb.length() - 1);
//                    if (!send(phoneNumbers, fcontent)) {
//                        log.error("发送短信失败", json.toJSONString());
//                        jedis.rpush(QUEUE, json.toJSONString());
//                    }
//                    sb = new StringBuilder();
//                }
//            }

        } catch (Exception e) {
            sleep();
        }
    }

    public static void main(String[] args) {
    }

    public boolean send(String phone, String content) {
        try {
            String username = constantMap.getString(ConstantKeys.MESSAGE_NAME).trim();
            String password = constantMap.getString(ConstantKeys.MESSAGE_PASSWORD).trim();
            System.out.println(username + "|" + password + "|" + phone);
            MessagesUtils.send(username, password, phone, content);
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
