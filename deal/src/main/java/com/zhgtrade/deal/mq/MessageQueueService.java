package com.zhgtrade.deal.mq;

/**
 * 消息队列服务
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-03-23 10:02
 */
public interface MessageQueueService {

    /**
     * 发送消息
     * @param channel
     * @param message
     */
    void publish(String channel, Object message);

    /**
     * 订阅消息
     * @param channel
     * @param listener
     */
    <T> void subscribe(String channel, MessageListener<T> listener, Class<T> clazz);

}
