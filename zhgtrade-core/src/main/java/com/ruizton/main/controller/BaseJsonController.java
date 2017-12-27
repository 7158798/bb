package com.ruizton.main.controller;


import com.ruizton.main.dao.FvirtualcointypeDAO;
import com.ruizton.main.model.Flimittrade;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.LimittradeService;
import com.ruizton.main.service.admin.SystemArgsService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 所有需要优化的Controller
 * 都应该放弃BaseController类
 * 改成集成这个类，此类提供了类似BaseController的方法
 * 去掉了@ModelAttribute这个性能杀死
 * 还去掉了注入HttpServletRequest这个奇葩的东西
 * 要获取这个对象必须通过方法级别的注入，能不用就不用
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date：
 */
public class BaseJsonController {

	public static final String JsonEncode = "application/json;charset=UTF-8" ;
	@Autowired
	private LimittradeService limittradeService ;
	@Autowired
	private SystemArgsService systemArgsService;
	@Autowired
	private FrontVirtualCoinService frontVirtualCoinService; 

	//获得session中的用户
	public Fuser GetSession(HttpSession session){
		Fuser fuser = null ;
		Object session_user = session.getAttribute("login_user") ;
		if(session_user!=null){
			fuser = (Fuser)session_user ;
		}
		return fuser ;
	}
	
	public boolean isNeedTradePassword(HttpSession session){
		if(GetSession(session) == null) return true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String key = GetSession(session).getFid()+"trade" ;
		Object obj = session.getAttribute(key) ;
		
		if(obj==null){
			return true ;
		}else{
			try {
				double hour = Double.valueOf(this.systemArgsService.getValue("tradePasswordHour"));
				double lastHour = Utils.getDouble((sdf.parse(obj.toString()).getTime()-new Date().getTime())/1000/60/60, 2);
				if(lastHour >= hour){
					session.removeAttribute(key);
					return true ;
				}else{
					return false ;
				}
			} catch (ParseException e) {
				return false ;
			}
		}
	}
	
	public void setNoNeedPassword(HttpSession session){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String key = GetSession(session).getFid()+"trade" ;
		session.setAttribute(key,sdf.format(new Date())) ;
	}
	
	public Flimittrade isLimitTrade(int vid) {
		Flimittrade flimittrade = null;
		String filter = "where fvirtualcointype.fid="+vid;
		List<Flimittrade> flimittrades = this.limittradeService.list(0, 0, filter, false);
		if(flimittrades != null && flimittrades.size() >0){
			flimittrade = flimittrades.get(0);
		}
		return flimittrade;
	}

	public boolean openTrade(String value){

		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		try {
			Date beforeDate = df.parse(value.trim().split("-")[0]);
			Date afterDate = df.parse(value.trim().split("-")[1]);
			Date time = df.parse(df.format(new Date()));
			if ( time.after(beforeDate) && time.before(afterDate)) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("输入的时间区间不能进行格式化，时间格式为：HH:mm-HH:mm");
		}

		//抛出异常，返回false
		return false;
//		Timestamp now = Utils.getTimestamp() ;
//		int nows = Integer.parseInt(new SimpleDateFormat("HH").format(now)) ;
//
//		boolean flag = true ;
//
////		String value = this.systemArgsService.getValue("openTrade") ;
//		int min = Integer.parseInt(value.trim().split("-")[0]) ;
//		int max = Integer.parseInt(value.trim().split("-")[1]) ;
//		//24-0代表24小时，0-24代表不开放交易
//		if(min == 0 && max == 24){
//			return false;
//		}
//		if(min == 24 && max == 0){
//			return true;
//		}
//
//		if(min<=max){
//			if(nows>=min && nows<=max){
//				flag = false ;
//			}
//		}
//
//		if(max<min){
//			if(!(nows>max && nows<min)){
//				flag = false ;
//			}
//		}
//
//		return flag ;
	}
	
}
