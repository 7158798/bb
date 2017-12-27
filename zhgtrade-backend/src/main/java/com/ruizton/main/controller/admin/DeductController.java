package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.DeductStatusEnum;
import com.ruizton.main.Enum.MessageStatusEnum;
import com.ruizton.main.model.*;
import com.ruizton.main.service.admin.*;
import com.ruizton.util.MathUtils;
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

//import javax.enterprise.inject.Model;

@Controller
public class DeductController {
	@Autowired
	private DeductlogService deductlogService;
	@Autowired
	private DeductService deductService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private VirtualWalletService virtualWalletService;
	@Autowired
	private WalletService walletService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("ssadmin/deductList")
	@RequiresPermissions("ssadmin/deductList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/deductList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String logDate = request.getParameter("logDate");
		String fstatus = request.getParameter("fstatus");
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
		if(logDate != null && logDate.trim().length() >0){
			filter.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') = '"+logDate+"' \n");
			modelAndView.addObject("logDate", logDate);
		}
		if(fstatus != null && fstatus.trim().length() >0){
			if(!fstatus.equals("0")){
				filter.append(" and fstatus="+fstatus+" \n");
			}
			modelAndView.addObject("fstatus", fstatus);
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
		Map map = new HashMap();
		map.put(0, "全部");
		map.put(DeductStatusEnum.WAIT_VALUE,DeductStatusEnum.getEnumString(DeductStatusEnum.WAIT_VALUE));
		map.put(DeductStatusEnum.AUDIT_VALUE,DeductStatusEnum.getEnumString(DeductStatusEnum.AUDIT_VALUE));
		map.put(DeductStatusEnum.COMPLETE_VALUE,DeductStatusEnum.getEnumString(DeductStatusEnum.COMPLETE_VALUE));
		modelAndView.addObject("statusMap",map);
		List<Fdeduct> list = this.deductService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("deductList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "deductList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fdeduct", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goDeductDetailJSP")
	public ModelAndView goDeductDetailJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			List<Fdeductlog> list = this.deductlogService.findByProperty("fdeductId", fid);
			modelAndView.addObject("fdeduct", list);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/sendDeduct")
	@RequiresPermissions("ssadmin/sendDeduct.html")
	public ModelAndView sendDeduct(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		String ids = request.getParameter("ids");
		String[] idString = ids.split(",");
		boolean flag = false;
		for(int i=0;i<idString.length;i++){
			int fid = Integer.parseInt(idString[i]);
			Fdeduct deduct = this.deductService.findById(fid);
			int uid = deduct.getFuser().getFid();
			double ftotalAmt = deduct.getFtotalAmt();
			double ftotalQty = deduct.getFtotalQty();
			Fwallet wallet = deduct.getFuser().getFwallet();
			if(deduct.getFstatus() != DeductStatusEnum.AUDIT_VALUE){
				flag = true;
				continue;
			}
			deduct.setFstatus(DeductStatusEnum.COMPLETE_VALUE);
			Fmessage message = new Fmessage();
			message.setFtitle("业务提成发放通知");
			message.setFcontent("您的业务提成已到账！"+"金豆数量:"+ftotalQty+"，人民币金额："+ftotalAmt);
			message.setFcreateTime(Utils.getTimestamp());
			message.setFstatus(MessageStatusEnum.NOREAD_VALUE);
			message.setFreceiver(deduct.getFuser());
			
			String sql = "where fvirtualcointype.fid=1 and fuser.fid="+uid;
			List<Fvirtualwallet> alls = this.virtualWalletService.list(0, 0, sql, false);
			Fvirtualwallet virtualwallet = alls.get(0);
			virtualwallet.setFtotal(MathUtils.add(virtualwallet.getFtotal(), ftotalQty));
			
			wallet.setFtotalRmb(MathUtils.add(wallet.getFtotalRmb(), ftotalAmt));
			try {
				this.deductService.updatelog(message, deduct, virtualwallet, wallet);
			} catch (Exception e) {
				flag = true;
				continue;
			}
		}
		String msg = "";
		if(flag){
			msg = "部分发奖失败，请重新发奖";
		}else{
			msg = "发奖成功";
		}
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", msg);
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/examineDeduct")
	@RequiresPermissions("ssadmin/examineDeduct.html")
	public ModelAndView examineDeduct(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		String ids = request.getParameter("ids");
		String[] idString = ids.split(",");
		boolean flag = false;
		for(int i=0;i<idString.length;i++){
			int fid = Integer.parseInt(idString[i]);
			Fdeduct deduct = this.deductService.findById(fid);
			if(deduct.getFstatus() != DeductStatusEnum.WAIT_VALUE){
				flag = true;
				continue;
			}
			deduct.setFstatus(DeductStatusEnum.AUDIT_VALUE);
			deduct.setFaudittime(Utils.getTimestamp());
			deduct.setFadmin((Fadmin)request.getSession().getAttribute("login_admin"));
			this.deductService.updateObj(deduct);
		}
		String msg = "";
		if(flag){
			msg = "部分审核失败，请重新审核";
		}else{
			msg = "审核成功";
		}
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", msg);
		return modelAndView;
	}

	@RequestMapping("/ssadmin/deductTotalList")
	@RequiresPermissions("ssadmin/deductTotalList.html")
	public ModelAndView deductTotalList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/deductTotalList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String startDate = request.getParameter("startDate");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(startDate != null && startDate.trim().length() >0){
			filter.append("and date_format(fchargeDate,'%Y-%m-%d')='"+startDate+"' \n");
			modelAndView.addObject("startDate", startDate);
		}
		List list = this.deductService.deductTotalList((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("deductTotalList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "deductTotalList");
		//总数量
		modelAndView.addObject("totalCount",this.deductService.deductTotalList((currentPage-1)*numPerPage, numPerPage, filter+"",false).size());
		return modelAndView ;
	}
}
