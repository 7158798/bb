package com.ruizton.main.code;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ruizton.main.Enum.CountLimitTypeEnum;
import com.ruizton.util.Constants;
import com.ruizton.util.Utils;

/**
 * 发送邮箱验证码
 * 
 * @author xuelin
 * @date   2016年5月6日
 * @desc
 */
@JsonIgnoreProperties({"expire", "type"})
public class EmailMessageCaptcha implements Captcha {
	private String code;
	private String email;
	private Timestamp createTime;

	public EmailMessageCaptcha() {
	}

	public EmailMessageCaptcha(String code, String email) {
		super();
		this.code = code;
		this.email = email;
		this.createTime = Utils.getTimestamp();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public Timestamp getCreateTime() {
		return createTime;
	}

	public String getEmail() {
		return email;
	}
	
	@Override
	public boolean isExpire() {
		return System.currentTimeMillis() - this.createTime.getTime() > Constants.CAPTCHA_TIME_OUT;
	}

	@Override
	public int getType() {
		return CountLimitTypeEnum.EMAIL;
	}
}
