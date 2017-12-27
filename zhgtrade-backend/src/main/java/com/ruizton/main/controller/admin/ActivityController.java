package com.ruizton.main.controller.admin;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.Enum.ActivityStatusEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Factivity;
import com.ruizton.main.model.Factivityreward;
import com.ruizton.main.model.Factivitytype;
import com.ruizton.main.model.Fpromotionactivityreward;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.ActivityRewardService;
import com.ruizton.main.service.admin.ActivityService;
import com.ruizton.main.service.admin.ActivityTypeService;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.PromotionActivityRewardService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.util.Utils;

@Controller
public class ActivityController extends BaseController {
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActivityTypeService activityTypeService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private ActivityRewardService activityRewardService ;
	@Autowired
	private PromotionActivityRewardService promotionActivityRewardService ;
	@Autowired
	private VirtualCoinService virtualCoinService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/activityList")
	@RequiresPermissions("ssadmin/activityList.html")
	public ModelAndView activityList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/activityList") ;
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
			filter.append("and (ftitle like '%"+keyWord+"%' or \n");
			filter.append("fcontent like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		if (request.getParameter("ftype") != null) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filter.append("and factivitytype.fvirtualCoinType.fid=" + type + "\n");
			}
			modelAndView.addObject("ftype", type);
		} else {
			modelAndView.addObject("ftype", 0);
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
		List<Factivity> list = this.activityService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("activityList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "activityList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Factivity", filter+""));
		
		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);
		
		return modelAndView ;
	}
	
	
	@RequestMapping("/ssadmin/activityLookup")
	@RequiresPermissions("ssadmin/activityLookup.html")
	public ModelAndView activityLookup(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/activityLookup1") ;
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
			filter.append("and (ftitle like '%"+keyWord+"%' or \n");
			filter.append("fcontent like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		if (request.getParameter("ftype") != null) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filter.append("and factivitytype.fvirtualCoinType.fid=" + type + "\n");
			}
			modelAndView.addObject("ftype", type);
		} else {
			modelAndView.addObject("ftype", 0);
		}
		
		List<Factivity> list = this.activityService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("activityList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "activityLookup1");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Factivity", filter+""));
		
		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);
		
		return modelAndView ;
	}
	
	
	@RequestMapping("ssadmin/goActivityJSP")
	public ModelAndView goActivityJSP(HttpServletRequest request) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Factivity activity = this.activityService.findById(fid);
			if(activity.getfEndTime() != null){
				String e = sdf.format(activity.getfEndTime());
				request.setAttribute("e", e);
			}
			if(activity.getfBeginTime() != null){
				String s = sdf.format(activity.getfBeginTime());
				request.setAttribute("s", s);
			}
			modelAndView.addObject("activity", activity);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/saveActivity")
	@RequiresPermissions("ssadmin/saveActivity.html")
	public ModelAndView saveActivity(HttpServletRequest request) throws Exception{
		Factivity activity = new Factivity();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ModelAndView modelAndView = new ModelAndView() ;
		String ftitle = request.getParameter("ftitle");
		String fcontent = request.getParameter("ftitle");
		StringBuffer sf = new StringBuffer();
		String  ftimeInterval= request.getParameter("ftimeInterval");
		if(request.getParameter("fisMultiple") != null){
			activity.setFisMultiple(true);
			if(ftimeInterval == null || ftimeInterval.trim().length() ==0 ){
				sf.append("可以多次参与，两次间隔不能为空!");
			}else{
				activity.setFtimeInterval(Long.valueOf(request.getParameter("ftimeInterval")));
			}
		}else{
			if(ftimeInterval != null && ftimeInterval.trim().length() >0 ){
				sf.append("不可以多次参与，两次间隔必须为空!");
			}
		}
		// 完成多少次可以获得奖励，一般是一次
		activity.setFrewardPerCount(Integer.parseInt(request.getParameter("frewardPerCount")));
		// 完成多少次可以获得推广奖励
		activity.setFrewardPromotionPerCount(Integer.parseInt(request.getParameter("frewardPromotionPerCount")));
		// 是否永久有限
		String fisActiveForever= request.getParameter("fisActiveForever");
        if(fisActiveForever != null && fisActiveForever.trim().length() >0){
        	activity.setFisActiveForever(true);
        	String s = request.getParameter("fBeginTime");
        	String e = request.getParameter("fEndTime");
        	if((s != null &&  s.trim().length() > 0) || (e != null && e.trim().length() > 0)){
        		sf.append("永久有效,开始时间与结束时间必须为空!");
        	}
        }else{
        	String s = request.getParameter("fBeginTime");
        	String e = request.getParameter("fEndTime");
        	if(s == null || e == null || s.trim().length() == 0 || e.trim().length() == 0){
        		sf.append("非永久有效,开始时间与结束时间不能空!");
        	}else{
        		String beginTime = sdf.format(sdf.parse(request.getParameter("fBeginTime")));
        		String endTime = sdf.format(sdf.parse(request.getParameter("fEndTime")));
        		if(Timestamp.valueOf(endTime).compareTo(Timestamp.valueOf(beginTime)) <0){
        			sf.append("结束时间小于开始时间!");
        		}
        		activity.setfBeginTime(Timestamp.valueOf(beginTime));
        		activity.setfEndTime(Timestamp.valueOf(endTime));
        	}
        }
        
        int activityTypeId = Integer.parseInt(request.getParameter("activityTypeLookup.id"));
        List<Factivity> all = this.activityService.findByProperty("factivitytype.fid", activityTypeId);
		if(all != null && all.size() >0){
			sf.append("已存在此活动类型的活动，不允许重复增加");
		}
        
		if(sf.toString().trim().length() >0){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message",sf.toString());
			return modelAndView;
		}
        
		Factivitytype activitytype = activityTypeService.findById(activityTypeId);
		activity.setFactivitytype(activitytype);
		activity.setFstatus(ActivityStatusEnum.NOT_ACTIVE);
		activity.setFcreateTime(Utils.getTimestamp());
		activity.setFlastUpdateTime(Utils.getTimestamp());
		activity.setFtitle(ftitle);
		activity.setFcontent(fcontent);
		this.activityService.saveObj(activity);

		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteActivity")
	@RequiresPermissions("ssadmin/deleteActivity.html")
	public ModelAndView deleteActivity(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int fid = Integer.parseInt(request.getParameter("uid"));
		
		List<Factivityreward> all1 = this.activityRewardService.findByProperty("factivity.fid", fid);
		if(all1 != null && all1.size() >0){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","存在本人奖励方案，不允许删除");
			return modelAndView;
		}
		
		List<Fpromotionactivityreward> all2 = this.promotionActivityRewardService.findByProperty("factivity.fid", fid);
		if(all2 != null && all2.size() >0){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","存在推荐人奖励方案，不允许删除");
			return modelAndView;
		}
		
		Factivity activity = this.activityService.findById(fid);
		if(activity.getFstatus() == ActivityStatusEnum.ACTIVE){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","活动已启用，不允许删除");
			return modelAndView;
		}
		this.activityService.deleteObj(fid);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateATStatus")
	@RequiresPermissions("ssadmin/updateATStatus.html")
	public ModelAndView updateATStatus(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int fid = Integer.parseInt(request.getParameter("uid"));
		int type = Integer.parseInt(request.getParameter("type"));
		Factivity activity = this.activityService.findById(fid);
		String msg = "";
		if(type == 1){
			msg = "启用成功";
			if(activity.getFstatus() == ActivityStatusEnum.ACTIVE){
				modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
				modelAndView.addObject("statusCode",300);
				modelAndView.addObject("message","已启用,无需作此操作");
				return modelAndView;
			}
			activity.setFstatus(ActivityStatusEnum.ACTIVE);
		}else{
			msg = "禁用成功";
			if(activity.getFstatus() == ActivityStatusEnum.NOT_ACTIVE){
				modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
				modelAndView.addObject("statusCode",300);
				modelAndView.addObject("message","已禁用,无需作此操作");
				return modelAndView;
			}
			activity.setFstatus(ActivityStatusEnum.NOT_ACTIVE);
		}
		this.activityService.updateObj(activity);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message",msg);
		return modelAndView;
	}

	@RequestMapping("ssadmin/updateActivity")
	@RequiresPermissions("ssadmin/updateActivity.html")
	public ModelAndView updateActivity(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("uid"));
		Factivity activity = this.activityService.findById(fid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		ModelAndView modelAndView = new ModelAndView() ;
		String ftitle = request.getParameter("ftitle");
		String fcontent = request.getParameter("ftitle");
		StringBuffer sf = new StringBuffer();
		String  ftimeInterval= request.getParameter("ftimeInterval");
		if(request.getParameter("fisMultiple") != null){
			activity.setFisMultiple(true);
			if(ftimeInterval == null || ftimeInterval.trim().length() ==0 ){
				sf.append("可以多次参与，两次间隔不能为空!");
			}else{
				activity.setFtimeInterval(Long.valueOf(request.getParameter("ftimeInterval")));
			}
		}else{
			activity.setFisMultiple(false);
			activity.setFtimeInterval(null);
			if(ftimeInterval != null && ftimeInterval.trim().length() >0 ){
				sf.append("不可以多次参与，两次间隔必须为空!");
			}
		}
		// 完成多少次可以获得奖励，一般是一次
		activity.setFrewardPerCount(Integer.parseInt(request.getParameter("frewardPerCount")));
		// 完成多少次可以获得推广奖励
		activity.setFrewardPromotionPerCount(Integer.parseInt(request.getParameter("frewardPromotionPerCount")));
		// 是否永久有限
		String fisActiveForever= request.getParameter("fisActiveForever");
        if(fisActiveForever != null && fisActiveForever.trim().length() >0){
        	activity.setFisActiveForever(true);
        	activity.setfBeginTime(null);
        	activity.setfEndTime(null);
        	String s = request.getParameter("fBeginTime");
        	String e = request.getParameter("fEndTime");
        	if((s != null &&  s.trim().length() > 0) || (e != null && e.trim().length() > 0)){
        		sf.append("永久有效,开始时间与结束时间必须为空!");
        	}
        }else{
        	activity.setFisActiveForever(false);
        	String s = request.getParameter("fBeginTime");
        	String e = request.getParameter("fEndTime");
        	if(s == null || e == null || s.trim().length() == 0 || e.trim().length() == 0){
        		sf.append("非永久有效,开始时间与结束时间不能空!");
        	}else{
        		String beginTime = sdf.format(sdf.parse(request.getParameter("fBeginTime")));
        		String endTime = sdf.format(sdf.parse(request.getParameter("fEndTime")));
        		if(Timestamp.valueOf(endTime).compareTo(Timestamp.valueOf(beginTime)) <0){
        			sf.append("结束时间小于开始时间!");
        		}
        		activity.setfBeginTime(Timestamp.valueOf(beginTime));
        		activity.setfEndTime(Timestamp.valueOf(endTime));
        	}
        }
        
		if(sf.toString().trim().length() >0){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message",sf.toString());
			return modelAndView;
		}
        
		int activityTypeId = Integer.parseInt(request.getParameter("activityTypeLookup.id"));
		Factivitytype activitytype = activityTypeService.findById(activityTypeId);
		activity.setFactivitytype(activitytype);
		activity.setFcreateTime(Utils.getTimestamp());
		activity.setFlastUpdateTime(Utils.getTimestamp());
		activity.setFtitle(ftitle);
		activity.setFcontent(fcontent);
		this.activityService.updateObj(activity);

		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
