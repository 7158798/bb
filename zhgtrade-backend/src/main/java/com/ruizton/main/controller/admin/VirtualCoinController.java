package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.*;
import com.ruizton.main.service.admin.*;
import com.ruizton.util.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VirtualCoinController extends BaseController {
	@Autowired
	private VirtualCoinService virtualCoinService ;
	@Autowired
	private WithdrawFeesService withdrawFeesService ;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private FeeService feeService;
	@Autowired
	private PoolService poolService;
	@Autowired
	private JedisPool jedisPool;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();

	private Map startCoinLockMap = new HashMap<>();				// 项目启用锁池
	private Map processCoinAddressLockMap = new HashMap<>();	// 项目生成地址锁池

	@RequestMapping("/ssadmin/virtualCoinTypeList")
	@RequiresPermissions("ssadmin/virtualCoinTypeList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/virtualCoinTypeList") ;
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
			filter.append("and (fname like '%"+keyWord+"%' OR \n");
			filter.append("fShortName like '%"+keyWord+"%' OR \n");
			filter.append("fdescription like '%"+keyWord+"%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by faddTime \n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}

		modelAndView.addObject("startLocks", startCoinLockMap);
		modelAndView.addObject("addressLocks", processCoinAddressLockMap);

		List<Fvirtualcointype> list = this.virtualCoinService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("virtualCoinTypeList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "virtualCoinTypeList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fvirtualcointype", filter+""));
		return modelAndView ;
	}


	@RequestMapping("/ssadmin/virtualCoinOrder")
	@RequiresPermissions("ssadmin/virtualCoinOrder.html")
	public ModelAndView coinOrder(HttpServletRequest request, @RequestParam (required = false, defaultValue = "4") int type) throws Exception{

		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/virtualCoinOrder") ;

		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");

		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		filter.append(" and fstatus = " + VirtualCoinTypeStatusEnum.Normal + " \n");
		if(type == 1){
			filter.append("and fisShare = 1 and coinTradeType = 1 order by typeOrder");
		}else if(type == 2){
			filter.append("and fisShare = 1 and coinTradeType = 2 order by typeOrder");
		}else if(type == 3){
			filter.append("and fisShare = 1 and equityType = 1 order by typeOrder");
		}else if(type == 4){
			filter.append("and fisShare = 1 and homeShow = 1 order by homeOrder ");
		} else if(type == 5){
			filter.append("order by totalOrder ");
		}

//		if(keyWord != null && keyWord.trim().length() >0){
//			filter.append("and (fname like '%"+keyWord+"%' OR \n");
//			modelAndView.addObject("keywords", keyWord);
//		}
		List<Fvirtualcointype> list = this.virtualCoinService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("virtualCoinTypeList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "virtualCoinTypeList");
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("type", type);
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fvirtualcointype", filter+""));
		return modelAndView ;
	}

	@RequestMapping("/ssadmin/walletAddressList")
	@RequiresPermissions("ssadmin/walletAddressList.html")
	public ModelAndView walletAddressList(HttpServletRequest request) throws Exception{
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/walletAddressList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and locate(b.fname, '" + keyWord + "') \n");
			modelAndView.addObject("keywords", keyWord);
		}
//		filter.append("and (a.fstatus=0 or a.fstatus is null)\n");
		filter.append("and a.fstatus=0 \n");
		
		List list = this.poolService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("walletAddressList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "walletAddressList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.poolService.countForList(filter.toString()));
		return modelAndView ;
	}
	
	
	@RequestMapping("ssadmin/goVirtualCoinTypeJSP")
	public ModelAndView goVirtualCoinTypeJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fvirtualcointype virtualCoinType = this.virtualCoinService.findById(fid);
			modelAndView.addObject("virtualCoinType", virtualCoinType);
			
			String filter = "where fvirtualcointype.fid="+fid+" order by flevel asc";
			List<Ffees> allFees = this.feeService.list(0, 0, filter, false);
			modelAndView.addObject("allFees", allFees);
		}
		return modelAndView;
	}


	/**
	 * 修改币种次序JSP页面
	 * @param request
	 * @return
	 * @throws Exception
     */
	@RequestMapping("ssadmin/goVirtualCoinOrder")
	public ModelAndView goVirtualCoinOrder(HttpServletRequest request) throws Exception{

		return goVirtualCoinTypeJSP(request);
	}

	@RequestMapping("ssadmin/saveVirtualCoinType")
	@RequiresPermissions("ssadmin/saveVirtualCoinType.html")
	public ModelAndView saveVirtualCoinType(
			@RequestParam(required=false) MultipartFile filedata,
			@RequestParam(required=false) String fdescription,
			@RequestParam(required=false) String fname,
			@RequestParam(required=false) String fShortName,
			@RequestParam(required=false) String faccess_key,
			@RequestParam(required=false) String fsecrt_key,
			@RequestParam(required=false) String fip,
			@RequestParam(required=false) String fport,
			@RequestParam(required=false) double fopenPrice,
			@RequestParam(required=false) String fSymbol,
			@RequestParam(required=false) String FIsWithDraw,
			@RequestParam(required=false) String fisShare,
			@RequestParam(required=false) String fintroUrl,
			@RequestParam(required=false) double ftotalamount,
			@RequestParam(required=false) int coinTradeType,
			@RequestParam(required=false) String equityType,
			@RequestParam(required=false) String homeShow,
			@RequestParam(required=false) String openTrade
			) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		Fvirtualcointype virtualCoinType = new Fvirtualcointype();
		String fpictureUrl = "";
		boolean isTrue = false;
		 if(filedata != null && !filedata.isEmpty()){
			InputStream inputStream = filedata.getInputStream() ;
			String fileRealName = filedata.getOriginalFilename() ;
			if(fileRealName != null && fileRealName.trim().length() >0){
				String[] nameSplit = fileRealName.split("\\.") ;
				String ext = nameSplit[nameSplit.length-1] ;
				if(ext!=null 
				 && !ext.trim().toLowerCase().endsWith("jpg")
				 && !ext.trim().toLowerCase().endsWith("png")){
					modelAndView.addObject("statusCode",300);
					modelAndView.addObject("message","非jpg、png文件格式");
					return modelAndView;
				}
				String realPath = getRequest().getSession().getServletContext().getRealPath("/")+Constants.AdminUploadDirectory;
				String fileName = Utils.getRandomImageName()+"."+ext;
				boolean flag = Utils.saveFile(realPath,fileName, inputStream) ;
				if(flag){
					fpictureUrl = "/"+Constants.AdminUploadDirectory+"/"+fileName ;
					isTrue = true;
				}
			}
		}
		if(isTrue){
			virtualCoinType.setFurl(fpictureUrl);
		}
		virtualCoinType.setFaddTime(Utils.getTimestamp());
		virtualCoinType.setFdescription(fdescription);
		virtualCoinType.setFname(fname);
		virtualCoinType.setfShortName(fShortName);
		virtualCoinType.setOpenTrade(openTrade);
		virtualCoinType.setFstatus(VirtualCoinTypeStatusEnum.Abnormal);
		virtualCoinType.setFaccess_key(faccess_key);
		virtualCoinType.setFsecrt_key(fsecrt_key);
		virtualCoinType.setFip(fip);
		virtualCoinType.setFport(fport);
		virtualCoinType.setFopenPrice(fopenPrice);
		virtualCoinType.setfSymbol(fSymbol);
		virtualCoinType.setFintroUrl(fintroUrl);
		virtualCoinType.setFtotalamount(ftotalamount);
		if(coinTradeType != 3){
			virtualCoinType.setCoinTradeType(coinTradeType);
			virtualCoinType.setEquityType(0);
		}else {
			virtualCoinType.setEquityType(1);
			virtualCoinType.setCoinTradeType(0);
		}
