package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fbetlog;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.BetlogService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class BetlogController extends BaseController {
	@Autowired
	private BetlogService betlogService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/betlogList")
	@RequiresPermissions("ssadmin/betlogList.html")
	public ModelAndView betlogList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/betlogList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String term = request.getParameter("term");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		try {
			if(term != null && term.trim().length() >0){
				int termId = Integer.parseInt(term);
				filter.append("and fpopcornlogId = "+termId+" \n");
				modelAndView.addObject("term", termId);
			}
		} catch (Exception e) {
			
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
		List<Fbetlog> list = this.betlogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("betlogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "betlogList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fbetlog", filter+""));
		return modelAndView ;
	}
	
}
