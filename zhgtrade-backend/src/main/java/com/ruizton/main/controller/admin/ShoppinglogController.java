package com.ruizton.main.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.Enum.MessageStatusEnum;
import com.ruizton.main.Enum.ShoppinglogStatusEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fshoppinglog;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fwallet;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.ShoppinglogService;
import com.ruizton.main.service.admin.SystemArgsService;
import com.ruizton.main.service.admin.UserService;
import com.ruizton.util.MathUtils;
import com.ruizton.util.Utils;

@Controller
public class ShoppinglogController extends BaseController {
	@Autowired
	private ShoppinglogService shoppinglogService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private SystemArgsService systemArgsService;
	@Autowired
	private UserService userService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/shoppinglogList")
	@RequiresPermissions("ssadmin/shoppinglogList.html")
	public ModelAndView shoppinglogList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/shoppinglogList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String status = request.getParameter("status");
		String supplierNo = request.getParameter("supplierNo");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		
		if(status != null && status.trim().length() >0){
			filter.append("and fstatus = "+status+" \n");
			modelAndView.addObject("status", status);
		}else{
			modelAndView.addObject("status", 0);
		}
		
		if(supplierNo != null && supplierNo.trim().length() >0){
			filter.append("and fgoods.fsupplierNo like '%"+supplierNo+"%' \n");
			modelAndView.addObject("supplierNo", supplierNo);
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
		
		Map<Integer,String> statusMap = new HashMap<Integer,String>();
		for(int i=1;i<=5;i++){
			statusMap.put(i, ShoppinglogStatusEnum.getEnumString(i));
		}
		statusMap.put(0,"全部");
		modelAndView.addObject("statusMap",statusMap);
		
		List<Fshoppinglog> list = this.shoppinglogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("shoppinglogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "shoppinglogList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fshoppinglog", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goShoppinglogJSP")
	public ModelAndView goShoppinglogJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fshoppinglog shoppinglog = this.shoppinglogService.findById(fid);
			modelAndView.addObject("shoppinglog", shoppinglog);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/sendShoppinglog")
	@RequiresPermissions("ssadmin/sendShoppinglog.html")
	public ModelAndView sendShoppinglog(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
        int fid = Integer.parseInt(request.getParameter("fid"));
        Fshoppinglog shoppinglog = this.shoppinglogService.findById(fid);
        if(shoppinglog.getFstatus() != ShoppinglogStatusEnum.PAY){
        	modelAndView.addObject("statusCode",300);
    		modelAndView.addObject("message","只有状态为已付款的订单记录才允许发货！");
    		return modelAndView;
        }
        
        boolean isVirtual = shoppinglog.getFgoods().getFisVirtual();
        if(isVirtual){
        	String fexpressNo = request.getParameter("fexpressNo");
        	if(fexpressNo == null || fexpressNo.trim().length() == 0){
        		modelAndView.addObject("statusCode",300);
        		modelAndView.addObject("message","实物订单快递单号不能为空！");
        		return modelAndView;
        	}
        	shoppinglog.setFexpressNo(fexpressNo);
        }else{
            shoppinglog.setFpasswordA(Utils.getRandomString(8));
            shoppinglog.setFpasswordB(Utils.getRandomString(8));
        }
        
        shoppinglog.setFsendtime(Utils.getTimestamp());
        shoppinglog.setFstatus(ShoppinglogStatusEnum.SEND);
        
        int userId = shoppinglog.getFuser().getFid();
        Fuser fuser = this.userService.findById(userId);
        Fwallet fwallet = fuser.getFwallet();
        double expressAmt = Double.valueOf(this.systemArgsService.getValue("expressAmt"));
        if(fwallet.getFtotalRmb() < expressAmt){
        	modelAndView.addObject("statusCode",300);
    		modelAndView.addObject("message","会员余额不足，扣快递费不成功，发货失败！");
    		return modelAndView;
        }
        fwallet.setFtotalRmb(MathUtils.subtract(fwallet.getFtotalRmb(), expressAmt));
		String content = "";
		String title = "";
		content = "您在商城购买的商品已发货，扣除快递费用￥"+expressAmt+",请知悉！";
		title = "商城发货通知";
		
		Fmessage message = new Fmessage();
		message.setFcontent(content);
		message.setFtitle(title);
		message.setFcreateTime(Utils.getTimestamp());
		message.setFstatus(MessageStatusEnum.NOREAD_VALUE);
		message.setFreceiver(fuser);
		
		
        try {
			this.shoppinglogService.updateSendObj(shoppinglog, message, fwallet);
		} catch (Exception e) {
			modelAndView.addObject("statusCode",300);
    		modelAndView.addObject("message","网络异常，发货失败！");
    		return modelAndView;
		}
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","发货成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/chargeShoppinglog")
	@RequiresPermissions("ssadmin/chargeShoppinglog.html")
	public ModelAndView chargeShoppinglog(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fshoppinglog shoppinglog = this.shoppinglogService.findById(fid);
        if(shoppinglog.getFstatus() != ShoppinglogStatusEnum.SEND 
        		&&shoppinglog.getFstatus() != ShoppinglogStatusEnum.USED){
    		modelAndView.addObject("statusCode",300);
    		modelAndView.addObject("message","只有已使用或已发货的订单记录才允许结算");
    		return modelAndView;
        }
        shoppinglog.setFstatus(ShoppinglogStatusEnum.CHARGE);
        this.shoppinglogService.updateObj(shoppinglog);

		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","结算成功");
		return modelAndView;
	}
}
