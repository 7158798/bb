package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.MessageStatusEnum;
import com.ruizton.main.Enum.PopcornBetStatusEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fpopcorn;
import com.ruizton.main.model.Fpopcornbetlog;
import com.ruizton.main.model.Fusersetting;
import com.ruizton.main.service.admin.*;
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
public class PopcornbetlogController extends BaseController {
	@Autowired
	private PopcornbetlogService popcornbetlogService;
	@Autowired
	private PopcornService popcornService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private VirtualWalletService virtualWalletService;
	@Autowired
	private UsersettingService usersettingService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/popcornbetlogList")
	@RequiresPermissions("ssadmin/popcornbetlogList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/popcornbetlogList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		String fstatus = request.getParameter("fstatus");
		String term = request.getParameter("term");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and fid like '%"+keyWord+"%' \n");
			filter.append("or fuser.floginName like '%"+keyWord+"%' \n");
			filter.append("or fuser.fnickName like '%"+keyWord+"%' \n");
			filter.append("or fuser.frealName like '%"+keyWord+"%' \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(fstatus != null && fstatus.trim().length() >0){
			if(!fstatus.equals("0")){
				filter.append(" and fstatus="+fstatus+" \n");
			}
			modelAndView.addObject("fstatus", fstatus);
		}
		if(term != null && term.trim().length() >0){
			if(!term.equals("0")){
				filter.append(" and fpopcornlog.fid="+term+" \n");
			}
			modelAndView.addObject("term", term);
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
		Map map = new HashMap();
		map.put(0, "全部");
		map.put(PopcornBetStatusEnum.LOST_VALUE,PopcornBetStatusEnum.getEnumString(PopcornBetStatusEnum.LOST_VALUE));
		map.put(PopcornBetStatusEnum.NO_OPEN_VALUE,PopcornBetStatusEnum.getEnumString(PopcornBetStatusEnum.NO_OPEN_VALUE));
		map.put(PopcornBetStatusEnum.SEND_VALUE,PopcornBetStatusEnum.getEnumString(PopcornBetStatusEnum.SEND_VALUE));
		map.put(PopcornBetStatusEnum.WAIT_VALUE,PopcornBetStatusEnum.getEnumString(PopcornBetStatusEnum.WAIT_VALUE));
		modelAndView.addObject("statusMap",map);
		List<Fpopcornbetlog> list = this.popcornbetlogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("popcornbetlogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "popcornbetlogList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fpopcornbetlog", filter+""));
		return modelAndView ;
	}
	
	
	@RequestMapping("/ssadmin/sendPopcornbetlog")
	@RequiresPermissions("ssadmin/sendPopcornbetlog.html")
	public ModelAndView sendPopcornbetlog(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		String ids = request.getParameter("ids");
		String[] idString = ids.split(",");
		boolean flag = false;
		Fpopcorn popcorn = this.popcornService.findById(1);
		for(int i=0;i<idString.length;i++){
			int fid = Integer.parseInt(idString[i]);
			Fpopcornbetlog popcornbetlog = this.popcornbetlogService.findById(fid);
			if(popcornbetlog.getFstatus() != PopcornBetStatusEnum.WAIT_VALUE){
				flag = true;
				continue;
			}
			popcornbetlog.setFstatus(PopcornBetStatusEnum.SEND_VALUE);
			double winQty = popcornbetlog.getFwinQty();
			double betQty = 0d;
			if(popcornbetlog.getFbetQty1() != null 
					&& popcornbetlog.getFactualResult1().intValue() == popcornbetlog.getFbetresult1().intValue()){
				winQty = winQty - popcornbetlog.getFbetQty1();
				betQty = betQty + popcornbetlog.getFbetQty1();
			}
			if(popcornbetlog.getFbetQty2() != null 
					&& popcornbetlog.getFactualResult2().intValue() == popcornbetlog.getFbetresult2().intValue()){
				winQty = winQty - popcornbetlog.getFbetQty2();
				betQty = betQty + popcornbetlog.getFbetQty2();
			}
			
			double qty = betQty+Utils.getDouble(winQty*(100d-popcorn.getFrate())/100d,2);
			double fees = betQty+Utils.getDouble(winQty*popcorn.getFrate()/100d,2);
			popcornbetlog.setFactualWinQty(qty);
			popcornbetlog.setFfees(fees);
			Fmessage message = new Fmessage();
			message.setFtitle("疯狂冰淇淋发奖通知");
			message.setFcontent("你在疯狂冰淇淋的奖品金券："+qty+"个，已发放，请注意查收!");
			message.setFcreateTime(Utils.getTimestamp());
			message.setFstatus(MessageStatusEnum.NOREAD_VALUE);
			message.setFreceiver(popcornbetlog.getFuser());
			
			String sql = "where fuser.fid="+popcornbetlog.getFuser().getFid();
			Fusersetting usersetting = this.usersettingService.list(0, 0, sql, false).get(0);
			usersetting.setFticketQty(usersetting.getFticketQty()+qty);
			try {
				this.popcornbetlogService.updatelog(message, popcornbetlog,usersetting);
			} catch (Exception e) {
				flag = true;
				continue;
			}
		}
		String msg = "";
		if(flag){
			msg = "部分发奖失败，请重新发奖";
		}else{
			msg = "发奖成功";
		}
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", msg);
		return modelAndView;
		
	}
}
