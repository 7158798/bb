package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fgoodtype;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.GoodtypeService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class GoodtypeController extends BaseController {
	@Autowired
	private GoodtypeService goodtypeService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/goodtypeList")
	@RequiresPermissions("ssadmin/goodtypeList.html")
	public ModelAndView goodtypeList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/goodtypeList") ;
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
			filter.append("and fname like '%"+keyWord+"%' \n");
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
		List<Fgoodtype> list = this.goodtypeService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("goodtypeList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "goodtypeList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fgoodtype", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goGoodtypeJSP")
	public ModelAndView goGoodtypeJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fgoodtype fgoodtype = this.goodtypeService.findById(fid);
			modelAndView.addObject("fgoodtype", fgoodtype);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateGoodtype")
	@RequiresPermissions("ssadmin/updateGoodtype.html")
	public ModelAndView updateGoodtype(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        int fid = Integer.parseInt(request.getParameter("fid"));
        Fgoodtype fgoodtype = this.goodtypeService.findById(fid);
        fgoodtype.setFname(request.getParameter("fname"));
        this.goodtypeService.updateObj(fgoodtype);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/addGoodtype")
	@RequiresPermissions("ssadmin/addGoodtype.html")
	public ModelAndView addGoodtype(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        Fgoodtype fgoodtype = new Fgoodtype();
        fgoodtype.setFname(request.getParameter("fname"));
        this.goodtypeService.saveObj(fgoodtype);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
