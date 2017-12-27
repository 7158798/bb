package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fgameharvestlog;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.GameharvestlogService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class GameHarvestLogController extends BaseController {
	@Autowired
	private GameharvestlogService gameharvestlogService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/gameharvestlogList")
	@RequiresPermissions("ssadmin/gameharvestlogList.html")
	public ModelAndView gameharvestlogList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/gameharvestlogList") ;
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
			filter.append("and (fuser.floginName like '%" + keyWord + "%' OR \n");
			filter.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
			filter.append("fuser.frealName like '%" + keyWord + "%' ) \n");
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
		List<Fgameharvestlog> list = this.gameharvestlogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("gameharvestlogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "gameharvestlogList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fgameharvestlog", filter+""));
		return modelAndView ;
	}
}
