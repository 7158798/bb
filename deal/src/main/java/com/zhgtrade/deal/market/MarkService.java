package com.zhgtrade.deal.market;//package com.zhgtrade.deal.market;
//
//import com.zhgtrade.deal.model.LatestDealData;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * 比特家
// * CopyRight : www.btc58.cc
// * Author : xxp
// * Date： 2016-05-16 12:54
// */
//public interface MarkService {
//
//    double getClosingPrice(final int fvirtualcointype);
//
//    double getOpenningPrice(final int fvirtualcointype);
//
//    double getEntrustBeforeWeek(final int fvirtualcointype);
//
//    double getHighestBuyPrice(int id, long time);
//
//    double getLowestSellPrice(int id, long time);
//
//    Map<String, Object> getSum24(int id, long time);
//
//    double getLatestDealPrize(int fid);
//
//    double getUpanddown(int id, double latestDealPrice);
//
//    double getUpanddownweek(int id, double latestDealPrice);
//
//    List<LatestDealData> findFvirtualCoinType();
//
//}
