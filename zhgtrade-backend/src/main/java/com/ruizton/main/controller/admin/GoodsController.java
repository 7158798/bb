package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.CanPayTypeEnum;
import com.ruizton.main.Enum.GoodsStatusEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fgoods;
import com.ruizton.main.model.Fgoodtype;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.GoodsService;
import com.ruizton.main.service.admin.GoodtypeService;
import com.ruizton.util.Constants;
import com.ruizton.util.Utils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GoodsController extends BaseController {
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private GoodtypeService goodtypeService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/goodsList")
	@RequiresPermissions("ssadmin/goodsList.html")
	public ModelAndView goodsList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/goodsList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String supplierNo = request.getParameter("supplierNo");
		String status = request.getParameter("status");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(supplierNo != null && supplierNo.trim().length() >0){
			filter.append("and fsupplierNo like '%"+supplierNo+"%' \n");
			modelAndView.addObject("supplierNo", supplierNo);
		}
		if(status != null && status.trim().length() >0 && !status.equals("0")){
			filter.append("and fstatus="+status+" \n");
			modelAndView.addObject("status", status);
		}else{
			modelAndView.addObject("status", 0);
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
		
		Map<Integer,String> statusMap = new HashMap<Integer,String>();
		for(int i=1;i<=3;i++){
			statusMap.put(i, GoodsStatusEnum.getEnumString(i));
		}
		statusMap.put(0,"全部");
		modelAndView.addObject("statusMap", statusMap);
		
		List<Fgoods> list = this.goodsService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("goodsList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "goodsList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fgoods", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goGoodsJSP")
	public ModelAndView goGoodsJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fgoods goods = this.goodsService.findById(fid);
			modelAndView.addObject("goods", goods);
		}
		List<Fgoodtype> fgoodtypes = this.goodtypeService.findAll();
		modelAndView.addObject("fgoodtypes", fgoodtypes);
		
		Map payMap = new HashMap();
		payMap.put(CanPayTypeEnum.CNY, CanPayTypeEnum.getEnumString(CanPayTypeEnum.CNY));
		payMap.put(CanPayTypeEnum.ZGC, CanPayTypeEnum.getEnumString(CanPayTypeEnum.ZGC));
		payMap.put(CanPayTypeEnum.ALL, CanPayTypeEnum.getEnumString(CanPayTypeEnum.ALL));
		modelAndView.addObject("payMap", payMap);
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateGoods")
	@RequiresPermissions("ssadmin/updateGoods.html")
	public ModelAndView updateGoods(HttpServletRequest request,
			MultipartFile filedata,
			int fid,
			String fdescription,
			int flastQty,
			int ftotalQty,
			String fname,
			double fprice,
			double fcanBuyQty,
			int fcanpaytype,
			double fmarketPrice,
			int ftype,
			String fqq,
			String fsupplierNo
			) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		Fgoods goods = this.goodsService.findById(fid);
        goods.setFcreatetime(Utils.getTimestamp());
        goods.setFdescription(fdescription);
        goods.setFisVirtual(false);
        goods.setFmarketPrice(fmarketPrice);
//        goods.setFscore(fscore);
        goods.setFgoodtype(this.goodtypeService.findById(ftype));
        goods.setFlastQty(flastQty);
        goods.setFtotalQty(ftotalQty);
        goods.setFname(fname);
        goods.setFcanBuyQty(fcanBuyQty);
        goods.setFcanpaytype(fcanpaytype);
        goods.setFqq(fqq);
        String fpictureUrl = null;
        boolean isFlag = false;
        if(filedata != null && !filedata.isEmpty()){
			InputStream inputStream = filedata.getInputStream() ;
			String fileRealName = filedata.getOriginalFilename() ;
			if(fileRealName != null && fileRealName.trim().length() >0){
				String[] nameSplit = fileRealName.split("\\.") ;
				String ext = nameSplit[nameSplit.length-1] ;
				if(ext!=null && !ext.trim().toLowerCase().endsWith("jpg")){
					modelAndView.addObject("statusCode",300);
					modelAndView.addObject("message","非jpg文件格式");
					return modelAndView;
				}
				String realPath = getRequest().getSession().getServletContext().getRealPath("/")+ Constants.AdminShopDirectory;
				String fileName = Utils.getRandomImageName()+"."+ext;
				boolean flag = Utils.saveFile(realPath,fileName, inputStream) ;
				if(flag){
					fpictureUrl = "/"+Constants.AdminShopDirectory+"/"+fileName ;
				}else{
					 isFlag = true;
				}
			}else{
				 isFlag = true;
			}
		}else{
			 isFlag = true;
		}
        if(!isFlag){
        	goods.setFpictureUrl(fpictureUrl);
        }
        
        goods.setFprice(fprice);
        goods.setFstatus(GoodsStatusEnum.SAVE);
        String filter = "where fuserNo='"+fsupplierNo+"'";
        int count = this.adminService.getAllCount("Fuser", filter);
        if(count == 0){
        	modelAndView.addObject("statusCode",300);
    		modelAndView.addObject("message","商家号不存在！");
    		return modelAndView;
        }
//        goods.setFsupplierNo(fsupplierNo);
        this.goodsService.updateObj(goods);
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/saveGoods")
	@RequiresPermissions("ssadmin/saveGoods.html")
	public ModelAndView saveGoods(HttpServletRequest request,
			MultipartFile filedata,
			String fdescription,
			int flastQty,
			int ftotalQty,
			String fname,
			double fprice,
			double fcanBuyQty,
			int fcanpaytype,
			double fmarketPrice,
			int ftype,
			String fqq,
			String fsupplierNo
			) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
        Fgoods goods = new Fgoods();
        goods.setFcreatetime(Utils.getTimestamp());
        goods.setFdescription(fdescription);
        goods.setFisVirtual(false);
        goods.setFlastQty(flastQty);
        goods.setFtotalQty(ftotalQty);
        goods.setFcanBuyQty(fcanBuyQty);
        goods.setFcanpaytype(fcanpaytype);
//        goods.setFscore(fscore);
        goods.setFname(fname);
        goods.setFqq(fqq);
        goods.setFmarketPrice(fmarketPrice);
        goods.setFgoodtype(this.goodtypeService.findById(ftype));
        String fpictureUrl = null;
        boolean isFlag = false;
        if(filedata != null && !filedata.isEmpty()){
			InputStream inputStream = filedata.getInputStream() ;
			String fileRealName = filedata.getOriginalFilename() ;
			if(fileRealName != null && fileRealName.trim().length() >0){
				String[] nameSplit = fileRealName.split("\\.") ;
				String ext = nameSplit[nameSplit.length-1] ;
				if(ext!=null && !ext.trim().toLowerCase().endsWith("jpg")){
					modelAndView.addObject("statusCode",300);
					modelAndView.addObject("message","非jpg文件格式");
					return modelAndView;
				}
				String realPath = getRequest().getSession().getServletContext().getRealPath("/")+Constants.AdminShopDirectory;
				String fileName = Utils.getRandomImageName()+"."+ext;
				boolean flag = Utils.saveFile(realPath,fileName, inputStream) ;
				if(flag){
					fpictureUrl = "/"+Constants.AdminShopDirectory+"/"+fileName ;
				}else{
					 isFlag = true;
				}
			}else{
				 isFlag = true;
			}
		}else{
			 isFlag = true;
		}
        if(isFlag){
        	modelAndView.addObject("statusCode",300);
    		modelAndView.addObject("message","请上传商品图片！");
    		return modelAndView;
        }
        goods.setFsellQty(0);
        goods.setFpictureUrl(fpictureUrl);
        goods.setFprice(fprice);
        goods.setFstatus(GoodsStatusEnum.SAVE);
        String filter = "where fuserNo='"+fsupplierNo+"'";
        int count = this.adminService.getAllCount("Fuser", filter);
        if(count == 0){
        	modelAndView.addObject("statusCode",300);
    		modelAndView.addObject("message","商家号不存在！");
    		return modelAndView;
        }
        goods.setFsupplierNo(fsupplierNo);
        this.goodsService.saveObj(goods);
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","保存成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/takeOutGoods")
	@RequiresPermissions("ssadmin/takeOutGoods.html")
	public ModelAndView takeOutGoods(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fgoods goods = this.goodsService.findById(fid);
        goods.setFstatus(GoodsStatusEnum.OUT);
        this.goodsService.updateObj(goods);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","商品下架成功");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/upGoods")
	@RequiresPermissions("ssadmin/upGoods.html")
	public ModelAndView upGoods(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fgoods goods = this.goodsService.findById(fid);
        goods.setFstatus(GoodsStatusEnum.NORMAL);
        this.goodsService.updateObj(goods);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","商品上架成功");
		return modelAndView;
	}
	
}
