package com.ruizton.main.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.Enum.ActivityRewardStatusEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fpromotionrewardrecord;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.PromotionRewardRecordService;
import com.ruizton.util.Utils;

@Controller
public class PromotionRewardRecordController extends BaseController {
	@Autowired
	private PromotionRewardRecordService PRewardRecordService;
	@Autowired
	private AdminService adminService;
	// 每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();

	@RequestMapping("/ssadmin/promotionRewardRecordList")
	@RequiresPermissions("ssadmin/promotionRewardRecordList.html")
	public ModelAndView promotionRewardRecordList(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/promotionRewardRecordList1");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (fuser.floginName like '%" + keyWord
					+ "%' or \n");
			filter.append("fuser.fnickName like '%" + keyWord + "%'  or \n");
			filter.append("factivity.ftitle like '%" + keyWord + "%'  or \n");
			filter.append("fuser.frealName like '%" + keyWord + "%'  )\n");
			modelAndView.addObject("keywords", keyWord);
		}

		if(request.getParameter("ftype") != null && request.getParameter("ftype").trim().length() >0){
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filter.append("and fstatus=" + request.getParameter("ftype")
						+ " \n");
			}
			modelAndView.addObject("ftype", request.getParameter("ftype"));
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

		Map typeMap = new HashMap();
		typeMap.put(0, "全部");
		typeMap.put(ActivityRewardStatusEnum.NOT_REWARD,
				ActivityRewardStatusEnum
						.getEnumString(ActivityRewardStatusEnum.NOT_REWARD));
		typeMap.put(ActivityRewardStatusEnum.REJECT, ActivityRewardStatusEnum
				.getEnumString(ActivityRewardStatusEnum.REJECT));
		typeMap.put(ActivityRewardStatusEnum.REWARD, ActivityRewardStatusEnum
				.getEnumString(ActivityRewardStatusEnum.REWARD));
		modelAndView.addObject("typeMap", typeMap);

		List<Fpromotionrewardrecord> list = this.PRewardRecordService.list(
				(currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("promotionRewardRecordList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "promotionRewardRecordList");
		// 总数量
		modelAndView.addObject(
				"totalCount",
				this.adminService.getAllCount("Fpromotionrewardrecord", filter
						+ ""));
		return modelAndView;
	}

	@RequestMapping("ssadmin/auditPromotionRewardRecord")
	@RequiresPermissions("ssadmin/auditPromotionRewardRecord.html")
	public ModelAndView auditPromotionRewardRecord(HttpServletRequest request) throws Exception {
		// 1审核2拒绝
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		int type = Integer.parseInt(request.getParameter("type"));
		int fid = Integer.parseInt(request.getParameter("uid"));
		Fpromotionrewardrecord promotionrewardrecord = this.PRewardRecordService
				.findById(fid);
		if (promotionrewardrecord.getFstatus() != ActivityRewardStatusEnum.NOT_REWARD) {
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "操作失败，只有待审核的奖励才允许做此操作");
			return modelAndView;
		}
		boolean flag = false;
		// 审核
		if (type == 1) {
			promotionrewardrecord.setFstatus(ActivityRewardStatusEnum.REWARD);
			promotionrewardrecord.setFlastUpdatetime(Utils.getTimestamp());
			try {
				this.PRewardRecordService.updateRecord(promotionrewardrecord);
				flag = true;
			} catch (Exception e) {
				modelAndView.addObject("statusCode", 300);
				modelAndView.addObject("message", "操作失败");
				return modelAndView;
			}
		} else if (type == 2) {
			promotionrewardrecord.setFstatus(ActivityRewardStatusEnum.REJECT);
			promotionrewardrecord.setFlastUpdatetime(Utils.getTimestamp());
			this.PRewardRecordService.updateObj(promotionrewardrecord);
			flag = true;
		}

		if (!flag) {
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "操作失败");
			return modelAndView;
		}

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "操作成功");
		return modelAndView;
	}

}
