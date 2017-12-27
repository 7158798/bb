package com.lc.push.mq;

import java.io.Closeable;

public interface MessageQueueService extends Closeable {

    void subscribe(MessageListener<String> listener, String... channels);

}
