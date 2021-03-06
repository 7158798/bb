package com.ruizton.main.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fbalancetype;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.BalancetypeService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.util.Utils;

@Controller
public class BalancetypeController extends BaseController {
	@Autowired
	private BalancetypeService balancetypeService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private VirtualCoinService virtualCoinService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/balancetypeList")
	@RequiresPermissions("ssadmin/balancetypeList.html")
	public ModelAndView balancetypeList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/balancetypeList") ;
		//当前页
		int currentPage = 1;
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		List<Fbalancetype> list = this.balancetypeService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("balancetypeList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "balancetypeList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fbalancetype", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goBalancetypeJSP")
	public ModelAndView goBalancetypeJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fbalancetype fbalancetype = this.balancetypeService.findById(fid);
			modelAndView.addObject("fbalancetype", fbalancetype);
		}
		List<Fvirtualcointype> allType = this.virtualCoinService.findAll();
		modelAndView.addObject("allType", allType);
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateBalancetype")
	@RequiresPermissions("ssadmin/updateBalancetype.html")
	public ModelAndView updateBalancetype(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        int fid = Integer.parseInt(request.getParameter("fid"));
        Fbalancetype balancetype = this.balancetypeService.findById(fid);
        balancetype.setFcreatetime(Utils.getTimestamp());
        balancetype.setFname(request.getParameter("fname"));
        balancetype.setFrate(Double.valueOf(request.getParameter("frate")));
        balancetype.setFday(Integer.parseInt(request.getParameter("fday")));
        int vid = Integer.parseInt(request.getParameter("vid"));
        Fvirtualcointype vt = this.virtualCoinService.findById(vid);
        balancetype.setFvirtualcointype(vt);
        this.balancetypeService.updateObj(balancetype);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteBalancetype")
	@RequiresPermissions("ssadmin/deleteBalancetype.html")
	public ModelAndView deleteBalancetype(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("uid"));
		this.balancetypeService.deleteObj(fid);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/saveBalancetype")
	@RequiresPermissions("ssadmin/saveBalancetype.html")
	public ModelAndView saveBalancetype(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        Fbalancetype balancetype = new Fbalancetype();
        balancetype.setFcreatetime(Utils.getTimestamp());
        balancetype.setFname(request.getParameter("fname"));
        balancetype.setFrate(Double.valueOf(request.getParameter("frate")));
        balancetype.setFday(Integer.parseInt(request.getParameter("fday")));
        int vid = Integer.parseInt(request.getParameter("vid"));
        Fvirtualcointype vt = this.virtualCoinService.findById(vid);
        balancetype.setFvirtualcointype(vt);
        this.balancetypeService.saveObj(balancetype);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","保存成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
