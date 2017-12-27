package com.ruizton.main.controller.admin;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.Enum.SubscriptionTypeEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fsubscription;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.SubscriptionService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;

@Controller
public class SubscriptionController extends BaseController {
	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private VirtualCoinService virtualCoinService;
	// 每页显示多少条数据
	private int numPerPage = 40;

	@RequestMapping("/ssadmin/subscriptionList")
	@RequiresPermissions("ssadmin/subscriptionList.html")
	public ModelAndView subscriptionList(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/subscriptionList");
		// 当前页
		int currentPage = 1;
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");

		if (StringUtils.hasText(request.getParameter("ftype"))) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filter.append("and fvirtualcointype.fid=" + type + "\n");
			}
			modelAndView.addObject("ftype", type);
		} else {
			modelAndView.addObject("ftype", 0);
		}
		
		if (StringUtils.hasText(request.getParameter("ftype1"))) {
			int type = Integer.parseInt(request.getParameter("ftype1"));
			if (type != 0) {
				filter.append("and fvirtualcointypeCost.fid=" + type + "\n");
			}
			modelAndView.addObject("ftype1", type);
		} else {
			modelAndView.addObject("ftype1", 0);
		}
		
		filter.append("and ftype=" + SubscriptionTypeEnum.COIN + "\n");

		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		}
		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		}

		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);

		List<Fsubscription> list = this.subscriptionService.list(
				(currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("subscriptionList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "subscriptionList");
		modelAndView.addObject("currentPage", currentPage);
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("Fsubscription", filter + ""));
		return modelAndView;
	}

	@RequestMapping("ssadmin/goSubscriptionJSP")
	public ModelAndView goSubscriptionJSP(HttpServletRequest request) throws Exception {
		String url = request.getParameter("url");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(url);
		if (request.getParameter("uid") != null) {
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fsubscription subscription = this.subscriptionService.findById(fid);
			modelAndView.addObject("subscription", subscription);
			if(subscription.getFendTime() != null){
				String e = sdf.format(subscription.getFendTime());
				request.setAttribute("e", e);
			}
			if(subscription.getFbeginTime() != null){
				String s = sdf.format(subscription.getFbeginTime());
				request.setAttribute("s", s);
			}
		}

		List<Fvirtualcointype> allType = this.virtualCoinService.findAll();
		modelAndView.addObject("allType", allType);

		return modelAndView;
	}

	@RequestMapping("ssadmin/saveSubscription")
	public ModelAndView saveSubscription(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int vid = Integer.parseInt(request.getParameter("vid"));
		int vid1 = Integer.parseInt(request.getParameter("vid1"));
		Fvirtualcointype vt = this.virtualCoinService.findById(vid);
		Fvirtualcointype vt1 = this.virtualCoinService.findById(vid1);
		String fisopen = request.getParameter("fisopen");
		double ftotal = Double.valueOf(request.getParameter("ftotal"));
		double fprice = Double.valueOf(request.getParameter("fprice"));
		int fbuyCount = Integer.parseInt(request.getParameter("fbuyCount"));
		int fbuyTimes = Integer.parseInt(request.getParameter("fbuyTimes"));
		String fbeginTimes = request.getParameter("fbeginTime");
		String fendTimes = request.getParameter("fendTime");
		Date beginDate = sdf.parse(fbeginTimes);
		Date endDate = sdf.parse(fendTimes);
		if (beginDate.compareTo(endDate) > 0) {
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "开始时间不能大于结束时间");
			return modelAndView;
		}
		Fsubscription subscription = new Fsubscription();
		subscription.setFbeginTime(Timestamp.valueOf(fbeginTimes));
		subscription.setFendTime(Timestamp.valueOf(fendTimes));
		if (fisopen != null && fisopen.trim().length() > 0) {
			subscription.setFisopen(true);
		} else {
			subscription.setFisopen(false);
		}
		subscription.setFvirtualcointype(vt);
		subscription.setFvirtualcointypeCost(vt1);
		subscription.setFbuyCount(fbuyCount);
		subscription.setFbuyTimes(fbuyTimes);
		subscription.setFprice(fprice);
		subscription.setFtotal(ftotal);
		subscription.setFcreateTime(Utils.getTimestamp());
		subscription.setFtype(SubscriptionTypeEnum.COIN);
		this.subscriptionService.saveObj(subscription);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "新增成功");
		modelAndView.addObject("callbackType", "closeCurrent");
		return modelAndView;
	}

	@RequestMapping("ssadmin/deleteSubscription")
	@RequiresPermissions("ssadmin/deleteSubscription.html")
	public ModelAndView deleteSubscription(HttpServletRequest request) throws Exception {
		int fid = Integer.parseInt(request.getParameter("uid"));
		this.subscriptionService.deleteObj(fid);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "删除成功");
		return modelAndView;
	}

	@RequestMapping("ssadmin/updateSubscription")
	@RequiresPermissions("ssadmin/updateSubscription.html")
	public ModelAndView updateSubscription(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		int fid = Integer.parseInt(request.getParameter("fid"));
		Fsubscription subscription = this.subscriptionService.findById(fid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int vid = Integer.parseInt(request.getParameter("vid"));
		int vid1 = Integer.parseInt(request.getParameter("vid1"));
		Fvirtualcointype vt = this.virtualCoinService.findById(vid);
		Fvirtualcointype vt1 = this.virtualCoinService.findById(vid1);
		String fisopen = request.getParameter("fisopen");
		double ftotal = Double.valueOf(request.getParameter("ftotal"));
		double fprice = Double.valueOf(request.getParameter("fprice"));
		int fbuyCount = Integer.parseInt(request.getParameter("fbuyCount"));
		int fbuyTimes = Integer.parseInt(request.getParameter("fbuyTimes"));
		String fbeginTimes = request.getParameter("fbeginTime");
		String fendTimes = request.getParameter("fendTime");
		Date beginDate = sdf.parse(fbeginTimes);
		Date endDate = sdf.parse(fendTimes);
		if (beginDate.compareTo(endDate) > 0) {
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "开始时间不能大于结束时间");
			return modelAndView;
		}

		subscription.setFbeginTime(Timestamp.valueOf(fbeginTimes));
		subscription.setFendTime(Timestamp.valueOf(fendTimes));
		if (fisopen != null && fisopen.trim().length() > 0) {
			subscription.setFisopen(true);
		} else {
			subscription.setFisopen(false);
		}
		subscription.setFvirtualcointypeCost(vt1);
		subscription.setFvirtualcointype(vt);
		subscription.setFbuyCount(fbuyCount);
		subscription.setFbuyTimes(fbuyTimes);
		subscription.setFprice(fprice);
		subscription.setFtotal(ftotal);
		subscription.setFtype(SubscriptionTypeEnum.COIN);
		this.subscriptionService.updateObj(subscription);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "修改成功");
		modelAndView.addObject("callbackType", "closeCurrent");
		return modelAndView;
	}
	
	
	
	@RequestMapping("/ssadmin/subscriptionList1")
	@RequiresPermissions("ssadmin/subscriptionList1.html")
	public ModelAndView subscriptionList1(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/subscriptionList1");
		// 当前页
		int currentPage = 1;
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");

		if (StringUtils.hasText(request.getParameter("ftype"))) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filter.append("and fvirtualcointype.fid=" + type + "\n");
			}
			modelAndView.addObject("ftype", type);
		} else {
			modelAndView.addObject("ftype", 0);
		}
		
		filter.append("and ftype=" + SubscriptionTypeEnum.RMB + "\n");

		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		}
		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		}

		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);

		List<Fsubscription> list = this.subscriptionService.list(
				(currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("subscriptionList1", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "subscriptionList1");
		modelAndView.addObject("currentPage", currentPage);
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("Fsubscription", filter + ""));
		return modelAndView;
	}

	@RequestMapping("ssadmin/saveSubscription1")
	@RequiresPermissions("ssadmin/saveSubscription1.html")
	public ModelAndView saveSubscription1(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int vid = Integer.parseInt(request.getParameter("vid"));
		Fvirtualcointype vt = this.virtualCoinService.findById(vid);
		String fisopen = request.getParameter("fisopen");
		double ftotal = Double.valueOf(request.getParameter("ftotal"));
		double fprice = Double.valueOf(request.getParameter("fprice"));
		int fbuyCount = Integer.parseInt(request.getParameter("fbuyCount"));
		int fbuyTimes = Integer.parseInt(request.getParameter("fbuyTimes"));
		String fbeginTimes = request.getParameter("fbeginTime");
		String fendTimes = request.getParameter("fendTime");
		Date beginDate = sdf.parse(fbeginTimes);
		Date endDate = sdf.parse(fendTimes);
		if (beginDate.compareTo(endDate) > 0) {
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "开始时间不能大于结束时间");
			return modelAndView;
		}
		Fsubscription subscription = new Fsubscription();
		subscription.setFbeginTime(Timestamp.valueOf(fbeginTimes));
		subscription.setFendTime(Timestamp.valueOf(fendTimes));
		if (fisopen != null && fisopen.trim().length() > 0) {
			subscription.setFisopen(true);
		} else {
			subscription.setFisopen(false);
		}
		subscription.setFvirtualcointype(vt);
		subscription.setFbuyCount(fbuyCount);
		subscription.setFbuyTimes(fbuyTimes);
		subscription.setFprice(fprice);
		subscription.setFtitle(request.getParameter("ftitle"));
		subscription.setFdays(Integer.parseInt(request.getParameter("fdays")));
		subscription.setFtotal(ftotal);
		subscription.setFcreateTime(Utils.getTimestamp());
		subscription.setFtype(SubscriptionTypeEnum.RMB);
		this.subscriptionService.saveObj(subscription);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "新增成功");
		modelAndView.addObject("callbackType", "closeCurrent");
		return modelAndView;
	}

	@RequestMapping("ssadmin/updateSubscription1")
	@RequiresPermissions("ssadmin/updateSubscription1.html")
	public ModelAndView updateSubscription1(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		int fid = Integer.parseInt(request.getParameter("fid"));
		Fsubscription subscription = this.subscriptionService.findById(fid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int vid = Integer.parseInt(request.getParameter("vid"));
		Fvirtualcointype vt = this.virtualCoinService.findById(vid);
		String fisopen = request.getParameter("fisopen");
		double ftotal = Double.valueOf(request.getParameter("ftotal"));
		double fprice = Double.valueOf(request.getParameter("fprice"));
		int fbuyCount = Integer.parseInt(request.getParameter("fbuyCount"));
		int fbuyTimes = Integer.parseInt(request.getParameter("fbuyTimes"));
		String fbeginTimes = request.getParameter("fbeginTime");
		String fendTimes = request.getParameter("fendTime");
		Date beginDate = sdf.parse(fbeginTimes);
		Date endDate = sdf.parse(fendTimes);
		if (beginDate.compareTo(endDate) > 0) {
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "开始时间不能大于结束时间");
			return modelAndView;
		}

		subscription.setFbeginTime(Timestamp.valueOf(fbeginTimes));
		subscription.setFendTime(Timestamp.valueOf(fendTimes));
		if (fisopen != null && fisopen.trim().length() > 0) {
			subscription.setFisopen(true);
		} else {
			subscription.setFisopen(false);
		}
		subscription.setFvirtualcointype(vt);
		subscription.setFbuyCount(fbuyCount);
		subscription.setFbuyTimes(fbuyTimes);
		subscription.setFprice(fprice);
		subscription.setFtitle(request.getParameter("ftitle"));
		subscription.setFdays(Integer.parseInt(request.getParameter("fdays")));
		subscription.setFtotal(ftotal);
		subscription.setFtype(SubscriptionTypeEnum.RMB);
		this.subscriptionService.updateObj(subscription);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "修改成功");
		modelAndView.addObject("callbackType", "closeCurrent");
		return modelAndView;
	}
	
	
	@RequestMapping("/ssadmin/subscriptionList2")
	@RequiresPermissions("ssadmin/subscriptionList2.html")
	public ModelAndView subscriptionList2(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/subscriptionList2");
		// 当前页
		int currentPage = 1;
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");

		if (request.getParameter("ftype") != null) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filter.append("and fvirtualcointype.fid=" + type + "\n");
			}
			modelAndView.addObject("ftype", type);
		} else {
			modelAndView.addObject("ftype", 0);
		}
		
		filter.append("and ftype=" + SubscriptionTypeEnum.MEADOW + "\n");

		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		}
		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		}

		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);

		List<Fsubscription> list = this.subscriptionService.list(
				(currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("subscriptionList2", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "subscriptionList2");
		modelAndView.addObject("currentPage", currentPage);
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("Fsubscription", filter + ""));
		return modelAndView;
	}

	@RequestMapping("ssadmin/saveSubscription2")
	@RequiresPermissions("ssadmin/saveSubscription2.html")
	public ModelAndView saveSubscription2(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int vid = Integer.parseInt(request.getParameter("vid"));
		Fvirtualcointype vt = this.virtualCoinService.findById(vid);
		String fisopen = request.getParameter("fisopen");
		double ftotal = Double.valueOf(request.getParameter("ftotal"));
		double fprice = Double.valueOf(request.getParameter("fprice"));
		int fbuyCount = Integer.parseInt(request.getParameter("fbuyCount"));
		int fbuyTimes = Integer.parseInt(request.getParameter("fbuyTimes"));
		String fbeginTimes = request.getParameter("fbeginTime");
		String fendTimes = request.getParameter("fendTime");
		Date beginDate = sdf.parse(fbeginTimes);
		Date endDate = sdf.parse(fendTimes);
		if (beginDate.compareTo(endDate) > 0) {
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "开始时间不能大于结束时间");
			return modelAndView;
		}
		if(fbuyCount == 0 || fbuyCount >9){
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "认购数量不能大于9或等于0");
			return modelAndView;
		}
		Fsubscription subscription = new Fsubscription();
		subscription.setFbeginTime(Timestamp.valueOf(fbeginTimes));
		subscription.setFendTime(Timestamp.valueOf(fendTimes));
		if (fisopen != null && fisopen.trim().length() > 0) {
			subscription.setFisopen(true);
		} else {
			subscription.setFisopen(false);
		}
		subscription.setFvirtualcointype(vt);
		subscription.setFbuyCount(fbuyCount);
		subscription.setFbuyTimes(fbuyTimes);
		subscription.setFprice(fprice);
		subscription.setFtotal(ftotal);
		subscription.setFcreateTime(Utils.getTimestamp());
		subscription.setFtype(SubscriptionTypeEnum.MEADOW);
		this.subscriptionService.saveObj(subscription);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "新增成功");
		modelAndView.addObject("callbackType", "closeCurrent");
		return modelAndView;
	}

	@RequestMapping("ssadmin/updateSubscription2")
	@RequiresPermissions("ssadmin/updateSubscription2.html")
	public ModelAndView updateSubscription2(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		int fid = Integer.parseInt(request.getParameter("fid"));
		Fsubscription subscription = this.subscriptionService.findById(fid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int vid = Integer.parseInt(request.getParameter("vid"));
		Fvirtualcointype vt = this.virtualCoinService.findById(vid);
		String fisopen = request.getParameter("fisopen");
		double ftotal = Double.valueOf(request.getParameter("ftotal"));
		double fprice = Double.valueOf(request.getParameter("fprice"));
		int fbuyCount = Integer.parseInt(request.getParameter("fbuyCount"));
		int fbuyTimes = Integer.parseInt(request.getParameter("fbuyTimes"));
		String fbeginTimes = request.getParameter("fbeginTime");
		String fendTimes = request.getParameter("fendTime");
		Date beginDate = sdf.parse(fbeginTimes);
		Date endDate = sdf.parse(fendTimes);
		if (beginDate.compareTo(endDate) > 0) {
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "开始时间不能大于结束时间");
			return modelAndView;
		}
		if(fbuyCount == 0 || fbuyCount >9){
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "认购数量不能大于9或等于0");
			return modelAndView;
		}
		subscription.setFbeginTime(Timestamp.valueOf(fbeginTimes));
		subscription.setFendTime(Timestamp.valueOf(fendTimes));
		if (fisopen != null && fisopen.trim().length() > 0) {
			subscription.setFisopen(true);
		} else {
			subscription.setFisopen(false);
		}
		subscription.setFvirtualcointype(vt);
		subscription.setFbuyCount(fbuyCount);
		subscription.setFbuyTimes(fbuyTimes);
		subscription.setFprice(fprice);
		subscription.setFtotal(ftotal);
		subscription.setFtype(SubscriptionTypeEnum.MEADOW);
		this.subscriptionService.updateObj(subscription);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "修改成功");
		modelAndView.addObject("callbackType", "closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("/ssadmin/subscriptionList3")
	@RequiresPermissions("ssadmin/subscriptionList3.html")
	public ModelAndView subscriptionList3(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/subscriptionList3");
		// 当前页
		int currentPage = 1;
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");

		if (request.getParameter("ftype") != null) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filter.append("and fvirtualcointype.fid=" + type + "\n");
			}
			modelAndView.addObject("ftype", type);
		} else {
			modelAndView.addObject("ftype", 0);
		}
		
		if (request.getParameter("ftype1") != null) {
			int type = Integer.parseInt(request.getParameter("ftype1"));
			if (type != 0) {
				filter.append("and fvirtualcointypeCost.fid=" + type + "\n");
			}
			modelAndView.addObject("ftype1", type);
		} else {
			modelAndView.addObject("ftype1", 0);
		}
		
		filter.append("and ftype=" + SubscriptionTypeEnum.SMELTER + "\n");

		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		}
		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		}

		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);

		List<Fsubscription> list = this.subscriptionService.list(
				(currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("subscriptionList3", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "subscriptionList3");
		modelAndView.addObject("currentPage", currentPage);
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("Fsubscription", filter + ""));
		return modelAndView;
	}


	@RequestMapping("ssadmin/saveSubscription3")
	@RequiresPermissions("ssadmin/saveSubscription3.html")
	public ModelAndView saveSubscription3(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int vid = Integer.parseInt(request.getParameter("vid"));
		int vid1 = Integer.parseInt(request.getParameter("vid1"));
		Fvirtualcointype vt = this.virtualCoinService.findById(vid);
		Fvirtualcointype vt1 = this.virtualCoinService.findById(vid1);
		String fisopen = request.getParameter("fisopen");
		double ftotal = Double.valueOf(request.getParameter("ftotal"));
		double fprice = Double.valueOf(request.getParameter("fprice"));
		int fbuyCount = Integer.parseInt(request.getParameter("fbuyCount"));
		int fbuyTimes = Integer.parseInt(request.getParameter("fbuyTimes"));
		String fbeginTimes = request.getParameter("fbeginTime");
		String fendTimes = request.getParameter("fendTime");
		Date beginDate = sdf.parse(fbeginTimes);
		Date endDate = sdf.parse(fendTimes);
		if (beginDate.compareTo(endDate) > 0) {
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "开始时间不能大于结束时间");
			return modelAndView;
		}
		Fsubscription subscription = new Fsubscription();
		subscription.setFbeginTime(Timestamp.valueOf(fbeginTimes));
		subscription.setFendTime(Timestamp.valueOf(fendTimes));
		if (fisopen != null && fisopen.trim().length() > 0) {
			subscription.setFisopen(true);
		} else {
			subscription.setFisopen(false);
		}
		subscription.setFvirtualcointype(vt);
		subscription.setFvirtualcointypeCost(vt1);
		subscription.setFbuyCount(fbuyCount);
		subscription.setFbuyTimes(fbuyTimes);
		subscription.setFprice(fprice);
		subscription.setFtotal(ftotal);
		subscription.setFcreateTime(Utils.getTimestamp());
		subscription.setFtype(SubscriptionTypeEnum.SMELTER);
		this.subscriptionService.saveObj(subscription);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "新增成功");
		modelAndView.addObject("callbackType", "closeCurrent");
		return modelAndView;
	}

	@RequestMapping("ssadmin/updateSubscription3")
	@RequiresPermissions("ssadmin/updateSubscription3.html")
	public ModelAndView updateSubscription3(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		int fid = Integer.parseInt(request.getParameter("fid"));
		Fsubscription subscription = this.subscriptionService.findById(fid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int vid = Integer.parseInt(request.getParameter("vid"));
		int vid1 = Integer.parseInt(request.getParameter("vid1"));
		Fvirtualcointype vt = this.virtualCoinService.findById(vid);
		Fvirtualcointype vt1 = this.virtualCoinService.findById(vid1);
		String fisopen = request.getParameter("fisopen");
		double ftotal = Double.valueOf(request.getParameter("ftotal"));
		double fprice = Double.valueOf(request.getParameter("fprice"));
		int fbuyCount = Integer.parseInt(request.getParameter("fbuyCount"));
		int fbuyTimes = Integer.parseInt(request.getParameter("fbuyTimes"));
		String fbeginTimes = request.getParameter("fbeginTime");
		String fendTimes = request.getParameter("fendTime");
		Date beginDate = sdf.parse(fbeginTimes);
		Date endDate = sdf.parse(fendTimes);
		if (beginDate.compareTo(endDate) > 0) {
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "开始时间不能大于结束时间");
			return modelAndView;
		}

		subscription.setFbeginTime(Timestamp.valueOf(fbeginTimes));
		subscription.setFendTime(Timestamp.valueOf(fendTimes));
		if (fisopen != null && fisopen.trim().length() > 0) {
			subscription.setFisopen(true);
		} else {
			subscription.setFisopen(false);
		}
		subscription.setFvirtualcointypeCost(vt1);
		subscription.setFvirtualcointype(vt);
		subscription.setFbuyCount(fbuyCount);
		subscription.setFbuyTimes(fbuyTimes);
		subscription.setFprice(fprice);
		subscription.setFtotal(ftotal);
		subscription.setFtype(SubscriptionTypeEnum.SMELTER);
		this.subscriptionService.updateObj(subscription);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "修改成功");
		modelAndView.addObject("callbackType", "closeCurrent");
		return modelAndView;
	}
}
