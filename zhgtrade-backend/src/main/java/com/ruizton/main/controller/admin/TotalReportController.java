package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Ftotalreport;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.TotalreportService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TotalReportController extends BaseController {
	@Autowired
	private TotalreportService totalreportService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/totalreportList")
	@RequiresPermissions("ssadmin/totalreportList.html")
	public ModelAndView totalreportList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/totalreportList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String logDate = request.getParameter("logDate");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(logDate != null && logDate.trim().length() >0){
			filter.append("and DATE_FORMAT(fdate,'%Y-%m-%d') = '"+logDate+"' \n");
			modelAndView.addObject("logDate", logDate);
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
		List<Ftotalreport> list = this.totalreportService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("totalreportList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "totalreportList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Ftotalreport", filter+""));
		return modelAndView ;
	}
	
}
