package com.zhgtrade.auto.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.util.Constants;
import com.ruizton.util.HttpUtils;
import com.ruizton.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.HashMap;
import java.util.Map;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/11/18
 */
public class WeChatAccessTokenExecutor implements TaskExecutor {
    private Logger logger = LoggerFactory.getLogger(WeChatAccessTokenExecutor.class);

    private int tryTimes = 3;

    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private JedisPool jedisPool;

    @Override
    public void execute() {
        Map map = new HashMap<>();
        map.put("grant_type", "client_credential");
        map.put("appid", constantMap.getString("weChatAppId"));
        map.put("secret", constantMap.getString("weChatSecret"));
        String jsonStr = HttpUtils.sendGetRequest("https://api.weixin.qq.com/cgi-bin/token", map);

        if(StringUtils.isEmpty(jsonStr) && tryTimes-- > 0){
            // 失败重试3次
            logger.error("获取微信access token失败！");
            try {
                Thread.sleep(1000);
                this.execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            tryTimes = 3;
            JSONObject jsonObject = JSON.parseObject(jsonStr);

            try(Jedis jedis = jedisPool.getResource(); Pipeline pipeline = jedis.pipelined()){
                byte[] key = Constants.WECHAT_ACCESS_TOKEN.getBytes(Constants.UTF8_CHARSET);
                pipeline.set(key, jsonObject.getString("access_token").getBytes(Constants.UTF8_CHARSET));
                pipeline.expire(key, 7200);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
