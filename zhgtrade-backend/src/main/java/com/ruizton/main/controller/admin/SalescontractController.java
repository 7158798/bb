package com.ruizton.main.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.Enum.SalescontractStatusEnum;
import com.ruizton.main.model.Fadmin;
import com.ruizton.main.model.Fsalescontract;
import com.ruizton.main.model.Fsalespercent;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.SalescontractService;
import com.ruizton.main.service.admin.SalespercentService;
import com.ruizton.main.service.admin.UserService;
import com.ruizton.main.service.admin.VirtualWalletService;
import com.ruizton.util.Utils;
import com.ruizton.util.XlsExport;

@Controller
public class SalescontractController {
	@Autowired
	private SalescontractService salescontractService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private SalespercentService salespercentService;
	@Autowired
	private UserService userService;
	@Autowired
	private VirtualWalletService virtualWalletService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/salescontractList")
	@RequiresPermissions("ssadmin/salescontractList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/salescontractList") ;
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
		List<Fsalescontract> list = this.salescontractService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("salescontractList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "salescontractList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fsalescontract", filter+""));
		return modelAndView ;
	}
	
	
	@RequestMapping("/ssadmin/goSalescontractJSP")
	public ModelAndView goSalescontractJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fsalescontract salescontract = this.salescontractService.findById(fid);
			modelAndView.addObject("fsalescontract", salescontract);
		}
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/salesContractForbid")
	@RequiresPermissions("ssadmin/salesContractForbid.html")
	public ModelAndView salesContractForbid(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		int uid = Integer.parseInt(request.getParameter("uid"));
		Fsalescontract salescontract = this.salescontractService.findById(uid);
		int nowStatus = salescontract.getFstatus();
		if(nowStatus != SalescontractStatusEnum.SAVE_VALUE){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","只有保存状态的业务员才能审核！");
			return modelAndView;
		}
		
		salescontract.setFstatus(SalescontractStatusEnum.AUDIT_VALUE);
		salescontract.setFcreateTime(Utils.getTimestamp());
		this.salescontractService.updateObj(salescontract);
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","审核成功！");
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/salesContractDisForbid")
	@RequiresPermissions("ssadmin/salesContractDisForbid.html")
	public ModelAndView salesContractDisForbid(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		int uid = Integer.parseInt(request.getParameter("uid"));
		Fsalescontract salescontract = this.salescontractService.findById(uid);
		int nowStatus = salescontract.getFstatus();
		if(nowStatus != SalescontractStatusEnum.AUDIT_VALUE){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","该业务员未审核");
			return modelAndView;
		}
		salescontract.setFstatus(SalescontractStatusEnum.SAVE_VALUE);
		salescontract.setFadmin(null);
		this.salescontractService.updateObj(salescontract);

		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","取消审核成功！");
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/deleteSalespercent")
	@RequiresPermissions("ssadmin/deleteSalespercent.html")
	public ModelAndView deleteSalespercent(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		int uid = Integer.parseInt(request.getParameter("uid"));
		if(this.salescontractService.findById(uid).getFstatus() == SalescontractStatusEnum.AUDIT_VALUE){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","该业务员已审核，无法删除");
			return modelAndView;
		}
		
		this.salescontractService.deleteObj(uid);
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/saveSalescontract")
	@RequiresPermissions("ssadmin/saveSalescontract.html")
	public ModelAndView saveSalescontrace(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		
		int uid = Integer.parseInt(request.getParameter("userLookup.id"));
		String filter = "where fuser.fid="+uid;
		int count = this.adminService.getAllCount("Fsalescontract", filter);
		if(count >0){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","业务员已存在，无法添加");
			return modelAndView;
		}
		
		Fuser user = this.userService.findById(uid);
		int id = Integer.parseInt(request.getParameter("salespercentLookup.id"));
		Fsalespercent salespercent = this.salespercentService.findById(id);
		
		Fadmin admin = (Fadmin)request.getSession().getAttribute("login_admin");
		Fsalescontract salescontract = new Fsalescontract();
		salescontract.setFuser(user);
		salescontract.setFsalespercent(salespercent);
		salescontract.setFgrade(Integer.parseInt(request.getParameter("salespercentLookup.fgrade")));
		salescontract.setFadmin(admin);
		salescontract.setFstatus(SalescontractStatusEnum.SAVE_VALUE);
		salescontract.setFcreateTime(Utils.getTimestamp());
		this.salescontractService.saveObj(salescontract);
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("callbackType","closeCurrent");
		modelAndView.addObject("message","添加成功");
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/updateSalescontract")
	@RequiresPermissions("ssadmin/updateSalescontract.html")
	public ModelAndView updateSalescontract(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		int fid = Integer.parseInt(request.getParameter("fid"));
		int uid = Integer.parseInt(request.getParameter("userLookup.id"));
		
		Fuser user = this.userService.findById(uid);
		Fsalescontract salescontract = salescontractService.findById(fid);
		if(salescontract.getFstatus() == SalescontractStatusEnum.AUDIT_VALUE){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","该业务员已审核，无法修改！");
			return modelAndView;
		}
		
		int id = Integer.parseInt(request.getParameter("salespercentLookup.id"));
		Fsalespercent salespercent = this.salespercentService.findById(id);
		salescontract.setFgrade(Integer.parseInt(request.getParameter("salespercentLookup.fgrade")));
		salescontract.setFsalespercent(salespercent);
		salescontract.setFuser(user);
		salescontract.setFcreateTime(Utils.getTimestamp());
		this.salescontractService.updateObj(salescontract);
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("callbackType","closeCurrent");
		modelAndView.addObject("message","更新成功");
		return modelAndView;
	}
	
	private static enum ExportFiled {
		会员登陆名,真实姓名,等级,级别类型,状态,创建时间;
	}
	
	public List<Fsalescontract> salesList(HttpServletRequest request) throws Exception{
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
		List<Fsalescontract> list = this.salescontractService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
        return list;
	}

	@RequestMapping("ssadmin/salescontractExport")
	@RequiresPermissions("ssadmin/salescontractExport.html")
	public ModelAndView salescontractExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		response.setContentType("Application/excel");
		response.addHeader("Content-Disposition",
				"attachment;filename=salesList.xls");
		XlsExport e = new XlsExport();
		int rowIndex = 0;

		// header
		e.createRow(rowIndex++);
		for (ExportFiled filed : ExportFiled.values()) {
			e.setCell(filed.ordinal(), filed.toString());
		}

		List<Fsalescontract> fsalescontracts = salesList(request);
		for (Fsalescontract fsalescontract : fsalescontracts) {
			e.createRow(rowIndex++);
			for (ExportFiled filed : ExportFiled.values()) {
				switch (filed) {
				case 会员登陆名:
					e.setCell(filed.ordinal(), fsalescontract.getFuser().getFloginName());
					break;
				case 真实姓名:
					e.setCell(filed.ordinal(), fsalescontract.getFuser().getFrealName());
					break;
				case 等级:
					e.setCell(filed.ordinal(), fsalescontract.getFsalespercent().getFlevel());
					break;
				case 级别类型:
					e.setCell(filed.ordinal(), fsalescontract.getFgrade_s());
					break;
				case 状态:
					e.setCell(filed.ordinal(),fsalescontract.getFstatus_s());
					break;
				case 创建时间:
					e.setCell(filed.ordinal(), fsalescontract.getFcreateTime());
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
}
