package com.zhgtrade.front.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruizton.main.Enum.CountLimitTypeEnum;
import com.ruizton.main.Enum.ValidateMessageStatusEnum;
import com.ruizton.main.code.Captcha;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.comm.MessageValidate;
import com.ruizton.main.comm.ValidateMap;
import com.ruizton.main.dto.LatestDealData;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvalidatemessage;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.front.FrontValidateService;
import com.ruizton.util.ConstantKeys;
import com.ruizton.util.Constants;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BaseController {
	@Autowired
	private ValidateMap messageValidateMap;
	@Autowired
	private ConstantMap constantMap;
	@Autowired
	private FrontValidateService frontValidateService;
	@Autowired
	private MessageQueueService messageQueueService;

	public Fuser getSessionUser(HttpServletRequest request){
		return (Fuser) request.getSession().getAttribute(Constants.USER_LOGIN_SESSION);
	}
	
	public boolean isLogin(HttpServletRequest request){
		return null != getSessionUser(request);
	}
	
	public String getStaticServer(){
		return "http://zhgtrade.oss-cn-qingdao.aliyuncs.com";
	}
	
	public void setSecondLoginSession(HttpServletRequest request, Fuser fuser){
		request.getSession().setAttribute(Constants.USER_SECOND_LOGIN_SESSION, fuser);
	}
	
	public void setLoginSession(HttpServletRequest request, Fuser fuser){
		request.getSession().setAttribute(Constants.USER_LOGIN_SESSION, fuser);
	}
	
	public void setFormToken(HttpServletRequest request){
		request.getSession().setAttribute(Constants.FORM_TOKEN, Utils.randomInteger(6));
	}

	public String getFormToken(HttpServletRequest request){
		return (String) request.getSession().getAttribute(Constants.FORM_TOKEN);
	}

	public void cleanFormToken(HttpServletRequest request){
		request.getSession().removeAttribute(Constants.FORM_TOKEN);
	}

	public Captcha getCaptcha(HttpServletRequest request){
		return (Captcha) request.getSession().getAttribute(Constants.SESSION_CAPTCHA_CODE);
	}
	
	public void cleanCaptcha(HttpServletRequest request){
		request.getSession().removeAttribute(Constants.SESSION_CAPTCHA_CODE);
	}

	public String generatePagin(int total,int currentPage,String path){
		if(total<=0){
			return "" ;
		}

		StringBuffer sb = new StringBuffer() ;

		if(currentPage==1){
			sb.append("<li><a style='color:#FFFFFF;' class='current_ss' href='javascript:void(0)'>1</a></li>") ;
		}else{
			sb.append("<li><a href='"+path+"currentPage=1'>&lt</a></li>") ;
			sb.append("<li><a href='"+path+"currentPage=1'>1</a></li>") ;
		}

		if(currentPage==2){
			sb.append("<li><a style='color:#FFFFFF;' class='current_ss' href='javascript:void(0)'>2</a></li>") ;
		}else if(total>=2){
			sb.append("<li><a href='"+path+"currentPage=2'>2</a></li>") ;
		}

		if(currentPage>=7){
			sb.append("<li><a href='javascript:void(0)'>...</a></li>") ;
		}

		//前三页
		int begin = currentPage-3 ;
		begin = begin<=2?3:begin ;
		for(int i=begin;i<currentPage;i++){
			sb.append("<li><a href='"+path+"currentPage="+i+"'>"+i+"</a></li>") ;
		}

		if(currentPage!=1&&currentPage!=2){
			sb.append("<li><a style='color:#FFFFFF;' class='current_ss' href='javascript:void(0)'>"+currentPage+"</a></li>") ;
		}

		//后三页
		begin = currentPage+1;
		begin = begin<=2?3:begin ;
		int end = currentPage+4 ;
		if(currentPage<6){
			int tInt = 6- currentPage ;
			end = end+((tInt>3?3:tInt)) ;
		}
		for(int i=begin;i<end&&i<=total;i++){
			sb.append("<li><a href='"+path+"currentPage="+i+"'>"+i+"</a></li>") ;
		}


		if(total-currentPage==4){
			sb.append("<li><a href='"+path+"currentPage="+total+"'>"+total+"</a></li>") ;
		}else if(total-currentPage>3){
			sb.append("<li><a href='javascript:void(0)'>...</a></li>") ;
		}

		if(total>=11&&total-currentPage>4){
			sb.append("<li><a href='"+path+"currentPage="+total+"'>"+total+"</a></li>") ;
		}

		if(currentPage<total){
			sb.append("<li><a href='"+path+"currentPage="+total+"'>&gt</a></li>") ;
		}

		return sb.toString() ;
	}

	public boolean validateMessageCode(Fuser fuser,String areaCode,String phone,int type,String code){
		boolean match = true ;
		MessageValidate messageValidate = this.messageValidateMap.getMessageMap(fuser.getFid()+"_"+type) ;
		if(messageValidate==null){
			match = false ;
		}else{
			if(!messageValidate.getAreaCode().equals(areaCode)
					||!messageValidate.getPhone().equals(phone)
					||!messageValidate.getCode().equals(code)){
				match = false ;
			}else{
				match = true ;
				this.messageValidateMap.removeMessageMap(fuser.getFid()+"_"+type);
			}
		}

		return match ;
	}

	public void updateCaptchaLimitedCount(HttpServletRequest request, Captcha captcha){
		if(null == captcha) return;
		if(CountLimitTypeEnum.TELEPHONE == captcha.getType()){
			this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), CountLimitTypeEnum.TELEPHONE);
		}else if(CountLimitTypeEnum.EMAIL == captcha.getType()){
			this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), CountLimitTypeEnum.EMAIL);
		}else if(CountLimitTypeEnum.IMAGE_CAPTCHA == captcha.getType()){
			this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), CountLimitTypeEnum.IMAGE_CAPTCHA);
		}
	}

	public void deleteCaptchaLimitedCount(HttpServletRequest request, Captcha captcha){
		if(null == captcha) return;
		cleanCaptcha(request);
		if(CountLimitTypeEnum.TELEPHONE == captcha.getType()){
			this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), CountLimitTypeEnum.TELEPHONE);
		}else if(CountLimitTypeEnum.EMAIL == captcha.getType()){
			this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), CountLimitTypeEnum.EMAIL);
		}else if(CountLimitTypeEnum.IMAGE_CAPTCHA == captcha.getType()){
			this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), CountLimitTypeEnum.IMAGE_CAPTCHA);
		}
	}

	public void loadConstants(Map map) {
		map.put("constant", constantMap.getMap());
	}

	public boolean SendMessage(Fuser fuser,int fuserid,String areaCode,String phone,int type){
		boolean canSend = true ;
		MessageValidate messageValidate = this.messageValidateMap.getMessageMap(fuserid+"_"+type) ;
		if(messageValidate!=null && Utils.timeMinus(Utils.getTimestamp(), messageValidate.getCreateTime())<120){
			canSend = false ;
		}

		if(canSend){
			MessageValidate messageValidate2 = new MessageValidate() ;
			messageValidate2.setAreaCode(areaCode) ;
			messageValidate2.setCode(Utils.randomInteger(6)) ;
			messageValidate2.setPhone(phone) ;
			messageValidate2.setCreateTime(Utils.getTimestamp()) ;
			this.messageValidateMap.putMessageMap(fuserid+"_"+type, messageValidate2) ;

			Fvalidatemessage fvalidatemessage = new Fvalidatemessage() ;
			fvalidatemessage.setFcontent(this.constantMap.getString(ConstantKeys.VALIDATE_MESSAGE_CONTENT).replace("#code#", messageValidate2.getCode())) ;
			fvalidatemessage.setFcreateTime(Utils.getTimestamp()) ;
			fvalidatemessage.setFphone(phone) ;
			fvalidatemessage.setFuser(fuser) ;
			fvalidatemessage.setFstatus(ValidateMessageStatusEnum.Not_send) ;
			this.frontValidateService.addFvalidateMessage(fvalidatemessage) ;
			//发送短信通知  yujie
			Fuser fuser2=new Fuser();
			fuser2.setFid(fuser.getFid());
			fvalidatemessage.setFuser(fuser2) ;
			messageQueueService.publish(QueueConstants.MESSAGE_COMMON_QUEUE, fvalidatemessage);
		}
		return canSend ;
	}

	public void sortCoinRank(List<LatestDealData> dealDatas, int sort){
		switch (sort){
			case 1:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 最新成交价升序
					return Double.valueOf(o1.getLastDealPrize()).compareTo(o2.getLastDealPrize());
				});
				break;
			case 2:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 最新成交价降序
					return Double.valueOf(o2.getLastDealPrize()).compareTo(o1.getLastDealPrize());
				});
				break;
			case 3:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 24h成交量升序
					return Double.valueOf(o1.getVolumn()).compareTo(o2.getVolumn());
				});
				break;
			case 4:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 24h成交量降序
					return Double.valueOf(o2.getVolumn()).compareTo(o1.getVolumn());
				});
				break;
			case 5:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 总市值升序
					return Double.valueOf(o1.getFmarketValue()).compareTo(o2.getFmarketValue());
				});
				break;
			case 6:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 总市值降序
					return Double.valueOf(o2.getFmarketValue()).compareTo(o1.getFmarketValue());
				});
				break;
			case 7:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 日涨跌幅升序
					return Double.valueOf(o1.getFupanddown()).compareTo(o2.getFupanddown());
				});
				break;
			case 8:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 日涨跌幅降序
					return Double.valueOf(o2.getFupanddown()).compareTo(o1.getFupanddown());
				});
				break;
			case 9:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 周涨跌幅升序
					return Double.valueOf(o1.getFupanddownweek()).compareTo(o2.getFupanddownweek());
				});
				break;
			case 10:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 周涨跌幅降序
					return Double.valueOf(o2.getFupanddownweek()).compareTo(o1.getFupanddownweek());
				});
				break;
			case 13:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 买价升序
					return Double.valueOf(o1.getLowestSellPrize()).compareTo(o2.getLowestSellPrize());
				});
				break;
			case 14:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 买价降序
					return Double.valueOf(o2.getLowestSellPrize()).compareTo(o1.getLowestSellPrize());
				});
				break;
			case 15:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 卖价升序
					return Double.valueOf(o1.getHigestBuyPrize()).compareTo(o2.getHigestBuyPrize());
				});
				break;
			case 16:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 卖价降序
					return Double.valueOf(o2.getHigestBuyPrize()).compareTo(o1.getHigestBuyPrize());
				});
				break;
			case 17:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 成交额升序
					return Double.valueOf(o1.getFentrustValue()).compareTo(o2.getFentrustValue());
				});
				break;
			case 18:
				Collections.sort(dealDatas, (o1, o2) -> {
					// 成交额降序
					return Double.valueOf(o2.getFentrustValue()).compareTo(o1.getFentrustValue());
				});
				break;
			case 19:
				// 首页排序
				Collections.sort(dealDatas, (o1, o2) -> {
					int val = 0;
					if(o1.getHomeOrder() == 0 && o2.getHomeOrder() != 0){
						val = 1;
					}else if(o1.getHomeOrder() != 0 && o2.getHomeOrder() == 0){
						val = -1;
					} else {
						val = Integer.valueOf(o1.getHomeOrder()).compareTo(o2.getHomeOrder());
					}
					return val;
				});
				break;
			case 20:
				// 板块排序
				Collections.sort(dealDatas, (o1, o2) -> {
					int val = 0;
					if(o1.getTypeOrder() == 0 && o2.getTypeOrder() != 0){
						val = 1;
					}else if(o1.getTypeOrder() != 0 && o2.getTypeOrder() == 0){
						val = -1;
					} else {
						val = Integer.valueOf(o1.getTypeOrder()).compareTo(o2.getTypeOrder());
					}
					return val;
				});
				break;
			default:
				// 默认排序
				Collections.sort(dealDatas, (o1, o2) -> {
					if(o1.getTotalOrder() == 0 && o2.getTotalOrder() != 0){
						return 1;
					}else if(o1.getTotalOrder() != 0 && o2.getTotalOrder() == 0){
						return -1;
					} else {
						return Integer.valueOf(o1.getTotalOrder()).compareTo(o2.getTotalOrder());
					}
				});
				break;
		}
	}
}
