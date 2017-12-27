package com.ruizton.main.controller.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruizton.main.model.DateWalletStatistic;
import com.ruizton.main.service.admin.DateWalletStatisticService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.Enum.CapitalOperationInStatus;
import com.ruizton.main.Enum.CapitalOperationOutStatus;
import com.ruizton.main.Enum.CapitalOperationTypeEnum;
import com.ruizton.main.Enum.EntrustTypeEnum;
import com.ruizton.main.Enum.VirtualCapitalOperationInStatusEnum;
import com.ruizton.main.Enum.VirtualCapitalOperationOutStatusEnum;
import com.ruizton.main.Enum.VirtualCapitalOperationTypeEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.BTCInfo;
import com.ruizton.main.model.BTCMessage;
import com.ruizton.main.model.Fvirtualcaptualoperation;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.BetlogService;
import com.ruizton.main.service.admin.CapitaloperationService;
import com.ruizton.main.service.admin.EntrustService;
import com.ruizton.main.service.admin.GameOperateLogService;
import com.ruizton.main.service.admin.GameharvestlogService;
import com.ruizton.main.service.admin.PopcornbetlogService;
import com.ruizton.main.service.admin.SpendlogService;
import com.ruizton.main.service.admin.UserService;
import com.ruizton.main.service.admin.VirtualCapitaloperationService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.main.service.admin.VirtualWalletService;
import com.ruizton.main.service.admin.WalletService;
import com.ruizton.util.BTCUtils;
import com.ruizton.util.DateUtils;
import com.ruizton.util.MathUtils;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import com.ruizton.util.XlsExport;

