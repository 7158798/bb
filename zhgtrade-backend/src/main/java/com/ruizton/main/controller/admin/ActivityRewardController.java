package com.ruizton.main.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Factivity;
import com.ruizton.main.model.Factivityreward;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.ActivityRewardService;
import com.ruizton.main.service.admin.ActivityService;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.util.Utils;

@Controller
public class ActivityRewardController extends BaseController {
	@Autowired
	private ActivityRewardService activityRewardService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private VirtualCoinService virtualCoinService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/activityrewardList")
	@RequiresPermissions("ssadmin/activityrewardList.html")
	public ModelAndView activityrewardList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/activityrewardList") ;
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
			filter.append("and factivity.ftitle like '%"+keyWord+"%' \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		if (request.getParameter("ftype") != null) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filter.append("and fvirtualcointype.fid=" + type + "\n");
			}
			modelAndView.addObject("ftype", type);
		} else {
			modelAndView.addObject("ftype", 0);
		}
		
		if(request.getParameter("aid") != null){
			filter.append("and factivity.fid="+request.getParameter("aid"));
			modelAndView.addObject("aid", request.getParameter("aid"));
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
		List<Factivityreward> list = this.activityRewardService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
	    modelAndView.addObject("activityrewardList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "activityrewardList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Factivityreward", filter+""));
		
		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);
		
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goActivityRewardJSP")
	public ModelAndView goActivityRewardJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Factivityreward activityreward = this.activityRewardService.findById(fid);
			modelAndView.addObject("activityreward", activityreward);
		}
		if(request.getParameter("aid") != null){
			int fid = Integer.parseInt(request.getParameter("aid"));
			Factivity activity = this.activityService.findById(fid);
			modelAndView.addObject("activity", activity);
		}
		List all = new ArrayList();
		Fvirtualcointype type = new Fvirtualcointype();
		type.setFid(0);
		type.setFname("人民币");
		all.add(type);
		List<Fvirtualcointype> allType = this.virtualCoinService.findAll();
		for (Fvirtualcointype fvirtualcointype : allType) {
			all.add(fvirtualcointype);
		}
		modelAndView.addObject("allType", all);
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/saveActivityReward")
	@RequiresPermissions("ssadmin/saveActivityReward.html")
	public ModelAndView saveActivityReward(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int vid = Integer.parseInt(request.getParameter("vid"));
		int acID = Integer.parseInt(request.getParameter("activityLookup.id"));
		String sql = "where 1=1 and factivity.fid="+acID;
		Factivity activity = this.activityService.findById(acID);
		Factivityreward activityreward = new Factivityreward();
		activityreward.setFactivity(activity);
		activityreward.setFcreateTime(Utils.getTimestamp());
		if(vid == 0){
			activityreward.setFvirtualCoinOrCny(false);
			sql = sql+" and fvirtualCoinOrCny=0";
		}else{
			Fvirtualcointype virtualcointype = this.virtualCoinService.findById(vid);
			activityreward.setFvirtualcointype(virtualcointype);
			activityreward.setFvirtualCoinOrCny(true);
			sql = sql+" and fvirtualCoinOrCny=1 and fvirtualcointype.fid="+vid;
		}
        List<Factivityreward> all = this.activityRewardService.list(0, 0, sql, false);
        if(all != null && all.size() >0){
        	modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","已存在此奖励规则，保存失败");
			return modelAndView;
        }
		double amount = Double.valueOf(request.getParameter("famount"));
		if(request.getParameter("frateOrReal") != null){
			activityreward.setFrateOrReal(true);
			if(amount >1){
				modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
				modelAndView.addObject("statusCode",300);
				modelAndView.addObject("message","按比率，比率不能大于1");
				return modelAndView;
			}
		}else{
			activityreward.setFrateOrReal(false);
		}
        
        activityreward.setFamount(amount);
        this.activityRewardService.saveObj(activityreward);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteActivityReward")
	@RequiresPermissions("ssadmin/deleteActivityReward.html")
	public ModelAndView deleteActivityReward(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("uid"));
		this.activityRewardService.deleteObj(fid);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");
		return modelAndView;
	}

	@RequestMapping("ssadmin/updateActivityReward")
	@RequiresPermissions("ssadmin/updateActivityReward.html")
	public ModelAndView updateActivityReward(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int uid = Integer.parseInt(request.getParameter("uid"));
		int vid = Integer.parseInt(request.getParameter("vid"));
		int acID = Integer.parseInt(request.getParameter("activityLookup.id"));
		String sql = "where 1=1 and factivity.fid="+acID +" and fid <> "+uid;
		Factivity activity = this.activityService.findById(acID);
		Factivityreward activityreward = this.activityRewardService.findById(uid);
		activityreward.setFactivity(activity);
		activityreward.setFcreateTime(Utils.getTimestamp());
		if(vid == 0){
			activityreward.setFvirtualCoinOrCny(false);
			sql = sql+" and fvirtualCoinOrCny=0";
		}else{
			Fvirtualcointype virtualcointype = this.virtualCoinService.findById(vid);
			activityreward.setFvirtualcointype(virtualcointype);
			activityreward.setFvirtualCoinOrCny(true);
			sql = sql+" and fvirtualCoinOrCny=1 and fvirtualcointype.fid="+vid;
		}
        List<Factivityreward> all = this.activityRewardService.list(0, 0, sql, false);
        if(all != null && all.size() >0){
        	modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","已存在此奖励规则，更新失败");
			return modelAndView;
        }
		double amount = Double.valueOf(request.getParameter("famount"));
		if(request.getParameter("frateOrReal") != null){
			activityreward.setFrateOrReal(true);
			if(amount >1){
				modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
				modelAndView.addObject("statusCode",300);
				modelAndView.addObject("message","按比率，比率不能大于1");
				return modelAndView;
			}
		}else{
			activityreward.setFrateOrReal(false);
		}
        
        activityreward.setFamount(amount);
        this.activityRewardService.updateObj(activityreward);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","更新成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
