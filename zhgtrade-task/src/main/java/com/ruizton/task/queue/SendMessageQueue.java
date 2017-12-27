package com.ruizton.task.queue;

import com.ruizton.main.Enum.ValidateMessageStatusEnum;
import com.ruizton.main.model.Fvalidatemessage;
import com.ruizton.main.mq.MessageListener;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.front.FrontSystemArgsService;
import com.ruizton.main.service.front.FrontValidateService;
import com.ruizton.util.ConstantKeys;
import com.ruizton.util.MessagesUtils;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 深度合并消息队列监听
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 俞杰（945351749@qq.com）
 * Date : 2016年3月31日 上午10:27:29
 */
public class SendMessageQueue implements MessageListener<Fvalidatemessage>, Runnable {

    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private FrontSystemArgsService frontSystemArgsService;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private ExecutorService executorService;

    private BlockingQueue<Fvalidatemessage> waitUpdateQueue = new LinkedBlockingDeque<>();

    @PostConstruct
    public void init() {
        messageQueueService.subscribe(QueueConstants.MESSAGE_COMMON_QUEUE, this, Fvalidatemessage.class);
        executorService.execute(this);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Fvalidatemessage message = waitUpdateQueue.take();
                Fvalidatemessage current_message = frontValidateService.findFvalidateMessageById(message.getFid());
                current_message.setFsendTime(Utils.getTimestamp());
                current_message.setFstatus(ValidateMessageStatusEnum.Send);
                frontValidateService.updateFvalidateMessage(current_message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMessage(Fvalidatemessage message) {
        try {
            System.out.println("send message to " + message.getFphone() + " : " + message.getFcontent());
            MessagesUtils.send(frontSystemArgsService.getSystemArgs(ConstantKeys.MESSAGE_NAME).trim(), frontSystemArgsService.getSystemArgs(ConstantKeys.MESSAGE_PASSWORD).trim(), message.getFphone(), message.getFcontent());
            waitUpdateQueue.put(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
