package com.ruizton.main.auto;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.cache.data.KlineDataService;
import com.ruizton.util.Constants;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;

public class KlinePeriodData implements Runnable {

    @Autowired
    private KlineDataService klineDataService;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private RedisTemplate redisTemplate;
    public static Timestamp lastUpdateTime = Utils.getTimestamp();

    @PostConstruct
    public void init() {
        executorService.execute(this);
    }

    public String getJsonString(int id, int key) {
        return klineDataService.getJsonString(id, key);
    }

    public String getJsonString(String begin, String end, int id, int key) {
        return klineDataService.getJsonString(begin, end, id, key);
    }

    //index json
    public String getIndexJsonString(int id) {
        return klineDataService.getIndexJsonString(id);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(3000);
                syncLastUpdateTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void syncLastUpdateTime() {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] bytes = connection.get(Constants.REDIS_CACHE_LAST_UPDATE_TIME);
                if (bytes != null && bytes.length > 0) {
                    Long time = JSON.parseObject(bytes, Long.class);
                    lastUpdateTime = new Timestamp(time);
                }
                return null;
            }
        });
    }

}
