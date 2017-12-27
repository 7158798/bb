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

import com.ruizton.main.Enum.GameOperateLogTypeEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fgameoperatelog;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.GameOperateLogService;
import com.ruizton.util.Utils;

@Controller
public class GameOperateLogController extends BaseController {
	@Autowired
	private GameOperateLogService gameOperateLogService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/gameoperatelogList")
	@RequiresPermissions("ssadmin/gameoperatelogList.html")
	public ModelAndView gameoperatelogList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/gameoperatelogList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String user = request.getParameter("user");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			if(!keyWord.equals("0")){
				filter.append("and ftype="+keyWord+"\n");
			}
			modelAndView.addObject("keywords", keyWord);
		}else{
			modelAndView.addObject("keywords", 0);
		}
		if(user != null && user.trim().length() >0){
			filter.append("and (fuser.floginName like '%" + user + "%' OR \n");
			filter.append("fuser.fnickName like '%" + user + "%' OR \n");
			filter.append("fuser.frealName like '%" + user + "%' ) \n");
			modelAndView.addObject("user", user);
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
		
		Map<Integer,String> typeMap = new HashMap<Integer,String>();
		typeMap.put(0, "全部");
		typeMap.put(GameOperateLogTypeEnum.UPGRAD, GameOperateLogTypeEnum.getEnumString(GameOperateLogTypeEnum.UPGRAD));
		typeMap.put(GameOperateLogTypeEnum.ZHONG, GameOperateLogTypeEnum.getEnumString(GameOperateLogTypeEnum.ZHONG));
		typeMap.put(GameOperateLogTypeEnum.SHOU, GameOperateLogTypeEnum.getEnumString(GameOperateLogTypeEnum.SHOU));
		typeMap.put(GameOperateLogTypeEnum.GRASS, GameOperateLogTypeEnum.getEnumString(GameOperateLogTypeEnum.GRASS));
		typeMap.put(GameOperateLogTypeEnum.BUY, GameOperateLogTypeEnum.getEnumString(GameOperateLogTypeEnum.BUY));
		modelAndView.addObject("typeMap", typeMap);
		
		List<Fgameoperatelog> list = this.gameOperateLogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("gameoperatelogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "gameoperatelogList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fgameoperatelog", filter+""));
		return modelAndView ;
	}
}
