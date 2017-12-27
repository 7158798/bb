package com.ruizton.main.controller.admin;

import com.ruizton.main.model.Fdeductlog;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.DeductService;
import com.ruizton.main.service.admin.DeductlogService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//import javax.enterprise.inject.Model;

@Controller
public class DeductlogController {
	@Autowired
	private DeductlogService deductlogService;
	@Autowired
	private DeductService deductService;
	@Autowired
	private AdminService adminService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();

	@RequestMapping("/ssadmin/deductlogDetail")
	@RequiresPermissions("ssadmin/deductlogDetail.html")
	public ModelAndView deductlogDetail(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/deductlogDetail") ;
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
			filter.append("and (fsourceUserId.floginName like '%"+keyWord+"%' or \n");
			filter.append("fsourceUserId.fnickName like '%"+keyWord+"%' or \n");
			filter.append("fsourceUserId.frealName like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(request.getParameter("uid") != null){
			int uid =Integer.parseInt(request.getParameter("uid"));
			filter.append("and fdeduct.fid ="+uid+" \n");
			modelAndView.addObject("uid", request.getParameter("uid"));
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
		List<Fdeductlog> list = this.deductlogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("deductlogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "deductlogDetail");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fdeductlog", filter+""));
		return modelAndView ;
	}

}
