package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.FfreeLottery;
import com.ruizton.main.model.FfreeLotteryRule;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.FreeLotterService;
import com.ruizton.main.service.front.FrontFreeLotteryService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class FreeLotteryController extends BaseController {
	@Autowired
	private FrontFreeLotteryService frontFreeLotteryService ;
	@Autowired
	private FreeLotterService freeLotterService;
	@Autowired
	private AdminService adminService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/freeLotterySetting")
	@RequiresPermissions("ssadmin/freeLotterySetting.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/freeLotterySetting") ;
		
		List<FfreeLotteryRule> list = this.frontFreeLotteryService.listFfreeLotteryRules(0, 0, " order by fid desc ", false) ;
		modelAndView.addObject("list", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "freeLotterySetting");
		return modelAndView ;
	}
	
	
	@RequestMapping("ssadmin/goFreeLotterySettingJSP")
	public ModelAndView goFreeLotterySettingJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			FfreeLotteryRule ffreeLotteryRule = this.frontFreeLotteryService.findFfreeLotteryRule(fid) ;
			modelAndView.addObject("ffreeLotteryRule", ffreeLotteryRule);
		}
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/updateFfreeLotteryRule")
	@RequiresPermissions("ssadmin/updateFfreeLotteryRule.html")
	public ModelAndView updateFfreeLotteryRule(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("fid"));
		ModelAndView modelAndView = new ModelAndView() ;
		FfreeLotteryRule ffreeLotteryRule = this.frontFreeLotteryService.findFfreeLotteryRule(fid) ;
		ffreeLotteryRule.setFreward(Utils.getDouble(Double.parseDouble(request.getParameter("freward").trim()), 6)) ;
		this.frontFreeLotteryService.updateFfreeLotteryRule(ffreeLotteryRule) ;
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","更新成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("/ssadmin/freeLotteryList")
	@RequiresPermissions("ssadmin/freeLotteryList.html")
	public ModelAndView gameoperatelogList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/freeLotteryList") ;
		//当前页
		int currentPage = 1;
		String user = request.getParameter("user");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(user != null && user.trim().length() >0){
			filter.append("and (fuser.floginName like '%" + user + "%' OR \n");
			filter.append("fuser.fnickName like '%" + user + "%' OR \n");
			filter.append("fuser.frealName like '%" + user + "%' ) \n");
			modelAndView.addObject("user", user);
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
		
		List<FfreeLottery> list = this.freeLotterService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("freeLotteryList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "freeLotteryList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("FfreeLottery", filter+""));
		return modelAndView ;
	}
}
