package com.ruizton.main.comm;

import java.util.HashMap;
import java.util.Map;

import com.ruizton.main.model.Emailvalidate;

public class ValidateMap {
	private Map<String, MessageValidate> messageMap = new HashMap<String, MessageValidate>() ;
	private Map<String, Emailvalidate> mailMap = new HashMap<String, Emailvalidate>() ;
	
	//短信
	public synchronized void putMessageMap(String key,MessageValidate messageValidate){
		this.messageMap.put(key, messageValidate) ;
	}
	
	public MessageValidate getMessageMap(String key){
		return this.messageMap.get(key) ;
	}
	public void removeMessageMap(String key){
		this.messageMap.remove(key) ;
	}
	
	//邮件
	public synchronized void putMailMap(String key,Emailvalidate messageValidate){
		this.mailMap.put(key, messageValidate) ;
	}
	
	public Emailvalidate getMailMap(String key){
		return this.mailMap.get(key) ;
	}
	public void removeMailMap(String key){
		this.mailMap.remove(key) ;
	}
}
