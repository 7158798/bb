package com.ruizton.main.controller.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.Enum.OperationlogEnum;
import com.ruizton.main.Enum.RemittanceTypeEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fadmin;
import com.ruizton.main.model.Foperationlog;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.OperationLogService;
import com.ruizton.main.service.admin.UserService;
import com.ruizton.util.Utils;
import com.ruizton.util.XlsExport;

@Controller
public class OperationLogController extends BaseController {
	@Autowired
	private OperationLogService operationLogService ;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private UserService userService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/operationLogList")
	@RequiresPermissions("ssadmin/operationLogList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/operationLogList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String logDate = request.getParameter("logDate");
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
			filter.append("fkey1 like '%"+keyWord+"%' or \n");
			filter.append("fuser.frealName like '%"+keyWord+"%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		if(logDate != null && logDate.trim().length() >0){
			filter.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') = '"+logDate+"' \n");
			modelAndView.addObject("logDate", logDate);
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
		List<Foperationlog> list = this.operationLogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("operationlogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "operationLogList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Foperationlog", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("/ssadmin/exportOperationLogList")
	@RequiresPermissions("ssadmin/exportOperationLogList.html")
	public void exportOperationLogList(HttpServletRequest request, HttpServletResponse response){
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String logDate = request.getParameter("logDate");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (fuser.floginName like '%" + keyWord + "%' or \n");
			filter.append("fuser.fnickName like '%" + keyWord + "%' or \n");
			filter.append("fkey1 like '%" + keyWord + "%' or \n");
			filter.append("fuser.frealName like '%" + keyWord + "%' )\n");
		}

		if (logDate != null && logDate.trim().length() > 0) {
			filter.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') = '" + logDate + "' \n");
		}

		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		} else {
			filter.append("order by fid \n");
		}
		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		} else {
			filter.append("desc \n");
		}
		List<Foperationlog> list = this.operationLogService.list(0, 0, filter + "", false);
		
		// 标题
		XlsExport xls = new XlsExport();
		xls.createRow(0);
		xls.setCell(0, "会员登录名");
		xls.setCell(1, "会员昵称");
		xls.setCell(2, "会员邮箱");
		xls.setCell(3, "会员手机号");
		xls.setCell(4, "会员真实姓名");
		xls.setCell(5, "汇款方式");
		xls.setCell(6, "状态");
		xls.setCell(7, "金额");
		xls.setCell(8, "描述");
		xls.setCell(9, "创建时间");
		xls.setCell(10, "修改时间");
		xls.setCell(11, "审核人");

		// 填入数据
		for (int i = 1, len = list.size(); i <= len; i++) {
			Foperationlog obj = list.get(i - 1);
			xls.createRow(i);
			xls.setCell(0, obj.getFuser().getFloginName());
			xls.setCell(1, obj.getFuser().getFnickName());
			xls.setCell(2, obj.getFuser().getFemail());
			xls.setCell(3, obj.getFuser().getFtelephone());
			xls.setCell(4, obj.getFuser().getFrealName());
			xls.setCell(5, obj.getFtype_s());
			xls.setCell(6, obj.getFstatus_s());
			xls.setCell(7, obj.getFtype_s());
			xls.setCell(8, obj.getFamount());
			xls.setCell(9, obj.getFcreateTime());
			xls.setCell(10, obj.getFlastUpdateTime());
			xls.setCell(11, obj.getFkey1());
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("人民币手工充值列表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("ssadmin/goOperationLogJSP")
	public ModelAndView goOperationLogJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Foperationlog operationlog = this.operationLogService.findById(fid);
			modelAndView.addObject("operationlog", operationlog);
		}
		modelAndView.setViewName(url) ;
		Map typeMap = new HashMap();
		typeMap.put(RemittanceTypeEnum.Type1, RemittanceTypeEnum.getEnumString(RemittanceTypeEnum.Type1));
		typeMap.put(RemittanceTypeEnum.Type2, RemittanceTypeEnum.getEnumString(RemittanceTypeEnum.Type2));
		typeMap.put(RemittanceTypeEnum.Type3, RemittanceTypeEnum.getEnumString(RemittanceTypeEnum.Type3));
		modelAndView.addObject("typeMap", typeMap);
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/saveOperationLog")
	@RequiresPermissions("ssadmin/saveOperationLog.html")
	public ModelAndView saveOperationLog(HttpServletRequest request) throws Exception{
		Foperationlog operationlog = new Foperationlog();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		String dateString = sdf.format(new java.util.Date());
		Timestamp tm = Timestamp.valueOf(dateString);
		int userId = Integer.parseInt(request.getParameter("userLookup.id"));
		int ftype = Integer.parseInt(request.getParameter("ftype"));
		String famount = request.getParameter("famount");
		String fdescription = request.getParameter("fdescription");
		
		Fuser user = this.userService.findById(userId);
		operationlog.setFcreateTime(tm);
		operationlog.setFlastUpdateTime(tm);
		operationlog.setFamount(Double.valueOf(famount));
		operationlog.setFdescription(fdescription);
		operationlog.setFtype(ftype);
		operationlog.setFuser(user);
		operationlog.setFstatus(OperationlogEnum.SAVE);
		this.operationLogService.saveObj(operationlog);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteOperationLog")
	@RequiresPermissions("ssadmin/deleteOperationLog.html")
	public ModelAndView deleteOperationLog(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int fid = Integer.parseInt(request.getParameter("uid"));
		Foperationlog operationLog = this.operationLogService.findById(fid);
		if(operationLog.getFstatus() == OperationlogEnum.AUDIT){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","删除失败，记录已审核");
			return modelAndView;
		}
		
		this.operationLogService.deleteObj(fid);
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/auditOperationLog")
	@RequiresPermissions("ssadmin/auditOperationLog.html")
	public ModelAndView auditOperationLog(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int fid = Integer.parseInt(request.getParameter("uid"));
		boolean flag = false;
		try {
			Fadmin sessionAdmin = (Fadmin)request.getSession().getAttribute("login_admin");
			flag = this.operationLogService.updateOperationLog(fid,sessionAdmin);
		} catch (Exception e) {
			flag = false;
		}
		if(!flag){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","审核失败");
			return modelAndView;
		}
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","审核成功");
		return modelAndView;
	}
}
