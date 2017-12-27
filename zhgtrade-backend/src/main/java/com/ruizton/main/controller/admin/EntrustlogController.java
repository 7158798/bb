package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.service.admin.EntrustlogService;
import com.ruizton.util.DateUtils;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class EntrustlogController extends BaseController {
	@Autowired
	private EntrustlogService entrustlogService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/entrustlogList")
	@RequiresPermissions("ssadmin/entrustlogList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/entrustlogList") ;
		
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		
		if(StringUtils.hasText(startDate) && StringUtils.hasText(endDate)){
            Date start = DateUtils.formatDate(startDate);
            Date end = DateUtils.formatDate(endDate);
            int days = DateUtils.getDays(start, end);
            if(days > 7){
            	modelAndView.addObject("startDate", startDate);
            	modelAndView.addObject("endDate", endDate);
            	modelAndView.addObject("statusCode", 300);
            	modelAndView.addObject("message", "日期间隔不能大于7天");
            	modelAndView.setViewName("ssadmin/comm/ajaxDone");
                return modelAndView;
            }
        }else{
        	endDate = DateUtils.formatDate(new Date());
        	startDate = DateUtils.formatDate(DateUtils.getDaysBefore(7));
        }
		
		StringBuilder filter = new StringBuilder();
		filter.append("where 1=1 \n");
//		filter.append(" and (c.fid != 15184) and (c.fid != 11101) and (c.fid != "+ Constants.RobotID +")  \n");
		// 排除机器人
		filter.append(" and b.robotStatus=0 ");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (LOCATE(c.fNickName, '" + keyWord + "')>0 \n");
			filter.append("or LOCATE(c.fRealName, '" + keyWord + "')>0 \n");
			filter.append("or LOCATE(c.floginName, '" + keyWord + "')>0 ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(StringUtils.hasText(startDate)){
			filter.append("and a.fCreateTime>=").append("'" + startDate + "' ");
			modelAndView.addObject("startDate", startDate);
		}
		if(StringUtils.hasText(endDate)){
			filter.append("and a.fCreateTime<=").append("'" + endDate + "' ");
			modelAndView.addObject("endDate", endDate);
		}
		List list = this.entrustlogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("entrustlogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "entrustlogList");
		
		if(StringUtils.hasText(request.getParameter("totalCount"))){
			modelAndView.addObject("totalCount", Integer.valueOf(request.getParameter("totalCount")));
		}else{
			modelAndView.addObject("totalCount",entrustlogService.countForList(filter.toString()));
		}
		return modelAndView ;
	}
}
