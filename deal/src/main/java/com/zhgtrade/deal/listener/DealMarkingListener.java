package com.zhgtrade.deal.listener;

import com.zhgtrade.deal.model.FentrustlogData;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-05-11 20:32
 */
public interface DealMarkingListener {

    void writeLog(FentrustlogData log);

    void updateMarking(int fid, double dealPrice, double highestBuy, double lowestDell);

}
