package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.*;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fwithdrawfees;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.CapitaloperationService;
import com.ruizton.main.service.admin.WithdrawFeesService;
import com.ruizton.util.DateUtils;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
public class WithdrawFeesController extends BaseController {
	@Autowired
	private WithdrawFeesService withdrawFeesService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private CapitaloperationService capitaloperationService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/withdrawFeesList")
	@RequiresPermissions("ssadmin/withdrawFeesList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/withdrawFeesList") ;
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
			Pattern pattern = Pattern.compile("[0-9]*"); 
		    boolean flag = pattern.matcher(keyWord).matches();
		    if(flag){
				filter.append("and flevel ="+keyWord+" \n");
				modelAndView.addObject("keywords", keyWord);
		    }
		}
		
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}
		
		List<Fwithdrawfees> list = this.withdrawFeesService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("withdrawFeesList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "withdrawFeesList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fwithdrawfees", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goWithdrawFeesJSP")
	public ModelAndView goWithdrawFeesJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fwithdrawfees withdrawfees = this.withdrawFeesService.findById(fid);
			modelAndView.addObject("withdrawfees", withdrawfees);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateWithdrawFees")
	@RequiresPermissions("ssadmin/updateWithdrawFees.html")
	public ModelAndView updateWithdrawFees(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        int fid = Integer.parseInt(request.getParameter("fid"));
        Fwithdrawfees withdrawfees = this.withdrawFeesService.findById(fid);
        withdrawfees.setFfee(Double.valueOf(request.getParameter("ffee")));
        withdrawfees.setFamount(Double.valueOf(request.getParameter("famount")));
        withdrawfees.setFshopRate(Double.valueOf(request.getParameter("fshopRate")));
        
        if(withdrawfees.getFfee()>=1 || withdrawfees.getFfee()<0){
        	modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","手续费率只能是小于1的小数！");
			return modelAndView;
        }
        
        this.withdrawFeesService.updateObj(withdrawfees);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}

	@RequestMapping("ssadmin/withdrawfeeReport.html")
	@RequiresPermissions("ssadmin/withdrawfeeReport.html")
	public ModelAndView withdrawfeeReport(
			@RequestParam(required = false, name = "startDate") String start,
			@RequestParam(required = false, name = "endDate") String end
	) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.setViewName("ssadmin/withdrawfeeReport");
		if(!StringUtils.hasText(start)){
			start = DateUtils.formatDate(DateUtils.getMonthBefore(), "yyyy-MM-dd");
		}
		if(!StringUtils.hasText(end)) {
			end = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
		}
		Date startDate = DateUtils.formatDate(start, "yyyy-MM-dd");
		Date endDate = DateUtils.formatDate(end, "yyyy-MM-dd");
		endDate.setTime(endDate.getTime() + 24 * 60 * 60 * 1000);//包含endDate
		List<String[]> list = capitaloperationService.listForWithdrawFeeReport(startDate, endDate, CapitalOperationTypeEnum.RMB_OUT, CapitalOperationInStatus.Come);
		StringBuilder key = new StringBuilder();
		StringBuilder value = new StringBuilder();
		double total = 0D;
		for (String[] strings : list) {
			key.append("\"" + strings[0] + "\"").append(",");
			value.append(strings[1]).append(",");
			total += Double.parseDouble(strings[1]);
		}
		if(list.size() == 0){
			key.append("\"0\"");
			value.append(0);

		}else{
			key.deleteCharAt(key.length() - 1);
			value.deleteCharAt(value.length() - 1);
		}
		modelAndView.addObject("key", "[" + key + "]");
		modelAndView.addObject("value", "[" + value + "]");
		modelAndView.addObject("startDate", start);
		modelAndView.addObject("endDate", end);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
}
