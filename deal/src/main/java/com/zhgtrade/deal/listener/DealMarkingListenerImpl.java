package com.zhgtrade.deal.listener;

import com.zhgtrade.deal.market.CacheDataService;
import com.zhgtrade.deal.model.FentrustlogData;
import com.zhgtrade.deal.mq.MessageQueueService;
import com.zhgtrade.deal.mq.QueueConstants;

import javax.annotation.Resource;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-05-11 20:35
 */
public class DealMarkingListenerImpl implements DealMarkingListener {

    @Resource
    private CacheDataService cacheDataService;

    @Resource
    private MessageQueueService messageQueueService;

    public DealMarkingListenerImpl() {
    }

    public DealMarkingListenerImpl(CacheDataService cacheDataService, MessageQueueService messageQueueService) {
        this.cacheDataService = cacheDataService;
        this.messageQueueService = messageQueueService;
    }

    @Override
    public void writeLog(FentrustlogData data) {
        if (!data.isactive()) {
            return;
        }
        // 1、更新最新成交
        cacheDataService.addFentrustLogData(data);
        // 2、设置K线数据
        messageQueueService.publish(QueueConstants.SYNC_FENTRUST_LOG_DATA_QUEUE, data);
    }

    @Override
    public void updateMarking(int fid, double dealPrice, double highestBuy, double lowestDell) {
        // 更新最新成交价格，最高买价，最低卖价
        cacheDataService.updateMarking(fid, dealPrice, highestBuy, lowestDell);
    }
}
