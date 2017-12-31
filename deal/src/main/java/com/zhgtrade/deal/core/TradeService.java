package com.zhgtrade.deal.core;

import com.zhgtrade.deal.model.FentrustData;
import com.zhgtrade.deal.model.FentrustlogData;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-06-12 14:00
 */
public interface TradeService {

    /**
     * 撮合交易
     * @param buy
     * @param sell
     * @param buyLog
     * @param sellLog
     * @return
     */
    boolean updateDealMaking(final FentrustData buy, final FentrustData sell, FentrustlogData buyLog, FentrustlogData sellLog);

    /**
     * 获取委托单
     * @param id
     * @return
     */
    FentrustData findByFid(int id);

}
