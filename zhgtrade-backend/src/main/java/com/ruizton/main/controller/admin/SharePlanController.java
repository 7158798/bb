package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.SharePlanLogStatusEnum;
import com.ruizton.main.Enum.SharePlanStatusEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.*;
import com.ruizton.main.service.admin.*;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SharePlanController extends BaseController {
	@Autowired
	private SharePlanService sharePlanService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private SharePlanLogService sharePlanLogService;
	@Autowired
	private VirtualCoinService virtualCoinService;
	@Autowired
	private VirtualWalletService virtualWalletService;
	// 每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();

	@RequestMapping("/ssadmin/sharePlanList")
	@RequiresPermissions("ssadmin/sharePlanList.html")
	public ModelAndView sharePlanList(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/sharePlanList");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and ftitle like '%" + keyWord + "%' \n");
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

		List<Fshareplan> list = this.sharePlanService.list((currentPage - 1)
				* numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("sharePlanList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "sharePlanList");
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("Fshareplan", filter + ""));

		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);

		return modelAndView;
	}
	
	@RequestMapping("ssadmin/goSharePlanJSP")
	public ModelAndView goSharePlanJSP(HttpServletRequest request) throws Exception {
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(url);
		if (request.getParameter("uid") != null) {
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fshareplan shareplan = this.sharePlanService.findById(fid);
			modelAndView.addObject("shareplan", shareplan);
		}
		List<Fvirtualcointype> allType = this.virtualCoinService.findAll();
		modelAndView.addObject("allType", allType);
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/auditSharePlan")
	@RequiresPermissions("ssadmin/auditSharePlan.html")
	public ModelAndView auditSharePlan(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		int uid = Integer.parseInt(request.getParameter("uid"));
		Fshareplan sharePlan = this.sharePlanService.findById(uid);
		if (sharePlan.getFstatus() != SharePlanStatusEnum.SAVE_VALUE) {
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "分红计划已审核");
			return modelAndView;
		}
		sharePlan.setFstatus(SharePlanStatusEnum.AUDITED_VALUE);
		sharePlan.setFcreateTime(Utils.getTimestamp());
		if(request.getSession().getAttribute("login_admin") != null){
			Fadmin sessionAdmin = (Fadmin)request.getSession().getAttribute("login_admin");
			sharePlan.setFcreator(sessionAdmin);
		}
		
		
		try {
			this.sharePlanService.update(sharePlan);
		} catch (Exception e) {
			e.printStackTrace() ;
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
		    modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "审核失败");
			return modelAndView;
		}
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "审核成功");
		return modelAndView;
	}
	
	
	/**
	 * 分钱：平台总交易手续费*比例/平台总币数*每个用户币数=每个用户得到的钱
	 * */
	@RequestMapping("ssadmin/saveSharePlan")
	@RequiresPermissions("ssadmin/saveSharePlan.html")
	public ModelAndView saveSharePlan(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		String title = request.getParameter("ftitle");
		int vid = Integer.parseInt(request.getParameter("vid"));
        BigDecimal amount = new BigDecimal(request.getParameter("famount"));
		Fvirtualcointype vType = this.virtualCoinService.findById(vid);
		Fshareplan sharePlan = new Fshareplan();
		sharePlan.setFstatus(SharePlanStatusEnum.SAVE_VALUE);
		sharePlan.setFtitle(title);
		sharePlan.setFvirtualcointype(vType);
		sharePlan.setFamount(amount);
		this.sharePlanService.saveObj(sharePlan);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "新增成功");
		modelAndView.addObject("callbackType", "closeCurrent");
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/deleteSharePlan")
	@RequiresPermissions("ssadmin/deleteSharePlan.html")
	public ModelAndView deleteSharePlan(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		int fid = Integer.parseInt(request.getParameter("uid"));
		Fshareplan shareplan = this.sharePlanService.findById(fid);
		if (shareplan.getFstatus() == SharePlanStatusEnum.AUDITED_VALUE) {
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "分红计划已审核，不允许删除");
			return modelAndView;
		}
		this.sharePlanService.deleteObj(fid);

		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "删除成功");
		return modelAndView;
	}

	@RequestMapping("ssadmin/updateSharePlan")
	@RequiresPermissions("ssadmin/updateSharePlan.html")
	public ModelAndView updateSharePlan(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		int uid = Integer.parseInt(request.getParameter("uid"));
		Fshareplan sharePlan = this.sharePlanService.findById(uid);
		String title = request.getParameter("ftitle");
		int vid = Integer.parseInt(request.getParameter("vid"));
        BigDecimal amount = new BigDecimal(request.getParameter("famount"));
		Fvirtualcointype vType = this.virtualCoinService.findById(vid);
		sharePlan.setFstatus(SharePlanStatusEnum.SAVE_VALUE);
		sharePlan.setFtitle(title);
		sharePlan.setFvirtualcointype(vType);
		sharePlan.setFamount(amount);
		this.sharePlanService.saveObj(sharePlan);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "更新成功");
		modelAndView.addObject("callbackType", "closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/sendMoney")
	@RequiresPermissions("ssadmin/sendMoney.html")
	public ModelAndView sendMoney(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		int uid = Integer.parseInt(request.getParameter("uid"));
		Fshareplan sharePlan = this.sharePlanService.findById(uid);
		if (sharePlan.getFstatus() != SharePlanStatusEnum.AUDITED_VALUE) {
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "分红计划未审核，不允许发钱");
			return modelAndView;
		}
		String filter = "where fshareplan.fid="+uid+" and fstatus="+SharePlanLogStatusEnum.NOSEND;
		List<Fshareplanlog> allLog = this.sharePlanLogService.list(0, 0, filter, false);
		if(allLog == null || allLog.size() ==0){
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "已发钱完毕，无需再发");
			return modelAndView;
		}
		//总数
		int totalCount = allLog.size();
		//总成功数
		int successCount = 0;
		for (Fshareplanlog fshareplanlog : allLog) {
			if(fshareplanlog.getFstatus() == SharePlanLogStatusEnum.HASSEND) continue;
			//钱包
			Fwallet wallet = fshareplanlog.getFuser().getFwallet();
			BigDecimal totalRmb = new BigDecimal(wallet.getFtotalRmb());
			wallet.setFtotalRmb(totalRmb.add(fshareplanlog.getFamount()).doubleValue());
			//记录
			fshareplanlog.setFstatus(SharePlanLogStatusEnum.HASSEND);
			try {
				this.sharePlanService.updateSharePlanLog(wallet, fshareplanlog);
			} catch (Exception e) {
				modelAndView.addObject("statusCode", 300);
				modelAndView.addObject("message", "部分成功，请重新发送!待发数："+totalCount+",本次发送数："+successCount);
				return modelAndView;
			}
			successCount = successCount+1;
		}
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "发钱成功，本次发送数："+successCount);
		return modelAndView;
	}
	
}
