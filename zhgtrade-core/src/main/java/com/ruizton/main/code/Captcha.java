package com.ruizton.main.code;

import java.sql.Timestamp;

/**
 * 验证码
 * 
 * @author xxp
 * @date   2016年5月6日
 * @desc
 */
public interface Captcha {
	
	String getCode();
	
	Timestamp getCreateTime();
	
	boolean isExpire();

	int getType();

}
