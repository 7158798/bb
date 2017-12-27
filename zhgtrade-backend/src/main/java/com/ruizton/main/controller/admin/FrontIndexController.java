package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.UserStatusEnum;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.comm.KeyValues;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Farticle;
import com.ruizton.main.model.Farticletype;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.service.front.FrontOthersService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.FrontVirtualCoinService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Controller
public class FrontIndexController extends BaseController {
	
	@Autowired
	private FrontOthersService frontOtherService ;
	@Autowired
	private FrontUserService frontUserService ;
//	@Autowired
//	private SystemArgsService systemArgsService;
	@Autowired
	private FrontVirtualCoinService frontVirtualCoinService ;
//	@Autowired
//	private RealTimeData realTimeData ;
	@Autowired
	private VirtualCoinService virtualCoinService;
//	@Autowired
//	private RealTimeDataService realTimeDataService;
	 
	
	@RequestMapping("/index")
	@RequiresPermissions("index.html")
	public ModelAndView index(
			@RequestParam(required=false,defaultValue="0")int index,
			@RequestParam(required=false,defaultValue="")String forwardUrl,
			@RequestParam(required=false,defaultValue="0")int symbol,
			HttpServletResponse resp
			){
		 
		//推广注册
		try{
			int id = 0 ;
			id = Integer.parseInt(getRequest().getParameter("r")) ;
			if(id!=0){
				Fuser intro = this.frontUserService.findById(id) ;
				if(intro!=null){
					resp.addCookie(new Cookie("r", String.valueOf(id))) ;
				}
			}
		}catch(Exception e){}
		 
		
		ModelAndView modelAndView = new ModelAndView() ;
		if(GetSession()==null){
			modelAndView.addObject("forwardUrl",forwardUrl) ;
		}else{
			Fuser fuser = this.frontUserService.findById(GetSession().getFid()) ;
			if(fuser.getFstatus()==UserStatusEnum.FORBBIN_VALUE){
				CleanSession() ;
			}
		}
		
		if(index==1){
			RemoveSecondLoginSession() ;
		}
		
		Fvirtualcointype fvirtualcointype = null ;
		if(symbol==0){
			fvirtualcointype = this.frontVirtualCoinService.findFirstFirtualCoin() ;
			symbol = fvirtualcointype.getFid() ;
		}else{
			fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol) ;
		}
		
		//友情链接
//		List<Ffriendlink> ffriendlinks = this.frontOtherService.findFfriendLink(LinkTypeEnum.LINK_VALUE) ;
		
		List<KeyValues> articles = new ArrayList<KeyValues>() ;
		List<Farticletype> farticletypes = this.frontOtherService.findFarticleTypeAll() ;
		for (int i = 0; i < farticletypes.size(); i++) {
			KeyValues keyValues = new KeyValues() ;
			Farticletype farticletype = farticletypes.get(i) ;
			List<Farticle> farticles = this.frontOtherService.findFarticle(farticletype.getFid(), 0, 10) ;
			keyValues.setKey(farticletype) ;
			keyValues.setValue(farticles) ;
			articles.add(keyValues) ;
			if(farticletype.getFid() == 2 && farticles.size() >0){
				modelAndView.addObject("newArticle", farticles.get(0)) ;
			}
		}
		
//		modelAndView.addObject("linkAddress1", this.systemArgsService.getValue("linkAddress1")) ;
//		modelAndView.addObject("linkAddress2", this.systemArgsService.getValue("linkAddress2")) ;
//		modelAndView.addObject("linkAddress3", this.systemArgsService.getValue("linkAddress3")) ;
		
		//qq群
//		List<Ffriendlink> QQquns = this.frontOtherService.findFriendLink(LinkTypeEnum.QQ_VALUE, 0, Integer.MAX_VALUE) ;
		

		List<Fvirtualcointype> fvirtualcointypes = this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal) ;
		
//		Set<Fentrust> buy = this.realTimeData.getEntrustBuyMap(symbol) ;
//		Set<Fentrust> sell = this.realTimeData.getEntrustSellMap(symbol) ;
//		Set<Fentrust> success = this.realTimeData.getEntrustSuccessMap(symbol) ;

//		modelAndView.addObject("buy", buy) ;
//		modelAndView.addObject("sell", sell) ;
//		modelAndView.addObject("success", success) ;

		// 无用代码
//		TreeMap<Fvirtualcointype, Map<String, Object>> realTimeMap = new TreeMap<Fvirtualcointype, Map<String,Object>>(new Comparator<Fvirtualcointype>() {
//
//			public int compare(Fvirtualcointype o1, Fvirtualcointype o2) {
//				return o1.getFid().compareTo(o2.getFid()) ;
//			}
//		}) ;
//		for (Fvirtualcointype fvirtualcointype2 : fvirtualcointypes) {
//			Map<String, Object> m = setRealTimeData(fvirtualcointype2.getFid()) ;
//			m.put("name", fvirtualcointype2.getfShortName()) ;
//			m.put("name1", fvirtualcointype2.getFname()) ;
//			realTimeMap.put(fvirtualcointype2,m ) ;
//		}
//		modelAndView.addObject("realTimeMap", realTimeMap) ;

//
//		//金豆
//		if(GetSession()!=null){
//			Fvirtualcointype jindou = this.frontVirtualCoinService.findFvirtualCoinById(1) ;
//			Fvirtualwallet jindouWallet = this.frontUserService.findVirtualWalletByUser(GetSession().getFid(), 1) ;
//			modelAndView.addObject("jindou", jindou) ;
//			modelAndView.addObject("jindouWallet", jindouWallet) ;
//		}
		modelAndView.addObject("fvirtualcointypes", fvirtualcointypes) ;
		modelAndView.addObject("fvirtualcointype", fvirtualcointype) ;

//        modelAndView.addObject("userQty", realTimeDataService.getUserQty()) ;
//        modelAndView.addObject("tradeAmt", realTimeDataService.getTradeAmt()+20000000) ;
//		char[] xx = String.valueOf(realTimeDataService.getTradeAmt()+20000000).toCharArray();
//		modelAndView.addObject("xx", xx) ;
		modelAndView.addObject("articles", articles) ;
//		modelAndView.addObject("ffriendlinks",ffriendlinks) ;
		modelAndView.setViewName("front/index") ;
		return modelAndView ;
	}
	
	
	@RequestMapping("/index_chart")
	@RequiresPermissions("index_chart.html")
	public ModelAndView index_chart(
			@RequestParam(required=false,defaultValue="0")int symbol
			) throws Exception {
		ModelAndView modelAndView = new ModelAndView() ;
		Fvirtualcointype fvirtualcointype = null ;
		if(symbol==0){
			fvirtualcointype = this.frontVirtualCoinService.findFirstFirtualCoin() ;
		}else{
			fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol) ;
		}
		modelAndView.addObject("fvirtualcointype", fvirtualcointype) ;
		
		String sql = "where fstatus="+VirtualCoinTypeStatusEnum.Normal+" order by version desc";
		List<Fvirtualcointype> fvirtualcointypes = this.virtualCoinService.list(0, 0, sql, false);
		modelAndView.addObject("fvirtualcointypes", fvirtualcointypes) ;
		
		modelAndView.setViewName("front/index_chart") ;
		return modelAndView ;
	}
	
}
