package com.ruizton.main.auto;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.utils.StringUtils;

import com.ruizton.main.model.Fandroidpushment;
import com.ruizton.main.service.admin.AndroidpustmentService;

public class PushUtil extends TimerTask {
	protected static final Logger LOG = LoggerFactory.getLogger(PushUtil.class);
	@Autowired
	private AndroidpustmentService androidpustmentService;
	@Autowired
	private RealTimeData realTimeData;

	private static final String appKey = "c1a2ed8320f26e9e6300f344";
	private static final String masterSecret = "9d6cd7cc52b803d5ee50d3e3";

	@Override
	public void run() {
		synchronized (this) {
			try {
				
				List<Fandroidpushment> alls = this.androidpustmentService.list(
						0, 0, "where pushment like '%true%'", false);
//				if(true){
//					return;
//				}
				for (Fandroidpushment fandroidpushment : alls) {
					// {"1":{"isopen":true,"high":444,"low":444}}
					try {
						String pushment = fandroidpushment.getPushment();
						JSONObject s = (JSONObject)JSONObject.fromObject(pushment).get("1");
						boolean isOpen = s.getBoolean("isopen");
						if (!isOpen) continue;
						double high = s.getDouble("high");
						double low = s.getDouble("low");
						double lastPrice = this.realTimeData
								.getLatestDealPrize(1);
						String content = "";
						boolean isTrue = false;
						if (lastPrice >= high && high >0d) {
							isTrue = true;
							content = "最新成交价￥" + lastPrice + "已达设置上限";
						} else if (lastPrice <= low && low >0d) {
							isTrue = true;
							content = "最新成交价￥" + lastPrice + "已达设置下限";
						}

                        if(isTrue){
                        	testSendPush(fandroidpushment.getRegistrationId(), content, "金豆网");
                        }
					} catch (Exception e) {
						System.out.println("=========push========"+e.getMessage());
					}
				}
			} catch (Exception e) {
				System.out.println("=========push========"+e.getMessage());
			} 
		}
	}
	
	public static void testSendPush(String registrationId,String content,String title) {
	    // HttpProxy proxy = new HttpProxy("localhost", 3128);
	    // Can use this https proxy: https://github.com/Exa-Networks/exaproxy
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
        
        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_android_tag_alertWithTitle(registrationId,content,title);
        
        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);
            
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        }
	}
	
    
    public static PushPayload buildPushObject_android_tag_alertWithTitle(String registrationId,String content,String title) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.registrationId(registrationId))
                .setNotification(Notification.android(content, title, null))
                .build();
    }

}
