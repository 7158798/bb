package com.ruizton.main.controller.admin;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.*;
import com.ruizton.main.service.admin.*;
import com.ruizton.util.Constants;
import com.ruizton.util.Utils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunpeng on 2016/7/26.
 */
@Controller
@RequestMapping("/ssadmin")
public class BlockArticleController extends BaseController{

    @Autowired
    private BlockArticleService blockArticleService;
    @Autowired
    private BlockArticleTypeService blockArticleTypeService;
    @Autowired
    @Value("${oss.cdn}")
    private String cdn;
    //每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();


    @RequestMapping("/blockArticleList")
    @RequiresPermissions("/ssadmin/blockArticleList.html")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();

        String pageNum = request.getParameter("pageNum");
        int currentPage = pageNum != null ? Integer.parseInt(pageNum) : 1;
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        String type = request.getParameter("type");

        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 ");
        if(keyWord != null && keyWord.trim().length() >0){
            filter.append("and (title like '%"+keyWord+"%' OR ");
            filter.append("keyword like '%"+keyWord+"%' ) ");
            modelAndView.addObject("keywords", keyWord);
        }

        if(type != null){
            filter.append("and blockArticleType.id = " + type);
            modelAndView.addObject("type", type);
        }


        if(orderField != null && orderField.trim().length() >0){
            filter.append("order by "+ orderField + " ");
        }else{
            filter.append("order by id ");
        }

        if(orderDirection != null && orderDirection.trim().length() >0){
            filter.append(orderDirection + " ");
        }else{
            filter.append("desc ");
        }

        Map typeMap = new HashMap();
        typeMap.put(0, "全部");
        List<BlockArticleType> typeList = blockArticleTypeService.findAllBlockArticleTypes();
        for (BlockArticleType blockArticleType : typeList) {
            typeMap.put(blockArticleType.getId(), blockArticleType.getName());
        }
        modelAndView.addObject("typeMap", typeMap);

        List<BlockArticle> list = blockArticleService.listBlockArticlesByFilter((currentPage - 1) * numPerPage, numPerPage, filter.toString(), true);
        modelAndView.addObject("blockArticleList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "blockArticleList");
        modelAndView.addObject("currentPage", currentPage);

        //总数量
        modelAndView.addObject("totalCount", blockArticleService.countBlockArticleByFilter(filter.toString()));
        modelAndView.setViewName("ssadmin/blockArticleList");
        return modelAndView;
    }

    @RequestMapping("/goBlockArticleJSP")
    public ModelAndView goArticleJSP(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        ModelAndView modelAndView = new ModelAndView() ;
        if(request.getParameter("id") != null){
            int id = Integer.parseInt(request.getParameter("id"));
            BlockArticle blockArticle = blockArticleService.findBlockArticleById(id);
            modelAndView.addObject("blockArticle", blockArticle);
        }
        modelAndView.setViewName(url);
        return modelAndView;
    }

    @RequestMapping("/saveBlockArticle")
    @RequiresPermissions("/ssadmin/saveBlockArticle.html")
    public ModelAndView saveBlockArticle(
            @RequestParam(required=false) MultipartFile filedata,
            @RequestParam("blockArticleLookup.id") int blockArticleTypeId,
            @RequestParam(required=false) String title,
            @RequestParam(required=false) String keyword,
            @RequestParam(required = false) String imgUrl,
            @RequestParam(required=false) String content
    ) throws Exception{

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        String fpictureUrl = "";
        boolean isTrue = false;
        BlockArticle blockArticle = new BlockArticle();
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
                        && !ext.trim().toLowerCase().endsWith("docx")
                        && !ext.trim().toLowerCase().endsWith("xlsx")
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
            blockArticle.setDocUrl(fpictureUrl);
        }
        if(!"".equals(imgUrl)){
            blockArticle.setImgUrl(imgUrl);
        }
        BlockArticleType blockArticleType = blockArticleTypeService.findBlockArticleTypeById(blockArticleTypeId);
        blockArticle.setBlockArticleType(blockArticleType);
        blockArticle.setTitle(title);
        blockArticle.setKeyword(keyword);
        blockArticle.setContent(content);
        blockArticle.setCreateTime(Utils.getTimestamp());
        blockArticle.setLastUpdateTime(Utils.getTimestamp());
        blockArticleService.save(blockArticle);
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","新增成功");
        modelAndView.addObject("callbackType","closeCurrent");
        return modelAndView;
    }

    @RequestMapping("/deleteBlockArticle")
    @RequiresPermissions("/ssadmin/deleteBlockArticle.html")
    public ModelAndView deleteArticle(HttpServletRequest request) throws Exception{
        int id = Integer.parseInt(request.getParameter("id"));
        blockArticleService.deleteBlockArticleById(id);
        ModelAndView modelAndView = new ModelAndView() ;
        modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","删除成功");
        return modelAndView;
    }

    @RequestMapping("/updateBlockArticle")
    @RequiresPermissions("/ssadmin/updateBlockArticle.html")
    public ModelAndView updateArticle(
            @RequestParam(required=false) MultipartFile filedata,
            @RequestParam("blockArticleLookup.id") int blockArticleTypeId,
            @RequestParam(required=false) String title,
            @RequestParam(required=false) String keyword,
            @RequestParam(required = false) String imgUrl,
            @RequestParam(required = false) String isDelete,
            @RequestParam(required=false) String content,
            @RequestParam(required=false) int id
    ) throws Exception{
        ModelAndView modelAndView = new ModelAndView() ;
        BlockArticle blockArticle = blockArticleService.findBlockArticleById(id);
        if("1".equals(isDelete)){
            blockArticle.setDocUrl(null);
        }
        String pictureUrl = "";
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
                        && !ext.trim().toLowerCase().endsWith("docx")
                        && !ext.trim().toLowerCase().endsWith("ppt")
                        && !ext.trim().toLowerCase().endsWith("xls")
                        && !ext.trim().toLowerCase().endsWith("xlsx")
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
                    pictureUrl = "/"+Constants.AdminUploadDirectory+"/"+fileName ;
                    isTrue = true;
                }
            }
        }
        if(isTrue){
            blockArticle.setDocUrl(pictureUrl);
        }
        if(!"".equals(imgUrl)){
            blockArticle.setImgUrl(imgUrl);
        }
        BlockArticleType blockArticleType = blockArticleTypeService.findBlockArticleTypeById(blockArticleTypeId);
        blockArticle.setBlockArticleType(blockArticleType);
        blockArticle.setTitle(title);
        blockArticle.setKeyword(keyword);
        blockArticle.setContent(content);
        blockArticle.setLastUpdateTime(Utils.getTimestamp());
        blockArticleService.updateBlockArticle(blockArticle);

        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","修改成功");
        modelAndView.addObject("callbackType","closeCurrent");
        modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
        return modelAndView;
    }
}
