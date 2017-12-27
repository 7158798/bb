package com.ruizton.main.cache.data.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruizton.main.cache.data.KlineDataService;
import com.ruizton.main.dto.FperiodData;
import com.ruizton.main.model.Fperiod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-01 14:37
 */
@Service
public class KlineDataServiceImpl implements KlineDataService {

    public static final int INDEX_KEY = 100;
    public static final int INDEX_KEY2 = 101;

    private int[] keys = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private RedisTemplate redisTemplate;

    private RedisSerializer defaultSerializer;

    @PostConstruct
    private void init() {
        defaultSerializer = redisTemplate.getDefaultSerializer();
    }

    private long[] timeStep =
            new long[]{
                    1,//1h
                    3,//3d
                    5,//5d
                    15,
                    30,
                    1 * 60,//30d
                    2 * 60,
                    4 * 60,
                    6 * 60,
                    12 * 60,
                    1 * 24 * 60,
                    3 * 64 * 24,
                    7 * 64 * 24
            };

    @Override
    public void addFperiod(int id, int key, Fperiod fperiod) {
        FperiodData data = new FperiodData(id, key, fperiod);
        addFperiod(data, id, key);

        //加入待计算容器
        if (key < keys.length - 1) {
            this.addFperiodContainer(id, key, data);
        }
    }

