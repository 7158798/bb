package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fgame;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.GameService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class GameController extends BaseController {
	@Autowired
	private GameService gameService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private VirtualCoinService virtualCoinService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/gameList")
	@RequiresPermissions("ssadmin/gameList.html")
	public ModelAndView gameList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/gameList") ;
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
			filter.append("and fname like '%" + keyWord + "%' \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by fid \n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		List<Fgame> list = this.gameService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("gameList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "gameList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fgame", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goGameJSP")
	public ModelAndView goGameJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fgame game = this.gameService.findById(fid);
			modelAndView.addObject("game", game);
		}
		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		modelAndView.addObject("allType", type);
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateGame")
	@RequiresPermissions("ssadmin/updateGame.html")
	public ModelAndView updateGame(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
        int fid = Integer.parseInt(request.getParameter("fid"));
        int vid = Integer.parseInt(request.getParameter("vid1"));
        int vid2 = Integer.parseInt(request.getParameter("vid2"));
        int vid3 = Integer.parseInt(request.getParameter("vid3"));
        Fvirtualcointype virtualcointype = this.virtualCoinService.findById(vid);
        Fvirtualcointype virtualcointype2 = this.virtualCoinService.findById(vid2);
        Fvirtualcointype virtualcointype3 = this.virtualCoinService.findById(vid3);
        Fgame game = this.gameService.findById(fid);
		String fdescription = request.getParameter("fdescription");// 游戏简介
		BigDecimal fminHarvestTime = new BigDecimal(request.getParameter("fminHarvestTime"));// 最小种豆时间
		BigDecimal fgrassTime = new BigDecimal(request.getParameter("fgrassTime"));// 变草时长
		
		
		game.setFvirtualcointype(virtualcointype);
		game.setFqty(Double.valueOf(request.getParameter("fqty")));
		
		game.setFfrozenvirtualcointype(virtualcointype2);
		game.setFfrozenqty(Double.valueOf(request.getParameter("ffrozenqty")));
		
		game.setFgrassqty(Double.valueOf(request.getParameter("fgrassqty")));
		game.setFgrassvirtualcointype(virtualcointype3);
		
		game.setFdescription(fdescription);
		game.setFminHarvestTime(fminHarvestTime);
		game.setFgrassTime(fgrassTime);
		game.setFlastUpdateTime(Utils.getTimestamp());
		game.setFdays(Integer.parseInt(request.getParameter("fdays")));
		this.gameService.updateObj(game);

		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
