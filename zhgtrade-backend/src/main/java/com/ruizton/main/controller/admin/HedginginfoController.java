package com.ruizton.main.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fhedginginfo;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.HedginginfoService;
import com.ruizton.util.Utils;

@Controller
public class HedginginfoController extends BaseController {
	@Autowired
	private HedginginfoService hedginginfoService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/hedginginfoList")
	@RequiresPermissions("ssadmin/hedginginfoList.html")
	public ModelAndView hedginginfoList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/hedginginfoList") ;
		//当前页
		int currentPage = 1;
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		List<Fhedginginfo> list = this.hedginginfoService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("hedginginfoList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "hedginginfoList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fhedginginfo", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goHedginginfoJSP")
	public ModelAndView goHedginginfoJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fhedginginfo fhedginginfo = this.hedginginfoService.findById(fid);
			modelAndView.addObject("fhedginginfo", fhedginginfo);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateHedginginfo")
	@RequiresPermissions("ssadmin/updateHedginginfo.html")
	public ModelAndView updateHedginginfo(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        int fid = Integer.parseInt(request.getParameter("fid"));
        Fhedginginfo hedginginfo = this.hedginginfoService.findById(fid);
        hedginginfo.setFcreatetime(Utils.getTimestamp());
        hedginginfo.setFrate(Double.valueOf(request.getParameter("frate")));
        hedginginfo.setFtitle(request.getParameter("ftitle"));
        this.hedginginfoService.updateObj(hedginginfo);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