//		if(StringUtils.hasText(equityType)){
//			virtualCoinType.setEquityType(Integer.valueOf(equityType));
//		}else{
//			virtualCoinType.setEquityType(0);
//		}
		if(StringUtils.hasText(homeShow)){
			virtualCoinType.setHomeShow(true);
		}else{
			virtualCoinType.setHomeShow(false);
		}
		if(FIsWithDraw != null && FIsWithDraw.trim().length() >0){
			virtualCoinType.setFIsWithDraw(true);
		}else{
			virtualCoinType.setFIsWithDraw(false);
		}
		if(fisShare != null && fisShare.trim().length() >0){
			virtualCoinType.setFisShare(true);
		}else{
			virtualCoinType.setFisShare(false);
		}
		this.virtualCoinService.saveObj(virtualCoinType);
		
		List<Fwithdrawfees> allWithDrawFees = withdrawFeesService.findAll();
		for (Fwithdrawfees fwithdrawfees : allWithDrawFees) {
			Ffees fees = new Ffees();
			fees.setWithdraw(0);
			fees.setFfee(0);
			fees.setFlevel(fwithdrawfees.getFlevel());
			fees.setFvirtualcointype(virtualCoinType);
			feeService.saveObj(fees);
		}

		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");

		return modelAndView;
	}
	
	//币种禁用时才能修改
	/*@RequestMapping("ssadmin/updateVirtualCoinType")
	@RequiresPermissions("ssadmin/updateVirtualCoinType.html")
	public ModelAndView updateVirtualCoinType(
			@RequestParam(required=false) String fdescription,
			@RequestParam(required=false) String fname,
			@RequestParam(required=false) String fShortName,
			@RequestParam(required=false) double fopenPrice,
			@RequestParam(required=false) String fSymbol,
			@RequestParam(required=false) String FIsWithDraw,
			@RequestParam(required=false) String fisShare,
			@RequestParam(required=false) int fid,
			@RequestParam(required=false) String fintroUrl
			) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		Fvirtualcointype virtualCoinType = this.virtualCoinService.findById(fid);
		if(virtualCoinType.getFstatus() == VirtualCoinTypeStatusEnum.Normal){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","请先禁用再进行修改！");
			return modelAndView;
		}
		
		virtualCoinType.setFaddTime(Utils.getTimestamp());
		virtualCoinType.setFdescription(fdescription);
		virtualCoinType.setFname(fname);
		virtualCoinType.setfShortName(fShortName);
		virtualCoinType.setFstatus(VirtualCoinTypeStatusEnum.Abnormal);
		virtualCoinType.setFopenPrice(fopenPrice);
		virtualCoinType.setfSymbol(fSymbol);
		virtualCoinType.setFintroUrl(fintroUrl);
		if(FIsWithDraw != null && FIsWithDraw.trim().length() >0){
			virtualCoinType.setFIsWithDraw(true);
		}else{
			virtualCoinType.setFIsWithDraw(false);
		}
		if(fisShare != null && fisShare.trim().length() >0){
			virtualCoinType.setFisShare(true);
		}else{
			virtualCoinType.setFisShare(false);
		}
		this.virtualCoinService.updateObj(virtualCoinType);
		
		List<Fwithdrawfees> allWithDrawFees = withdrawFeesService.findAll();
		for (Fwithdrawfees fwithdrawfees : allWithDrawFees) {
		    String filter = "where flevel="+fwithdrawfees.getFlevel()+" and fvirtualcointype.fid="+fid;
			List<Ffees> feesList = this.feeService.list(0, 0, filter, false);
			if(feesList == null || feesList.size() == 0){
				Ffees fees = new Ffees();
				fees.setWithdraw(0);
				fees.setFfee(0);
				fees.setFlevel(fwithdrawfees.getFlevel());
				fees.setFvirtualcointype(virtualCoinType);
				feeService.saveObj(fees);
			}
		}
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","更新成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}*/

	@RequestMapping("ssadmin/updateVirtualCoinType")
	@RequiresPermissions("ssadmin/updateVirtualCoinType.html")
	public ModelAndView updateVirtualCoinType(
			@RequestParam(required=false) String fdescription,
			@RequestParam(required=false) String fname,
			@RequestParam(required=false) String fShortName,
			@RequestParam(required=false) double fopenPrice,
			@RequestParam(required=false) String fSymbol,
			@RequestParam(required=false) String FIsWithDraw,
			@RequestParam(required=false) String fisShare,
			@RequestParam(required=false) String fintroUrl,
			@RequestParam(required=false) MultipartFile filedata,
			@RequestParam(required=false) String faccess_key,
			@RequestParam(required=false) String fsecrt_key,
			@RequestParam(required=false) String fport,
			@RequestParam(required=false) String fip,
			@RequestParam(required=false) int fid,
			@RequestParam(required=false) String openTrade,
			@RequestParam(required=false) double ftotalamount,
			@RequestParam(required=false) int coinTradeType,
			@RequestParam(required=false) String equityType,
			@RequestParam(required=false) String homeShow
			) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		Fvirtualcointype virtualCoinType = this.virtualCoinService.findById(fid);

		System.out.println("币种为：" + coinTradeType);
		virtualCoinType.setFaddTime(Utils.getTimestamp());
		virtualCoinType.setFdescription(fdescription);
		virtualCoinType.setFname(fname);
		virtualCoinType.setfShortName(fShortName);
		virtualCoinType.setFopenPrice(fopenPrice);
		virtualCoinType.setfSymbol(fSymbol);
		virtualCoinType.setFintroUrl(fintroUrl);
		virtualCoinType.setFaddTime(Utils.getTimestamp());
		virtualCoinType.setFaccess_key(faccess_key);
		virtualCoinType.setFsecrt_key(fsecrt_key);
		virtualCoinType.setFip(fip);
		virtualCoinType.setFport(fport);
		virtualCoinType.setFtotalamount(ftotalamount);
		virtualCoinType.setOpenTrade(openTrade);
		if(coinTradeType != 3){
			virtualCoinType.setCoinTradeType(coinTradeType);
			virtualCoinType.setEquityType(0);
		}else {
			virtualCoinType.setEquityType(1);
			virtualCoinType.setCoinTradeType(0);
		}
		if(FIsWithDraw != null && FIsWithDraw.trim().length() >0){
			virtualCoinType.setFIsWithDraw(true);
		}else{
			virtualCoinType.setFIsWithDraw(false);
		}
		if(fisShare != null && fisShare.trim().length() >0){
			virtualCoinType.setFisShare(true);
		}else{
			virtualCoinType.setFisShare(false);
		}

		String fpictureUrl = "";
		boolean isTrue = false;
		if(filedata != null && !filedata.isEmpty()){
			InputStream inputStream = filedata.getInputStream() ;
			String fileRealName = filedata.getOriginalFilename() ;
			if(fileRealName != null && fileRealName.trim().length() >0){
				String[] nameSplit = fileRealName.split("\\.") ;
				String ext = nameSplit[nameSplit.length-1] ;
				if(ext!=null
						&& !ext.trim().toLowerCase().endsWith("jpg")
						&& !ext.trim().toLowerCase().endsWith("png")){
					modelAndView.addObject("statusCode",300);
					modelAndView.addObject("message","非jpg、png文件格式");
					return modelAndView;
				}
				String realPath = getRequest().getSession().getServletContext().getRealPath("/")+Constants.AdminUploadDirectory;
				String fileName = Utils.getRandomImageName()+"."+ext;
				boolean flag = Utils.saveFile(realPath,fileName, inputStream) ;
				if(flag){
					fpictureUrl = "/"+Constants.AdminUploadDirectory+"/"+fileName ;
					isTrue = true;
				}
			}
		}
		if(isTrue){
			virtualCoinType.setFurl(fpictureUrl);
		}
