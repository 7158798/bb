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

import com.ruizton.main.Enum.LendEntrustStatus1Enum;
import com.ruizton.main.Enum.LendEntrustStatus2Enum;
import com.ruizton.main.Enum.LendEntrustStatus3Enum;
import com.ruizton.main.Enum.LendEntrustTypeEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Flendentrust;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.LendEntrustService;
import com.ruizton.util.Utils;

@Controller
public class LendEntrustController extends BaseController {
	@Autowired
	private LendEntrustService lendEntrustService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/lendentrustList")
	@RequiresPermissions("ssadmin/lendentrustList.html")
	public ModelAndView lendentrustList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
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
			filter.append("and (fuser.floginName like '%"+keyWord+"%' or \n");
			filter.append("fuser.fnickName like '%"+keyWord+"%' or \n");
			filter.append("fuser.frealName like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		String url = "";
		String type = request.getParameter("type");
		if(type != null && type.trim().length() >0){
			int type1 = Integer.parseInt(type);
			if(type1 != 0){
				filter.append("and ftype="+type1+" \n");
				url="lendEntrust"+type+"List";
			}
			modelAndView.addObject("type", type1);
		}
		String status1 = request.getParameter("status1");
		if(status1 != null && status1.trim().length() >0){
			int ss = Integer.parseInt(status1);
			if(ss != 0){
				filter.append("and fstatus1="+ss+" \n");
			}
			modelAndView.addObject("status1", status1);
		}else{
			modelAndView.addObject("status1", 0);
		}
		
		String status2 = request.getParameter("status2");
		if(status2 != null && status2.trim().length() >0){
			int ss = Integer.parseInt(status2);
			if(ss != 0){
				filter.append("and fstatus2="+ss+" \n");
			}
			modelAndView.addObject("status2", status2);
		}else{
			modelAndView.addObject("status2", 0);
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
		
		Map status1Map = new HashMap();
		status1Map.put("0", "全部");
		status1Map.put(1, LendEntrustStatus1Enum.getEnumString(1));
		status1Map.put(2, LendEntrustStatus1Enum.getEnumString(2));
		status1Map.put(3, LendEntrustStatus1Enum.getEnumString(3));
		status1Map.put(4, LendEntrustStatus1Enum.getEnumString(4));
		modelAndView.addObject("status1Map", status1Map);
		
		if(Integer.parseInt(type) == LendEntrustTypeEnum.BORROW){
			Map status2Map = new HashMap();
			status2Map.put("0", "全部");
			status2Map.put(1, LendEntrustStatus2Enum.getEnumString(1));
			status2Map.put(2, LendEntrustStatus2Enum.getEnumString(2));
			status2Map.put(3, LendEntrustStatus2Enum.getEnumString(3));
			status2Map.put(4, LendEntrustStatus2Enum.getEnumString(4));
			modelAndView.addObject("status2Map", status2Map);
		}else{
			Map status2Map = new HashMap();
			status2Map.put("0", "全部");
			status2Map.put(1, LendEntrustStatus3Enum.getEnumString(1));
			status2Map.put(2, LendEntrustStatus3Enum.getEnumString(2));
			status2Map.put(3, LendEntrustStatus3Enum.getEnumString(3));
			status2Map.put(4, LendEntrustStatus3Enum.getEnumString(4));
			modelAndView.addObject("status2Map", status2Map);
		}
		
		List<Flendentrust> list = this.lendEntrustService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.setViewName("ssadmin/"+url) ;
		modelAndView.addObject("lendentrustList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", url);
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Flendentrust", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("/ssadmin/userBorrowsList")
	@RequiresPermissions("ssadmin/userBorrowsList.html")
	public ModelAndView userBorrowsList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (c.floginName like '%"+keyWord+"%' or \n");
			filter.append("c.frealName like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		List<Map> list = this.lendEntrustService.getUserBorrows((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.setViewName("ssadmin/userBorrowsList") ;
		modelAndView.addObject("userBorrowsList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "userBorrowsList");
		//总数量
		List<Map> list1 = this.lendEntrustService.getUserBorrows(0, 0, filter+"",false);
		modelAndView.addObject("totalCount",list1.size());
		return modelAndView ;
	}
	
	@RequestMapping("/ssadmin/userLendsList")
	@RequiresPermissions("ssadmin/userLendsList.html")
	public ModelAndView userLendsList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (c.floginName like '%"+keyWord+"%' or \n");
			filter.append("c.frealName like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		List<Map> list = this.lendEntrustService.getUserLends((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.setViewName("ssadmin/userLendsList") ;
		modelAndView.addObject("userLendsList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "userLendsList");
		//总数量
		List<Map> list1 = this.lendEntrustService.getUserLends(0, 0, filter+"",false);
		modelAndView.addObject("totalCount",list1.size());
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goLendEntrustJSP")
	public ModelAndView goLendEntrustJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Flendentrust lendentrust = this.lendEntrustService.findById(fid);
			modelAndView.addObject("lendentrust", lendentrust);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateLendEntrust")
	@RequiresPermissions("ssadmin/updateLendEntrust.html")
	public ModelAndView updateLendEntrust(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        int fid = Integer.parseInt(request.getParameter("fid"));
       
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
