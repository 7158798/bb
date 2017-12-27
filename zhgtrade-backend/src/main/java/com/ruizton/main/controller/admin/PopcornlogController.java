package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.PopcornStatusEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fpopcornlog;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.PopcornlogService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PopcornlogController extends BaseController {
	@Autowired
	private PopcornlogService popcornlogService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/popcornlogList")
	@RequiresPermissions("ssadmin/popcornlogList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/popcornlogList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		String fstatus = request.getParameter("fstatus");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and fstatus like '%"+keyWord+"%' \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(fstatus != null && fstatus.trim().length() >0){
			if(!fstatus.equals("0")){
				filter.append(" and fstatus="+fstatus+" \n");
			}
			modelAndView.addObject("fstatus", fstatus);
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
		Map map = new HashMap();
		map.put(0, "全部");
		map.put(PopcornStatusEnum.END_VALUE,PopcornStatusEnum.getEnumString(PopcornStatusEnum.END_VALUE));
		map.put(PopcornStatusEnum.GOING_VALUE,PopcornStatusEnum.getEnumString(PopcornStatusEnum.GOING_VALUE));
		map.put(PopcornStatusEnum.OPEN_VALUE,PopcornStatusEnum.getEnumString(PopcornStatusEnum.OPEN_VALUE));
		modelAndView.addObject("statusMap",map);
		List<Fpopcornlog> list = this.popcornlogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("popcornlogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "popcornlogList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fpopcornlog", filter+""));
		return modelAndView ;
	}
}
