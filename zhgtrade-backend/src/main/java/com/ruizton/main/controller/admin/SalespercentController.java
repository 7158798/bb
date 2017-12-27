package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.UserGradeEnum;
import com.ruizton.main.model.Fsalespercent;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.SalespercentService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SalespercentController {
	@Autowired
	private SalespercentService salespercentService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/salespercentList")
	@RequiresPermissions("ssadmin/salespercentList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/salespercentList") ;
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
			filter.append("and flevel like '%"+keyWord+"%' \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}
		List<Fsalespercent> list = this.salespercentService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("salespercentList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "salespercentList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fsalespercent", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("/ssadmin/goSalespercentJSP")
	public ModelAndView goSalespercentJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fsalespercent salespercent = this.salespercentService.findById(fid);
			modelAndView.addObject("fsalespercent", salespercent);
		}
		
		Map map = new HashMap();
		map.put(UserGradeEnum.LEVEL1, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL1));
		map.put(UserGradeEnum.LEVEL2, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL2));
		map.put(UserGradeEnum.LEVEL3, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL3));
		map.put(UserGradeEnum.LEVEL4, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL4));
		map.put(UserGradeEnum.LEVEL5, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL5));
		modelAndView.addObject("map", map);
		
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/saveSalespercent")
	@RequiresPermissions("ssadmin/saveSalespercent.html")
	public ModelAndView saveSalespercent(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		String dateString = sdf.format(new java.util.Date());
		Timestamp tm = Timestamp.valueOf(dateString);
		int flevel = Integer.parseInt(request.getParameter("flevel"));
		String filter = "where flevel="+flevel;
		int count = this.adminService.getAllCount("Fsalespercent", filter);
		if(count >0){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","该等级已经存在，无法添加");
			return modelAndView;
		}
//		double fqty = Double.valueOf(request.getParameter("fqty")).doubleValue();
		double ftotalpercent = Double.valueOf(request.getParameter("ftotalpercent")).doubleValue();
		double feggpercent = Double.valueOf(request.getParameter("feggpercent")).doubleValue();
		double fleaderpercent = Double.valueOf(request.getParameter("fleaderpercent")).doubleValue();
		if(ftotalpercent > 100){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","总分成比例不能大于100");
			return modelAndView;
		}
		if(feggpercent > 100){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","砸金豆比例不能大于100");
			return modelAndView;
		}
		if(fleaderpercent > 100){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","领导奖比例不能大于100");
			return modelAndView;
		}
		
		Fsalespercent salespercent = new Fsalespercent();
		salespercent.setFcreatetime(tm);
		salespercent.setFlevel(flevel);
		salespercent.setFgrade(Integer.parseInt(request.getParameter("type")));
		salespercent.setFtotalpercent(ftotalpercent);
		salespercent.setFeggpercent(feggpercent);
		salespercent.setFleaderpercent(fleaderpercent);
		this.salespercentService.saveObj(salespercent);
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/updateSalespercent")
	@RequiresPermissions("ssadmin/updateSalespercent.html")
	public ModelAndView updateSalespercent(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		int fid = Integer.parseInt(request.getParameter("fid"));
		int flevel = Integer.parseInt(request.getParameter("flevel"));
//		double fqty = Double.valueOf(request.getParameter("fqty")).doubleValue();
		double ftotalpercent = Double.valueOf(request.getParameter("ftotalpercent")).doubleValue();
		double feggpercent = Double.valueOf(request.getParameter("feggpercent")).doubleValue();
		double fleaderpercent = Double.valueOf(request.getParameter("fleaderpercent")).doubleValue();
		if(ftotalpercent > 100){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","总分成比例不能大于100");
			return modelAndView;
		}
		if(feggpercent > 100){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","砸金豆比例不能大于100");
			return modelAndView;
		}
		if(fleaderpercent > 100){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","领导奖比例不能大于100");
			return modelAndView;
		}
		
		Fsalespercent salespercent = this.salespercentService.findById(fid);
		salespercent.setFcreatetime(Utils.getTimestamp());
		salespercent.setFlevel(flevel);
		salespercent.setFgrade(Integer.parseInt(request.getParameter("type")));
		salespercent.setFtotalpercent(ftotalpercent);
		salespercent.setFeggpercent(feggpercent);
		salespercent.setFleaderpercent(fleaderpercent);
		this.salespercentService.updateObj(salespercent);
		
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	
	
	@RequestMapping("/ssadmin/salespercentLookup")
	@RequiresPermissions("ssadmin/salespercentLookup.html")
	public ModelAndView salespercentLookup(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/salespercentLookup");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");

		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (floginName like '%" + keyWord + "%' or \n");
			modelAndView.addObject("keywords", keyWord);
		}

		List<Fsalespercent> list = this.salespercentService.list(
				(currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("salespercent1", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "operationLogList");
		modelAndView.addObject("currentPage", currentPage);
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("Fsalespercent", filter + ""));
		return modelAndView;
	}
	
}

