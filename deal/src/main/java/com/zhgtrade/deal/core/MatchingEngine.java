package com.zhgtrade.deal.core;

import com.zhgtrade.deal.model.FentrustData;

/**
 * 撮合引擎
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-05-10 14:32
 */
public interface MatchingEngine {

    double updateDealMaking(FentrustData _buyFentrust, FentrustData _sellFentrust, int fviFid);

}
