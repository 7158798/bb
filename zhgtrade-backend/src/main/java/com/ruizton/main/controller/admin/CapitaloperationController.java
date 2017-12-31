package com.ruizton.main.controller.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
/*import com.ruizton.main.Enum.AwardOrDeductTicketOriginEnum;*/
import com.ruizton.main.Enum.ChargePayTypeEnum;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.util.Constants;
import com.ruizton.util.DateUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.Enum.CapitalOperationInStatus;
import com.ruizton.main.Enum.CapitalOperationOutStatus;
import com.ruizton.main.Enum.CapitalOperationTypeEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fadmin;
import com.ruizton.main.model.Fcapitaloperation;
import com.ruizton.main.model.Fwallet;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.CapitaloperationService;
import com.ruizton.main.service.admin.SystemArgsService;
import com.ruizton.main.service.admin.WalletService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.util.MathUtils;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import com.ruizton.util.XlsExport;

@Controller
public class CapitaloperationController extends BaseController {
	@Autowired
	private CapitaloperationService capitaloperationService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private WalletService walletService;
	@Autowired
	private SystemArgsService systemArgsService;
	@Autowired
	private FrontUserService frontUserService;
	@Autowired
	private MessageQueueService messageQueueService;
	@Autowired
	private ConstantMap constantMap;
	// 每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();

	@RequestMapping("/ssadmin/capitaloperationList")
	@RequiresPermissions("ssadmin/capitaloperationList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/capitaloperationList");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		String capitalId = request.getParameter("capitalId");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filterSQL = new StringBuffer();
		filterSQL.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filterSQL.append("and (fBank like '%" + keyWord + "%' OR \n");
			filterSQL.append("fuser.floginName like '%" + keyWord + "%' OR \n");
			filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
			filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
			filterSQL.append("fAccount like '%" + keyWord + "%' OR \n");
			filterSQL.append("fPhone like '%" + keyWord + "%' OR \n");
			filterSQL.append("fPayee like '%" + keyWord + "%' OR \n");
			filterSQL.append("famount like '%" + keyWord + "%') \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		String logDate = request.getParameter("logDate");
		if(logDate != null && logDate.trim().length() >0){
			filterSQL.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') = '"+logDate+"' \n");
			modelAndView.addObject("logDate", logDate);
		}

		if (capitalId != null && capitalId.trim().length() > 0) {
			boolean flag = Utils.isNumeric(capitalId);
			if (flag) {
				filterSQL.append("AND fid =" + capitalId + " \n");
				modelAndView.addObject("capitalId", capitalId);
			}
		}

		if (request.getParameter("fstatus") != null
				&& request.getParameter("fstatus").trim().length() > 0) {
			String fstatus = request.getParameter("fstatus");
			if (!fstatus.equals("0")) {
				if (fstatus.indexOf("充值") != -1) {
					filterSQL.append("AND ftype ="
							+ CapitalOperationTypeEnum.RMB_IN + " \n");
					filterSQL.append("AND fstatus ="
							+ fstatus.replace("充值-", "") + " \n");
				} else if (fstatus.indexOf("提现") != -1) {
					filterSQL.append("AND ftype ="
							+ CapitalOperationTypeEnum.RMB_OUT + " \n");
					filterSQL.append("AND fstatus ="
							+ fstatus.replace("提现-", "") + " \n");
				}
			}
			modelAndView.addObject("fstatus", fstatus);
		} else {
			modelAndView.addObject("fstatus", "0");
		}

		if (orderField != null && orderField.trim().length() > 0) {
			filterSQL.append("order by " + orderField + "\n");
		} else {
			filterSQL.append("order by fcreateTime \n");
		}

		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filterSQL.append(orderDirection + "\n");
		} else {
			filterSQL.append("desc \n");
		}

		Map statusMap = new HashMap();
		statusMap.put("0", "全部");
		statusMap.put(
				"充值-" + CapitalOperationInStatus.Come,
				"充值-"
						+ CapitalOperationInStatus
								.getEnumString(CapitalOperationInStatus.Come));
		statusMap
				.put("充值-" + CapitalOperationInStatus.Invalidate,
						"充值-"
								+ CapitalOperationInStatus
										.getEnumString(CapitalOperationInStatus.Invalidate));
		statusMap
				.put("充值-" + CapitalOperationInStatus.NoGiven,
						"充值-"
								+ CapitalOperationInStatus
										.getEnumString(CapitalOperationInStatus.NoGiven));
		statusMap
				.put("充值-" + CapitalOperationInStatus.WaitForComing,
						"充值-"
								+ CapitalOperationInStatus
										.getEnumString(CapitalOperationInStatus.WaitForComing));
		statusMap
				.put("提现-" + CapitalOperationOutStatus.Cancel,
						"提现-"
								+ CapitalOperationOutStatus
										.getEnumString(CapitalOperationOutStatus.Cancel));
		statusMap
				.put("提现-" + CapitalOperationOutStatus.OperationLock,
						"提现-"
								+ CapitalOperationOutStatus
										.getEnumString(CapitalOperationOutStatus.OperationLock));
		statusMap
				.put("提现-" + CapitalOperationOutStatus.OperationSuccess,
						"提现-"
								+ CapitalOperationOutStatus
										.getEnumString(CapitalOperationOutStatus.OperationSuccess));
		statusMap
				.put("提现-" + CapitalOperationOutStatus.WaitForOperation,
						"提现-"
								+ CapitalOperationOutStatus
										.getEnumString(CapitalOperationOutStatus.WaitForOperation));
		modelAndView.addObject("statusMap", statusMap);

