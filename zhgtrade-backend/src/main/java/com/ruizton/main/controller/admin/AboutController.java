package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fabout;
import com.ruizton.main.service.admin.AboutService;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AboutController extends BaseController {
	@Autowired
	private AboutService aboutService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/aboutList")
	@RequiresPermissions("ssadmin/aboutList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/aboutList") ;
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
		}else{
			filter.append("order by fid \n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		List<Fabout> list = this.aboutService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("aboutList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "aboutList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fabout", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goAboutJSP")
	public ModelAndView goAboutJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fabout about = this.aboutService.findById(fid);
			modelAndView.addObject("fabout", about);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateAbout")
	@RequiresPermissions("ssadmin/updateAbout.html")
	public ModelAndView updateAbout(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        int fid = Integer.parseInt(request.getParameter("fid"));
        Fabout about = this.aboutService.findById(fid);
        about.setFcontent(request.getParameter("fcontent"));
        about.setFtitle(request.getParameter("ftitle"));
        this.aboutService.updateObj(about);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
