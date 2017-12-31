package com.ruizton.main.mq;

/**
 * 消息监听器
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-03-23 10:02
 */
public interface MessageListener<T> {

    void onMessage(T message);

}
