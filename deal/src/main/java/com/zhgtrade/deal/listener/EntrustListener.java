package com.zhgtrade.deal.listener;

import com.zhgtrade.deal.model.FentrustData;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-05-12 12:57
 */
public interface EntrustListener {

    /**
     * 下单事件
     * @param data
     */
    void onCreateEntrust(FentrustData data);

    /**
     * 更新订单事件
     * @param data
     */
    void onUpdateEntrust(FentrustData data);

    /**
     * 取消订单事件
     * @param data
     */
    void onCancelEntrust(FentrustData data);

}
