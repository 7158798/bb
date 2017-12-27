package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fpopcorn;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.PopcornService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PopcornController extends BaseController {
	@Autowired
	private PopcornService popcornService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/popcornList")
	@RequiresPermissions("ssadmin/popcornList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/popcornList") ;
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
			filter.append("and ftime like '%"+keyWord+"%' \n");
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
		List<Fpopcorn> list = this.popcornService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("popcornList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "popcornList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fpopcorn", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goPopcornJSP")
	public ModelAndView goAboutJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fpopcorn popcorn = this.popcornService.findById(fid);
			modelAndView.addObject("fpopcorn", popcorn);
		}
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/updatePopcorn")
	@RequiresPermissions("ssadmin/updatePopcorn.html")
	public ModelAndView updatePopcorn(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		
		int fid = Integer.parseInt(request.getParameter("fid"));
		int ftime = Integer.parseInt(request.getParameter("ftime"));
		int fspantime = Integer.parseInt(request.getParameter("fspantime"));
		double frate = Double.valueOf(request.getParameter("frate"));
		double fbetRate = Double.valueOf(request.getParameter("fbetRate"));
		if(frate > 100){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","手续费比例不能大于100");
			return modelAndView;
		}
		String ftitle = request.getParameter("ftitle");
		
		Fpopcorn popcorn = this.popcornService.findById(fid);
		popcorn.setFtime(ftime);
		popcorn.setFtitle(ftitle);
		popcorn.setFrate(frate);
		popcorn.setFbetRate(fbetRate);
		popcorn.setFspantime(fspantime);
		popcorn.setFcreatetime(Utils.getTimestamp());
		
		this.popcornService.updateObj(popcorn);
		
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;		
	}
	
}