		List<Fcapitaloperation> list = this.capitaloperationService.list(
				(currentPage - 1) * numPerPage, numPerPage, filterSQL + "",
				true);
		modelAndView.addObject("capitaloperationList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "capitaloperationList");
		modelAndView.addObject("wechatOrderNoPrefix", Constants.WECHAT_ORDER_NO_PREFIX);
		// 总数量
		modelAndView.addObject(
				"totalCount",
				this.adminService.getAllCount("Fcapitaloperation", filterSQL
						+ ""));
		return modelAndView;
	}

	@RequestMapping("/ssadmin/capitalInList")
	@RequiresPermissions("ssadmin/capitalInList.html")
	public ModelAndView capitalInList(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		String type = "(" + CapitalOperationTypeEnum.RMB_IN + ")";
		String status = "(" + CapitalOperationInStatus.WaitForComing + ")";
		modelAndView.setViewName("ssadmin/capitalInList");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		String capitalId = request.getParameter("capitalId");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");

		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filterSQL = new StringBuffer();
		filterSQL.append("where 1=1 \n");
		filterSQL.append("and payType = " + 0 + "\n");
		filterSQL.append("and ftype IN " + type + "\n");
		filterSQL.append("AND fstatus IN " + status + "\n");

		if (keyWord != null && keyWord.trim().length() > 0) {
			filterSQL.append("and (fBank like '%" + keyWord + "%' OR \n");
			filterSQL.append("fAccount like '%" + keyWord + "%' OR \n");
			filterSQL.append("fPhone like '%" + keyWord + "%' OR \n");
			filterSQL.append("fPayee like '%" + keyWord + "%' OR \n");
			filterSQL.append("famount like '%" + keyWord + "%') \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		String logDate = request.getParameter("logDate");
		if(logDate != null && logDate.trim().length() >0){
			filterSQL.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') = '"+logDate+"' \n");
			modelAndView.addObject("logDate", logDate);
		}

		if (capitalId != null && capitalId.trim().length() > 0) {
			boolean flag = Utils.isNumeric(capitalId);
			if (flag) {
				filterSQL.append(" AND fid =" + capitalId + "\n");
				modelAndView.addObject("capitalId", capitalId);
			}
		}
		if (orderField != null && orderField.trim().length() > 0) {
			filterSQL.append("order by " + orderField + "\n");
		}
		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filterSQL.append(orderDirection + "\n");
		}
		List<Fcapitaloperation> list = this.capitaloperationService.list(
				(currentPage - 1) * numPerPage, numPerPage, filterSQL + "",
				true);
		modelAndView.addObject("capitaloperationList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "capitalInList");
		modelAndView.addObject("wechatOrderNoPrefix", Constants.WECHAT_ORDER_NO_PREFIX);
		// 总数量
		modelAndView.addObject(
				"totalCount",
				this.adminService.getAllCount("Fcapitaloperation", filterSQL
						+ ""));
		return modelAndView;
	}
	
	/**
	 * 
	 * @param response
	 */
	@RequestMapping("/ssadmin/exportCapitalOperation")
	@RequiresPermissions("ssadmin/exportCapitalOperation.html")
	public void exportCapitalOperation(HttpServletRequest request, HttpServletResponse response){
		String type = StringUtils.null2EmptyString(request.getParameter("type"));
		String status = StringUtils.null2EmptyString(request.getParameter("status"));
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		String capitalId = request.getParameter("capitalId");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		StringBuilder filterSQL = new StringBuilder();
		filterSQL.append("where 1=1 \n");
		filterSQL.append("and ftype = " + type + "\n");
		filterSQL.append("AND fstatus = " + status + "\n");

		if (keyWord != null && keyWord.trim().length() > 0) {
			filterSQL.append("and (fBank like '%" + keyWord + "%' OR \n");
			filterSQL.append("fAccount like '%" + keyWord + "%' OR \n");
			filterSQL.append("fPhone like '%" + keyWord + "%' OR \n");
			filterSQL.append("fPayee like '%" + keyWord + "%' OR \n");
			filterSQL.append("famount like '%" + keyWord + "%') \n");
		}
		
		String logDate = request.getParameter("logDate");
		if(logDate != null && logDate.trim().length() >0){
			filterSQL.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') = '"+logDate+"' \n");
		}

		if (capitalId != null && capitalId.trim().length() > 0) {
			boolean flag = Utils.isNumeric(capitalId);
			if (flag) {
				filterSQL.append(" AND fid =" + capitalId + "\n");
			}
		}
		if (orderField != null && orderField.trim().length() > 0) {
			filterSQL.append("order by " + orderField + "\n");
		}
		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filterSQL.append(orderDirection + "\n");
		}
		List<Fcapitaloperation> list = this.capitaloperationService.list(0, 0, filterSQL + "", false);
		
		// 标题
		XlsExport xls = new XlsExport();
		xls.createRow(0);
		xls.setCell(0, "会员登录名");
		xls.setCell(1, "会员昵称");
		xls.setCell(2, "会员邮箱");
		xls.setCell(3, "会员手机号");
		xls.setCell(4, "会员真实姓名");
		xls.setCell(5, "类型");
		xls.setCell(6, "状态");
		xls.setCell(7, "金额");
		xls.setCell(8, "手续费");
		xls.setCell(9, "汇款银行");
		xls.setCell(10, "汇款账号");
		xls.setCell(11, "汇款账人");
		xls.setCell(12, "汇款手机号码");
		xls.setCell(13, "汇入银行");
		xls.setCell(14, "汇入账号");
		xls.setCell(15, "备注");
		xls.setCell(16, "创建时间");
		xls.setCell(17, "最后修改时间");

		// 填入数据
		for (int i = 1, len = list.size(); i <= len; i++) {
			Fcapitaloperation obj = list.get(i - 1);
			xls.createRow(i);
			xls.setCell(0, obj.getFuser().getFloginName());
			xls.setCell(1, obj.getFuser().getFnickName());
			xls.setCell(2, obj.getFuser().getFemail());
			xls.setCell(3, obj.getFuser().getFtelephone());
			xls.setCell(4, obj.getFuser().getFrealName());
			xls.setCell(5, obj.getFtype_s());
			xls.setCell(6, obj.getFstatus_s());
			xls.setCell(7, obj.getFamount());
			xls.setCell(8, obj.getFfees());
			xls.setCell(9, StringUtils.null2EmptyString(obj.getfBank()));
			xls.setCell(10, obj.getFaccount_s());
			xls.setCell(11, obj.getfPayee());
			xls.setCell(12, obj.getfPhone());
			xls.setCell(13, StringUtils.null2EmptyString(obj.getSystembankinfo().getFbankName()));
			xls.setCell(14, StringUtils.null2EmptyString(obj.getSystembankinfo().getFbankNumber()));

			if(2 == obj.getPayType().getIndex()){
				xls.setCell(15, Constants.WECHAT_ORDER_NO_PREFIX + StringUtils.null2EmptyString(obj.getFremark()));
			}else{
				xls.setCell(15, StringUtils.null2EmptyString(obj.getFremark()));
			}

			xls.setCell(16, obj.getFcreateTime());
			xls.setCell(17, obj.getfLastUpdateTime());
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			String name = String.valueOf(CapitalOperationTypeEnum.RMB_IN).equals(type) ? "人民币充值审核" : "人民币体现审核";
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name + "-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/ssadmin/capitalOutList")
	@RequiresPermissions("ssadmin/capitalOutList.html")
	public ModelAndView capitalOutList(HttpServletRequest request) throws Exception {
		String type = "(" + CapitalOperationTypeEnum.RMB_OUT + ")";
		String status = "(" + CapitalOperationOutStatus.WaitForOperation + ","
				+ CapitalOperationOutStatus.OperationLock + ")";
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/capitalOutList");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		String capitalId = request.getParameter("capitalId");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}

		StringBuffer filterSQL = new StringBuffer();
		filterSQL.append("where 1=1 \n");
		filterSQL.append("and ftype IN " + type + "\n");
		filterSQL.append("AND fstatus IN " + status + "\n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filterSQL.append(" AND (fBank like '%" + keyWord + "%' OR \n");
			filterSQL.append(" fAccount like '%" + keyWord + "%' OR \n");
			filterSQL.append(" fPhone like '%" + keyWord + "%' OR \n");
			filterSQL.append(" fPayee like '%" + keyWord + "%' OR \n");
			filterSQL.append(" famount like '%" + keyWord + "%') \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		String logDate = request.getParameter("logDate");
		if(logDate != null && logDate.trim().length() >0){
			filterSQL.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') = '"+logDate+"' \n");
			modelAndView.addObject("logDate", logDate);
		}

		if (capitalId != null && capitalId.trim().length() > 0) {
			boolean flag = Utils.isNumeric(capitalId);
			if (flag) {
				filterSQL.append(" AND fid =" + capitalId + "\n");
				modelAndView.addObject("capitalId", capitalId);
			}
		}
		if (orderField != null && orderField.trim().length() > 0) {
			filterSQL.append("order by " + orderField + "\n");
		}
		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filterSQL.append(orderDirection + "\n");
		}
		List<Fcapitaloperation> list = this.capitaloperationService.list(
				(currentPage - 1) * numPerPage, numPerPage, filterSQL + "",
				true);
		modelAndView.addObject("capitaloperationList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "capitalOutList");
		// 总数量
		modelAndView.addObject(
				"totalCount",
				this.adminService.getAllCount("Fcapitaloperation", filterSQL
						+ ""));
		return modelAndView;
	}

	@RequestMapping("ssadmin/goCapitaloperationJSP")
	public ModelAndView goCapitaloperationJSP(HttpServletRequest request) throws Exception {
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(url);
		if (request.getParameter("uid") != null) {
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fcapitaloperation capitaloperation = this.capitaloperationService
					.findById(fid);
			modelAndView.addObject("capitaloperation", capitaloperation);
		}
		return modelAndView;
	}

	@RequestMapping("/ssadmin/capitalInCancel")
	@RequiresPermissions("ssadmin/capitalInCancel.html")
	public ModelAndView capitalInCancel(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		int fid = Integer.parseInt(request.getParameter("uid"));
		Fcapitaloperation capitalOperation = this.capitaloperationService
				.findById(fid);
		int status = capitalOperation.getFstatus();
		if (status == CapitalOperationInStatus.Come) {
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			String status_s = CapitalOperationInStatus
					.getEnumString(CapitalOperationInStatus.Come);
			modelAndView.addObject("message", "取消失败,状态为:" + status_s
					+ "的充值记录不允许取消!");
			return modelAndView;
		}
		capitalOperation.setFstatus(CapitalOperationInStatus.Invalidate);
		this.capitaloperationService.updateObj(capitalOperation);
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "取消成功");
		return modelAndView;
	}

	@RequestMapping("ssadmin/capitalInAudit")
	@RequiresPermissions("ssadmin/capitalInAudit.html")
	public ModelAndView capitalInAudit(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		int fid = Integer.parseInt(request.getParameter("uid"));
		Fcapitaloperation capitalOperation = this.capitaloperationService
				.findById(fid);
		int status = capitalOperation.getFstatus();
		if (status != CapitalOperationInStatus.WaitForComing) {
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			String status_s = CapitalOperationInStatus
					.getEnumString(CapitalOperationInStatus.WaitForComing);
			modelAndView.addObject("message", "审核失败,只有状态为:" + status_s
					+ "的充值记录才允许审核!");
			return modelAndView;
		}
		// 根据用户查钱包最后修改时间
		Fwallet walletInfo = capitalOperation.getFuser().getFwallet();
		if (walletInfo == null) {
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "审核失败，会员钱包信息异常!");
			return modelAndView;
		}
		
		
		double rechargeRate = Double.valueOf(this.systemArgsService.getValue("rechargeRate"));
		double amount = capitalOperation.getFamount();

		Fadmin admin = (Fadmin) request.getSession()
				.getAttribute("login_admin");
		// 充值操作
		capitalOperation.setFstatus(CapitalOperationInStatus.Come);
		capitalOperation.setfLastUpdateTime(Utils.getTimestamp());
		capitalOperation.setfAuditee_id(admin);
		// 钱包操作
		walletInfo.setFlastUpdateTime(Utils.getTimestamp());
		double total = MathUtils.add(walletInfo.getFtotalRmb(), amount);
		total = MathUtils.add(total, MathUtils.multiply(amount, rechargeRate));
		walletInfo.setFtotalRmb(total);

		boolean flag = false;
		try {
			this.capitaloperationService.updateCapital(capitalOperation,
					walletInfo,true);
			flag = true;
		} catch (Exception e) {
			flag = false;
		}
		if (!flag) {
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "审核失败");
			return modelAndView;
		}

//		try {
//			String activityTime = constantMap.getString("activityTime");
//			if(Utils.isActivityTime(activityTime)) {
//				Map<String, Integer> map = new HashMap<String, Integer>();
//				map.put("userId", capitalOperation.getFuser().getFid());
//				if (amount >= 100 && amount < 1000) {
//					map.put("origin", AwardOrDeductTicketOriginEnum.ChargeLevel1);
//					map.put("addedCount", 4);
//				} else if (amount >= 1000 && amount < 10000) {
//					map.put("origin", AwardOrDeductTicketOriginEnum.ChargeLevel2);
//					map.put("addedCount", 10);
//				} else if (amount >= 10000 && amount < 50000) {
//					map.put("origin", AwardOrDeductTicketOriginEnum.ChargeLevel3);
//					map.put("addedCount", 40);
//				}else if (amount >= 50000 && amount < 100000) {
//					map.put("origin", AwardOrDeductTicketOriginEnum.ChargeLevel4);
//					map.put("addedCount", 60);
//				}else if (amount >= 100000) {
//					map.put("origin", AwardOrDeductTicketOriginEnum.ChargeLevel5);
//					map.put("addedCount", 80);
//				}
//				messageQueueService.publish(QueueConstants.AWARD_TICKET_QUEUE, map);
//			}
//		}catch (Exception e){
//			e.printStackTrace();
//		}finally {
//		}
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "审核成功");
		return modelAndView;
	}

	@RequestMapping("ssadmin/capitalOutAudit")
	@RequiresPermissions("ssadmin/capitalOutAudit.html")
	public ModelAndView capitalOutAudit(HttpServletRequest request, @RequestParam(required = true) int type)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		int fid = Integer.parseInt(request.getParameter("uid"));
		Fcapitaloperation capitalOperation = this.capitaloperationService
				.findById(fid);
		int status = capitalOperation.getFstatus();

		switch (type) {
		case 1:
			if (status != CapitalOperationOutStatus.OperationLock) {
				modelAndView.setViewName("ssadmin/comm/ajaxDone");
				modelAndView.addObject("statusCode", 300);
				String status_s = CapitalOperationOutStatus
						.getEnumString(CapitalOperationOutStatus.OperationLock);
				modelAndView.addObject("message", "审核失败,只有状态为:‘" + status_s
						+ "’的提现记录才允许审核!");
				return modelAndView;
			}
			break;
		case 2:
			if (status != CapitalOperationOutStatus.WaitForOperation) {
				modelAndView.setViewName("ssadmin/comm/ajaxDone");
				modelAndView.addObject("statusCode", 300);
				String status_s = CapitalOperationOutStatus
						.getEnumString(CapitalOperationOutStatus.WaitForOperation);
				modelAndView.addObject("message", "锁定失败,只有状态为:‘" + status_s
						+ "’的提现记录才允许锁定!");
				return modelAndView;
			}
			break;
		case 3:
			if (status != CapitalOperationOutStatus.OperationLock) {
				modelAndView.setViewName("ssadmin/comm/ajaxDone");
				modelAndView.addObject("statusCode", 300);
				String status_s = CapitalOperationOutStatus
						.getEnumString(CapitalOperationOutStatus.OperationLock);
				modelAndView.addObject("message", "取消锁定失败,只有状态为:‘" + status_s
						+ "’的提现记录才允许取消锁定!");
				return modelAndView;
			}
			break;
		case 4:
			if (status == CapitalOperationOutStatus.Cancel) {
				modelAndView.setViewName("ssadmin/comm/ajaxDone");
				modelAndView.addObject("statusCode", 300);
				modelAndView.addObject("message", "取消失败,该记录已处于取消状态!");
				return modelAndView;
			}
			break;
		default:
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "非法提交！");
			return modelAndView;
		}

