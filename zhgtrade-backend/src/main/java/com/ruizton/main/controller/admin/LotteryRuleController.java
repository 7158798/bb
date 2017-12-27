package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Flotteryrule;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.LotteryRuleService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LotteryRuleController extends BaseController {
	@Autowired
	private LotteryRuleService lotteryRuleService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private VirtualCoinService virtualCoinService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/lotteryRuleList")
	@RequiresPermissions("ssadmin/lotteryRuleList.html")
	public ModelAndView lotteryRuleList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/lotteryRuleList") ;
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
			filter.append("and ftitle like '%"+keyWord+"%' \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}
		List<Flotteryrule> list = this.lotteryRuleService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("lotteryRuleList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "lotteryRuleList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Flotteryrule", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goLotteryRuleJSP")
	public ModelAndView goLotteryRuleJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Flotteryrule lotteryrule = this.lotteryRuleService.findById(fid);
			modelAndView.addObject("lotteryrule", lotteryrule);
		}
		
		List<Fvirtualcointype> allType = this.virtualCoinService.findAll();
		modelAndView.addObject("allType", allType);
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteLotteryRule")
	@RequiresPermissions("ssadmin/deleteLotteryRule.html")
	public ModelAndView deleteLotteryRule(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("uid"));
		this.lotteryRuleService.deleteObj(fid);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateLotteryRule")
	@RequiresPermissions("ssadmin/updateLotteryRule.html")
	public ModelAndView updateLotteryRule(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
        int fid = Integer.parseInt(request.getParameter("fid"));
        Flotteryrule lotteryrule = this.lotteryRuleService.findById(fid);
        double fqty = Double.valueOf(request.getParameter("fqty"));
		String ftitle = request.getParameter("ftitle");
		int vid = Integer.parseInt(request.getParameter("vid"));
	    lotteryrule.setFvirtualCoinTypeId(this.virtualCoinService.findById(vid));
        lotteryrule.setFqty(fqty);
        lotteryrule.setFtitle(ftitle);
        lotteryrule.setFplayEggTimes(Integer.parseInt(request.getParameter("fplayEggTimes")));
        lotteryrule.setFscore(Double.valueOf(request.getParameter("fscore")));
        this.lotteryRuleService.updateObj(lotteryrule);
        
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/saveLotteryRule")
	@RequiresPermissions("ssadmin/saveLotteryRule.html")
	public ModelAndView saveLotteryRule(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        Flotteryrule lotteryrule = new Flotteryrule();
        double fqty = Double.valueOf(request.getParameter("fqty"));
		String ftitle = request.getParameter("ftitle");
        int vid = Integer.parseInt(request.getParameter("vid"));
        lotteryrule.setFvirtualCoinTypeId(this.virtualCoinService.findById(vid));
        lotteryrule.setFqty(fqty);
        lotteryrule.setFtitle(ftitle);
        lotteryrule.setFcreateTime(Utils.getTimestamp());
        this.lotteryRuleService.saveObj(lotteryrule);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
