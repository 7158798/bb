package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.MessageStatusEnum;
import com.ruizton.main.Enum.PayCodeStatusEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fpaycode;
import com.ruizton.main.model.Fproxy;
import com.ruizton.main.model.Fwallet;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.PaycodeService;
import com.ruizton.main.service.admin.SystemArgsService;
import com.ruizton.main.service.admin.WalletService;
import com.ruizton.util.MathUtils;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
public class PaycodeController extends BaseController {
	@Autowired
	private PaycodeService paycodeService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private WalletService walletService;
	@Autowired
	private SystemArgsService systemArgsService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/paycodeList")
	@RequiresPermissions("ssadmin/paycodeList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/paycodeList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		String fcreatetime = request.getParameter("fcreatetime");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and fuser.floginName like '%"+keyWord+"%' \n");
			filter.append("or fuser.fnickName like '%"+keyWord+"%' \n");
			filter.append("or fuser.frealName like '%"+keyWord+"%' \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(fcreatetime != null && fcreatetime.trim().length() >0){
			filter.append("and fcreatetime like '%"+fcreatetime+"%' \n");
			modelAndView.addObject("fcreatetime", fcreatetime);
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
		List<Fpaycode> list = this.paycodeService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("paycodeList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "paycodeList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fpaycode", filter+""));
		return modelAndView ;
	}
	
	
	@RequestMapping("ssadmin/goPaycodeJSP")
	public ModelAndView goPaycodeJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fpaycode paycode = this.paycodeService.findById(fid);
			modelAndView.addObject("fpaycode", paycode);
		}
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/savePaycode")
	@RequiresPermissions("ssadmin/savePaycode.html")
	public ModelAndView updateAbout(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        double famount = Double.valueOf(request.getParameter("famount")).doubleValue();
        Fpaycode paycode = new Fpaycode();
        paycode.setFamount(famount);
        paycode.setFkey(UUID.randomUUID().toString());
        paycode.setFvalue(UUID.randomUUID().toString());
        paycode.setFstatus(PayCodeStatusEnum.PAYCODE_CREATE);
        paycode.setFcreatetime(Utils.getTimestamp());
        
        this.paycodeService.saveObj(paycode);
        
        
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","生成成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/sendPaycode")
	@RequiresPermissions("ssadmin/sendPaycode.html")
	public ModelAndView sendPaycode(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		String ids = request.getParameter("ids");
		String[] idString = ids.split(",");
		
		boolean isCan = false ;
		for (int i = 0; i < idString.length; i++) {
			int fid = Integer.parseInt(idString[i]);
			Fpaycode paycode = this.paycodeService.findById(fid);
			if(paycode.getFstatus()!=PayCodeStatusEnum.PAYCODE_USER_CONFIRM){
				isCan = true ;
				break ;
			}
		}
		if(isCan==true){
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "只有用户确认后的记录才能审核.");
			return modelAndView ;
		}
		
		boolean flag = false;
		for (int i = 0; i < idString.length; i++) {
			int fid = Integer.parseInt(idString[i]);
			Fpaycode paycode = this.paycodeService.findById(fid);
			if (paycode.getFstatus() != PayCodeStatusEnum.PAYCODE_USER_CONFIRM) {
				flag = true;
				continue;
			}
			paycode.setFstatus(PayCodeStatusEnum.PAYCODE_SUCCESS);
			paycode.setFadminTime(Utils.getTimestamp());
			Fmessage message = new Fmessage();
			message.setFtitle("支付码充值到账通知");
			message.setFcontent("支付码充值：" + paycode.getFamount() + "到账，请注意查收!");
			message.setFcreateTime(Utils.getTimestamp());
			message.setFstatus(MessageStatusEnum.NOREAD_VALUE);
			message.setFreceiver(paycode.getFuser());
			
			double qty = paycode.getFamount();
			String sql = "where fid="+ paycode.getFuser().getFwallet().getFid();
			List<Fwallet> alls = this.walletService.list(0, 0,sql, false);
			Fwallet wallet = alls.get(0);
			
			double total = Utils.getDouble(qty, 4);
			wallet.setFtotalRmb(MathUtils.add(wallet.getFtotalRmb(), total));
			try {
				this.paycodeService.updatelog(message, paycode,wallet);
			} catch (Exception e) {
				flag = true;
				continue;
			}
		}
		String msg = "";
		if (flag) {
			msg = "部分审核失败，请重新审核";
		} else {
			msg = "审核成功";
		}
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", msg);
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/cancelPaycode")
	@RequiresPermissions("ssadmin/cancelPaycode.html")
	public ModelAndView cancelPaycode(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		String ids = request.getParameter("ids");
		String[] idString = ids.split(",");
		
		boolean isCan = false ;
		for (int i = 0; i < idString.length; i++) {
			int fid = Integer.parseInt(idString[i]);
			Fpaycode paycode = this.paycodeService.findById(fid);
			if(paycode.getFstatus()!=PayCodeStatusEnum.PAYCODE_CREATE&&paycode.getFstatus()!=PayCodeStatusEnum.PAYCODE_USER_CONFIRM){
				isCan = true ;
				break ;
			}
		}
		if(isCan==true){
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "只有刚创建或者没有确定充值的记录才能取消.");
			return modelAndView ;
		}
		
		boolean flag = false;
		for (int i = 0; i < idString.length; i++) {
			int fid = Integer.parseInt(idString[i]);
			Fpaycode paycode = this.paycodeService.findById(fid);
			if (paycode.getFstatus() != PayCodeStatusEnum.PAYCODE_SUCCESS) {
				paycode.setFstatus(PayCodeStatusEnum.PAYCODE_FAILURE);
			}
			
			try {
				this.paycodeService.updateObj(paycode) ;
			} catch (Exception e) {
				flag = true;
			}
		}
		String msg = "";
		if (flag) {
			msg = "部分取消失败";
		} else {
			msg = "取消成功";
		}
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", msg);
		return modelAndView;
	}
	
	
	@RequestMapping("/ssadmin/proxyList")
	@RequiresPermissions("ssadmin/proxyList.html")
	public ModelAndView proxyList() throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/proxyList") ;
		StringBuffer filter = new StringBuffer("");
		List<Fproxy> list = this.paycodeService.listFproxy() ;
		modelAndView.addObject("proxyList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", 1);
		modelAndView.addObject("rel", "proxyList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fproxy", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goProxyJSP")
	public ModelAndView goProxyJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fproxy fproxy = this.paycodeService.findFproxyById(fid) ;
			modelAndView.addObject("fproxy", fproxy);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/saveProxy")
	@RequiresPermissions("ssadmin/saveProxy.html")
	public ModelAndView saveProxy(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        
		Fproxy fproxy = new Fproxy() ;
		fproxy.setName(request.getParameter("name")) ;
		fproxy.setAmount(request.getParameter("amount")) ;
		fproxy.setQq(request.getParameter("qq")) ;
		fproxy.setRealname(request.getParameter("realname")) ;
		fproxy.setAccount(request.getParameter("account")) ;
		this.paycodeService.saveFproxy(fproxy) ;
        
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","增加成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateProxy")
	@RequiresPermissions("ssadmin/updateProxy.html")
	public ModelAndView updateProxy(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        
		Fproxy fproxy = this.paycodeService.findFproxyById(Integer.parseInt(request.getParameter("uid"))) ;
		fproxy.setName(request.getParameter("name")) ;
		fproxy.setAmount(request.getParameter("amount")) ;
		fproxy.setQq(request.getParameter("qq")) ;
		fproxy.setRealname(request.getParameter("realname")) ;
		fproxy.setAccount(request.getParameter("account")) ;
		this.paycodeService.updateFproxy(fproxy) ;
        
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteProxy")
	@RequiresPermissions("ssadmin/deleteProxy.html")
	public ModelAndView deleteProxy(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        
		Fproxy fproxy = this.paycodeService.findFproxyById(Integer.parseInt(request.getParameter("uid"))) ;
		this.paycodeService.deleteFproxy(fproxy) ;
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");
//		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