		// 根据用户查钱包最后修改时间
		Fwallet walletInfo = capitalOperation.getFuser().getFwallet();
		if (walletInfo == null) {
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "审核失败，会员钱包信息异常!");
			return modelAndView;
		}

		double amount = capitalOperation.getFamount();
		double frees = capitalOperation.getFfees();
//		double frozenRmb = Utils.getDouble(walletInfo.getFfrozenRmb(),2);
		double totalAmt = MathUtils.add(amount, frees);
//		if (frozenRmb < totalAmt) {
//			modelAndView.setViewName("ssadmin/comm/ajaxDone");
//			modelAndView.addObject("statusCode", 300);
//			modelAndView.addObject("message", "审核失败,冻结金额:" + frozenRmb
//					+ "小于提现金额:" + totalAmt + "，操作异常!");
//			return modelAndView;
//		}

		// 充值操作
		Fadmin admin = (Fadmin) request.getSession()
				.getAttribute("login_admin");
		capitalOperation.setfLastUpdateTime(Utils.getTimestamp());
		capitalOperation.setfAuditee_id(admin);

		// 钱包操作//1审核,2锁定,3取消锁定,4取消提现
		String tips = "";
		switch (type) {
		case 1:
			capitalOperation
					.setFstatus(CapitalOperationOutStatus.OperationSuccess);

			walletInfo.setFlastUpdateTime(Utils.getTimestamp());
			walletInfo.setFfrozenRmb(MathUtils.subtract(walletInfo.getFfrozenRmb(), totalAmt));
			tips = "审核";
			break;
		case 2:
			capitalOperation
					.setFstatus(CapitalOperationOutStatus.OperationLock);
			tips = "锁定";
			break;
		case 3:
			capitalOperation
					.setFstatus(CapitalOperationOutStatus.WaitForOperation);
			tips = "取消锁定";
			break;
		case 4:
			capitalOperation.setFstatus(CapitalOperationOutStatus.Cancel);
			walletInfo.setFlastUpdateTime(Utils.getTimestamp());
			walletInfo.setFfrozenRmb(MathUtils.subtract(walletInfo.getFfrozenRmb(), totalAmt));
			walletInfo.setFtotalRmb(MathUtils.add(walletInfo.getFtotalRmb(), totalAmt));
			tips = "取消";
			break;
		}

		boolean flag = false;
		try {
			this.capitaloperationService.updateCapital(capitalOperation,
					walletInfo,false);
			flag = true;
		} catch (Exception e) {
			flag = false;
		}
		if (!flag) {
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", tips + "失败");
			return modelAndView;
		}

		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", tips + "成功");
		return modelAndView;
	}

	private static enum ExportFiled {
		会员登陆名, 会员昵称, 会员邮箱, 会员手机号, 会员真实姓名, 类型, 状态, 金额, 手续费, 备注, 创建时间, 最后修改时间, 审核人;
	}

	private List<Fcapitaloperation> getCapitalOperationList(HttpServletRequest request, String type,
			String status) {
		String keyWord = request.getParameter("keywords");
		String capitalId = request.getParameter("capitalId");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		StringBuffer filterSQL = new StringBuffer();
		filterSQL.append("where 1=1 \n");
		if (type != null && type.trim().length() > 0) {
			filterSQL.append("and ftype IN " + type + "\n");
		}
		if (status != null && status.trim().length() > 0) {
			filterSQL.append("AND fstatus IN " + status + "\n");
		}
		try {
			if (keyWord != null && keyWord.trim().length() > 0) {
				keyWord = new String(request.getParameter("keyWord").getBytes(
						"iso8859-1"), "utf-8");
				filterSQL.append("and (fBank like '%" + keyWord + "%' OR \n");
				filterSQL.append("fuser.floginName like '%" + keyWord
						+ "%' OR \n");
				filterSQL.append("fuser.fnickName like '%" + keyWord
						+ "%' OR \n");
				filterSQL.append("fuser.frealName like '%" + keyWord
						+ "%' OR \n");
				filterSQL.append("fAccount like '%" + keyWord + "%' OR \n");
				filterSQL.append("fPhone like '%" + keyWord + "%' OR \n");
				filterSQL.append("fPayee like '%" + keyWord + "%' OR \n");
				filterSQL.append("famount like '%" + keyWord + "%') \n");
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		if (capitalId != null && capitalId.trim().length() > 0) {
			boolean flag = Utils.isNumeric(capitalId);
			if (flag) {
				filterSQL.append("AND fid =" + capitalId + " \n");
			}
		}

		try {
			if (request.getParameter("fstatus") != null
					&& request.getParameter("fstatus").trim().length() > 0) {
				String fstatus = new String(request.getParameter("fstatus")
						.getBytes("iso8859-1"), "utf-8");
				if (!fstatus.equals("0")) {
					if (fstatus.indexOf("充值") != -1) {
						filterSQL.append("AND ftype ="
								+ CapitalOperationTypeEnum.RMB_IN + " \n");
						filterSQL.append("AND fstatus ="
								+ fstatus.replace("充值-", "") + " \n");
					} else if (fstatus.indexOf("提现") != -1) {
						filterSQL.append("AND ftype ="
								+ CapitalOperationTypeEnum.RMB_OUT + " \n");
						filterSQL.append("AND fstatus ="
								+ fstatus.replace("提现-", "") + " \n");
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if (orderField != null && orderField.trim().length() > 0) {
			filterSQL.append("order by " + orderField + "\n");
		} else {
			filterSQL.append("order by fcreateTime \n");
		}

		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filterSQL.append(orderDirection + "\n");
		} else {
			filterSQL.append("desc \n");
		}
		List<Fcapitaloperation> list = this.capitaloperationService.list(0, 0,
				filterSQL + "", false);
		return list;
	}

	@RequestMapping("ssadmin/capitaloperationExport")
	@RequiresPermissions("ssadmin/capitaloperationExport.html")
	public ModelAndView capitaloperationExport(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		response.setContentType("Application/excel");
		response.addHeader("Content-Disposition",
				"attachment;filename=capitaloperationList.xls");
		XlsExport e = new XlsExport();
		int rowIndex = 0;

		// header
		e.createRow(rowIndex++);
		for (ExportFiled filed : ExportFiled.values()) {
			e.setCell(filed.ordinal(), filed.toString());
		}
		String type = null;
		String status = null;
		List<Fcapitaloperation> capitalOperationList = getCapitalOperationList(request, 
				type, status);
		for (Fcapitaloperation capitalOperation : capitalOperationList) {
			e.createRow(rowIndex++);
			for (ExportFiled filed : ExportFiled.values()) {
				switch (filed) {
				case 会员登陆名:
					if (capitalOperation.getFuser() != null)
						e.setCell(filed.ordinal(), capitalOperation.getFuser()
								.getFloginName());
					break;
				case 会员昵称:
					if (capitalOperation.getFuser() != null)
						e.setCell(filed.ordinal(), capitalOperation.getFuser()
								.getFnickName());
					break;
				case 会员邮箱:
					if (capitalOperation.getFuser() != null)
						e.setCell(filed.ordinal(), capitalOperation.getFuser()
								.getFemail());
					break;
				case 会员手机号:
					if (capitalOperation.getFuser() != null)
						e.setCell(filed.ordinal(), capitalOperation.getFuser()
								.getFtelephone());
					break;
				case 会员真实姓名:
					if (capitalOperation.getFuser() != null)
						e.setCell(filed.ordinal(), capitalOperation.getFuser()
								.getFrealName());
					break;
				case 类型:
					e.setCell(filed.ordinal(), capitalOperation.getFtype_s());
					break;
				case 状态:
					e.setCell(filed.ordinal(), capitalOperation.getFstatus_s());
					break;
				case 金额:
					e.setCell(filed.ordinal(), capitalOperation.getFamount());
					break;
				case 手续费:
					e.setCell(filed.ordinal(), capitalOperation.getFfees());
					break;
				case 备注:
					e.setCell(filed.ordinal(), capitalOperation.getFremark());
					break;
				case 创建时间:
					e.setCell(filed.ordinal(),
							capitalOperation.getFcreateTime());
					break;
				case 最后修改时间:
					e.setCell(filed.ordinal(),
							capitalOperation.getfLastUpdateTime());
					break;
				case 审核人:
					if (capitalOperation.getfAuditee_id() != null)
						e.setCell(filed.ordinal(), capitalOperation
								.getfAuditee_id().getFname());
					break;
				default:
					break;
				}
			}
		}

		e.exportXls(response);

		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "导出成功");
		return modelAndView;
	}

	private static enum Export1Filed {
		会员登陆名, 会员昵称, 会员邮箱, 会员手机号, 会员真实姓名, 类型, 状态, 金额, 手续费, 银行, 收款帐号, 手机号码, 备注, 创建时间, 最后修改时间;
	}
	
  
	@RequestMapping("ssadmin/capitalOutExport")
	@RequiresPermissions("ssadmin/capitalOutExport.html")
	public ModelAndView capitalOutExport(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		response.setContentType("Application/excel");
		response.addHeader("Content-Disposition",
				"attachment;filename=capitalOutList.xls");
		XlsExport e = new XlsExport();
		int rowIndex = 0;

		// header
		e.createRow(rowIndex++);
		for (Export1Filed filed : Export1Filed.values()) {
			e.setCell(filed.ordinal(), filed.toString());
		}
		String type = "(" + CapitalOperationTypeEnum.RMB_OUT + ")";
		String status = "(" + CapitalOperationOutStatus.WaitForOperation + ","
				+ CapitalOperationOutStatus.OperationLock + ")";
		List<Fcapitaloperation> capitalOperationList = getCapitalOperationList(request, 
				type, status);
		for (Fcapitaloperation capitalOperation : capitalOperationList) {
			e.createRow(rowIndex++);
			for (Export1Filed filed : Export1Filed.values()) {
				switch (filed) {
				case 会员登陆名:
					if (capitalOperation.getFuser() != null)
						e.setCell(filed.ordinal(), capitalOperation.getFuser()
								.getFloginName());
					break;
				case 会员昵称:
					if (capitalOperation.getFuser() != null)
						e.setCell(filed.ordinal(), capitalOperation.getFuser()
								.getFnickName());
					break;
				case 会员邮箱:
					if (capitalOperation.getFuser() != null)
						e.setCell(filed.ordinal(), capitalOperation.getFuser()
								.getFemail());
					break;
				case 会员手机号:
					if (capitalOperation.getFuser() != null)
						e.setCell(filed.ordinal(), capitalOperation.getFuser()
								.getFtelephone());
					break;
				case 会员真实姓名:
					if (capitalOperation.getFuser() != null)
						e.setCell(filed.ordinal(), capitalOperation.getFuser()
								.getFrealName());
					break;
				case 类型:
					e.setCell(filed.ordinal(), capitalOperation.getFtype_s());
					break;
				case 状态:
					e.setCell(filed.ordinal(), capitalOperation.getFstatus_s());
					break;
				case 金额:
					e.setCell(filed.ordinal(), capitalOperation.getFamount());
					break;
				case 手续费:
					e.setCell(filed.ordinal(), capitalOperation.getFfees());
					break;
				case 银行:
					e.setCell(filed.ordinal(), capitalOperation.getfBank());
					break;
				case 收款帐号:
					e.setCell(filed.ordinal(), capitalOperation.getFaccount_s());
					break;
				case 手机号码:
					e.setCell(filed.ordinal(), capitalOperation.getfPhone());
					break;
				case 备注:
					e.setCell(filed.ordinal(), capitalOperation.getFid());
					break;
				case 创建时间:
					e.setCell(filed.ordinal(),
							capitalOperation.getFcreateTime());
					break;
				case 最后修改时间:
					e.setCell(filed.ordinal(),
							capitalOperation.getfLastUpdateTime());
					break;
				default:
					break;
				}
			}
		}

		e.exportXls(response);

		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "导出成功");
		return modelAndView;
	}
	
	private static enum Export1FiledSq {
		序号, 银行, 省, 市区, 支行名, 开户名, 卡号, 金额, 电话号码, 邮箱, 备注;
	}
	@RequestMapping("ssadmin/capitalOutExportSq")
	@RequiresPermissions("ssadmin/capitalOutExportSq.html")
	public ModelAndView capitalOutExportSq(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		response.setContentType("Application/excel");
		response.addHeader("Content-Disposition",
				"attachment;filename=capitalOutListSq.xls");
		XlsExport e = new XlsExport();
		int rowIndex = 0;

		// header
		e.createRow(rowIndex++);
		for (Export1FiledSq filed : Export1FiledSq.values()) {
			e.setCell(filed.ordinal(), filed.toString());
		}
		String type = "(" + CapitalOperationTypeEnum.RMB_OUT + ")";
		String status = "(" + CapitalOperationOutStatus.WaitForOperation + ","
				+ CapitalOperationOutStatus.OperationLock + ")";
		List<Fcapitaloperation> capitalOperationList = getCapitalOperationList(request,
				type, status);
		for (Fcapitaloperation capitalOperation : capitalOperationList) {
			e.createRow(rowIndex++);
			for (Export1FiledSq filed : Export1FiledSq.values()) { 
				switch (filed) {
				case 序号:
					e.setCell(filed.ordinal(), rowIndex-1);
					break;
				case 银行:
					e.setCell(filed.ordinal(), capitalOperation.getfBank());
					break;
				case 省:
					e.setCell(filed.ordinal(), capitalOperation.getFProvince());
					break;
				case 市区:
					e.setCell(filed.ordinal(), capitalOperation.getFCity());
					break;
				case 支行名:
					e.setCell(filed.ordinal(), capitalOperation.getFBranch());
					break;
				case 开户名:
					if (capitalOperation.getFuser() != null)
						e.setCell(filed.ordinal(), capitalOperation.getFuser().getFrealName());
					break;
				case 卡号:
					e.setCell(filed.ordinal(), capitalOperation.getFaccount_s());
					break;
				case 金额:
					e.setCell(filed.ordinal(), capitalOperation.getFamount());
					break;
				case 电话号码:
					e.setCell(filed.ordinal(), capitalOperation.getfPhone());
					break; 
				case 邮箱:
					e.setCell(filed.ordinal(), capitalOperation.getFuser().getFemail());
					break;
				case 备注:
					e.setCell(filed.ordinal(), "比特家交易平台提现");
					break; 
				default:
					break;
				}
			}
		}

		e.exportXls(response);

		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "导出成功");
		return modelAndView;
	}

	@RequestMapping("ssadmin/capitalOutAuditAll")
	@RequiresPermissions("ssadmin/capitalOutAuditAll.html")
	public ModelAndView capitalOutAuditAll(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		String ids = request.getParameter("ids");
		String[] idString = ids.split(",");
		// 充值操作
		Fadmin admin = (Fadmin) request.getSession()
				.getAttribute("login_admin");
		for(int i=0;i<idString.length;i++){
			int id = Integer.parseInt(idString[i]);
			Fcapitaloperation capitalOperation = this.capitaloperationService.findById(id);
			int status = capitalOperation.getFstatus();
			if (status != CapitalOperationOutStatus.WaitForOperation) {
				modelAndView.setViewName("ssadmin/comm/ajaxDone");
				modelAndView.addObject("statusCode", 300);
				String status_s = CapitalOperationOutStatus
						.getEnumString(CapitalOperationOutStatus.WaitForOperation);
				modelAndView.addObject("message", "锁定失败,只有状态为:‘" + status_s
						+ "’的提现记录才允许锁定!");
				return modelAndView;
			}
			capitalOperation.setfLastUpdateTime(Utils.getTimestamp());
			capitalOperation.setfAuditee_id(admin);
			capitalOperation.setFstatus(CapitalOperationOutStatus.OperationLock);
			this.capitaloperationService.updateObj(capitalOperation);
		}

		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "批量锁定成功");
		return modelAndView;
	}

	@RequestMapping("ssadmin/capitalInReport")
	@RequiresPermissions(value = {"ssadmin/capitalInReport.html", "ssadmin/exportCapitalInReport.html"})
	public String capitalInReport(@RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date startDate,
								  @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date endDate,
								  @RequestParam(required = false)String payType,
								  @RequestParam(required = false)String url,
								  ModelMap modelMap){
		if(null != endDate){
			endDate = DateUtils.getDateLastTime(endDate);
		}

		if(null == startDate && null == endDate){
			startDate = DateUtils.getMonthBefore();
			endDate = DateUtils.getDateLastTime(new Date());
		}

		String bank = null;
		ChargePayTypeEnum payTypeEnum = null;
		if("1".equals(payType)){
			payTypeEnum = ChargePayTypeEnum._95epay_EBank;
		}else if("2".equals(payType)){
			payTypeEnum = ChargePayTypeEnum.Default;
			bank = "支付宝";
		}else if("3".equals(payType)){
			payTypeEnum = ChargePayTypeEnum.Default;
			bank = "银行";
		}else if("4".equals(payType)){
			payTypeEnum = ChargePayTypeEnum.Wechat_Scan_Code;
			bank = "微信扫码";
		}

		Object[] dataArr = this.capitaloperationService.capitalInReport(startDate, endDate, payTypeEnum, bank);

		if("ssadmin/excel/exportCapitalInReport".equals(url)){
			modelMap.put("xAxis", dataArr[0]);
			modelMap.put("yAxis", dataArr[1]);
			return url;
		}

		modelMap.put("xAxis", JSON.toJSONString(dataArr[0]));
		modelMap.put("yAxis", JSON.toJSONString(dataArr[1]));
		modelMap.put("startDate", startDate);
		modelMap.put("endDate", endDate);
		return "ssadmin/charts/capitalInReport";
	}
}
