package com.zhgtrade.deal.market;

import com.zhgtrade.deal.model.FentrustlogData;
import com.zhgtrade.deal.model.LatestDealData;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-05-11 20:24
 */
public interface CacheDataService {

    void addFentrustLogData(FentrustlogData data);

    void clearFentrustLogData(int fviFid);

    void updateMarking(int fid, double dealPrice, double highestBuy, double lowestDell);

    void updateLatestDealData(LatestDealData data);

    void updateLatestDealDataInfo(LatestDealData latestDealData);

    void addLatestDealData(LatestDealData latestDealData);

    LatestDealData getLatestDealData(int fid);
}
