package com.ruizton.task;

import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 比特家
 * 自动生成每分钟的K线
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-03-31 21:59
 */
public class AutoMinuteKlineTask {

    @Autowired
    private MessageQueueService messageQueueService;

    public void run() {
        messageQueueService.publish(QueueConstants.COUNT_MINUTE_KLINE_QUEUE, true);
    }

}
