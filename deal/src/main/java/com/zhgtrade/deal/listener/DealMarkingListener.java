package com.zhgtrade.deal.listener;

import com.zhgtrade.deal.model.FentrustlogData;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-05-11 20:32
 */
public interface DealMarkingListener {

    void writeLog(FentrustlogData log);

    void updateMarking(int fid, double dealPrice, double highestBuy, double lowestDell);

}
