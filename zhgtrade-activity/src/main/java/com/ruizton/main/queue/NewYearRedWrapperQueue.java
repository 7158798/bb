package com.ruizton.main.queue;

import com.ruizton.main.model.NewYearRedWrapper;
import com.ruizton.main.mq.MessageListener;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.service.activity.NewYearRedWrapperService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/12/28
 */
public class NewYearRedWrapperQueue implements MessageListener<NewYearRedWrapper> {
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private NewYearRedWrapperService newYearRedWrapperService;

    @PostConstruct
    protected void init(){
        messageQueueService.subscribe(NewYearRedWrapper.NEW_YEAR_RED_WRAPPER_QUEUE, this, NewYearRedWrapper.class);
    }

    @Override
    public void onMessage(NewYearRedWrapper redWrapper) {
        newYearRedWrapperService.updateDrawWrapperMoneyToWallet(redWrapper);
    }
}
