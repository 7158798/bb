package com.ruizton.main.code;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ruizton.main.Enum.CountLimitTypeEnum;
import com.ruizton.util.Constants;
import com.ruizton.util.Utils;

/**
 * 图片验证码
 * 
 * @author xuelin
 * @date   2016年5月6日
 * @desc
 */
@JsonIgnoreProperties({"expire", "type"})
public class ImageCaptcha implements Captcha {
	private String code;
	private Timestamp createTime;

	public ImageCaptcha() {
	}

	public ImageCaptcha(String code) {
		super();
		this.code = code;
		this.createTime = Utils.getTimestamp();
	}

	public void setCode(String code) {
		this.code = code;
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
	
	@Override
	public boolean isExpire() {
		return System.currentTimeMillis() - this.createTime.getTime() > Constants.CAPTCHA_TIME_OUT;
	}

	@Override
	public int getType() {
		return CountLimitTypeEnum.IMAGE_CAPTCHA;
	}

}
