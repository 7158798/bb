package com.ruizton.task.queue;

import com.ruizton.main.Enum.ValidateMessageStatusEnum;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvalidatemessage;
import com.ruizton.main.mq.MessageListener;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.front.FrontValidateService;
import com.ruizton.util.ConstantKeys;
import com.ruizton.util.Utils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.request.AlibabaAliqinFcTtsNumSinglecallRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 语音验证码
 *
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/11/16
 */
public class SendVoiceQueue implements MessageListener<Fvalidatemessage>, Runnable {
    private Logger logger = LoggerFactory.getLogger(SendVoiceQueue.class);

    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private ConstantMap constantMap;

    private BlockingQueue<Fvalidatemessage> waitUpdateQueue = new LinkedBlockingDeque<>();

    @PostConstruct
    public void init() {
        messageQueueService.subscribe(QueueConstants.VOICE_COMMON_QUEUE, this, Fvalidatemessage.class);
        executorService.execute(this);
    }

    protected void sendVoice(Fvalidatemessage message) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient(constantMap.getString(ConstantKeys.ALI_VOICE_API_URL),
                constantMap.getString(ConstantKeys.ALI_VOICE_API_KEY),
                constantMap.getString(ConstantKeys.ALI_VOICE_API_SECRET));
        AlibabaAliqinFcTtsNumSinglecallRequest req = new AlibabaAliqinFcTtsNumSinglecallRequest();

        req.setCalledNum(message.getFphone());
        req.setCalledShowNum(constantMap.getString(ConstantKeys.ALI_VOICE_API_CALLED_NUM));
        req.setTtsCode(constantMap.getString(ConstantKeys.ALI_VOICE_API_TTS));

        // 模板参数
        Fuser fuser = message.getFuser();
        if(null == fuser || !fuser.getFhasRealValidate()){
            req.setTtsParamString("{\"name\":\"比特家用户\", \"number\":\"" + message.getFcontent() + "\"}");
        }else{
            req.setTtsParamString("{\"name\":\"" + fuser.getFrealName() + "\", \"number\":\"" + message.getFcontent() + "\"}");
        }

        TaobaoResponse response = client.execute(req);
    }

    @Override
    public void onMessage(Fvalidatemessage message) {
        try {
            logger.debug("正在发送语音验证码" + message.getFphone() + " : " + message.getFcontent());
            sendVoice(message);
            waitUpdateQueue.put(message);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送语音验证码失败了" + message.getFphone() + " : " + message.getFcontent(), e);
        }
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

    public static void main(String[] args) throws ApiException {
        SendVoiceQueue v = new SendVoiceQueue();
        v.constantMap = new ConstantMap();
        v.constantMap.put(ConstantKeys.ALI_VOICE_API_KEY, "23525756");
        v.constantMap.put(ConstantKeys.ALI_VOICE_API_SECRET, "e6482cf8d5fd55e947c2eeaee80244e5");
        v.constantMap.put(ConstantKeys.ALI_VOICE_API_URL, "http://gw.api.taobao.com/router/rest");
        v.constantMap.put(ConstantKeys.ALI_VOICE_API_TTS, "TTS_25815342");
        v.constantMap.put(ConstantKeys.ALI_VOICE_API_CALLED_NUM, "01053912807");

        Fvalidatemessage message = new Fvalidatemessage();
        message.setFcontent("c124");
        message.setFphone("18682029734");
        v.sendVoice(message);
    }
}
