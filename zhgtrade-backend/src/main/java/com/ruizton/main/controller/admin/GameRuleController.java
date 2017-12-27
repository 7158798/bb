package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.GameTypeEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fgamerule;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.GameRuleService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GameRuleController extends BaseController {
	@Autowired
	private GameRuleService gameRuleService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private VirtualCoinService virtualCoinService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/gameRuleList")
	@RequiresPermissions("ssadmin/gameRuleList.html")
	public ModelAndView gameRuleList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/gameRuleList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			try {
				int flevel = Integer.parseInt(keyWord.trim());
				filter.append("and flevel =" + flevel + " \n");
				modelAndView.addObject("keywords", flevel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}
		List<Fgamerule> list = this.gameRuleService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("gameRuleList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "gameRuleList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fgamerule", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goGameRuleJSP")
	public ModelAndView goGameRuleJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fgamerule gamerule = this.gameRuleService.findById(fid);
			modelAndView.addObject("gamerule", gamerule);
		}
		
		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		modelAndView.addObject("allType", type);
		
		Map<Integer,String> typeMap = new HashMap<Integer,String>();
		typeMap.put(0, GameTypeEnum.getEnumString(0));
		typeMap.put(1, GameTypeEnum.getEnumString(1));
		typeMap.put(2, GameTypeEnum.getEnumString(2));
		typeMap.put(3, GameTypeEnum.getEnumString(3));
		typeMap.put(4, GameTypeEnum.getEnumString(4));
		typeMap.put(5, GameTypeEnum.getEnumString(5));
		typeMap.put(6, GameTypeEnum.getEnumString(6));
		typeMap.put(7, GameTypeEnum.getEnumString(7));
		typeMap.put(8, GameTypeEnum.getEnumString(8));
		modelAndView.addObject("typeMap", typeMap);
		
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateGameRule")
	@RequiresPermissions("ssadmin/updateGameRule.html")
	public ModelAndView updateGameRule(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
        int fid = Integer.parseInt(request.getParameter("fid"));
        Fgamerule gameRule = this.gameRuleService.findById(fid);
    	BigDecimal fcanZdtimes = new BigDecimal(request.getParameter("fcanZdtimes"));//可种豆时长
    	BigDecimal fharvestQty = new BigDecimal(request.getParameter("fharvestQty"));//每小时收获数量
    	BigDecimal fupgradeNeedQty = new BigDecimal(request.getParameter("fupgradeNeedQty"));//升级所需豆数量
    	Integer fupgradeNeedReQty = Integer.parseInt(request.getParameter("fupgradeNeedReQty"));//升级所需推荐注册数
    	BigDecimal fexpendQty = new BigDecimal(request.getParameter("fexpendQty"));
    	int vid1 = Integer.parseInt(request.getParameter("vid1"));
    	Fvirtualcointype virtualcointype =this.virtualCoinService.findById(vid1);
    	int vid3 = Integer.parseInt(request.getParameter("vid3"));
    	Fvirtualcointype virtualcointype3 =this.virtualCoinService.findById(vid3);
    	int vid = Integer.parseInt(request.getParameter("vid"));
    	Fvirtualcointype virtualcointype11 =this.virtualCoinService.findById(vid);

        gameRule.setFcanZdtimes(fcanZdtimes);
        gameRule.setFharvestQty(fharvestQty);
        gameRule.setFlastUpdateTime(Utils.getTimestamp());
        gameRule.setFupgradeNeedQty(fupgradeNeedQty);
        gameRule.setFupgradeNeedReQty(fupgradeNeedReQty);
        gameRule.setFexpendQty(fexpendQty);
        gameRule.setFupgradeCoinType(virtualcointype);
        gameRule.setFvirtualcointype1(virtualcointype3);
        gameRule.setFvirtualcointype(virtualcointype11);
        gameRule.setFbadRate(new BigDecimal(request.getParameter("fbadRate")));
        gameRule.setFgoodRate(new BigDecimal(request.getParameter("fgoodRate")));
        gameRule.setFsendqty(Integer.parseInt(request.getParameter("fsendqty")));
        gameRule.setFtotalqty(Integer.parseInt(request.getParameter("ftotalqty")));
        gameRule.setFtype(Integer.parseInt(request.getParameter("ftype")));
        gameRule.setFbuyQty(new BigDecimal(request.getParameter("fbuyQty")));
        
        this.gameRuleService.updateObj(gameRule);
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
