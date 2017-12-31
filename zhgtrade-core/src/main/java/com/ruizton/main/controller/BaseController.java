package com.ruizton.main.controller;


import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.model.Fadmin;
import com.ruizton.main.model.Fuser;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BaseController {

	@Autowired
	private HttpServletRequest request ;
	@Autowired
	private ConstantMap constantMap ;
	@Autowired
	private RealTimeDataService realTimeDataService;

	public HttpServletRequest getRequest() {
		return request;
	}
	
	public HttpSession getSession(){
		return this.getRequest().getSession() ;
	}
	
	//获得管理员session
	public Fadmin getAdminSession(){
		Object session = getSession().getAttribute("login_admin") ;
		Fadmin fadmin = null ;
		if(session!=null){
			fadmin = (Fadmin)session ;
		}
		return fadmin ;
	}
	
	//获得session中的用户
	public Fuser GetSession(){
		Fuser fuser = null ;
		HttpSession session = getSession() ;
		Object session_user = session.getAttribute("login_user") ;
		if(session_user!=null){
			fuser = (Fuser)session_user ;
		}
		return fuser ;
	}
	
	public void RemoveSecondLoginSession(){
		HttpSession session = getSession() ;
		session.setAttribute("second_login_user", null) ;
	}

	public void CleanSession(){
		try {
			HttpSession session = getSession() ;
			String key = GetSession().getFid()+"trade" ;
			getSession().removeAttribute(key);
			session.setAttribute("login_user", null) ;
		} catch (Exception e) {
		}
	}
	
	//for menu
	@ModelAttribute
	public void menuSelect(){
		if(Utils.isAjax(request)){
			return;
		}
		
		//banner菜单
		String selectMenu = null ;
		String uri = getRequest().getRequestURI() ;
		
		if(uri.startsWith("/trade/subscriptionrmb")
				||uri.startsWith("/trade/subscriptioncoin")
				||uri.startsWith("/hongbao")
				||uri.startsWith("/hedging/")
				||uri.startsWith("/gamecenter/goldenEgg")
				||uri.startsWith("/gamecenter/")){
			selectMenu = "appcenter";
		}else if(uri.startsWith("/appcenter/")
				||uri.startsWith("/trade/subscriptioncoin")
				||uri.startsWith("/trade/subscriptionrmb")){
			selectMenu = "appcenter" ;
		}else if(uri.startsWith("/trade")
				||uri.startsWith("/user")
				||uri.startsWith("/lend/")
				||uri.startsWith("/introl/")
				||uri.startsWith("/question")){
			selectMenu = "trade";
		}else if(uri.startsWith("/voteList")
				||uri.startsWith("/voteLogList")){
			selectMenu = "vote" ;
		}else if(uri.startsWith("/market")){
			selectMenu = "market" ;
		}else if(uri.startsWith("/service")){
			selectMenu = "service" ;
		}else if(uri.startsWith("/financial")
				||uri.startsWith("/shop/sell")
				||uri.startsWith("/balance/")
				||uri.startsWith("/vouchers/")
				||uri.startsWith("/shop/myorder")
				||uri.startsWith("/account")){
			selectMenu = "financial" ;
		}else if(uri.startsWith("/bbs")){
			selectMenu = "bbs" ;
		}else if(uri.startsWith("/download")){
			selectMenu = "download" ;
		}else if(uri.startsWith("/shop/index")
				||uri.startsWith("/shop/view")){
			selectMenu = "shop" ;
		}else{
			selectMenu = "index";
		}
		getRequest().setAttribute("selectMenu", selectMenu) ;
		
		//是否显示游戏栏
//		Fgame fgame = this.frontGameService.findFirstGame() ;
//		if(fgame!=null)
//		{
//			getRequest().setAttribute("showgame", true) ;
//		}
		//是否显示显示虚拟币充值、提现
//		boolean showWithdrawBtc = this.frontVirtualCoinService.isExistsCanWithdrawCoinType() ;
//		getRequest().setAttribute("withdrawBtc", false) ;
		
		//是否显示融资融币
//		boolean showLend = this.frontLendService.findFlendsystemargsById(FlendSystemargsIds.ISOPENED).getBooleanValue() ;
//		getRequest().setAttribute("showLend", false) ;
		
		//左侧菜单
		if(uri.startsWith("/trade")
				||uri.startsWith("/user")
				||uri.startsWith("/shop/")
				||uri.startsWith("/divide/")
				||uri.startsWith("/introl/mydivide")
				||uri.startsWith("/account")
				||uri.startsWith("/financial")
				||uri.startsWith("/hedging/")
				||uri.startsWith("/balance/")
				||uri.startsWith("/vouchers")//融资融币
				||uri.startsWith("/gamecenter/")
				||uri.startsWith("/question")){
			String leftMenu = null ;
			int selectGroup = 1 ;
			
			if(uri.startsWith("/question/questionColumn")){
				leftMenu = "questionColumn";
				selectGroup = 4 ;
			}else if(uri.startsWith("/question/question")){
				leftMenu = "question";
				selectGroup = 4 ;
			}else if(uri.startsWith("/user/apply")){
				leftMenu = "apply";
				selectGroup = 4 ;
			}if(uri.startsWith("/question/message")){
				leftMenu = "message";
				selectGroup = 4 ;
			}else if(uri.startsWith("/user/userinfo")){
				leftMenu = "userinfo" ;
				selectGroup = 3 ;
			}else if(uri.startsWith("/user/intro")){
				leftMenu = "intro" ;
				selectGroup = 3 ;
			}else if(uri.startsWith("/user/security")){
				leftMenu = "security" ;
				selectGroup = 3 ;
			}else if(uri.startsWith("/account/record")){
				leftMenu = "record" ;
				selectGroup = 2 ;
			}else if(uri.startsWith("/account/payCode")){
				leftMenu = "payCode" ;
				selectGroup = 2 ;
			}else if(uri.startsWith("/account/deduct")){
				leftMenu = "record" ;
				selectGroup = 2 ;
			}else if(uri.startsWith("/account/rechargeCny")){
				leftMenu = "rechargeCny" ;
				selectGroup = 2 ;
			}else if(uri.startsWith("/account/withdrawCny")){
				leftMenu = "withdrawCny" ;
				selectGroup = 2 ;
			}else if(uri.startsWith("/account/rechargeBtc")){
				leftMenu = "rechargeBtc" ;
				selectGroup = 2 ;
			}else if(uri.startsWith("/account/withdrawBtc")){
				leftMenu = "withdrawBtc" ;
				selectGroup = 2 ;
			}else if(uri.startsWith("/divide/")){
				leftMenu = "divide" ;
				selectGroup = 1 ;
			}else if(uri.startsWith("/free/lottery")){
				leftMenu = "free_lottery" ;
				selectGroup = 1 ;
			}else if(uri.startsWith("/hedging/")){
				leftMenu = "hedging" ;
				selectGroup = 1 ;
			}else if(uri.startsWith("/trade/subscriptioncoin")){
				leftMenu = "subscriptioncoin" ;
				selectGroup = 6 ;
			}else if(uri.startsWith("/gamecenter/game")){
				leftMenu = "game" ;
				selectGroup = 6 ;
			}else if(uri.startsWith("/trade/subscriptionrmb")){
				leftMenu = "subscriptionrmb" ;
				selectGroup = 6 ;
			}else if(uri.startsWith("/trade/subscriptionyg")
					||uri.startsWith("/trade/subscriptionylog")){
				leftMenu = "subscriptionyg" ;
				selectGroup = 1 ;
			}else if(uri.startsWith("/trade/entrust")){
				leftMenu = "entrust" ;
				selectGroup = 1 ;
			}else if(uri.startsWith("/balance/")){
				leftMenu = "balance" ;
				selectGroup = 1 ;
			}else if(uri.startsWith("/gamecenter/index")
					||uri.startsWith("/gamecenter/popcorn")
					||uri.startsWith("/gamecenter/goldenEgg")
					||uri.startsWith("/gamecenter/")){
				leftMenu = "gamecenter" ;
				selectGroup = 1 ;
			}else if(uri.startsWith("/lend")){
				leftMenu = "lendContent";
				selectGroup = 1 ;
			}else if(uri.startsWith("/financial/")){
				leftMenu = "financial" ;
				selectGroup = 2 ;
			}else if(uri.startsWith("/vouchers/")){
				leftMenu = "vouchers" ;
				selectGroup = 2 ;
			}else if(uri.startsWith("/introl/")){
				leftMenu = "introl" ;
				selectGroup = 3 ;
			}else if(uri.startsWith("/shop/myorder")){
				leftMenu = "myorder" ;
				selectGroup = 2 ;
			}else if(uri.startsWith("/shop/sell")){
				leftMenu = "mysell" ;
				selectGroup = 2 ;
			}else if(uri.startsWith("/trade/coin")){
				selectGroup = 1 ;
				Integer type = 0 ;
				String coinType = getRequest().getParameter("coinType") ;
				if(coinType==null || "".equals(coinType.trim())){
					type = 0 ;
				}else{
					type = Integer.parseInt(coinType.trim()) ;
				}
				getRequest().setAttribute("selectGroup", selectGroup) ;
				getRequest().setAttribute("leftMenu", String.valueOf(type)) ;
				return ;
			}
			getRequest().setAttribute("selectGroup", selectGroup) ;
			getRequest().setAttribute("leftMenu", leftMenu) ;
		}
	}
	/**
	 * 添加静态资源路径前缀
	* 比特家
	* CopyRight : www.zhgtrade.com
	* Author : 俞杰（945351749@qq.com）
	* Date : 2016年4月7日 下午5:24:30
	 */
	@ModelAttribute
	public void staticFileUrl(){
		if(Utils.isAjax(request)){
			return;
		}
		
		String url="http://zhgtrade.oss-cn-qingdao.aliyuncs.com";
		getRequest().setAttribute("staticFileUrl", url) ;
	}
	@ModelAttribute
	public void addConstant(){//此方法会在每个controller前执行
		if(Utils.isAjax(request)){
			return;
		}
		
		Cookie[] cookies = request.getCookies() ;
		int okhelp = 0 ;
		if(cookies!=null && cookies.length>0){
			for (Cookie cookie : cookies) {
				if("okhelp".equalsIgnoreCase(cookie.getName())){
					try {
						okhelp = Integer.parseInt(cookie.getValue());
					} catch (Exception e) {
						
					}
				}
			}
		}
		if(okhelp != 0 && okhelp != 1){
			okhelp = 1 ;
		}
		request.setAttribute("okhelp", okhelp) ;
		//前端常量
		getRequest().setAttribute("constant", constantMap.getMap()) ;

		// 最新成交价格，数据结构精简
		getRequest().setAttribute("realTimePrize", realTimeDataService.getLatestDealDataList()) ;

	}

}
