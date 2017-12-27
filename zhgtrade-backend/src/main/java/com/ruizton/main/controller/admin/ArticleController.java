package com.ruizton.main.controller.admin;

import com.ruizton.main.comm.ParamArray;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Farticle;
import com.ruizton.main.model.Farticletype;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.ArticleService;
import com.ruizton.main.service.admin.ArticleTypeService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.util.Constants;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ArticleController extends BaseController {
	@Autowired
	private ArticleService articleService ;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private ArticleTypeService articleTypeService ;
	@Autowired
	private FrontVirtualCoinService frontVirtualCoinService;
	@Value("${oss.cdn}")
	private String cdn;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/articleList")
	@RequiresPermissions("ssadmin/articleList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/articleList") ;
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
			filter.append("and (fTitle like '%"+keyWord+"%' OR \n");
			filter.append("fkeyword like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(StringUtils.hasText(request.getParameter("ftype"))){
			int type = Integer.parseInt(request.getParameter("ftype"));
			if(type != 0){
				filter.append("and farticletype.fid="+request.getParameter("ftype")+"\n");
			}
			modelAndView.addObject("ftype", request.getParameter("ftype"));
		}
		
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by fcreateDate \n");
		}
		
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		
		Map typeMap = new HashMap();
		typeMap.put(0, "全部");
		List<Farticletype> all = this.articleTypeService.findAll();
		for (Farticletype farticletype : all) {
			typeMap.put(farticletype.getFid(), farticletype.getFname());
		}
		modelAndView.addObject("typeMap", typeMap);
		
		List<Farticle> list = this.articleService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("articleList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "articleList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Farticle", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goArticleJSP")
	public ModelAndView goArticleJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Farticle article = this.articleService.findById(fid);
			modelAndView.addObject("farticle", article);
		}
		return modelAndView;
	}
	
	@RequestMapping(value="ssadmin/upload")
	@ResponseBody
	public String upload(HttpServletRequest request, ParamArray param) throws Exception{
		MultipartFile multipartFile = param.getFiledata() ;
		InputStream inputStream = multipartFile.getInputStream() ;
		String realName = multipartFile.getOriginalFilename() ;
		
		if(realName!=null && realName.trim().toLowerCase().endsWith("jsp")){
			return "" ;
		}
		
		String[] nameSplit = realName.split("\\.") ;
		String ext = nameSplit[nameSplit.length-1] ;
		String realPath = getRequest().getSession().getServletContext().getRealPath("/")+ Constants.AdminArticleDirectory;
		String fileName = Utils.getRandomImageName()+"."+ext;
		boolean flag = Utils.saveFile(realPath,fileName, inputStream) ;
		String result = "";
		if(!flag){
			result = "上传失败";
		}
		JSONObject resultJson = new JSONObject() ;
		resultJson.accumulate("err",result) ;
//		String path = request.getContextPath();
//		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		String basePath = cdn + "/";
		resultJson.accumulate("msg",basePath+Constants.AdminArticleDirectory+"/"+fileName) ;
		return resultJson.toString();
	}
	
	@RequestMapping("ssadmin/saveArticle")
	@RequiresPermissions("ssadmin/saveArticle.html")
	public ModelAndView saveArticle(
			@RequestParam(required=false) MultipartFile filedata,
			@RequestParam("articleLookup.id") int articleTypeId,
			@RequestParam(required=false) String ftitle,
			@RequestParam(required=false) String fKeyword,
			@RequestParam(required=false) String fcontent,
			@RequestParam(required=false) String isTop,
			@RequestParam("cointypelookup.coinId") int coinId
			) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		Farticle article = new Farticle();
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
				 && !ext.trim().toLowerCase().endsWith("png")
				 && !ext.trim().toLowerCase().endsWith("txt")
				 && !ext.trim().toLowerCase().endsWith("ppt")
				 && !ext.trim().toLowerCase().endsWith("xls")
				 && !ext.trim().toLowerCase().endsWith("xlsx")
				 && !ext.trim().toLowerCase().endsWith("pdf")
				 && !ext.trim().toLowerCase().endsWith("doc")
				 && !ext.trim().toLowerCase().endsWith("docx")){
					modelAndView.addObject("statusCode",300);
					modelAndView.addObject("message","非jpg、png、txt、ppt、xls、pdf、doc文件格式");
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
			article.setFdocurl(fpictureUrl);
		}
		Farticletype articletype = this.articleTypeService.findById(articleTypeId);
		article.setFarticletype(articletype);
		article.setFtitle(ftitle);
		article.setFkeyword(fKeyword);
		article.setFcontent(fcontent);
		article.setFlastModifyDate(Utils.getTimestamp());
		article.setFcreateDate(Utils.getTimestamp());
		if(StringUtils.hasText(isTop)){
			article.setTop(true);
		}
		if(coinId != 0){
			Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(coinId);
			if(fvirtualcointype != null){
				article.setFvirtualcointype(fvirtualcointype);
			}
		}
		this.articleService.saveObj(article);
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");

		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteArticle")
	@RequiresPermissions("ssadmin/deleteArticle.html")
	public ModelAndView deleteArticle(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("uid"));
		Farticle article = articleService.findById(fid);
		this.articleService.deleteObj(fid);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");

		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateArticle")
	@RequiresPermissions("ssadmin/updateArticle.html")
	public ModelAndView updateArticle(
			@RequestParam(required=false) MultipartFile filedata,
			@RequestParam("articleLookup.id") int articleTypeId,
			@RequestParam(required=false) String ftitle,
			@RequestParam(required=false) String fKeyword,
			@RequestParam(required=false) String fcontent,
			@RequestParam(required=false) int fid,
			@RequestParam(required=false) String isDelete,
			@RequestParam(required=false) String isTop,
			@RequestParam("cointypelookup.coinId") int coinId
			) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		
		Farticle article = this.articleService.findById(fid);
		if("1".equals(isDelete)){
			article.setFdocurl(null);
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
				 && !ext.trim().toLowerCase().endsWith("png")
				 && !ext.trim().toLowerCase().endsWith("txt")
				 && !ext.trim().toLowerCase().endsWith("ppt")
				 && !ext.trim().toLowerCase().endsWith("xls")
				 && !ext.trim().toLowerCase().endsWith("xlsx")
				 && !ext.trim().toLowerCase().endsWith("docx")
				 && !ext.trim().toLowerCase().endsWith("pdf")
				 && !ext.trim().toLowerCase().endsWith("doc")){
					modelAndView.addObject("statusCode",300);
					modelAndView.addObject("message","非jpg、png、txt、ppt、xls、pdf、doc文件格式");
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
			article.setFdocurl(fpictureUrl);
		}
		Farticletype articletype = this.articleTypeService.findById(articleTypeId);
		article.setFarticletype(articletype);
		article.setFtitle(ftitle);
		article.setFkeyword(fKeyword);
		article.setFcontent(fcontent);
		article.setFlastModifyDate(Utils.getTimestamp());
		if(StringUtils.hasText(isTop)){
			article.setTop(true);
		}else{
			article.setTop(false);
		}

		Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(coinId);
		if(fvirtualcointype != null){
			article.setFvirtualcointype(fvirtualcointype);
		}else{
			article.setFvirtualcointype(null);
		}
		this.articleService.updateObj(article);
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");

		return modelAndView;
	}
}
