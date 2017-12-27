package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.LendRuleTypeEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Flendrule;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.LendRuleService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LendRuleController extends BaseController {
	@Autowired
	private LendRuleService lendRuleService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/lendRuleList")
	@RequiresPermissions("ssadmin/lendRuleList.html")
	public ModelAndView lendRuleList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/lendRuleList") ;
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
			int type = Integer.parseInt(keyWord);
			if(type != 0){
				filter.append("and ftype="+type+" \n");
			}
			modelAndView.addObject("keywords", type);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}
		
		List<Flendrule> list = this.lendRuleService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("lendRuleList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "lendRuleList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Flendrule", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goLendRuleJSP")
	public ModelAndView goLendRuleJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Flendrule lendrule = this.lendRuleService.findById(fid);
			modelAndView.addObject("lendrule", lendrule);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateLendRule")
	@RequiresPermissions("ssadmin/updateLendRule.html")
	public ModelAndView updateLendRule(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
        int fid = Integer.parseInt(request.getParameter("fid"));
        Flendrule lendrule = this.lendRuleService.findById(fid);
        if(lendrule.getFtype() == LendRuleTypeEnum.LEND_TIME){
        	modelAndView.addObject("statusCode",300);
    		modelAndView.addObject("message","此类型不允许修改!");
    		return modelAndView;
        }
        lendrule.setFamount(Double.valueOf(request.getParameter("famount")));
        lendrule.setFrate(Double.valueOf(request.getParameter("frate")));
        this.lendRuleService.updateObj(lendrule);
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
