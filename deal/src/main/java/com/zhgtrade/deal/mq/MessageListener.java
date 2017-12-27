package com.zhgtrade.deal.mq;

/**
 * 消息监听器
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-03-23 10:02
 */
public interface MessageListener<T> {

    void onMessage(T message);

}
