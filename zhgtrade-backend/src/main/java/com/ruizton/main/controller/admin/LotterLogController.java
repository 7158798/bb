package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.LotteryRewardTypeEnum;
import com.ruizton.main.Enum.LotterylogStatusEnum;
import com.ruizton.main.Enum.MessageStatusEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Flotterylog;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.LotteryLogService;
import com.ruizton.main.service.admin.VirtualWalletService;
import com.ruizton.util.MathUtils;
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
public class LotterLogController extends BaseController {
	@Autowired
	private LotteryLogService lotteryLogService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private VirtualWalletService virtualWalletService;
	//每页显示多少条数据
	private int numPerPage = 100;
	
	@RequestMapping("/ssadmin/lotterylogList")
	@RequiresPermissions("ssadmin/lotterylogList.html")
	public ModelAndView lotterylogList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/lotterylogList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String fstatus = request.getParameter("fstatus");
		String ftype = request.getParameter("ftype");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and fuser.floginName like '%"+keyWord+"%' \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		if(fstatus != null && fstatus.trim().length() >0){
			if(!fstatus.equals("0")){
				filter.append(" and fstatus="+fstatus+" \n");
			}
			modelAndView.addObject("fstatus", fstatus);
		}
		
		if(ftype != null && ftype.trim().length() >0){
			if(!ftype.equals("0")){
				int f =Integer.parseInt(ftype);
				if(f == LotteryRewardTypeEnum.SHIWU){
					filter.append(" and fflag=1 \n");
				}else{
					filter.append(" and fflag=0 \n");
				}
			}
			modelAndView.addObject("ftype", ftype);
		}
		
		String logDate = request.getParameter("logDate");
		if(logDate != null && logDate.trim().length() >0){
			filter.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') = '"+logDate+"' \n");
			modelAndView.addObject("logDate", logDate);
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
		map.put(LotterylogStatusEnum.SEND,LotterylogStatusEnum.getEnumString(LotterylogStatusEnum.SEND));
		map.put(LotterylogStatusEnum.NOT_SEND,LotterylogStatusEnum.getEnumString(LotterylogStatusEnum.NOT_SEND));
		modelAndView.addObject("statusMap",map);
		
		Map map1 = new HashMap();
		map1.put(0, "全部");
		map1.put(LotteryRewardTypeEnum.SHIWU,LotteryRewardTypeEnum.getEnumString(LotteryRewardTypeEnum.SHIWU));
		map1.put(LotteryRewardTypeEnum.XUNIBI,LotteryRewardTypeEnum.getEnumString(LotteryRewardTypeEnum.XUNIBI));
		modelAndView.addObject("typeMap",map1);
		
		List<Flotterylog> list = this.lotteryLogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("lotterylogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "lotterylogList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Flotterylog", filter+""));
		return modelAndView ;
	}

	@RequestMapping("ssadmin/sendLotteryaward")
	@RequiresPermissions("ssadmin/sendLotteryaward.html")
	public ModelAndView sendLotteryaward(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		String ids = request.getParameter("ids");
		String[] idString = ids.split(",");
		boolean flag = false;
		for(int i=0;i<idString.length;i++){
			int fid = Integer.parseInt(idString[i]);
			Flotterylog lotterylog = this.lotteryLogService.findById(fid);
			if(lotterylog.getFstatus() == LotterylogStatusEnum.SEND){
				flag = true;
				continue;
			}
			lotterylog.setFstatus(LotterylogStatusEnum.SEND);
			
			Fmessage message = new Fmessage();
			message.setFtitle("砸金蛋发奖通知");
			message.setFcontent("你在砸金蛋的奖品："+lotterylog.getFtitle()+lotterylog.getFqty()+"个，已发放，请注意查收!");
			message.setFcreateTime(Utils.getTimestamp());
			message.setFstatus(MessageStatusEnum.NOREAD_VALUE);
			message.setFreceiver(lotterylog.getFuser());
			
			if(!lotterylog.isFflag()){
				double qty = lotterylog.getFqty();
				String sql = "where fvirtualcointype.fid=1 and fuser.fid="+lotterylog.getFuser().getFid();
				List<Fvirtualwallet> alls = this.virtualWalletService.list(0, 0, sql, false);
				Fvirtualwallet virtualwallet = alls.get(0);
				virtualwallet.setFtotal(MathUtils.add(virtualwallet.getFtotal(), qty));
				try {
					this.lotteryLogService.updatelog(message, lotterylog,virtualwallet);
				} catch (Exception e) {
					flag = true;
					continue;
				}
			}else{
				try {
					this.lotteryLogService.updatelog(message, lotterylog);
				} catch (Exception e) {
					flag = true;
					continue;
				}
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
