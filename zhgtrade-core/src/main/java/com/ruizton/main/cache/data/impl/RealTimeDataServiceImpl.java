package com.ruizton.main.cache.data.impl;

import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.cache.data.RealTimeDealDataService;
import com.ruizton.main.cache.data.RealTimeEntrustService;
import com.ruizton.main.cache.data.RealTimePriceService;
import com.ruizton.main.dto.FentrustData;
import com.ruizton.main.dto.FentrustlogData;
import com.ruizton.main.dto.LatestDealData;
import com.ruizton.main.model.Fentrust;
import com.ruizton.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-01 14:37
 */
@Service
public class RealTimeDataServiceImpl implements RealTimeDataService {

    @Autowired
    private RealTimeEntrustDepthServiceImpl realTimeEntrustDepthService;
    @Autowired
    private RealTimeEntrustService realTimeEntrustService;
    @Autowired
    private RealTimePriceService realTimePriceService;
    @Autowired
    private RealTimeDealDataService realTimeDealDataService;
    @Autowired
    private JedisPool jedisPool;

    @Override
    public Set<FentrustlogData> getEntrustSuccessMap(int id) {
        return realTimeEntrustService.getEntrustSuccessMap(id);
    }

    @Override
    public Set<FentrustlogData> getEntrustSuccessMapLimit(int id, int limit) {
        return realTimeEntrustService.getEntrustSuccessMapLimit(id, limit);
    }

    @Override
    public void addEntrustBuyMap(int id, Fentrust fentrust) {
    	realTimeEntrustService.addEntrustBuyMap(id, fentrust);
    }

    @Override
    public void removeEntrustBuyMap(int id, Fentrust fentrust) {
        realTimeEntrustService.removeEntrustBuyMap(id, fentrust);
    }

    @Override
    public void addEntrustSellMap(int id, Fentrust fentrust) {
    	realTimeEntrustService.addEntrustSellMap(id, fentrust);
    }

    @Override
    public void removeEntrustSellMap(int id, Fentrust fentrust) {
        realTimeEntrustService.removeEntrustSellMap(id, fentrust);
    }

    @Override
    public double getLatestDealPrize(int id) {
        return realTimePriceService.getLatestDealPrize(id);
    }

    @Override
    public double getLowestSellPrize(int id) {
        return realTimePriceService.getLowestSellPrize(id);
    }

    @Override
    public double getHighestBuyPrize(int id) {
        return realTimePriceService.getHighestBuyPrize(id);
    }

    @Override
    public Map<String, List<Object[]>> getHourPriceTrendData() {
        return realTimePriceService.getHourPriceTrendData();
    }

    @Override
    public List<LatestDealData> getLatestDealDataList() {
        List<LatestDealData> list = realTimeDealDataService.getLatestDealDataList();
        return list;
    }

    @Override
    public String getSellDepthMap(int id, int deep) {
        return realTimeEntrustDepthService.getSellDepthMap(id,deep);
    }

    @Override
    public String getBuyDepthMap(int id, int deep) {
        return realTimeEntrustDepthService.getBuyDepthMap(id,deep);
    }

    @Override
    public LatestDealData getLatestDealData(int id) {
        return realTimeDealDataService.getLatestDealData(id);
    }

	@Override
	public void removeEntrustSellMap(int id, FentrustData fentrustData) {
		realTimeEntrustService.removeEntrustSellMap(id, fentrustData);
		
	}

	@Override
	public void removeEntrustBuyMap(int id, FentrustData fentrustData) {
		realTimeEntrustService.removeEntrustBuyMap(id, fentrustData);
	}

    @Override
    public String getMarketJSON(int fid) {
        return realTimeEntrustDepthService.getMarketJSON(fid);
    }

    @Override
    public String getWeChatAccessToken() {
        try(Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = jedis.get(Constants.WECHAT_ACCESS_TOKEN.getBytes(Constants.UTF8_CHARSET));
            if(!ObjectUtils.isEmpty(bytes)){
                return new String(bytes, Constants.UTF8_CHARSET);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
