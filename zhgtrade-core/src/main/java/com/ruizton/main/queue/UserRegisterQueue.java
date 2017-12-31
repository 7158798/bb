package com.ruizton.main.queue;

import com.ruizton.main.model.Fuser;
import com.ruizton.main.mq.MessageListener;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.front.FrontUserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 比特家 用户注册初始化账号
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/6/24
 */
public class UserRegisterQueue implements MessageListener<Fuser> {
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private MessageQueueService messageQueueService;

    @PostConstruct
    public void init() {
        messageQueueService.subscribe(QueueConstants.SYNC_USER_REGISTER_QUEUE, this, Fuser.class);
    }

    @Override
    public void onMessage(Fuser message) {
        frontUserService.updateInitAccount(message);
    }
}
