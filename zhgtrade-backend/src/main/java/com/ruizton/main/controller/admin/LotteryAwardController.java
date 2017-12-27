package com.ruizton.main.controller.admin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.Enum.LotteryRewardTypeEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Flotteryaward;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.LotteryawardService;
import com.ruizton.util.Utils;

@Controller
public class LotteryAwardController extends BaseController {
	@Autowired
	private LotteryawardService lotteryawardService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/lotteryawardList")
	@RequiresPermissions("ssadmin/lotteryawardList.html")
	public ModelAndView lotteryawardList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/lotteryawardList") ;
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
			filter.append("and fname like '%"+keyWord+"%' \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}
		
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}
		
		List<Flotteryaward> list = this.lotteryawardService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("lotteryawardList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "lotteryawardList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fabout", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goLotteryawardJSP")
	public ModelAndView goLotteryawardJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Flotteryaward lotteryaward = this.lotteryawardService.findById(fid);
			modelAndView.addObject("lotteryaward", lotteryaward);
		}
		Map map = new HashMap();
		map.put(LotteryRewardTypeEnum.SHIWU, LotteryRewardTypeEnum.getEnumString(LotteryRewardTypeEnum.SHIWU));
		map.put(LotteryRewardTypeEnum.XUNIBI, LotteryRewardTypeEnum.getEnumString(LotteryRewardTypeEnum.XUNIBI));
		modelAndView.addObject("map", map);
		
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateLotteryaward")
	@RequiresPermissions("ssadmin/updateLotteryaward.html")
	public ModelAndView updateLotteryaward(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        int fid = Integer.parseInt(request.getParameter("fid"));
        Flotteryaward lotteryaward = this.lotteryawardService.findById(fid);
        lotteryaward.setFchance(new BigDecimal(request.getParameter("fchance")));
        lotteryaward.setFname(request.getParameter("fname"));
        lotteryaward.setCount(Double.valueOf(request.getParameter("fqty")));
        lotteryaward.setFtotal(Double.valueOf(request.getParameter("ftotal")));
        lotteryaward.setType(Integer.parseInt(request.getParameter("ftype")));
        this.lotteryawardService.updateObj(lotteryaward);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/saveLotteryaward")
	@RequiresPermissions("ssadmin/saveLotteryaward.html")
	public ModelAndView saveLotteryaward(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        Flotteryaward lotteryaward = new Flotteryaward();
        lotteryaward.setFchance(new BigDecimal(request.getParameter("fchance")));
        lotteryaward.setFname(request.getParameter("fname"));
        lotteryaward.setFtotal(Double.valueOf(request.getParameter("ftotal")));
        lotteryaward.setCount(Double.valueOf(request.getParameter("fqty")));
        lotteryaward.setType(Integer.parseInt(request.getParameter("ftype")));
        this.lotteryawardService.updateObj(lotteryaward);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","保存成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteLotteryaward")
	@RequiresPermissions("ssadmin/deleteLotteryaward.html")
	public ModelAndView deleteLotteryaward(HttpServletRequest request) throws Exception {
		int fid = Integer.parseInt(request.getParameter("uid"));
		this.lotteryawardService.deleteObj(fid);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "删除成功");
		return modelAndView;
	}
}
