package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.ApplyinfoStatusEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fapplyinfo;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.ApplyinfoService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ApplyinfoController extends BaseController {
	@Autowired
	private ApplyinfoService applyinfoService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/applyinfoList")
	@RequiresPermissions("ssadmin/applyinfoList.html")
	public ModelAndView applyinfoList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/applyinfoList") ;
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
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (fuser.floginName like '%" + keyWord + "%' or \n");
			filter.append("fuser.fnickName like '%" + keyWord + "%'  or \n");
			filter.append("fuser.frealName like '%" + keyWord + "%'  or \n");
			filter.append("fuser.ftelephone like '%" + keyWord + "%'  or \n");
			filter.append("fuser.femail like '%" + keyWord + "%'  or \n");
			filter.append("fuser.fidentityNo like '%" + keyWord + "%' )\n");
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
		List<Fapplyinfo> list = this.applyinfoService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("applyinfoList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "applyinfoList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fapplyinfo", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goApplyJSP")
	public ModelAndView goApplyJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fapplyinfo applyinfo = this.applyinfoService.findById(fid);
			modelAndView.addObject("applyinfo", applyinfo);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/auditApplyinfo")
	@RequiresPermissions("ssadmin/auditApplyinfo.html")
	public ModelAndView auditApplyinfo(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		int fid = Integer.parseInt(request.getParameter("uid"));
		Fapplyinfo applyinfo = this.applyinfoService.findById(fid);
		
		if(applyinfo.getFstatus() != ApplyinfoStatusEnum.WAIT){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","处笔申请已处理！");
			return modelAndView;
		}
		
		applyinfo.setFstatus(ApplyinfoStatusEnum.SUCCESS);
		
		Fuser fuser = applyinfo.getFuser();
		fuser.setFgrade(applyinfo.getFgrade());
		try {
			this.applyinfoService.updateAuditApplyinfo(fuser, applyinfo);
		} catch (Exception e) {
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","网络异常");
			return modelAndView;
		}
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","操作成功");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/rejectApplyinfo")
	@RequiresPermissions("ssadmin/rejectApplyinfo.html")
	public ModelAndView rejectApplyinfo(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		int fid = Integer.parseInt(request.getParameter("uid"));
		String reason = request.getParameter("freason");
		Fapplyinfo applyinfo = this.applyinfoService.findById(fid);
		
		if(applyinfo.getFstatus() != ApplyinfoStatusEnum.WAIT){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","处笔申请已处理！");
			return modelAndView;
		}
		
		applyinfo.setFstatus(ApplyinfoStatusEnum.REJECT);
		applyinfo.setFrejectReason(reason);
		this.applyinfoService.updateObj(applyinfo);
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","操作成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
}
