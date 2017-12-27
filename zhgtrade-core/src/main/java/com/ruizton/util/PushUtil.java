package com.ruizton.util;

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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.jpush.api.utils.StringUtils;

import com.ruizton.main.auto.RealTimeData;
import com.ruizton.main.model.Fandroidpushment;
import com.ruizton.main.service.admin.AndroidpustmentService;

public class PushUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(PushUtil.class);
    @Autowired
	private AndroidpustmentService androidpustmentService;
    @Autowired
	private RealTimeData realTimeData;
    
	private static final String appKey ="afc3906f71ad2aebe5d263b4";
	private static final String masterSecret = "edf212839c2b8642dfd7e71c";
    private static String postUrl = "http://api.jpush.cn:8800/v2/push";
	
	public void work() {
		 try {
	            //创建连接
	            URL url = new URL(postUrl);
	            HttpURLConnection connection = (HttpURLConnection) url
	                    .openConnection();
	            List<Fandroidpushment> alls = this.androidpustmentService.list(0, 0, "where pushment like '%true%'", false);
	            for (Fandroidpushment fandroidpushment : alls) {
	            //	{"1":{"isopen":true,"high":444,"low":444}}
	            	try {
						String pushment = fandroidpushment.getPushment();
						JSONArray s = JSONObject.fromObject(pushment).getJSONArray("1");
						boolean isOpen = s.getBoolean(0);
						if(!isOpen) continue;
						double high = s.getDouble(1);
						double low = s.getDouble(2);
						double lastPrice = this.realTimeData.getLatestDealPrize(1);
						String content = "";
						if(lastPrice >= high){
							content = "最新成交价￥"+lastPrice+"已达设置上限";
						}else if(lastPrice <= low){
							content = "最新成交价￥"+lastPrice+"已达设置下限";
						}
						
						//POST请求
			            DataOutputStream out = new DataOutputStream(
			                    connection.getOutputStream());
			            JSONObject obj = new JSONObject();
			            int sendno = new Random().nextInt();
			            int receiverType = 5;
			            String receiverValue = fandroidpushment.getRegistrationId();
			            String input = String.valueOf(sendno) + receiverType + receiverValue + masterSecret;
			            String verificationCode = StringUtils.toMD5(input);
			            obj.element("sendno", sendno);
			            obj.element("app_key", appKey);
			            obj.element("receiver_type", receiverType);
			            obj.element("receiver_value",receiverValue);
			            obj.element("verification_code", verificationCode);
			            obj.element("msg_type", "2");
			            obj.element("msg_content", content);
			            obj.element("platform", "android");
			            out.writeBytes(obj.toString());
			            out.flush();
			            out.close();
			          //读取响应
			            BufferedReader reader = new BufferedReader(new InputStreamReader(
			                    connection.getInputStream()));
			            String lines;
			            StringBuffer sb = new StringBuffer("");
			            while ((lines = reader.readLine()) != null) {
			                lines = new String(lines.getBytes(), "utf-8");
			                sb.append(lines);
			            }
			            reader.close();
					} catch (Exception e) {
						continue;
					}
				}
	            connection.disconnect();
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
    
}

