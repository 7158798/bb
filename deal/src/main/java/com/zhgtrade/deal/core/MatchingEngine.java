package com.zhgtrade.deal.core;

import com.zhgtrade.deal.model.FentrustData;

/**
 * 撮合引擎
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-05-10 14:32
 */
public interface MatchingEngine {

    double updateDealMaking(FentrustData _buyFentrust, FentrustData _sellFentrust, int fviFid);

}
