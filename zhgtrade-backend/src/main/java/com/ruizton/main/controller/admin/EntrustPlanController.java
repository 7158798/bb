package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.EntrustStatusEnum;
import com.ruizton.main.Enum.EntrustTypeEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fentrustplan;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.EntrustPlanService;
import com.ruizton.main.service.admin.VirtualCoinService;
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
public class EntrustPlanController extends BaseController {
	@Autowired
	private EntrustPlanService entrustPlanService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private VirtualCoinService virtualCoinService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/entrustPlanList")
	@RequiresPermissions("ssadmin/entrustPlanList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/entrustPlanList") ;
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
			filter.append("and (fuser.floginName like '%"+keyWord+"%' OR \n");
			filter.append("fuser.frealName like '%"+keyWord+"%' OR \n");
			filter.append("fuser.fnickName like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		if(request.getParameter("ftype") != null){
			int type = Integer.parseInt(request.getParameter("ftype"));
			if(type != 0){
				filter.append("and fvirtualcointype.fid="+type+"\n");
			}
			modelAndView.addObject("ftype", type);
		}else{
			modelAndView.addObject("ftype", 0);
		}
		
		String status = request.getParameter("status");
		if(status != null && status.trim().length() >0){
			if(!status.equals("0")){
				filter.append("and fstatus="+status+" \n");
			}
			modelAndView.addObject("status", status);
		}else{
			modelAndView.addObject("status", 0);
		}
		
		String entype = request.getParameter("entype");
		if(entype != null && entype.trim().length() >0){
			if(!entype.equals("-1")){
				filter.append("and ftype="+entype+" \n");
			}
			modelAndView.addObject("entype", entype);
		}else{
			modelAndView.addObject("entype", -1);
		}
		
		try {
			String price = request.getParameter("price");
			if(price != null && price.trim().length() >0){
				double p = Double.valueOf(price);
				filter.append("and fprize="+p+" \n");
			}
			modelAndView.addObject("price", price);
		} catch (Exception e) {
		}
		
		String logDate = request.getParameter("logDate");
		if(logDate != null && logDate.trim().length() >0){
			filter.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') = '"+logDate+"' \n");
			modelAndView.addObject("logDate", logDate);
		}
		
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by fcreateTime \n");
		}
		
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		
		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);
		
		Map statusMap = new HashMap();
		statusMap.put(EntrustStatusEnum.AllDeal, EntrustStatusEnum.getEnumString(EntrustStatusEnum.AllDeal));
		statusMap.put(EntrustStatusEnum.Cancel, EntrustStatusEnum.getEnumString(EntrustStatusEnum.Cancel));
		statusMap.put(EntrustStatusEnum.Going, EntrustStatusEnum.getEnumString(EntrustStatusEnum.Going));
		statusMap.put(EntrustStatusEnum.PartDeal, EntrustStatusEnum.getEnumString(EntrustStatusEnum.PartDeal));
		statusMap.put(0,"全部");
		modelAndView.addObject("statusMap", statusMap);
		
		Map entypeMap = new HashMap();
		entypeMap.put(EntrustTypeEnum.BUY, EntrustTypeEnum.getEnumString(EntrustTypeEnum.BUY));
		entypeMap.put(EntrustTypeEnum.SELL, EntrustTypeEnum.getEnumString(EntrustTypeEnum.SELL));
		entypeMap.put(-1,"全部");
		modelAndView.addObject("entypeMap", entypeMap);
		
		List<Fentrustplan> list = this.entrustPlanService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("entrustplanList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "entrustPlanList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fentrustplan", filter+""));
		return modelAndView ;
	}
	
}
