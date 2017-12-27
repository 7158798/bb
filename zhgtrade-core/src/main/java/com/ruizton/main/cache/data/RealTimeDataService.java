package com.ruizton.main.cache.data;

/**
 * 交易数据实时服务
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-03-23 10:02
 */
public interface RealTimeDataService extends RealTimeEntrustDepthService,
        RealTimeEntrustService,
        RealTimePriceService,
        RealTimeDealDataService {

    String getWeChatAccessToken();
}
