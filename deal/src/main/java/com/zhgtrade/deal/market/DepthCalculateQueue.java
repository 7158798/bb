package com.zhgtrade.deal.market;

import com.zhgtrade.deal.model.FentrustData;
import com.zhgtrade.deal.mq.MessageListener;

/**
 * 深度合并消息队列监听
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date : 2016年5月12日 上午10:27:29
 */
public interface DepthCalculateQueue extends MessageListener<FentrustData>, Runnable {

    /**
     * 计算深度挂单数据
     * @param data
     */
    void calculateDepthEntrust(FentrustData data);


}
