package com.ruizton.main.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.Enum.LendEntrustLogStatusEnum;
import com.ruizton.main.Enum.LendEntrustTypeEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Flendentrustlog;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.LendEntrustLogService;
import com.ruizton.main.service.admin.LendEntrustService;
import com.ruizton.main.service.admin.UserService;
import com.ruizton.util.Utils;

@Controller
public class LendEntrustLogController extends BaseController {
	@Autowired
	private LendEntrustLogService lendEntrustLogService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private LendEntrustService lendEntrustService;
	@Autowired
	private UserService userService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/lendentrustlogList")
	@RequiresPermissions("ssadmin/lendentrustlogList.html")
	public ModelAndView lendentrustlogList(HttpServletRequest request) throws Exception{
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
		int type = Integer.parseInt(request.getParameter("type"));
		String uid = request.getParameter("uid");
		if(type == LendEntrustTypeEnum.LEND){
			filter.append("and fLendEntrustLendId.fid="+uid+" \n");
		}else{
			filter.append("and fLendEntrustBorrowId.fid="+uid+" \n");
		}
		
		String status1 = request.getParameter("status1");
		if(status1 != null && status1.trim().length() >0){
			int ss = Integer.parseInt(status1);
			if(ss != 0){
				filter.append("and fstatus="+ss+" \n");
			}
			modelAndView.addObject("status1", status1);
		}else{
			modelAndView.addObject("status1", 0);
		}
		
		modelAndView.addObject("type", type);
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
		
		Map status1Map = new HashMap();
		status1Map.put("0", "全部");
		status1Map.put(1, LendEntrustLogStatusEnum.getEnumString(1));
		status1Map.put(2, LendEntrustLogStatusEnum.getEnumString(2));
		status1Map.put(3, LendEntrustLogStatusEnum.getEnumString(3));
		modelAndView.addObject("status1Map", status1Map);
		
		List<Flendentrustlog> list = this.lendEntrustLogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		for (Flendentrustlog flendentrustlog : list) {
			int id = 0;
			if(type == LendEntrustTypeEnum.LEND){
				id = flendentrustlog.getfLendEntrustLendId().getFid();
			}else{
				id = flendentrustlog.getfLendEntrustBorrowId().getFid();
			}
			Fuser fuser = this.userService.findById(this.lendEntrustService.findById(id).getFuser().getFid());
			flendentrustlog.setFuser(fuser);
		}
		modelAndView.setViewName("ssadmin/lendentrustlogList") ;
		modelAndView.addObject("lendentrustlogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "lendentrustlogList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Flendentrustlog", filter+""));
		return modelAndView ;
	}
}
