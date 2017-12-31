package com.ruizton.main.cache.data.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ruizton.main.cache.data.RealTimeCenter;
import com.ruizton.main.cache.data.RealTimePriceService;
import com.ruizton.util.Constants;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-04-01 14:37
 */
@Service("realTimePriceService")
public class RealTimePriceServiceImpl implements RealTimePriceService {

    @Resource
    private JedisPool jedisPool;

    @Resource
    private RealTimeCenter realTimeCenter;

    @Override
    public double getLatestDealPrize(final int id) {
        return realTimeCenter.getPrice(RealTimeCenter.Type.LAST, id);
    }

    @Override
    public double getLowestSellPrize(int id) {
        return realTimeCenter.getPrice(RealTimeCenter.Type.SELL, id);
    }

    @Override
    public double getHighestBuyPrize(int id) {
        return realTimeCenter.getPrice(RealTimeCenter.Type.BUY, id);
    }

    /**
     *  获取首页3天成交价数据
     *
     * @return
     */
    @Override
    public Map<String, List<Object[]>> getHourPriceTrendData() {
        try{
            try (Jedis jedis = jedisPool.getResource()) {
                byte[] bytes = jedis.get(Constants.THREE_DAY_PRICE_TREND.getBytes());
                if(null != bytes && bytes.length > 0) return JSON.parseObject(new String(bytes), new TypeReference<Map<String, List<Object[]>>>(){});
                // 没有数据
                return Collections.emptyMap();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    /**
     *  插入首页3天成交价数据
     *
     * @return
     */
    public void pushHourPriceTrendData(Object datas){
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(Constants.THREE_DAY_PRICE_TREND.getBytes(), JSON.toJSONString(datas).getBytes());
        }
    }

}
