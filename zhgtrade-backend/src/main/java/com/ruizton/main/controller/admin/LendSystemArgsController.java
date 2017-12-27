package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Flendsystemargs;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.LendSystemArgsService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LendSystemArgsController extends BaseController {
	@Autowired
	private LendSystemArgsService lendSystemArgsService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/lendSystemArgsList")
	@RequiresPermissions("ssadmin/lendSystemArgsList.html")
	public ModelAndView lendSystemArgsList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/lendSystemArgsList") ;
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
			filter.append("and fkey like '%"+keyWord+"%' \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}
		List<Flendsystemargs> list = this.lendSystemArgsService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("lendSystemArgsList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "lendSystemArgsList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Flendsystemargs", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goLendsystemargsJSP")
	public ModelAndView goLendsystemargsJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Flendsystemargs lendsystemargs = this.lendSystemArgsService.findById(fid);
			modelAndView.addObject("lendsystemargs", lendsystemargs);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateLendsystemargs")
	@RequiresPermissions("ssadmin/updateLendsystemargs.html")
	public ModelAndView updateLendsystemargs(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
        int fid = Integer.parseInt(request.getParameter("fid"));
        Flendsystemargs lendsystemargs = this.lendSystemArgsService.findById(fid);
        String fvalue = request.getParameter("fvalue");
        String fdesc = request.getParameter("fdesc");
        lendsystemargs.setFdesc(fdesc);
        lendsystemargs.setFvalue(fvalue);
        this.lendSystemArgsService.updateObj(lendsystemargs);
			
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
