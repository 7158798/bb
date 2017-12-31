package com.ruizton.task.queue;

import com.ruizton.main.Enum.ValidateMailStatusEnum;
import com.ruizton.main.model.Fvalidateemail;
import com.ruizton.main.mq.MessageListener;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.front.FrontSystemArgsService;
import com.ruizton.main.service.front.FrontValidateService;
import com.ruizton.util.SendMailUtil;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 深度合并消息队列监听
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp（xxly68@qq.com）
 * Date : 2016年3月31日 上午10:27:29
 */
public class SendMailQueue implements MessageListener<Fvalidateemail>, Runnable {

    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private FrontSystemArgsService frontSystemArgsService;
    @Autowired
    private FrontValidateService frontValidateService;

    private BlockingQueue<Fvalidateemail> waitUpdateQueue = new LinkedBlockingDeque<>();

    @PostConstruct
    public void init() {
        messageQueueService.subscribe(QueueConstants.EMAIL_COMMON_QUEUE, this, Fvalidateemail.class);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Fvalidateemail message = waitUpdateQueue.take();
                Fvalidateemail current_validateemail = frontValidateService.findValidateMailsById(message.getFid());
                current_validateemail.setFsendTime(Utils.getTimestamp());
                current_validateemail.setFstatus(message.getFstatus());
                frontValidateService.updateValidateMails(current_validateemail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMessage(Fvalidateemail message) {
        try {
            String email = message.getEmail();
            if (email == null) {
                email = message.getFuser().getFemail();
            }
            boolean flag = SendMailUtil.send(frontSystemArgsService.getSystemArgs("smtp"), frontSystemArgsService.getSystemArgs("mailName"), frontSystemArgsService.getSystemArgs("mailPassword"), email, message.getFtitle(), message.getFcontent());
            if (flag) {
                message.setFstatus(ValidateMailStatusEnum.Send);
            } else {
                message.setFstatus(ValidateMailStatusEnum.Fail);
            }
            waitUpdateQueue.put(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
