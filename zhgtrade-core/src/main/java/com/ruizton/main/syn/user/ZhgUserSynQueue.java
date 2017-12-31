package com.ruizton.main.syn.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.mq.MessageListener;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/6/22
 */
public class ZhgUserSynQueue implements MessageListener<ZhgUserSyn> {
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private FrontUserService frontUserService;

    private static Logger logger = LoggerFactory.getLogger(ZhgUserSynQueue.class);

    @Override
    public void onMessage(ZhgUserSyn message) {
        if(null != message.getStartTime() && new Date().before(message.getStartTime())){
            // 重试时间未到
            messageQueueService.publish(QueueConstants.SYNC_ZHG_USER_SYSTEM_QUEUE, message);
            return;
        }

        boolean isErr = false;
        if (ZhgUserSynUtils.MODIFY_USER_INFO == message.getType()) {
            isErr = modifyUserInfo(message);
        } else if (ZhgUserSynUtils.REGISTER_USER_INFO == message.getType()) {
            isErr = registerUserInfo(message);
        } else if (ZhgUserSynUtils.SYN_USER_INFO == message.getType()) {
            isErr = synUserInfo(message);
        }

        if(isErr){
            // 出错了30s后重试
            message.setStartTime(DateUtils.getSecondAfter(new Date(), 30));
            messageQueueService.publish(QueueConstants.SYNC_ZHG_USER_SYSTEM_QUEUE, message);
        }
    }

    @PostConstruct
    public void init() {
        messageQueueService.subscribe(QueueConstants.SYNC_ZHG_USER_SYSTEM_QUEUE, this, ZhgUserSyn.class);
    }

    protected boolean modifyUserInfo(ZhgUserSyn zhgUserSyn) {
        boolean isErr = false;
        try {
            JSONObject jsonResponse = HttpUtils.sendPostRequestForJson(constantMap.getString(ConstantKeys.USER_SYS_MOD_URL), zhgUserSyn.getParams());
            if (!"1".equals(jsonResponse.getString("ret"))) {
                logger.error("同步用户信息失败，响应内容：" + jsonResponse);
                isErr = true;
            }
        } catch (Exception e) {
            logger.error("同步用户信息失败", e);
            isErr = true;
        }
        return isErr;
    }

    protected boolean registerUserInfo(ZhgUserSyn zhgUserSyn) {
        boolean isErr = false;
        try {
            JSONObject jsonResponse = HttpUtils.sendPostRequestForJson(constantMap.getString(ConstantKeys.USER_SYS_REG_URL), zhgUserSyn.getParams());
            List<Fuser> list = frontUserService.findUserByProperty("floginName", zhgUserSyn.getParams().get(ZhgUserSynUtils.USERNAME_KEY));
            Fuser fuser = list.get(0);
            if (!"1".equals(jsonResponse.getString("ret"))) {
                if(StringUtils.isEmpty(fuser.getZhgOpenId())){
                    // 没有openid则重新注册
                    isErr = true;
                }
                logger.error("注册用户信息失败，响应内容：" + jsonResponse);
            } else {
                String openId = jsonResponse.getString("data");
                fuser.setZhgOpenId(openId);
                frontUserService.updateFuser(fuser);
            }
        } catch (Exception e) {
            logger.error("注册用户信息失败", e);
            isErr = true;
        }

        return isErr;
    }

    protected boolean synUserInfo(ZhgUserSyn zhgUserSyn) {
        boolean isErr = false;
        try {
            JSONObject jsonResponse = HttpUtils.sendPostRequestForJson(constantMap.getString(ConstantKeys.USER_SYS_SYN_INFO_URL), zhgUserSyn.getParams());
            if (!"1".equals(jsonResponse.getString("ret"))) {
                logger.error("同步用户信息失败，响应内容：" + jsonResponse);
                isErr = true;
            } else {
                JSONObject data = jsonResponse.getJSONObject("data");

                String openId = (String) zhgUserSyn.getParams().get(ZhgUserSynUtils.OPEN_ID_KEY);
                String password = data.getString(ZhgUserSynUtils.PASSWORD_KEY);
                /*String mobile = data.getString(ZhgUserSynUtils.MOBILE_KEY);
                String email = data.getString(ZhgUserSynUtils.EMAIL_KEY);
                String realName = data.getString(ZhgUserSynUtils.REAL_NAME_KEY);
                String cardId = data.getString(ZhgUserSynUtils.CARDI_ID_KEY);
                String cardType = data.getString(ZhgUserSynUtils.CARDI_TYPE_KEY);*/

                Fuser fuser = frontUserService.findByZhgOpenId(openId);
                if (StringUtils.hasText(password)) {
                    fuser.setFloginPassword(password);
                }
                /*if (StringUtils.isMobile(mobile)) {
                    fuser.setFisTelephoneBind(true);
                    fuser.setFisTelValidate(true);
                    fuser.setFtelephone(mobile);
                }
                if (StringUtils.isEmail(email)) {
                    fuser.setFemail(email);
                    fuser.setFisMailValidate(true);
                }
                if(!fuser.getFhasRealValidate()){
                    if (StringUtils.hasText(realName)) {
                        fuser.setFrealName(realName);
                    }
                    if (StringUtils.hasText(cardId)) {
                        fuser.setFidentityNo(cardId);
                    }
                    if (StringUtils.hasText(cardType)) {
                        fuser.setFidentityType(Integer.valueOf(cardType));
                    }
                    if(StringUtils.hasText(fuser.getFidentityNo()) && StringUtils.hasText(fuser.getFrealName())){
                        fuser.setFpostRealValidate(true);
                        fuser.setFpostRealValidateTime(Utils.getTimestamp());
                    }
                }*/
                frontUserService.updateFuser(fuser);
            }
        } catch (Exception e) {
            logger.error("同步用户信息失败", e);
            e.printStackTrace();
            isErr = true;
        }

        return isErr;
    }

}