//		if(StringUtils.hasText(equityType)){
//			virtualCoinType.setEquityType(1);
//		}else{
//			virtualCoinType.setEquityType(0);
//		}
		if(StringUtils.hasText(homeShow)){
			virtualCoinType.setHomeShow(true);
		}else{
			virtualCoinType.setHomeShow(false);
		}
		this.virtualCoinService.updateObj(virtualCoinType);

		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","更新成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}

	@RequestMapping("ssadmin/updateVirtualCoinTypeUnforbidden")
	@RequiresPermissions("ssadmin/updateVirtualCoinTypeUnforbidden.html")
	public ModelAndView updateVirtualCoinTypeUnforbidden(
			@RequestParam(required=false) MultipartFile filedata,
			@RequestParam(required=false) String faccess_key,
			@RequestParam(required=false) String fsecrt_key,
			@RequestParam(required=false) String fport,
			@RequestParam(required=false) String fip,
			@RequestParam(required=false) int fid,
			@RequestParam(required=false) String openTrade,
			@RequestParam(required=false) double ftotalamount,
			@RequestParam(required=false) int coinTradeType,
			@RequestParam(required=false) String equityType,
			@RequestParam(required=false) String homeShow
			) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		System.out.println(fid);
		Fvirtualcointype virtualCoinType = this.virtualCoinService.findById(fid);
		
		String fpictureUrl = "";
		boolean isTrue = false;
		if(filedata != null && !filedata.isEmpty()){
			InputStream inputStream = filedata.getInputStream() ;
			String fileRealName = filedata.getOriginalFilename() ;
			if(fileRealName != null && fileRealName.trim().length() >0){
				String[] nameSplit = fileRealName.split("\\.") ;
				String ext = nameSplit[nameSplit.length-1] ;
				if(ext!=null 
						&& !ext.trim().toLowerCase().endsWith("jpg")
						&& !ext.trim().toLowerCase().endsWith("png")){
					modelAndView.addObject("statusCode",300);
					modelAndView.addObject("message","非jpg、png文件格式");
					return modelAndView;
				}
				String realPath = getRequest().getSession().getServletContext().getRealPath("/")+Constants.AdminUploadDirectory;
				String fileName = Utils.getRandomImageName()+"."+ext;
				boolean flag = Utils.saveFile(realPath,fileName, inputStream) ;
				if(flag){
					fpictureUrl = "/"+Constants.AdminUploadDirectory+"/"+fileName ;
					isTrue = true;
				}
			}
		}
		if(isTrue){
			virtualCoinType.setFurl(fpictureUrl);
		}
		virtualCoinType.setFaddTime(Utils.getTimestamp());
		virtualCoinType.setFaccess_key(faccess_key);
		virtualCoinType.setFsecrt_key(fsecrt_key);
		virtualCoinType.setFip(fip);
		virtualCoinType.setFport(fport);
		virtualCoinType.setFtotalamount(ftotalamount);
		virtualCoinType.setOpenTrade(openTrade);

		if(coinTradeType != 3){
			virtualCoinType.setCoinTradeType(coinTradeType);
			virtualCoinType.setEquityType(0);
		}else{
			virtualCoinType.setEquityType(1);
			virtualCoinType.setCoinTradeType(0);
		}

