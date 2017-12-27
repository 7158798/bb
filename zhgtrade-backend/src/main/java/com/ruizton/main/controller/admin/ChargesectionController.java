package com.ruizton.main.controller.admin;

import com.ruizton.main.model.Fchargesection;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.ChargesectionService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ChargesectionController {
	@Autowired
	private ChargesectionService chargesectionService;
	@Autowired
	private AdminService adminService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();

	@RequestMapping("/ssadmin/chargesectionList")
	@RequiresPermissions("ssadmin/chargesectionList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/chargesectionList") ;
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
			filter.append("and fstartHour="+keyWord+"\n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}
		List<Fchargesection> list = this.chargesectionService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("chargesectionList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "salespercentList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fchargesection", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("/ssadmin/goChargesectionJSP")
	public ModelAndView goSalespercentJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fchargesection chargesection = this.chargesectionService.findById(fid);
			modelAndView.addObject("fchargesection", chargesection);
		}
		return modelAndView;
	}
	
	@RequestMapping("/ssadmin/saveChargesection")
	@RequiresPermissions("ssadmin/saveChargesection.html")
	public ModelAndView saveChargesection(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		int startHour = Integer.parseInt(request.getParameter("fstartHour"));
		int endHour = Integer.parseInt(request.getParameter("fendHour"));
		String filter = "where fstartHour="+startHour+" and fendHour="+endHour;
		int count = this.adminService.getAllCount("Fchargesection", filter);
		if(startHour > endHour){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","开始结算小时必须小于结束结算小时！");
			return modelAndView;
		}
		if(count >0){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","已存在该开始结算小时和结束结算小时！");
			return modelAndView;
		}
		if(startHour > 24 || endHour > 24){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","开始或结束小时不能大于24!");
			return modelAndView;
		}
		Fchargesection chargesection = new Fchargesection();
		chargesection.setFstartHour(startHour);
		chargesection.setFendHour(endHour);
		chargesection.setFcreatetime(Utils.getTimestamp());

		this.chargesectionService.saveObj(chargesection);
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","添加成功！");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("/ssadmin/updateChargesection")
	@RequiresPermissions("ssadmin/updateChargesection.html")
	public ModelAndView updateChargesection(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		int startHour = Integer.parseInt(request.getParameter("fstartHour"));
		int endHour = Integer.parseInt(request.getParameter("fendHour"));
		int uid = Integer.parseInt(request.getParameter("uid"));
		String filter = "where fstartHour="+startHour+" and fendHour="+endHour+" and fid<>"+uid;
		Fchargesection chargesection = this.chargesectionService.findById(uid);
		int count = this.adminService.getAllCount("Fchargesection", filter);
		if(startHour > endHour){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","开始结算小时必须小于结束结算小时！");
			return modelAndView;
		}
		if(startHour > 24 || endHour > 24){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","开始或结束小时不能大于24!");
			return modelAndView;
		}
		
		if(count >0){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","已存在该开始结算小时和结束结算小时！");
			return modelAndView;
		}
		
		chargesection.setFstartHour(startHour);
		chargesection.setFendHour(endHour);
		chargesection.setFcreatetime(Utils.getTimestamp());

		this.chargesectionService.updateObj(chargesection);
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","更新成功！");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}

}
