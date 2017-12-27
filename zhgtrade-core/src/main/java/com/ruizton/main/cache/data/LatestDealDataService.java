package com.ruizton.main.cache.data;

import com.ruizton.main.dto.LatestDealData;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-01 14:37
 */
public interface LatestDealDataService {

    void addLatestDealData(LatestDealData data);

    void updateLatestDealDataInfo(LatestDealData latestDealData);

    void updateLastDealPrice(int fid, double price);

    void updateHighestPrice(int fid, double price);

    void updateLowestPrice(int fid, double price);

}
