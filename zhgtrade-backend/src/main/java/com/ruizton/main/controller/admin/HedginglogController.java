package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fhedginglog;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.HedginglogService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.util.StringUtils;
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
public class HedginglogController extends BaseController {
	@Autowired
	private HedginglogService hedginglogService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private VirtualCoinService virtualCoinService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/hedginglogList")
	@RequiresPermissions("ssadmin/hedginglogList.html")
	public ModelAndView hedginglogList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/hedginglogList") ;
		//当前页
		int currentPage = 1;
		String keyWord = request.getParameter("keywords");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		
		if (StringUtils.hasText(request.getParameter("ftype"))) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filter.append("and fhedging.fvirtualcointypeByFid.fid=" + type + "\n");
			}
			modelAndView.addObject("ftype", type);
		} else {
			modelAndView.addObject("ftype", 0);
		}
		
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and fnumber ='"+keyWord+"' \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		List<Fhedginglog> list = this.hedginglogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		
		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);
		
		modelAndView.addObject("hedginglogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "hedginglogList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fhedginglog", filter+""));
		return modelAndView ;
	}
	
}
