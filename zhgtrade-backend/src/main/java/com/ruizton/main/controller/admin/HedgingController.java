package com.ruizton.main.controller.admin;

import java.sql.Time;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fhedging;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.HedgingService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.util.Utils;

@Controller
public class HedgingController extends BaseController {
	@Autowired
	private HedgingService hedgingService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private VirtualCoinService virtualCoinService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/hedgingList")
	@RequiresPermissions("ssadmin/hedgingList.html")
	public ModelAndView hedgingList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/hedgingList") ;
		//当前页
		int currentPage = 1;
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		List<Fhedging> list = this.hedgingService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("hedgingList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "hedgingList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fhedging", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goHedgingJSP")
	public ModelAndView goHedgingJSP(HttpServletRequest request) throws Exception {
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(url);
		if (request.getParameter("uid") != null) {
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fhedging hedging = this.hedgingService.findById(fid);
			modelAndView.addObject("hedging", hedging);
		}

		List<Fvirtualcointype> allType = this.virtualCoinService.findAll();
		modelAndView.addObject("allType", allType);

		return modelAndView;
	}

	
	@RequestMapping("ssadmin/saveHedging")
	@RequiresPermissions("ssadmin/saveHedging.html")
	public ModelAndView saveHedging(HttpServletRequest request) throws Exception{
		Fhedging fhedging = new Fhedging();
		fhedging.setFmaxqty(Double.valueOf(request.getParameter("fmaxqty")));
		fhedging.setFminqty(Double.valueOf(request.getParameter("fminqty")));
		fhedging.setFstarttime(Time.valueOf(request.getParameter("fstarttime")));
		fhedging.setFendtime(Time.valueOf(request.getParameter("fendtime")));
		/*fhedging.setFrate1(Double.valueOf(request.getParameter("frate1")));
		fhedging.setFrate2(Double.valueOf(request.getParameter("frate2")));
		fhedging.setFrate3(Double.valueOf(request.getParameter("frate3")));*/
		fhedging.setFrate1(0.0);
		fhedging.setFrate2(0.0);
		fhedging.setFrate3(0.0);
		fhedging.setFupthreshold(Double.valueOf(request.getParameter("fupthreshold")));
		fhedging.setFdownthreshold(Double.valueOf(request.getParameter("fdownthreshold")));
		fhedging.setFstartpricetime(Time.valueOf(request.getParameter("fstartpricetime")));
		fhedging.setFendpricetime(Time.valueOf(request.getParameter("fendpricetime")));
		fhedging.setFcreatetime(Utils.getTimestamp());
		fhedging.setFpriceurl(request.getParameter("fpriceurl"));
		fhedging.setFvirtualcointypeByFid(this.virtualCoinService.findById(Integer.parseInt(request.getParameter("vid1"))));
		fhedging.setFvirtualcointypeByFvid2(this.virtualCoinService.findById(Integer.parseInt(request.getParameter("vid2"))));
        this.hedgingService.saveObj(fhedging);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteHedging")
	@RequiresPermissions("ssadmin/deleteHedging.html")
	public ModelAndView deleteHedging(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("uid"));
		this.hedgingService.deleteObj(fid);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateHedging")
	@RequiresPermissions("ssadmin/updateHedging.html")
	public ModelAndView updateHedging(HttpServletRequest request) throws Exception{
		Fhedging fhedging = this.hedgingService.findById(Integer.parseInt(request.getParameter("uid")));
		fhedging.setFmaxqty(Double.valueOf(request.getParameter("fmaxqty")));
		fhedging.setFminqty(Double.valueOf(request.getParameter("fminqty")));
		fhedging.setFstarttime(Time.valueOf(request.getParameter("fstarttime")));
		fhedging.setFendtime(Time.valueOf(request.getParameter("fendtime")));
		/*fhedging.setFrate1(Double.valueOf(request.getParameter("frate1")));
		fhedging.setFrate2(Double.valueOf(request.getParameter("frate2")));
		fhedging.setFrate3(Double.valueOf(request.getParameter("frate3")));*/
		fhedging.setFupthreshold(Double.valueOf(request.getParameter("fupthreshold")));
		fhedging.setFdownthreshold(Double.valueOf(request.getParameter("fdownthreshold")));
		fhedging.setFstartpricetime(Time.valueOf(request.getParameter("fstartpricetime")));
		fhedging.setFendpricetime(Time.valueOf(request.getParameter("fendpricetime")));
		fhedging.setFcreatetime(Utils.getTimestamp());
		fhedging.setFpriceurl(request.getParameter("fpriceurl"));
		fhedging.setFvirtualcointypeByFid(this.virtualCoinService.findById(Integer.parseInt(request.getParameter("vid1"))));
		fhedging.setFvirtualcointypeByFvid2(this.virtualCoinService.findById(Integer.parseInt(request.getParameter("vid2"))));
        this.hedgingService.updateObj(fhedging);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