//		if(StringUtils.hasText(equityType)){
//			virtualCoinType.setEquityType(1);
//		}else{
//			virtualCoinType.setEquityType(0);
//		}
		if(StringUtils.hasText(homeShow)){
			virtualCoinType.setHomeShow(true);
		}else{
			virtualCoinType.setHomeShow(false);
		}
		this.virtualCoinService.updateObj(virtualCoinType);
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","更新成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}

	/**
	 * 更新虚拟币显示次序
	 * @param fid
	 * @param homeOrder
	 * @param typeOrder
     * @return
     * @throws Exception
     */
	@RequestMapping("ssadmin/updateVirtualCoinOrder")
	@RequiresPermissions("ssadmin/updateVirtualCoinOrder.html")
	public ModelAndView updateVirtualCoinOrder(
			@RequestParam int fid,
			@RequestParam int homeOrder,
			@RequestParam int typeOrder
			) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		Fvirtualcointype virtualCoinType = this.virtualCoinService.findById(fid);

		virtualCoinType.setFaddTime(Utils.getTimestamp());
		virtualCoinType.setHomeOrder(homeOrder);
		virtualCoinType.setTypeOrder(typeOrder);
		virtualCoinService.updateObj(virtualCoinType);
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","更新成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping("/ssadmin/updateHomeOrder")
	public int updateHomeOrder(int id, int value){
		Fvirtualcointype virtualCoinType = this.virtualCoinService.findById(id);
		virtualCoinType.setFaddTime(Utils.getTimestamp());
		virtualCoinType.setHomeOrder(value);
		virtualCoinService.updateObj(virtualCoinType);
		return value;
	}

	@ResponseBody
	@RequestMapping("/ssadmin/updateTypeOrder")
	public int updateTypeOrder(int id, int value){
		Fvirtualcointype virtualCoinType = this.virtualCoinService.findById(id);
		virtualCoinType.setFaddTime(Utils.getTimestamp());
		virtualCoinType.setTypeOrder(value);
		virtualCoinService.updateObj(virtualCoinType);
		return value;
	}

	@ResponseBody
	@RequestMapping("/ssadmin/updteTotalOrder")
	@RequiresPermissions("/ssadmin/updteTotalOrder.html")
	public int updateTotalOrder(int id, int value){
		Fvirtualcointype virtualCoinType = virtualCoinService.findById(id);
		virtualCoinType.setFaddTime(Utils.getTimestamp());
		virtualCoinType.setTotalOrder(value);
		virtualCoinService.updateObj(virtualCoinType);
		return value;
	}

	@RequestMapping("ssadmin/goWallet")
	@RequiresPermissions("ssadmin/goWallet.html")
	public ModelAndView goWallet(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int fid = Integer.parseInt(request.getParameter("uid"));
		String password = request.getParameter("passWord");
		Fvirtualcointype virtualcointype = this.virtualCoinService.findById(fid);
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;

		if(startCoinLockMap.containsKey(fid)){
			// 启用中
			modelAndView.addObject("message","项目正在启用中");
			modelAndView.addObject("statusCode",300);
			return modelAndView;
		}else if(VirtualCoinTypeStatusEnum.Normal == virtualcointype.getFstatus()){
			modelAndView.addObject("message","项目已启用");
			modelAndView.addObject("statusCode",300);
			return modelAndView;
		}

		// 启用上锁
		startCoinLockMap.put(fid, true);

		new Thread(() -> {
			boolean flag = false;
			Fvirtualcointype fvirtualcointype = this.virtualCoinService.findById(fid);
			fvirtualcointype.setFstatus(VirtualCoinTypeStatusEnum.Normal);

			try {
				flag = this.virtualCoinService.updateCoinType(fvirtualcointype,password);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}

			if(!flag){
				System.err.println("项目" + fvirtualcointype.getFname() + "启用失败");
			}

			// 移除锁
			startCoinLockMap.remove(fvirtualcointype.getFid());
		}).start();


		modelAndView.addObject("message","项目正在启用中");
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteVirtualCoinType")
	@RequiresPermissions({"ssadmin/deleteVirtualCoinType.html?status=1"})
	public ModelAndView deleteVirtualCoinType(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int fid = Integer.parseInt(request.getParameter("uid"));
		Fvirtualcointype virtualcointype = this.virtualCoinService.findById(fid);
		int status = Integer.parseInt(request.getParameter("status"));
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		boolean flag = false;
		
		if(status == 1){//禁用
			virtualcointype.setFstatus(VirtualCoinTypeStatusEnum.Abnormal);
			this.virtualCoinService.updateObj(virtualcointype);
			flag = true;
		}
		
		if(!flag){
			modelAndView.addObject("message","操作失败");
			modelAndView.addObject("statusCode",300);
		}else if(flag && status == 1){
			modelAndView.addObject("message","禁用成功");
			modelAndView.addObject("statusCode",200);
		}else{
			modelAndView.addObject("message","启用成功");
			modelAndView.addObject("statusCode",200);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/testWallet")
	@RequiresPermissions("ssadmin/testWallet.html")
	public ModelAndView testWallet(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		int fid = Integer.parseInt(request.getParameter("uid"));
		Fvirtualcointype type = this.virtualCoinService.findById(fid);
		BTCMessage btcMessage = new BTCMessage() ;
		btcMessage.setACCESS_KEY(type.getFaccess_key()) ;
		btcMessage.setIP(type.getFip()) ;
		btcMessage.setPORT(type.getFport()) ;
		btcMessage.setSECRET_KEY(type.getFsecrt_key()) ;
		if(btcMessage.getACCESS_KEY()==null
				||btcMessage.getIP()==null
				||btcMessage.getPORT()==null
				||btcMessage.getSECRET_KEY()==null){
			modelAndView.addObject("message","钱包连接失败，请检查配置信息");
			modelAndView.addObject("statusCode",300);
		}
		try {
			BTCUtils btcUtils = new BTCUtils(btcMessage) ;
			double balance = btcUtils.getbalanceValue();
			modelAndView.addObject("message","测试成功，钱包余额:"+balance);
			modelAndView.addObject("statusCode",200);
		} catch (Exception e) {
			modelAndView.addObject("message","钱包连接失败，请检查配置信息");
			modelAndView.addObject("statusCode",300);
			e.printStackTrace();
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateCoinFee")
	@RequiresPermissions("ssadmin/updateCoinFee.html")
	public ModelAndView updateCoinFee(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int fid = Integer.parseInt(request.getParameter("fid"));
		List<Ffees> all = this.feeService.findByProperty("fvirtualcointype.fid", fid);
		
		//add by hank
		for (Ffees ffees : all) {
			String feeKey = "fee"+ffees.getFid();
			String withdrawKey = "withdraw"+ffees.getFid();
			double fee = Double.valueOf(request.getParameter(feeKey));
			double withdraw = Double.valueOf(request.getParameter(withdrawKey));
			
			if(fee>=1 || fee<0 || withdraw>=1 || withdraw<0){
				modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
				modelAndView.addObject("statusCode",300);
				modelAndView.addObject("message","手续费率只能是小于1的小数！");
				return modelAndView;
			}
		}
		
		
		
		for (Ffees ffees : all) {
			String feeKey = "fee"+ffees.getFid();
			String withdrawKey = "withdraw"+ffees.getFid();
			double fee = Double.valueOf(request.getParameter(feeKey));
			double withdraw = Double.valueOf(request.getParameter(withdrawKey));
			ffees.setFfee(fee);
			ffees.setWithdraw(withdraw);
			this.feeService.updateObj(ffees);
		}
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","更新成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/createWalletAddress")
	@RequiresPermissions("ssadmin/createWalletAddress.html")
	public ModelAndView createWalletAddress(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		int fid = Integer.parseInt(request.getParameter("uid"));
		Fvirtualcointype type = this.virtualCoinService.findById(fid);

		if(processCoinAddressLockMap.containsKey(fid)){
			modelAndView.addObject("message","正在生成虚拟地址!");
			modelAndView.addObject("statusCode",300);
			return modelAndView;
		}

		if(!type.isFIsWithDraw()){
			modelAndView.addObject("message","不允许充值和提现的虚拟币类型不能生成虚拟地址!");
			modelAndView.addObject("statusCode",300);
			return modelAndView;
		}
		BTCMessage btcMessage = new BTCMessage() ;
		btcMessage.setACCESS_KEY(type.getFaccess_key()) ;
		btcMessage.setIP(type.getFip()) ;
		btcMessage.setPORT(type.getFport()) ;
		btcMessage.setSECRET_KEY(type.getFsecrt_key()) ;
		btcMessage.setPASSWORD(request.getParameter("passWord"));
		if(btcMessage.getACCESS_KEY()==null
				||btcMessage.getIP()==null
				||btcMessage.getPORT()==null
				||btcMessage.getSECRET_KEY()==null){
			modelAndView.addObject("message","钱包连接失败，请检查配置信息");
			modelAndView.addObject("statusCode",300);
			return modelAndView;
		}

		BTCUtils btcUtils = new BTCUtils(btcMessage);
		try {
			btcUtils.getbalanceValue();
		} catch (Exception e) {
			modelAndView.addObject("message","钱包连接失败，请检查配置信息");
			modelAndView.addObject("statusCode",300);
			return modelAndView;
		}

		// 生成地址上锁
		processCoinAddressLockMap.put(fid, true);

		new Thread(() -> {
			int count = 10000;
			try {
				for(int i=0;i<count;i++){
					String address = btcUtils.getNewaddressValueForAdmin(sdf.format(Utils.getTimestamp()));
					if(address == null || address.trim().length() ==0){
						System.err.println("链接钱包，获取" + type.getFname() + "地址受限！");
						break;
					}
					Fpool poolInfo = new Fpool();
					poolInfo.setFaddress(address);
					poolInfo.setFvirtualcointype(type);
					poolService.saveObj(poolInfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					btcUtils.walletlock();
				} catch (Exception e) {
					e.printStackTrace();
				}
				processCoinAddressLockMap.remove(fid);
			}
		}).start();

		modelAndView.addObject("message","正在生成虚拟地址!");
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	/**
	 * 前端虚拟币选择列表
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/ssadmin/viCoinTypeRadioList")
	public String viCoinTypeSelectList(Map<String, Object> param){
		List<Fvirtualcointype> types = virtualCoinService.findAll();
		param.put("list", types);
		return "ssadmin/coinvote/radioList";
	}

	/**
	 * 虚拟币详情
	 *
	 * @param fVid
	 * @param map
     * @return
     */
	@RequestMapping("/ssadmin/viewVirtualCoinDetail")
	public String viewVirtualCoinDetail(@RequestParam(value = "fVid", required = true)int fVid, Map<String, Object> map){
		FTypeDetail detail = virtualCoinService.findDetailByFvid(fVid);
		map.put("detail", detail);
		map.put("fVid", fVid);
		return "ssadmin/virtualCoinDetail";
	}

	@RequestMapping("/ssadmin/updateVirtualCoinDetail")
	@RequiresPermissions("ssadmin/updateVirtualCoinDetail.html")
	public String updateVirtualCoinDetail(Map<String, Object> map,
										  @RequestParam(required = false)String fid,
										  @RequestParam(required = true)int fviFid,
										  @RequestParam(required = false)String desc,
										  @RequestParam(required = false)String name,
										  @RequestParam(required = false)String ename,
										  @RequestParam(required = false)String developers,
										  @RequestParam(required = false)String algorithm,
										  @RequestParam(required = false)String releaseTime,
										  @RequestParam(required = false)String blocksTime,
										  @RequestParam(required = false)String blocksReward,
										  @RequestParam(required = false)String amount,
										  @RequestParam(required = false)String remark,
										  @RequestParam(required = false)String walletLink,
										  @RequestParam(required = false)String sourceLink,
										  @RequestParam(required = false)String whitePaperLink,
										  @RequestParam(required = false)String blockBrowserLink,
										  @RequestParam(required = false)String feature,
										  @RequestParam(required = false)String defects){

		FTypeDetail fTypeDetail = null;
		if(StringUtils.hasText(fid)){
			fTypeDetail = virtualCoinService.findDetailById(Integer.valueOf(fid));
		}else{
			fTypeDetail = new FTypeDetail();
			fTypeDetail.setFviFid(fviFid);
		}
		if(StringUtils.hasText(releaseTime)){
			fTypeDetail.setReleaseTime(DateUtils.formatDate(releaseTime, "yyyy-MM-dd HH:mm:ss"));
		}
		fTypeDetail.setDesc(desc);
		fTypeDetail.setEname(ename);
		fTypeDetail.setName(name);
		fTypeDetail.setDevelopers(developers);
		fTypeDetail.setAlgorithm(algorithm);
		fTypeDetail.setBlocksTime(blocksTime);
		fTypeDetail.setBlocksReward(blocksReward);
		fTypeDetail.setAmount(amount);
		fTypeDetail.setRemark(remark);
		fTypeDetail.setWalletLink(walletLink);
		fTypeDetail.setSourceLink(sourceLink);
		fTypeDetail.setWhitePaperLink(whitePaperLink);
		fTypeDetail.setBlockBrowserLink(blockBrowserLink);
		fTypeDetail.setFeature(feature);
		fTypeDetail.setDefects(defects);

		virtualCoinService.updateDetail(fTypeDetail);
		map.put("statusCode",200);
		map.put("message","更新成功");
		map.put("callbackType","closeCurrent");
		return "ssadmin/comm/ajaxDone";
	}

	@RequestMapping("ssadmin/coinDetailList")
	@RequiresPermissions("ssadmin/coinDetailList.html")
	public String coinDetailList(Map map){
		List<Fvirtualcointype> coins = virtualCoinService.findAll();
		List<FTypeDetail> details = virtualCoinService.findAllDetail();
		Map<Integer, FTypeDetail> detailMap = new HashMap<>();
		for(FTypeDetail detail : details){
			detailMap.put(detail.getFviFid(), detail);
		}
		map.put("coins", coins);
		map.put("details", detailMap);
		return "ssadmin/coinDetailList";
	}

	/**
	 * 查询用户虚拟币地址
	 *
 	 * @param map
	 * @param keyword
	 * @param symbol
	 * @param page
	 * @param total
     * @return
     */
	@RequestMapping("ssadmin/userVirtualCoinAddressList")
	@RequiresPermissions("ssadmin/userVirtualCoinAddressList.html")
	public String userVirtualCoinAddressList(Map<String, Object> map, HttpServletRequest request,
											 @RequestParam(required = false)String keyword,
											 @RequestParam(required = false, defaultValue = "-1")Integer symbol,
											 @RequestParam(value = "pageNum", required = false, defaultValue = "1")int page,
											 @RequestParam(required = false, defaultValue = "0")int total){
		map.put("symbol", symbol);
		if(symbol <= 0){
			symbol = null;
		}

		List<Fvirtualcointype> coins = virtualCoinService.findAll();
		Map<String, Fvirtualcointype> coinMap = new HashMap<>(coins.size());
		for(Fvirtualcointype coin : coins){
			coinMap.put(coin.getFid() + "", coin);
		}

		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");

		List list = virtualCoinService.findUserVirtualCoinAddress(keyword, symbol, orderField, orderDirection, (page - 1) * numPerPage, numPerPage, true);
		map.put("list", list);
		if(1 == page){
			total = virtualCoinService.countUserVirtualCoinAddress(keyword, symbol);
		}

		map.put("coins", coinMap);
		map.put("list", list);
		map.put("keyword", keyword);
		map.put("page", page);
		map.put("total", total);
		map.put("numPerPage", numPerPage);
		return "ssadmin/userViCoinAddressList";
	}

	@RequestMapping("ssadmin/exportUserVirtualCoinAddressList")
	@RequiresPermissions("ssadmin/exportUserVirtualCoinAddressList.html")
	public void exportUserVirtualCoinAddressList(HttpServletRequest request, HttpServletResponse response,
											 @RequestParam(required = false)String keyword,
											 @RequestParam(required = false, defaultValue = "-1")Integer symbol){
		if(symbol <= 0){
			symbol = null;
		}

		List<Map<String, Object>> list = virtualCoinService.findUserVirtualCoinAddress(keyword, symbol, null, null, 0, 65535, true);
		XlsExport xls = new XlsExport();

		List<Fvirtualcointype> coins = virtualCoinService.findAll();
		Map<String, Fvirtualcointype> coinMap = new HashMap<>(coins.size());
		for(Fvirtualcointype coin : coins){
			coinMap.put(coin.getFid() + "", coin);
		}

		// 标题
		xls.createRow(0);
		xls.setCell(0, "会员登录名");
		xls.setCell(1, "会员真实姓名");
		xls.setCell(2, "会员手机号");
		xls.setCell(3, "会员邮箱号");
		xls.setCell(4, "币类型");
		xls.setCell(5, "会员地址");
		xls.setCell(6, "会员分配时间");

		// 填入数据
		for(int i=1, len=list.size(); i<=len; i++){
			Map<String, Object> map = list.get(i-1);
			xls.createRow(i);
			xls.setCell(0, StringUtils.null2EmptyString(map.get("floginName")));
			xls.setCell(1, StringUtils.null2EmptyString(map.get("fRealName")));
			xls.setCell(2, StringUtils.null2EmptyString(map.get("fTelephone")));
			xls.setCell(3, StringUtils.null2EmptyString(map.get("fEmail")));
			xls.setCell(4, coinMap.get(map.get("fVi_fId").toString()).getFname());
			xls.setCell(5, map.get("fAdderess").toString());
			xls.setCell(6, DateUtils.formatDate((Timestamp)map.get("fCreateTime"), "yyyy-MM-dd HH:mm:ss"));
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户虚拟币地址分配表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/ssadmin/cointypelookup")
	public ModelAndView forLookup(HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/coinTypeLookup");
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
			filter.append("and (fname like '%"+keyWord+"%' OR \n");
			filter.append("fShortName like '%"+keyWord+"%' OR \n");
			filter.append("fdescription like '%"+keyWord+"%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by faddTime \n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		List<Fvirtualcointype> list = this.virtualCoinService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("virtualCoinTypeList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "virtualCoinTypeList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fvirtualcointype", filter+""));
		return modelAndView ;

	}

}
