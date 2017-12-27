package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Flendentrustfinishlog;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.LendEntrustFinishLogService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LendEntrustFinishLogController extends BaseController {
	@Autowired
	private LendEntrustFinishLogService lendEntrustFinishLogService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/lendEntrustFinishLogList")
	@RequiresPermissions("ssadmin/lendEntrustFinishLogList.html")
	public ModelAndView lendEntrustFinishLogList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		//当前页
		int currentPage = 1;
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		String uid = request.getParameter("uid");
		filter.append("and flendentrustlog.fid="+uid+" \n");
		modelAndView.addObject("uid", uid);
		
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
		List<Flendentrustfinishlog> list = this.lendEntrustFinishLogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.setViewName("ssadmin/lendentrustfinishlogList") ;
		modelAndView.addObject("lendEntrustFinishLogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "lendEntrustFinishLogList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Flendentrustfinishlog", filter+""));
		return modelAndView ;
	}
}
