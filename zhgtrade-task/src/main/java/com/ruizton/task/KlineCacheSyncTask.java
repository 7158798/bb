package com.ruizton.task;

import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.*;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date：  13:12
 */
public class KlineCacheSyncTask {

    @Resource
    JdbcTemplate jdbc;

    @Resource
    JedisPool jedisPool;

    private Map<Integer, Integer> timeStep = new TreeMap<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2.compareTo(o1);
        }
    });

    public KlineCacheSyncTask() {
        timeStep.put(1, 0);
        timeStep.put(3, 1);
        timeStep.put(5, 2);
        timeStep.put(15, 3);
        timeStep.put(30, 4);
        timeStep.put(1 * 60, 5);
        timeStep.put(2 * 60, 6);
        timeStep.put(4 * 60, 7);
        timeStep.put(6 * 60, 8);
        timeStep.put(12 * 60, 9);
        timeStep.put(1 * 24 * 60, 10);
        timeStep.put(3 * 24 * 60, 11);
        timeStep.put(7 * 24 * 60, 12);
    }

    public List<Integer> getFviFids() {
        return jdbc.queryForList("SELECT fid FROM fvirtualcointype WHERE fstatus = 1 and FIsShare = 1", Integer.class);
    }

    public String getKlineData(int fviFid, int key) {
        if (key == 0) {
            // 一分钟数据从fperiod表查询
            return getKlineDataFromPeriod(fviFid);
        }
        return getKlineDataFromBigPeriod(fviFid, key);
    }

    public String getKlineDataFromBigPeriod(int fviFid, int key) {
        List<String> list = jdbc.queryForList("select k from (\n" +
                "select ftime, CONCAT('[', UNIX_TIMESTAMP(ftime), ',0,0,', fkai, ',', fshou, ',', fgao, ',', fdi, ',', fliang*2, ']') k FROM kline WHERE fvi_fid = ? and fkey = ? AND fliang > 0 ORDER BY ftime desc limit 800\n" +
                ") a ORDER BY ftime", new Object[]{fviFid, key}, String.class);

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(list.get(i));
        }
        sb.append("]") ;
        return sb.toString();
    }

    public String getKlineDataFromPeriod(int fviFid) {
        List<String> list = jdbc.queryForList("select k from (\n" +
                "select ftime, CONCAT('[', UNIX_TIMESTAMP(ftime), ',0,0,', fkai, ',', fshou, ',', fgao, ',', fdi, ',', fliang*2, ']') k FROM fperiod WHERE fvi_fid = ? AND fliang > 0 ORDER BY fid desc limit 800\n" +
                ") a ORDER BY ftime", new Object[]{fviFid}, String.class);

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(list.get(i));
        }
        sb.append("]") ;
        return sb.toString();
    }

    public void saveToRedis(int fviFid, int key, String json) {
        String mkey = ("cache:kline:" + fviFid);
        String jkey = "" + key;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(mkey, jkey, json);
        }
    }

    public void syncCoinKlineToRedis(int fviFid) {
        timeStep.forEach((key, val) -> {
            String data = getKlineData(fviFid, val);
            saveToRedis(fviFid, val, data);
        });
    }

    /**
     * 同步K线数据到Redis
     */
    public void run() {
        System.out.println("syncCoinKlineToRedis");
        synchronized (this) {
            List<Integer> fviFids = getFviFids();
            fviFids.forEach(fviFid -> {
                syncCoinKlineToRedis(fviFid);
            });
        }
    }


}
