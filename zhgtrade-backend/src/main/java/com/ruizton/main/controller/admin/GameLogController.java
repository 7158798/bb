package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fgamelog;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.GameLogService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class GameLogController extends BaseController {
	@Autowired
	private GameLogService gameLogService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/gameLogList")
	@RequiresPermissions("ssadmin/gameLogList.html")
	public ModelAndView gameLogList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/gameLogList") ;
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
		List<Fgamelog> list = this.gameLogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("gameLogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "gameList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fgamelog", filter+""));
		return modelAndView ;
	}
}
