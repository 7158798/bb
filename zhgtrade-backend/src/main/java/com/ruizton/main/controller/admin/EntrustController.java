package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.EntrustStatusEnum;
import com.ruizton.main.Enum.EntrustTypeEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fentrust;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.EntrustService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.util.Constants;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import com.ruizton.util.XlsExport;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EntrustController extends BaseController {
	@Autowired
	private EntrustService entrustService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private VirtualCoinService virtualCoinService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/entrustList")
	@RequiresPermissions("ssadmin/entrustList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/entrustList") ;
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
		filter.append(" and (fuser.fid != 15184) and (fuser.fid != 11101) and (fuser.fid != "+ Constants.RobotID +")  \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (fuser.floginName like '%"+keyWord+"%' OR \n");
			filter.append("fuser.frealName like '%"+keyWord+"%' OR \n");
			filter.append("fuser.fnickName like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		if(request.getParameter("ftype") != null){
			int type = Integer.parseInt(request.getParameter("ftype"));
			if(type != 0){
				filter.append("and fvirtualcointype.fid="+type+"\n");
			}
			modelAndView.addObject("ftype", type);
		}else{
			modelAndView.addObject("ftype", 0);
		}
		
		String status = request.getParameter("status");
		if(status != null && status.trim().length() >0){
			if(!status.equals("0")){
				filter.append("and fstatus="+status+" \n");
			}
			modelAndView.addObject("status", status);
		}else{
			modelAndView.addObject("status", 0);
		}
		
		String entype = request.getParameter("entype");
		if(entype != null && entype.trim().length() >0){
			if(!entype.equals("-1")){
				filter.append("and fentrustType="+entype+" \n");
			}
			modelAndView.addObject("entype", entype);
		}else{
			modelAndView.addObject("entype", -1);
		}
		
		try {
			String price = request.getParameter("price");
			if(price != null && price.trim().length() >0){
				double p = Double.valueOf(price);
				filter.append("and fprize="+p+" \n");
			}
			modelAndView.addObject("price", price);
		} catch (Exception e) {
		}
		
		String logDate = request.getParameter("logDate");
		if(logDate != null && logDate.trim().length() >0){
			filter.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') = '"+logDate+"' \n");
			modelAndView.addObject("logDate", logDate);
		}
		
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			//filter.append("order by fcreateTime \n");
			filter.append("order by fid \n");
		}
		
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		
		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);
		
		Map statusMap = new HashMap();
		statusMap.put(EntrustStatusEnum.AllDeal, EntrustStatusEnum.getEnumString(EntrustStatusEnum.AllDeal));
		statusMap.put(EntrustStatusEnum.Cancel, EntrustStatusEnum.getEnumString(EntrustStatusEnum.Cancel));
		statusMap.put(EntrustStatusEnum.Going, EntrustStatusEnum.getEnumString(EntrustStatusEnum.Going));
		statusMap.put(EntrustStatusEnum.PartDeal, EntrustStatusEnum.getEnumString(EntrustStatusEnum.PartDeal));
		statusMap.put(0,"全部");
		modelAndView.addObject("statusMap", statusMap);
		
		Map entypeMap = new HashMap();
		entypeMap.put(EntrustTypeEnum.BUY, EntrustTypeEnum.getEnumString(EntrustTypeEnum.BUY));
		entypeMap.put(EntrustTypeEnum.SELL, EntrustTypeEnum.getEnumString(EntrustTypeEnum.SELL));
		entypeMap.put(-1,"全部");
		modelAndView.addObject("entypeMap", entypeMap);
		
		
		List<Fentrust> list = this.entrustService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("entrustList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "entrustList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fentrust", filter+""));
		return modelAndView ;
	}
	
	/**
	 * 导出委托订单列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/ssadmin/exportEntrustList")
	@RequiresPermissions("ssadmin/exportEntrustList.html")
	public void exportEntrustList(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "keywords", required = false, defaultValue = "") String keyWord, 		
			@RequestParam(value = "ftype", required = false) Integer coinId, 
			@RequestParam(value = "logDate", required = false) String logDate, 
			@RequestParam(value = "price", required = false) Double price, 
			@RequestParam(value = "status", required = false) Short status, 
			@RequestParam(value = "entype", required = false) Short entrustType){
		
		
		if(StringUtils.hasText(keyWord)){
			try {
				keyWord = URLDecoder.decode(keyWord, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				keyWord = null;
			}
		}
		
		// 输入校验
		if(null != coinId && coinId < 1){
			coinId = null;
		}
		if(null != status && status < 1){
			status = null;
		}
		if(null != entrustType && entrustType < 1){
			entrustType = null;
		}
		
		List<Object> list = this.entrustService.findForExport(keyWord, coinId, logDate, price, entrustType, status, null, null);
		
		// 标题
		XlsExport xls = new XlsExport();
		xls.createRow(0);
		xls.setCell(0, "会员登录名");
		xls.setCell(1, "会员昵称");
		xls.setCell(2, "会员邮箱");
		xls.setCell(3, "会员手机号");
		xls.setCell(4, "会员真实姓名");
		xls.setCell(5, "虚拟币类型");
		xls.setCell(6, "交易类型");
		xls.setCell(7, "状态");
		xls.setCell(8, "单价");
		xls.setCell(9, "数量");
		xls.setCell(10, "未成交数量");
		xls.setCell(11, "已成交数量");
		xls.setCell(12, "总金额");
		xls.setCell(13, "成交总金额");
		
		// 填入数据
		for(int i=1, len=list.size(); i<=len; i++){
			Object[] obj = (Object[]) list.get(i-1);
			xls.createRow(i);
			xls.setCell(0, null == obj[0] ? "" : obj[0].toString());
			xls.setCell(1, null == obj[1] ? "" : obj[1].toString());
			xls.setCell(2, null == obj[11] ? "" : obj[11].toString());
			xls.setCell(3, null == obj[12] ? "" : obj[12].toString());

			xls.setCell(4, null == obj[2] ? "" : obj[2].toString());
			xls.setCell(5, null == obj[3] ? "" : obj[3].toString());
			xls.setCell(6, EntrustTypeEnum.getEnumString((int)obj[4]));
			xls.setCell(7, EntrustStatusEnum.getEnumString((int)obj[5]));
			xls.setCell(8, obj[6].toString());
			xls.setCell(9, obj[7].toString());
			xls.setCell(10, obj[8].toString());
			xls.setCell(11, ((BigDecimal)obj[7]).intValue() - ((BigDecimal)obj[8]).intValue());
			xls.setCell(12, obj[9].toString());
			xls.setCell(13, obj[10].toString());
		}
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("委托交易列表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}












