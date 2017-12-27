package com.ruizton.main.cache.data.impl;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.cache.data.LatestDealDataService;
import com.ruizton.main.dto.LatestDealData;
import com.ruizton.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-15 11:34
 */
@Service
public class LatestDealDataServiceImpl implements LatestDealDataService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void addLatestDealData(final LatestDealData data) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] m = (data.getFid() + "").getBytes();
                byte[] key = Constants.REDIS_CACHE_LAST;
                connection.hSet(key, m, JSON.toJSONBytes(data));
                return null;
            }
        });
    }

    public void updateLatestDealDataInfo(final LatestDealData latestDealData) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] m = (latestDealData.getFid() + "").getBytes();
                byte[] key = Constants.REDIS_CACHE_LAST;
                byte[] data = connection.hGet(key, m);
                LatestDealData latest;
                if (data != null && data.length > 0) {
                    latest = JSON.parseObject(data, LatestDealData.class);
                } else {
                    latest = new LatestDealData();
                    latest.setFid(latestDealData.getFid());
                }
                latest.setStatus(latestDealData.getStatus());
                latest.setFisShare(latestDealData.isFisShare());
                latest.setFname(latestDealData.getFname());
                latest.setfShortName(latestDealData.getfShortName());
                latest.setFisShare(latestDealData.isFisShare());
                latest.setFname_sn(latestDealData.getFname_sn());
                latest.setFurl(latestDealData.getFurl());
                latest.setCoinTradeType(latestDealData.getCoinTradeType());
                latest.setEquityType(latestDealData.getEquityType());
                latest.setHomeShow(latestDealData.isHomeShow());
                connection.hSet(key, m, JSON.toJSONBytes(latest));
                return null;
            }
        });
    }

    private LatestDealData getLatestDealData(final int fid) {
        return (LatestDealData) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] m = (fid + "").getBytes();
                byte[] key = Constants.REDIS_CACHE_LAST;
                byte[] bdata = connection.hGet(key, m);

                LatestDealData latest;
                if (bdata != null && bdata.length > 0) {
                    latest = JSON.parseObject(bdata, LatestDealData.class);
                } else {
                    latest = new LatestDealData();
                    latest.setFid(fid);
                }
                return latest;
            }
        });
    }

    public void updateLastDealPrice(final int fid, final double price) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] m = (fid + "").getBytes();
                byte[] key = Constants.REDIS_CACHE_LAST;

                LatestDealData latest = getLatestDealData(fid);
                latest.setLastDealPrize(price);

                connection.hSet(key, m, JSON.toJSONBytes(latest));
                return null;
            }
        });
    }

    @Override
    public void updateHighestPrice(final int fid, final double price) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] m = (fid + "").getBytes();
                byte[] key = Constants.REDIS_CACHE_LAST;

                LatestDealData latest = getLatestDealData(fid);
                latest.setHigestBuyPrize(price);

                connection.hSet(key, m, JSON.toJSONBytes(latest));
                return null;
            }
        });
    }

    @Override
    public void updateLowestPrice(final int fid, final double price) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] m = (fid + "").getBytes();
                byte[] key = Constants.REDIS_CACHE_LAST;

                LatestDealData latest = getLatestDealData(fid);
                latest.setLowestSellPrize(price);

                connection.hSet(key, m, JSON.toJSONBytes(latest));
                return null;
            }
        });
    }

}
