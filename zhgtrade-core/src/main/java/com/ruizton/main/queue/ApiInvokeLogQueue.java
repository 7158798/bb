package com.ruizton.main.queue;

import com.ruizton.main.model.ApiInvokeLog;
import com.ruizton.main.mq.MessageListener;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.front.ApiInvokeLogService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/7/22
 */
public class ApiInvokeLogQueue implements MessageListener {
    @Autowired
    MessageQueueService messageQueueService;
    @Autowired
    private ApiInvokeLogService apiInvokeLogService;

    @PostConstruct
    public void init(){
        messageQueueService.subscribe(QueueConstants.API_INVOKE_LOG_QUEUE, this, ApiInvokeLog.class);
    }

    @Override
    public void onMessage(Object message) {
        apiInvokeLogService.saveLog((ApiInvokeLog) message);
    }
}
