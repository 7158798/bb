package com.zhgtrade.deal.market.impl;

import com.zhgtrade.deal.market.DepthEntrustData;
import com.zhgtrade.deal.market.DepthEntrustService;
import com.zhgtrade.deal.model.FentrustData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-05-12 13:46
 */
public class MemoryDepthEntrustServiceImpl implements DepthEntrustService {

    private Map<Integer, DepthEntrustData> dataMap = new ConcurrentHashMap<>();

    @Override
    public void updateDepthEntrust(int fviFid, int type, int deep, Double price, double count, double money) {
        DepthEntrustData depthEntrustData = dataMap.get(fviFid);
        if (depthEntrustData == null) {
            depthEntrustData = new DepthEntrustData();
            dataMap.put(fviFid, depthEntrustData);
        }
        depthEntrustData.incr(type, deep, price, count, money);
    }

    @Override
    public List<FentrustData> getDepthEntrust(int fviFid, int type, int deep, int size) {
        List<FentrustData> list = new ArrayList<>();
        DepthEntrustData depthEntrustData = dataMap.get(fviFid);
        if (depthEntrustData != null) {
            List<Double[]> listData = depthEntrustData.getDepthEntrustData(type, deep, size);
            for (Double[] row : listData) {
                FentrustData data = new FentrustData();
                data.setFprize(row[0]);
                data.setFleftCount(row[1]);
                data.setFamount(row[2]);
                list.add(data);
            }
        }
        return list;
    }

    @Override
    public Set<Integer> getDepthKeys() {
        return dataMap.keySet();
    }

    @Override
    public void clearDepthEntrust() {
        try {

        } finally {

        }
    }
}