    private void doSave(int id, int key, final String jsonString) {
        final byte[] mkey = ("cache:json:kline:" + id + ":" + key).getBytes();
        final byte[] jkey = ("" + key).getBytes();

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(mkey, jkey, jsonString.getBytes());
        }
    }

    public void setJsonString(int id, int key, String jsonString) {
        doSave(id, key, jsonString);
    }

    @Override
    public String getJsonString(int id, int key) {
        String data = getJson(id, key);
        return substringData(data);
    }

    @Override
    public String getJsonString(String begin, String end, int id, int key) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long startTimestamp = 0;
        long endTimestamp = 0;
        try {

            //得到分钟
            startTimestamp = format.parse(begin).getTime() / 1000;
            endTimestamp = format.parse(end).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String data = getJson(id, key);

        int startline = data.indexOf(startTimestamp + "");
        int endline = data.indexOf(endTimestamp + "");
        if(startline > -1 && endline > -1){
            return data.substring(startline, endline);
        }else {
            JSONArray jsonArray = JSON.parseArray(data);

            int startIndex = serach(jsonArray, startTimestamp);
            int endIndex = serach(jsonArray, endTimestamp);

            String startStr = jsonArray.getJSONArray(startIndex).getString(0);
            String endStr = jsonArray.getJSONArray(endIndex).getString(0);

            startline = data.indexOf(startStr);
            endIndex = data.indexOf(endStr);

            if(startIndex > -1 && endIndex > -1){

                String result = data.substring(startline, endIndex - 2);
                return "[[" + result + "]";
            }
            return data;

//            sb.append("[");
//
//            for (int i = 0; i < jsonArray.size(); i++){
//                JSONArray array = jsonArray.getJSONArray(i);
//                if(array.getLong(0) > startTimestamp && array.getLong(0) < endTimestamp){
//                    sb.append(array);
//                    sb.append(",");
//                }
//            }
//
//            if(sb.length() > 1){
//                sb.deleteCharAt(sb.length() - 1);
//            }
//            sb.append("]");

        }



    }

    private static int serach(JSONArray arry, long target){
        int start = 0;
        int end = arry.size() - 1;
        int middle = (end + start) >>> 1;

        while(start <= end){
            if(arry.getJSONArray(middle).getLong(0) > target){
                end = middle;
                if(start - end == -1){
                    return start;
                }
            }else if(arry.getJSONArray(middle).getLong(0) < target){
                start = middle;
                if(start - end == -1){
                    return end;
                }
            }else if(arry.getJSONArray(middle).getLong(0) == target){
                return middle;
            }

            middle = (end + start) >>> 1;
        }
        return -1;
    }

    @Override
    public String getIndexJsonString(int id) {
        return getJson(id, INDEX_KEY);
    }

    private String getJson(int id, int key) {
        final byte[] mkey = ("cache:kline:" + id).getBytes();
        final byte[] jkey = ("" + key).getBytes();

        byte[] bytes;
        try (Jedis jedis = jedisPool.getResource()) {
            bytes = jedis.hget(mkey, jkey);
        }

        if (bytes != null) {
            return new String(bytes);
        }

        return "[]";
    }

    private static String substringData(String data){

        int length = data.length();
        int count = 0;
        for (int i = length - 1; i >= 0; i--) {
            if(data.charAt(i) == ']'){
                count ++;
            }

            if(count == 802){
                return "[" + data.substring(i + 2);
            }
        }
        return data;
    }

    private List<FperiodData> getFromRedis(final byte[] mkey) {
        return (List<FperiodData>) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                List<FperiodData> fperiods = new ArrayList<>();
                List<byte[]> bytes = connection.lRange(mkey, 0, -1);
                for (byte[] b : bytes) {
                    fperiods.add((FperiodData) defaultSerializer.deserialize(b));
                }
                return fperiods;
            }
        });
    }
    
    private void addFperiod(final FperiodData data, final int id, final int key) {
        final byte[] mkey = ("cache:kline:" + id + ":" + key).getBytes();
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.rPush(mkey, defaultSerializer.serialize(data));
                return null;
            }
        });
    }

    private void insertToContainer(final int id, final int key, final FperiodData data) {
        final byte[] mkey = ("cache:klineContainer:" + id + ":" + key).getBytes();
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.lPush(mkey, defaultSerializer.serialize(data));
                return null;
            }
        });
    }

    private List<FperiodData> getFromContainer(final int id, final int key) {
        final byte[] mkey = ("cache:klineContainer:" + id + ":" + key).getBytes();
        return getFromRedis(mkey);
    }

    private void delContainer(final int id, final int key) {
        final byte[] mkey = ("cache:klineContainer:" + id + ":" + key).getBytes();
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.del(mkey);
                return null;
            }
        });
    }

    private void addFperiodContainer(int id, int key, FperiodData data) {

        insertToContainer(id, key, data);

        if (key < keys.length - 1) {
            //不是最后一个
            long timestep1 = this.timeStep[key];
            long timestep2 = this.timeStep[key + 1];
            long times = timestep2 / timestep1;

            List<FperiodData> fperiods = getFromContainer(id, key);

            if (fperiods.size() >= times) {
                Fperiod calRet = this.calculate(fperiods);
                delContainer(id, key);
                this.addFperiod(id, key + 1, calRet);
            }
        }
    }

    private Fperiod calculate(List<FperiodData> fperiods) {
        double fkai = 0F;
        double fgao = 0F;
        double fdi = 0F;
        double fshou = 0F;
        double fliang = 0F;
        int fid;
        Timestamp ftime;
        Fperiod fperiod = new Fperiod();

        fid = fperiods.get(0).getFid();
        fkai = fperiods.get(0).getFkai();
        fshou = fperiods.get(fperiods.size() - 1).getFshou();
        ftime = fperiods.get(0).getFtime();
        for (FperiodData f : fperiods) {
            fgao = fgao < f.getFgao() ? f.getFgao() : fgao;
            if (fdi == 0F) {
                fdi = f.getFdi();
            } else {
                fdi = fdi > f.getFdi() ? f.getFdi() : fdi;
            }
            fliang += f.getFliang();
        }

        fperiod.setFid(fid);
        fperiod.setFkai(fkai);
        fperiod.setFgao(fgao);
        fperiod.setFdi(fdi);
        fperiod.setFshou(fshou);
        fperiod.setFliang(fliang);
        fperiod.setFtime(ftime);

        return fperiod;
    }

}
