package com.zhgtrade.deal.listener;

import com.zhgtrade.deal.model.FentrustData;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
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
