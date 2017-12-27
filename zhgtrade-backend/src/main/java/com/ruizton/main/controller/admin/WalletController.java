package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fcapitaloperation;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.Fwallet;
import com.ruizton.main.service.admin.*;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.util.*;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WalletController extends BaseController {
	@Autowired
	private WalletService walletService ;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private UserService userService ;
	@Autowired
	private FrontUserService frontUserService;
	@Autowired
	private CapitaloperationService capitaloperationService;
	@Autowired
	private VirtualCoinService virtualCoinService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/walletList")
	@RequiresPermissions("ssadmin/walletList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/walletList") ;
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
			filter.append("and (floginName like '%"+keyWord+"%' or \n");
			filter.append("fnickName like '%"+keyWord+"%' or \n");
			filter.append("frealName like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by fregisterTime \n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		List<Fuser> list = this.userService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("userList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "walletList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fuser", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("/ssadmin/viewUserWallet")
	@RequiresPermissions("ssadmin/viewUserWallet.html")
	public ModelAndView viewUserWallet(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/viewUserWallet") ;
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
			filter.append("and (floginName like '%"+keyWord+"%' or \n");
			filter.append("fnickName like '%"+keyWord+"%' or \n");
			filter.append("frealName like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(request.getParameter("cid") != null){
			int cid =Integer.parseInt(request.getParameter("cid"));
			Fcapitaloperation c = this.capitaloperationService.findById(cid);
			filter.append("and fid ="+c.getFuser().getFid()+" \n");
			modelAndView.addObject("cid",cid);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by fregisterTime \n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		List<Fuser> list = this.userService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("userList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "viewUserWallet");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fuser", filter+""));
		return modelAndView ;
	}
	
	/**
	 * 导出总资金大于0的会员资金列表
	 * @param
	 */
	@RequestMapping(value = "/ssadmin/exportUserWalletList", method = RequestMethod.GET)
	@RequiresPermissions("ssadmin/exportUserWalletList.html")
	public void exportUserWalletList(HttpServletResponse response, HttpServletRequest request){
		String keyWord = request.getParameter("keywords");

		List<Map<String, Object>> list = walletService.findGtZeroByTotal(keyWord);
		XlsExport xls = new XlsExport();
		
		// 标题
		xls.createRow(0);
		xls.setCell(0, "会员登录名");
		xls.setCell(1, "会员昵称");
		xls.setCell(2, "会员邮箱");
		xls.setCell(3, "会员手机号");
		xls.setCell(4, "会员真实姓名");
		xls.setCell(5, "会员手机号");
		xls.setCell(6, "会员可用资金量");
		xls.setCell(7, "会员冻结资金量");
		xls.setCell(8, "会员资金总量");
		
		// 填入数据
		for(int i=1, len=list.size(); i<=len; i++){
			Map<String, Object> map = list.get(i-1);
			xls.createRow(i);
			xls.setCell(0, map.get("floginName").toString());
			xls.setCell(1, map.get("fNickName").toString());
			xls.setCell(2, StringUtils.null2EmptyString(map.get("fEmail")));
			xls.setCell(3, StringUtils.null2EmptyString(map.get("fTelephone")));
			xls.setCell(4, map.get("fRealName").toString());
			xls.setCell(5, map.get("fTelephone").toString());
			xls.setCell(6, (double) map.get("fTotalRMB"));
			xls.setCell(7, (double) map.get("fFrozenRMB"));
			xls.setCell(8, (double) map.get("total"));
		}
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("会员资金列表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 会员资金问题
	 *
	 * @param request
	 * @param symbol   0:rmb
	 * @param keyword
     * @return
     */
	@RequestMapping("ssadmin/walletErrorList")
	@RequiresPermissions("ssadmin/walletErrorList.html")
	public String walletErrorList(HttpServletRequest request, Map<String, Object> map,
								  @RequestParam(required = false, defaultValue = "-1") Integer symbol,
								  @RequestParam(required = false) String keyword,
								  @RequestParam(value = "pageNum", required = false, defaultValue = "1")int page,
								  @RequestParam(required = false, defaultValue = "0")Integer total){
		map.put("symbol", symbol);
		if(symbol < 0){
			symbol = null;
		}

		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		List<Map<String, Object>> list = walletService.findWalletErrorList(symbol, keyword, orderField, orderDirection, (page - 1) * numPerPage, numPerPage , true);
		if(1 == page){
			total = walletService.countWalletErrorList(symbol, keyword);
		}

		List<Fvirtualcointype> coins = virtualCoinService.findAll();
		Map<String, Fvirtualcointype> coinMap = new HashMap<>(coins.size());
		for(Fvirtualcointype coin : coins){
			coinMap.put(coin.getFid() + "", coin);
		}
		map.put("coins", coinMap);
		map.put("list", list);
		map.put("keyword", keyword);
		map.put("page", page);
		map.put("total", total);
		map.put("numPerPage", numPerPage);
		return "ssadmin/walletErrorList";
	}

	@RequestMapping(value = "/ssadmin/exportWalletErrorList", method = RequestMethod.GET)
	@RequiresPermissions("ssadmin/exportWalletErrorList.html")
	public void exportWalletErrorList(HttpServletResponse response,
									  @RequestParam(required = false, defaultValue = "-1") Integer symbol,
									  @RequestParam(required = false) String keyword){

		if(symbol < 0){
			symbol = null;
		}
		List<Map<String, Object>> list = walletService.findWalletErrorList(symbol, keyword, null, null, 0, 0 , false);

		List<Fvirtualcointype> coins = virtualCoinService.findAll();
		Map<String, Fvirtualcointype> coinMap = new HashMap<>(coins.size());
		for(Fvirtualcointype coin : coins){
			coinMap.put(coin.getFid() + "", coin);
		}

		XlsExport xls = new XlsExport();

		// 标题
		xls.createRow(0);
		xls.setCell(0, "会员登录名");
		xls.setCell(1, "会员真实姓名");
		xls.setCell(2, "会员手机号");
		xls.setCell(3, "会员邮箱号");
		xls.setCell(4, "会员钱包类型");
		xls.setCell(5, "会员钱包余额");
		xls.setCell(6, "会员钱包冻结额");

		// 填入数据
		for(int i=1, len=list.size(); i<=len; i++){
			Map<String, Object> map = list.get(i-1);
			xls.createRow(i);
			xls.setCell(0, StringUtils.null2EmptyString(map.get("floginName")));
			xls.setCell(1, StringUtils.null2EmptyString(map.get("fRealName")));
			xls.setCell(2, StringUtils.null2EmptyString(map.get("fTelephone")));
			xls.setCell(3, StringUtils.null2EmptyString(map.get("fEmail")));
			xls.setCell(4, "0".equals(map.get("fvid").toString()) ? "人民币" : coinMap.get(map.get("fvid").toString()).getFname());
			xls.setCell(5, map.get("fTotal").toString());
			xls.setCell(6, map.get("fFrozen").toString());
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("会员钱包警报表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}















