package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.LinkTypeEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Ffriendlink;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.FriendLinkService;
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

@Controller
public class FriendLinkController extends BaseController {
	@Autowired
	private FriendLinkService friendLinkService ;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/linkList")
	@RequiresPermissions("ssadmin/linkList.html")
	public ModelAndView linkList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/linkList") ;
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
        filter.append("where ftype="+LinkTypeEnum.LINK_VALUE+" \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (fname like '%"+keyWord+"%' or \n");
			filter.append("furl like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by fcreateTime \n");
		}
		
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		List<Ffriendlink> list = this.friendLinkService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("linkList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "linkList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Ffriendlink", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goLinkJSP")
	public ModelAndView goLinkJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Ffriendlink friendlink = this.friendLinkService.findById(fid);
			modelAndView.addObject("friendlink", friendlink);
		}
		Map map = new HashMap();
		map.put(LinkTypeEnum.LINK_VALUE, LinkTypeEnum.getEnumString(LinkTypeEnum.LINK_VALUE));
		modelAndView.addObject("linkTypeMap", map);
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/saveLink")
	@RequiresPermissions("ssadmin/saveLink.html")
	public ModelAndView saveLink(HttpServletRequest request) throws Exception{
		Ffriendlink link = new Ffriendlink();
		link.setFdescription(request.getParameter("link.fdescription"));
		link.setFname(request.getParameter("link.fname"));
		link.setForder(Integer.parseInt(request.getParameter("link.forder")));
		link.setFurl(request.getParameter("link.furl"));
		link.setFcreateTime(Utils.getTimestamp());
		link.setFtype(LinkTypeEnum.LINK_VALUE);
		this.friendLinkService.saveObj(link);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteLink")
	@RequiresPermissions("ssadmin/deleteLink.html")
	public ModelAndView deleteLink(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("uid"));
		this.friendLinkService.deleteObj(fid);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/updateLink")
	@RequiresPermissions("ssadmin/updateLink.html")
	public ModelAndView updateLink(HttpServletRequest request) throws Exception{
		Ffriendlink link = this.friendLinkService.findById(Integer.parseInt(request.getParameter("link.fid")));
		link.setFdescription(request.getParameter("link.fdescription"));
		link.setFname(request.getParameter("link.fname"));
		link.setForder(Integer.parseInt(request.getParameter("link.forder")));
		link.setFurl(request.getParameter("link.furl"));
		this.friendLinkService.updateObj(link);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
