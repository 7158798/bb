package com.lc.push.mq;

public interface MessageListener<T> {

    void onMessage(String channel, T message);

}