@Controller
public class VirtualCapitaloperationController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private WalletService walletService;
	@Autowired
	private CapitaloperationService capitaloperationService;
	@Autowired
	private VirtualWalletService virtualWalletService;
	@Autowired
	private GameharvestlogService gameharvestlogService;
	@Autowired
	private VirtualCapitaloperationService virtualCapitaloperationService;
	@Autowired
	private EntrustService entrustService;
	@Autowired
	private VirtualCoinService virtualCoinService;
	@Autowired
	private GameOperateLogService gameOperateLogService;
	@Autowired
	private SpendlogService spendlogService;
	@Autowired
	private PopcornbetlogService popcornbetlogService;
	@Autowired
	private BetlogService betlogService;
	@Autowired
	private DateWalletStatisticService dateWalletStatisticService;
	// 每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();

	@RequestMapping("/ssadmin/virtualCaptualoperationList")
	@RequiresPermissions("ssadmin/virtualCaptualoperationList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/virtualCaptualoperationList1");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filterSQL = new StringBuffer();
		filterSQL.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filterSQL.append("and (fuser.floginName like '%" + keyWord
					+ "%' OR \n");
			filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
			filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
			filterSQL.append("withdraw_virtual_address like '%" + keyWord
					+ "%' OR \n");
			filterSQL.append("recharge_virtual_address like '%" + keyWord
					+ "%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}

		if (request.getParameter("ftype") != null) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
			}
			modelAndView.addObject("ftype", type);
		} else {
			modelAndView.addObject("ftype", 0);
		}
		filterSQL.append("and fuser.fid is not null \n");

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

		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);

		List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService
				.list((currentPage - 1) * numPerPage, numPerPage, filterSQL
						+ "", true);
		modelAndView.addObject("virtualCapitaloperationList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "virtualCapitaloperationList");
		// 总数量
		modelAndView.addObject("totalCount", this.adminService.getAllCount(
				"Fvirtualcaptualoperation", filterSQL + ""));
		return modelAndView;
	}

	/**
	 * 导出操作记录
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ssadmin/exportVirtualCaptualoperationList")
	@RequiresPermissions("ssadmin/exportVirtualCaptualoperationList.html")
	public void exportVirtualCaptualoperationList(HttpServletRequest request, HttpServletResponse response){
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		StringBuffer filterSQL = new StringBuffer();
		filterSQL.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filterSQL.append("and (fuser.floginName like '%" + keyWord + "%' OR \n");
			filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
			filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
			filterSQL.append("withdraw_virtual_address like '%" + keyWord + "%' OR \n");
			filterSQL.append("recharge_virtual_address like '%" + keyWord + "%' )\n");
		}
		if (request.getParameter("ftype") != null) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
			}
		}
		filterSQL.append("and fuser.fid is not null \n");

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

		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService.list(0, 0, filterSQL + "", false);
		
		// 标题
		XlsExport xls = new XlsExport();
		xls.createRow(0);
		xls.setCell(0, "会员登录名");
		xls.setCell(1, "会员昵称");
		xls.setCell(2, "会员邮箱");
		xls.setCell(3, "会员手机号");
		xls.setCell(4, "会员真实姓名");
		xls.setCell(5, "虚拟币类型");
		xls.setCell(6, "状态");
		xls.setCell(7, "交易类型");
		xls.setCell(8, "数量");
		xls.setCell(9, "手续费");
		xls.setCell(10, "提现地址");
		xls.setCell(11, "充值地址");
		xls.setCell(12, "交易ID");
		xls.setCell(13, "创建时间");
		xls.setCell(14, "最后修改时间");
		
		// 填入数据
		for(int i=1, len=list.size(); i<=len; i++){
			Fvirtualcaptualoperation obj = list.get(i-1);
			xls.createRow(i);
			xls.setCell(0, obj.getFuser().getFloginName());
			xls.setCell(1, obj.getFuser().getFnickName());
			xls.setCell(2, obj.getFuser().getFemail());
			xls.setCell(3, obj.getFuser().getFtelephone());
			xls.setCell(4, obj.getFuser().getFrealName());
			xls.setCell(5, obj.getFvirtualcointype().getFname());
			xls.setCell(6, obj.getFstatus_s());
			xls.setCell(7, obj.getFtype_s());
			xls.setCell(8, obj.getFamount());
			xls.setCell(9, obj.getFfees());
			xls.setCell(10, obj.getWithdraw_virtual_address());
			xls.setCell(11, obj.getRecharge_virtual_address());
			xls.setCell(12, obj.getFtradeUniqueNumber());
			xls.setCell(13, obj.getFcreateTime());
			xls.setCell(14, obj.getFlastUpdateTime());
		}
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("虚拟币操作列表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/ssadmin/virtualCapitalInList")
	@RequiresPermissions("ssadmin/virtualCapitalInList.html")
	public ModelAndView virtualCapitalInList(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/virtualCapitalInList");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filterSQL = new StringBuffer();
		filterSQL.append("where 1=1 and ftype="
				+ VirtualCapitalOperationTypeEnum.COIN_IN + "\n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filterSQL.append("and (fuser.floginName like '%" + keyWord
					+ "%' OR \n");
			filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
			filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
			filterSQL.append("withdraw_virtual_address like '%" + keyWord
					+ "%' OR \n");
			filterSQL.append("recharge_virtual_address like '%" + keyWord
					+ "%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}

		if (request.getParameter("ftype") != null) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
			}
			modelAndView.addObject("ftype", type);
		} else {
			modelAndView.addObject("ftype", 0);
		}

		filterSQL.append("and fuser.fid is not null \n");

		if (orderField != null && orderField.trim().length() > 0) {
			filterSQL.append("order by " + orderField + "\n");
		}
		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filterSQL.append(orderDirection + "\n");
		}

		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);

		List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService
				.list((currentPage - 1) * numPerPage, numPerPage, filterSQL
						+ "", true);
		modelAndView.addObject("virtualCapitaloperationList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "virtualCapitalInList");
		// 总数量
		modelAndView.addObject("totalCount", this.adminService.getAllCount(
				"Fvirtualcaptualoperation", filterSQL + ""));
		return modelAndView;
	}
	
	/**
	 * 导出虚拟币充值记录
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ssadmin/exportViCoinChargeList")
	@RequiresPermissions("ssadmin/exportViCoinChargeList.html")
	public void exportViCoinChargeList(HttpServletRequest request, HttpServletResponse response){
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		StringBuffer filterSQL = new StringBuffer();
		filterSQL.append("where 1=1 and ftype="
				+ VirtualCapitalOperationTypeEnum.COIN_IN + "\n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filterSQL.append("and (fuser.floginName like '%" + keyWord
					+ "%' OR \n");
			filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
			filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
			filterSQL.append("withdraw_virtual_address like '%" + keyWord
					+ "%' OR \n");
			filterSQL.append("recharge_virtual_address like '%" + keyWord
					+ "%' )\n");
		}

		if (request.getParameter("ftype") != null) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
			}
		}

		filterSQL.append("and fuser.fid is not null \n");

		if (orderField != null && orderField.trim().length() > 0) {
			filterSQL.append("order by " + orderField + "\n");
		}
		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filterSQL.append(orderDirection + "\n");
		}

		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService.list(0, 0, filterSQL + "", false);
		
		// 标题
		XlsExport xls = new XlsExport();
		xls.createRow(0);
		xls.setCell(0, "会员登录名");
		xls.setCell(1, "会员昵称");
		xls.setCell(2, "会员邮箱");
		xls.setCell(3, "会员手机号");
		xls.setCell(4, "会员真实姓名");
		xls.setCell(5, "虚拟币类型");
		xls.setCell(6, "状态");
		xls.setCell(7, "交易类型");
		xls.setCell(8, "数量");
		xls.setCell(9, "手续费");
		xls.setCell(10, "充值地址");
		xls.setCell(11, "交易ID");
		xls.setCell(12, "创建时间");
		xls.setCell(13, "最后修改时间");

		// 填入数据
		for (int i = 1, len = list.size(); i <= len; i++) {
			Fvirtualcaptualoperation obj = list.get(i - 1);
			xls.createRow(i);
			xls.setCell(0, obj.getFuser().getFloginName());
			xls.setCell(1, obj.getFuser().getFnickName());
			xls.setCell(2, obj.getFuser().getFemail());
			xls.setCell(3, obj.getFuser().getFtelephone());
			xls.setCell(4, obj.getFuser().getFrealName());
			xls.setCell(5, obj.getFvirtualcointype().getFname());
			xls.setCell(6, obj.getFstatus_s());
			xls.setCell(7, obj.getFtype_s());
			xls.setCell(8, obj.getFamount());
			xls.setCell(9, obj.getFfees());
			xls.setCell(10, obj.getRecharge_virtual_address());
			xls.setCell(11, obj.getFtradeUniqueNumber());
			xls.setCell(12, obj.getFcreateTime());
			xls.setCell(13, obj.getFlastUpdateTime());
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("虚拟币充值列表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/ssadmin/exportViCoinWithdrawList")
	@RequiresPermissions("ssadmin/exportViCoinWithdrawList.html")
	public void exportViCoinWithdrawList(HttpServletRequest request, HttpServletResponse response){
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		StringBuffer filterSQL = new StringBuffer();
		filterSQL.append("where 1=1 and ftype="
				+ VirtualCapitalOperationTypeEnum.COIN_OUT + "\n");
		filterSQL.append("and fstatus IN("
				+ VirtualCapitalOperationOutStatusEnum.WaitForOperation + ","
				+ VirtualCapitalOperationOutStatusEnum.OperationLock + ")\n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filterSQL.append("and (fuser.floginName like '%" + keyWord
					+ "%' OR \n");
			filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
			filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
			filterSQL.append("withdraw_virtual_address like '%" + keyWord
					+ "%' OR \n");
			filterSQL.append("recharge_virtual_address like '%" + keyWord
					+ "%' )\n");
		}
		
		if (request.getParameter("ftype") != null) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
			}
		}
		
		filterSQL.append("and fuser.fid is not null \n");
		
		if (orderField != null && orderField.trim().length() > 0) {
			filterSQL.append("order by " + orderField + "\n");
		}
		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filterSQL.append(orderDirection + "\n");
		}
		
		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService.list(0, 0, filterSQL + "", false);
		
		// 标题
		XlsExport xls = new XlsExport();
		xls.createRow(0);
		xls.setCell(0, "会员登录名");
		xls.setCell(1, "会员昵称");
		xls.setCell(2, "会员邮箱");
		xls.setCell(3, "会员手机号");
		xls.setCell(4, "会员真实姓名");
		xls.setCell(5, "虚拟币类型");
		xls.setCell(6, "状态");
		xls.setCell(7, "交易类型");
		xls.setCell(8, "数量");
		xls.setCell(9, "手续费");
		xls.setCell(10, "提现地址");
		xls.setCell(11, "交易ID");
		xls.setCell(12, "创建时间");
		xls.setCell(13, "最后修改时间");
		
		// 填入数据
		for (int i = 1, len = list.size(); i <= len; i++) {
			Fvirtualcaptualoperation obj = list.get(i - 1);
			xls.createRow(i);
			xls.setCell(0, obj.getFuser().getFloginName());
			xls.setCell(1, obj.getFuser().getFnickName());
			xls.setCell(2, obj.getFuser().getFemail());
			xls.setCell(3, obj.getFuser().getFtelephone());
			xls.setCell(4, obj.getFuser().getFrealName());
			xls.setCell(5, obj.getFvirtualcointype().getFname());
			xls.setCell(6, obj.getFstatus_s());
			xls.setCell(7, obj.getFtype_s());
			xls.setCell(8, obj.getFamount());
			xls.setCell(9, obj.getFfees());
			xls.setCell(10, obj.getWithdraw_virtual_address());
			xls.setCell(11, obj.getFtradeUniqueNumber());
			xls.setCell(12, obj.getFcreateTime());
			xls.setCell(13, obj.getFlastUpdateTime());
		}
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("虚拟币提现列表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/ssadmin/virtualCapitalOutList")
	@RequiresPermissions("ssadmin/virtualCapitalOutList.html")
	public ModelAndView virtualCapitalOutList(HttpServletRequest request) throws Exception {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/virtualCapitalOutList");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filterSQL = new StringBuffer();
		filterSQL.append("where 1=1 and ftype="
				+ VirtualCapitalOperationTypeEnum.COIN_OUT + "\n");
		filterSQL.append("and fstatus IN("
				+ VirtualCapitalOperationOutStatusEnum.WaitForOperation + ","
				+ VirtualCapitalOperationOutStatusEnum.OperationLock + ")\n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filterSQL.append("and (fuser.floginName like '%" + keyWord
					+ "%' OR \n");
			filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
			filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
			filterSQL.append("withdraw_virtual_address like '%" + keyWord
					+ "%' OR \n");
			filterSQL.append("recharge_virtual_address like '%" + keyWord
					+ "%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}

		if (StringUtils.hasText(request.getParameter("ftype"))) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
			}
			modelAndView.addObject("ftype", type);
		} else {
			modelAndView.addObject("ftype", 0);
		}
		if (orderField != null && orderField.trim().length() > 0) {
			filterSQL.append("order by " + orderField + "\n");
		}
		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filterSQL.append(orderDirection + "\n");
		}

		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);

		List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService
				.list((currentPage - 1) * numPerPage, numPerPage, filterSQL
						+ "", true);
		modelAndView.addObject("virtualCapitaloperationList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "virtualCapitalOutList");
		// 总数量
		modelAndView.addObject("totalCount", this.adminService.getAllCount(
				"Fvirtualcaptualoperation", filterSQL + ""));
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/goVirtualCapitaloperationChangeStatus")
	@RequiresPermissions({"ssadmin/goVirtualCapitaloperationChangeStatus.html?type=1", "ssadmin/goVirtualCapitaloperationChangeStatus.html?type=2", "ssadmin/goVirtualCapitaloperationChangeStatus.html?type=3"})
	public ModelAndView goVirtualCapitaloperationChangeStatus(
			@RequestParam(required = true) int type,
			@RequestParam(required = true) int uid) throws Exception {


		ModelAndView modelAndView = new ModelAndView();
		Fvirtualcaptualoperation fvirtualcaptualoperation = this.virtualCapitaloperationService
				.findById(uid);
		fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp());

		int userId = fvirtualcaptualoperation.getFuser().getFid();
		Fvirtualcointype fvirtualcointype = fvirtualcaptualoperation
				.getFvirtualcointype();
		int coinTypeId = fvirtualcointype.getFid();
		List<Fvirtualwallet> virtualWallet = this.virtualWalletService
				.findByTwoProperty("fuser.fid", userId, "fvirtualcointype.fid",
						coinTypeId);
		if (virtualWallet == null || virtualWallet.size() == 0) {
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "审核失败，会员虚拟币钱包信息异常!");
			return modelAndView;
		}
		Fvirtualwallet virtualWalletInfo = virtualWallet.get(0);

		int status = fvirtualcaptualoperation.getFstatus();
		String tips = "";
		switch (type) {
		case 1:
			tips = "锁定";
			if (status != CapitalOperationOutStatus.WaitForOperation) {
				modelAndView.setViewName("ssadmin/comm/ajaxDone");
				modelAndView.addObject("statusCode", 300);
				String status_s = CapitalOperationOutStatus
						.getEnumString(CapitalOperationOutStatus.WaitForOperation);
				modelAndView.addObject("message", "锁定失败,只有状态为:‘" + status_s
						+ "’的充值记录才允许锁定!");
				return modelAndView;
			}
			fvirtualcaptualoperation
					.setFstatus(VirtualCapitalOperationOutStatusEnum.OperationLock);
			break;
		case 2:
			tips = "取消锁定";
			if (status != CapitalOperationOutStatus.OperationLock) {
				modelAndView.setViewName("ssadmin/comm/ajaxDone");
				modelAndView.addObject("statusCode", 300);
				String status_s = CapitalOperationOutStatus
						.getEnumString(CapitalOperationOutStatus.OperationLock);
				modelAndView.addObject("message", "取消锁定失败,只有状态为:‘" + status_s
						+ "’的充值记录才允许取消锁定!");
				return modelAndView;
			}
			fvirtualcaptualoperation
					.setFstatus(VirtualCapitalOperationOutStatusEnum.WaitForOperation);
			break;
		case 3:
			tips = "取消提现";
			if (status == CapitalOperationOutStatus.Cancel) {
				modelAndView.setViewName("ssadmin/comm/ajaxDone");
				modelAndView.addObject("statusCode", 300);
				modelAndView.addObject("message", "取消提现失败,该记录已处于取消状态!");
				return modelAndView;
			}
			double fee = fvirtualcaptualoperation.getFfees();
			double famount = fvirtualcaptualoperation.getFamount();
			fvirtualcaptualoperation
					.setFstatus(VirtualCapitalOperationOutStatusEnum.Cancel);
			virtualWalletInfo.setFfrozen(MathUtils.subtract(MathUtils.subtract(virtualWalletInfo.getFfrozen(), fee), famount));
			virtualWalletInfo.setFtotal(MathUtils.add(MathUtils.add(virtualWalletInfo.getFtotal(), fee), famount));
			virtualWalletInfo.setFlastUpdateTime(Utils.getTimestamp());
			break;
		}

		boolean flag = false;
		try {
			this.virtualCapitaloperationService
					.updateObj(fvirtualcaptualoperation);
			this.virtualWalletService.updateObj(virtualWalletInfo);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (flag) {
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 200);
			modelAndView.addObject("message", tips + "成功！");
		} else {
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "未知错误，请刷新列表后重试！");
		}

		return modelAndView;
	}

	@RequestMapping("ssadmin/goVirtualCapitaloperationJSP")
	public ModelAndView goVirtualCapitaloperationJSP(HttpServletRequest request) throws Exception {

		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(url);
		String xid = "";
		BTCMessage msg = new BTCMessage();
		if (request.getParameter("uid") != null) {
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fvirtualcaptualoperation virtualcaptualoperation = this.virtualCapitaloperationService
					.findById(fid);
			xid = virtualcaptualoperation.getFtradeUniqueNumber();
			msg.setACCESS_KEY(virtualcaptualoperation.getFvirtualcointype()
					.getFaccess_key());
			msg.setIP(virtualcaptualoperation.getFvirtualcointype().getFip());
			msg.setPORT(virtualcaptualoperation.getFvirtualcointype()
					.getFport());
			msg.setSECRET_KEY(virtualcaptualoperation.getFvirtualcointype()
					.getFsecrt_key());
			modelAndView.addObject("virtualCapitaloperation",
					virtualcaptualoperation);
		}
		if (request.getParameter("type") != null
				&& request.getParameter("type").equals("ViewStatus")) {
			BTCUtils btc = new BTCUtils(msg);
			BTCInfo btcInfo = btc.gettransactionValue(xid, "send");
			modelAndView.addObject("confirmations", btcInfo.getConfirmations());
		}
		return modelAndView;
	}

	@RequestMapping("ssadmin/virtualCapitalOutAudit")
	@RequiresPermissions("ssadmin/virtualCapitalOutAudit.html")
	public ModelAndView virtualCapitalOutAudit(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		int fid = Integer.parseInt(request.getParameter("uid"));
		Fvirtualcaptualoperation virtualcaptualoperation = this.virtualCapitaloperationService
				.findById(fid);
		int status = virtualcaptualoperation.getFstatus();
		if (status != VirtualCapitalOperationOutStatusEnum.OperationLock) {
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			String status_s = VirtualCapitalOperationOutStatusEnum
					.getEnumString(VirtualCapitalOperationOutStatusEnum.OperationLock);
			modelAndView.addObject("message", "审核失败,只有状态为:" + status_s
					+ "的提现记录才允许审核!");
			return modelAndView;
		}
		// 根据用户查钱包最后修改时间
		int userId = virtualcaptualoperation.getFuser().getFid();
		Fvirtualcointype fvirtualcointype = virtualcaptualoperation
				.getFvirtualcointype();
		int coinTypeId = fvirtualcointype.getFid();
		List<Fvirtualwallet> virtualWallet = this.virtualWalletService
				.findByTwoProperty("fuser.fid", userId, "fvirtualcointype.fid",coinTypeId);
		if (virtualWallet == null || virtualWallet.size() == 0) {
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "审核失败，会员虚拟币钱包信息异常!");
			return modelAndView;
		}

		Fvirtualwallet virtualWalletInfo = virtualWallet.get(0);
		double amount = Utils.getDouble(virtualcaptualoperation.getFamount()+virtualcaptualoperation.getFfees(), 4);
//		double frozenRmb = Utils.getDouble(virtualWalletInfo.getFfrozen(), 4);
//		if (frozenRmb < amount) {
//			modelAndView.setViewName("ssadmin/comm/ajaxDone");
//			modelAndView.addObject("statusCode", 300);
//			modelAndView.addObject("message", "审核失败,冻结数量:" + frozenRmb
//					+ "小于提现数量:" + amount + "，操作异常!");
//			return modelAndView;
//		}
		BTCMessage btcMsg = new BTCMessage();
		btcMsg.setACCESS_KEY(fvirtualcointype.getFaccess_key());
		btcMsg.setSECRET_KEY(fvirtualcointype.getFsecrt_key());
		btcMsg.setIP(fvirtualcointype.getFip());
		btcMsg.setPASSWORD(request.getParameter("fpassword"));
		btcMsg.setPORT(fvirtualcointype.getFport());
		BTCUtils btcUtils = new BTCUtils(btcMsg);

		try {
			double balance = btcUtils.getbalanceValue();
			if (balance < amount) {
				modelAndView.setViewName("ssadmin/comm/ajaxDone");
				modelAndView.addObject("statusCode", 300);
				modelAndView.addObject("message", "审核失败，钱包余额：" + balance + "小于"
						+ amount);
				return modelAndView;
			}
		} catch (Exception e1) {
			System.out.println("error 1 " + e1);
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "审核失败，钱包连接失败");
			return modelAndView;
		}

		// 充值操作
		virtualcaptualoperation
				.setFstatus(VirtualCapitalOperationOutStatusEnum.OperationSuccess);
		virtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp());

		// 钱包操作
		virtualWalletInfo.setFlastUpdateTime(Utils.getTimestamp());
		virtualWalletInfo.setFfrozen(MathUtils.subtract(virtualWalletInfo.getFfrozen(), amount));
		boolean flag = false;
		String errMsg = "审核失败";
		try {
			this.virtualCapitaloperationService.updateCapital(
					virtualcaptualoperation, virtualWalletInfo, btcUtils);
			flag = true;
		} catch (Exception e) {
			errMsg = e.getMessage();
			System.out.println("error " + e);
			flag = false;
		}
		if (!flag) {
			modelAndView.setViewName("ssadmin/comm/ajaxDone");
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", errMsg);
			return modelAndView;
		}
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "审核成功");
		modelAndView.addObject("callbackType", "closeCurrent");
		return modelAndView;
	}
	
	/**
	 * 钱包信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("ssadmin/walletReport")
	@RequiresPermissions("ssadmin/walletReport.html")
	public ModelAndView walletReport(HttpServletRequest request,
									 @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date curDate){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.setViewName("ssadmin/walletReport");

		if(null == curDate){
			curDate = new Date();
		}

		if(DateUtils.isSameDate(curDate, new Date())){
			// 全站总人民币
			Map walletMap = this.walletService.getTotalMoney();
			modelAndView.addObject("wallet", walletMap);

			// 全站总币数量
			List<Map> virtualQtyList = this.virtualWalletService.getTotalQty();
			modelAndView.addObject("virtualQtyList", virtualQtyList);
		}else{
			List<DateWalletStatistic> list = dateWalletStatisticService.findByDate(curDate);

			HashMap coinMap = null;
			Fvirtualcointype fvirtualcointype;
			List virtualQtyList = new ArrayList<>(list.size());
			for(DateWalletStatistic log : list){
				if(log.isRMB()){
					Map walletMap = new HashMap<>();
					walletMap.put("totalRmb", log.getTotalBalance());
					walletMap.put("frozenRmb", log.getTotalFreeze());
					modelAndView.addObject("wallet", walletMap);
				}else{
					fvirtualcointype = virtualCoinService.findById(log.getCoinType());
					if(null == coinMap){
						coinMap = new HashMap();
					}else if(!coinMap.isEmpty()){
						coinMap = (HashMap) coinMap.clone();
					}
					coinMap.put("fName", fvirtualcointype.getFname());
					coinMap.put("totalQty", log.getTotalBalance());
					coinMap.put("frozenQty", log.getTotalFreeze());
					virtualQtyList.add(coinMap);
				}
			}
			modelAndView.addObject("virtualQtyList", virtualQtyList);
		}

		modelAndView.addObject("curDate", curDate);
		modelAndView.addObject("rel", "walletReport");
		return modelAndView;
	}
	
	/**
	 * 当前委托
	 * 
	 * @param request
	 * @return 
	 */
	@RequestMapping("ssadmin/curEntrustReport")
	@RequiresPermissions("ssadmin/curEntrustReport.html")
	public ModelAndView curEntrustReport(HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.setViewName("ssadmin/curEntrustReport");
		
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String isAll = request.getParameter("isAll");
		int type = StringUtils.hasText(request.getParameter("type"))?Integer.valueOf(request.getParameter("type")):EntrustTypeEnum.BUY;
		
		if(StringUtils.hasText(isAll)){
			startDate = null;
			endDate = null;
		}else if(!StringUtils.hasText(startDate) && !StringUtils.hasText(endDate)){
			endDate = DateUtils.formatDate(new Date());
			startDate = DateUtils.formatDate(DateUtils.getDaysBefore(30));
		}

		// 当前委托卖出
		List entrustSellMap = this.entrustService.getTotalQty(type, startDate, endDate);
		modelAndView.addObject("entrustMap", entrustSellMap);
		
		modelAndView.addObject("startDate", startDate);
		modelAndView.addObject("endDate", endDate);
		modelAndView.addObject("isAll", isAll);
		modelAndView.addObject("type", type);
		modelAndView.addObject("rel", "curEntrustReport");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/totalReport")
	@RequiresPermissions("ssadmin/totalReport.html")
	public ModelAndView totalReport(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.setViewName("ssadmin/totalReport");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = sdf.parse(sdf.format(new Date()));
		// 总会员数
		modelAndView.addObject("totalUser",
				this.adminService.getAllCount("Fuser", ""));
		// 总认证数
		modelAndView.addObject("totalValidateUser",this.adminService.getAllCount("Fuser", "where fhasRealValidate=1"));

		// 今天注册数
		modelAndView.addObject("todayTotalUser",this.adminService.getAllCount("Fuser", "where date_format(fregisterTime,'%Y-%m-%d')=date_format(now(),'%Y-%m-%d')"));
		// 今天认证数
		modelAndView.addObject("todayValidateUser", this.adminService.getAllCount("Fuser", "where date_format(fhasRealValidateTime,'%Y-%m-%d')=date_format(now(),'%Y-%m-%d')"));


		// 今日充值总金额
		Map amountInMap = this.capitaloperationService.getTotalAmount(
				CapitalOperationTypeEnum.RMB_IN, "("
						+ CapitalOperationInStatus.Come + ")", true);
		Map totalAmountInMap = this.capitaloperationService.getTotalAmount(
				CapitalOperationTypeEnum.RMB_IN, "("
						+ CapitalOperationInStatus.Come + ")", false);
		modelAndView.addObject("amountInMap", amountInMap);
		modelAndView.addObject("totalAmountInMap", totalAmountInMap);
		
		// 今日提现总金额
		Map amountOutMap = this.capitaloperationService.getTotalAmount(
				CapitalOperationTypeEnum.RMB_OUT, "("
						+ CapitalOperationOutStatus.OperationSuccess + ")",
				true);
		Map amountOutMap1 = this.capitaloperationService.getTotalAmount(
				CapitalOperationTypeEnum.RMB_OUT, "("
						+ CapitalOperationOutStatus.OperationSuccess + ")",
				false);
		modelAndView.addObject("amountOutMap", amountOutMap);
		modelAndView.addObject("amountOutMap1", amountOutMap1);

		// 所有提现未转帐总金额
		String coStatus = "(" + CapitalOperationOutStatus.WaitForOperation
				+ "," + CapitalOperationOutStatus.OperationLock + ")";
		Map amountOutWaitingMap = this.capitaloperationService.getTotalAmount(
				CapitalOperationTypeEnum.RMB_OUT, coStatus, false);
		modelAndView.addObject("amountOutWaitingMap", amountOutWaitingMap);

		// 今日充值虚拟币总数量
		List virtualInMap = this.virtualCapitaloperationService.getTotalAmount(
				VirtualCapitalOperationTypeEnum.COIN_IN, "("
						+ VirtualCapitalOperationInStatusEnum.SUCCESS + ")",
				true);
		modelAndView.addObject("virtualInMap", virtualInMap);

		// 今日提现虚拟币
		List virtualOutSuccessMap = this.virtualCapitaloperationService
				.getTotalAmount(VirtualCapitalOperationTypeEnum.COIN_OUT, "("
						+ VirtualCapitalOperationOutStatusEnum.OperationSuccess
						+ ")", true);
		modelAndView.addObject("virtualOutSuccessMap", virtualOutSuccessMap);

		// 等待提现虚拟币
		String voStatus = "("
				+ VirtualCapitalOperationOutStatusEnum.WaitForOperation + ","
				+ VirtualCapitalOperationOutStatusEnum.OperationLock + ")";

		List virtualOutWaitingMap = this.virtualCapitaloperationService
				.getTotalAmount(VirtualCapitalOperationTypeEnum.COIN_OUT,
						voStatus, false);
		modelAndView.addObject("virtualOutWaitingMap", virtualOutWaitingMap);

//		Map todayFees = this.popcornbetlogService.getTotalFees(true);
//		Map totalFees = this.popcornbetlogService.getTotalFees(false);
//		modelAndView.addObject("todayFees", todayFees);
//		modelAndView.addObject("totalFees", totalFees);
//		
//		Map todayWinQty = this.betlogService.getTotalFees(true);
//		Map totalWinQty = this.betlogService.getTotalFees(false);
//		modelAndView.addObject("todayWinQty", todayWinQty);
//		modelAndView.addObject("totalWinQty", totalWinQty);
//		
//		Map todayOPEN = gameOperateLogService.getTotalQty(GameOperateLogTypeEnum.OPEN, true,null);
//		Map totalOPEN = gameOperateLogService.getTotalQty(GameOperateLogTypeEnum.OPEN, false,null);
//		modelAndView.addObject("todayZhong", todayOPEN);
//		modelAndView.addObject("totalZhong", totalOPEN);
//		
//		//买土地消耗
//		Map todayUpgrad = gameOperateLogService.getTotalQty(GameOperateLogTypeEnum.BUY, true,null);
//		Map totalUpgrad = gameOperateLogService.getTotalQty(GameOperateLogTypeEnum.BUY, false,null);
//		modelAndView.addObject("todayUpgrad", todayUpgrad);
//		modelAndView.addObject("totalUpgrad", totalUpgrad);
//		
//		Map todaySpend = spendlogService.getTotalQty(true);
//		Map totalSpend = spendlogService.getTotalQty(false);
//		modelAndView.addObject("todaySpend", todaySpend);
//		modelAndView.addObject("totalSpend", totalSpend);
//
//		Map todayZhongQty = gameOperateLogService.getTotalQty(GameOperateLogTypeEnum.ZHONG, true,null);
//		Map totalZhongQty = gameOperateLogService.getTotalQty(GameOperateLogTypeEnum.ZHONG, false,null);
//
//		Map todayZhongPerson = gameOperateLogService.getTotalPerson(GameOperateLogTypeEnum.ZHONG, true);
//		Map totalZhongPerson = gameOperateLogService.getTotalPerson(GameOperateLogTypeEnum.ZHONG, false);
//		
//		modelAndView.addObject("todayZhongQty", todayZhongQty);
//		modelAndView.addObject("totalZhongQty", totalZhongQty);
//		modelAndView.addObject("todayZhongPerson", todayZhongPerson);
//		modelAndView.addObject("totalZhongPerson", totalZhongPerson);
//		
//		Map todayBuyKillQty = gameOperateLogService.getTotalQty(GameOperateLogTypeEnum.BUYKILL, true,null);
//		Map totalBuyKillQty = gameOperateLogService.getTotalQty(GameOperateLogTypeEnum.BUYKILL, false,null);
//		modelAndView.addObject("todayBuyKillQty", todayBuyKillQty);
//		modelAndView.addObject("totalBuyKillQty", totalBuyKillQty);
//		
//		//总冻结金额
//		String filter = "where ftype="+GameharvestlogTypeEnum.OWNER;
//		double ownerQty_frozen_total = this.gameharvestlogService.getFrozenQty("ffrozenqty", filter,false);
//		double ownerQty_frozen_today = this.gameharvestlogService.getFrozenQty("ffrozenqty", filter,true);
//		//推荐冻结金额
//		String filter1 = "where ftype="+GameharvestlogTypeEnum.INTROL;
//		double introlQty_frozen_total = this.gameharvestlogService.getFrozenQty("ffrozenqty", filter1,false);
//		double introlQty_frozen_today = this.gameharvestlogService.getFrozenQty("ffrozenqty", filter1,true);
//		
//		Map ownerQty_av_total = gameOperateLogService.getTotalQty(GameOperateLogTypeEnum.SHOU_OWNER, false,null);
//		Map ownerQty_av_today = gameOperateLogService.getTotalQty(GameOperateLogTypeEnum.SHOU_OWNER, true,null);
//		Map introlQty_av_total = gameOperateLogService.getTotalQty(GameOperateLogTypeEnum.SHOU_INTROL, false,null);
//		Map introlQty_av_today = gameOperateLogService.getTotalQty(GameOperateLogTypeEnum.SHOU_INTROL, true,null);
//		
//		modelAndView.addObject("ownerQty_frozen_total",ownerQty_frozen_total) ;
//		modelAndView.addObject("ownerQty_frozen_today",ownerQty_frozen_today) ;
//		modelAndView.addObject("ownerQty_av_total",ownerQty_av_total.get("totalQty") == null?"0":ownerQty_av_total.get("totalQty")) ;
//		modelAndView.addObject("ownerQty_av_today",ownerQty_av_today.get("totalQty") == null?"0":ownerQty_av_today.get("totalQty")) ;
//		modelAndView.addObject("introlQty_frozen_total",introlQty_frozen_total) ;
//		modelAndView.addObject("introlQty_frozen_today",introlQty_frozen_today) ;
//		modelAndView.addObject("introlQty_av_total",introlQty_av_total.get("totalQty") == null?"0":introlQty_av_total.get("totalQty")) ;
//		modelAndView.addObject("introlQty_av_today",introlQty_av_today.get("totalQty") == null?"0":introlQty_av_today.get("totalQty")) ;
//		
//		Map subscriptionQty = gameOperateLogService.getTotalSubscriptionQty();
//		modelAndView.addObject("subscriptionQty", subscriptionQty);
//		
		Map map = this.userService.getTotalBalanceUser();
		modelAndView.addObject("cny", map.get("cny"));
		modelAndView.addObject("tmc", map.get("tmc"));
		
		modelAndView.addObject("rel", "totalReport");
		return modelAndView;
	}
}
