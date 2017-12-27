package com.ruizton.main.controller.admin;

import com.ruizton.main.model.BlockArticleType;
import com.ruizton.main.service.admin.*;
import com.ruizton.util.Utils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by sunpeng on 2016/7/27.
 */
@Controller
@RequestMapping("/ssadmin")
public class BlockArticleTypeController {
    @Autowired
    private BlockArticleTypeService blockArticleTypeService;
    @Autowired
    private BlockArticleService blockArticleService;
    //每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/blockArticleTypeList")
    @RequiresPermissions("/ssadmin/blockArticleTypeList.html")
    public ModelAndView index(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView = new ModelAndView() ;
        modelAndView.setViewName("ssadmin/blockArticleTypeList") ;

        String pageNum = request.getParameter("pageNum");
        int currentPage = pageNum != null ? Integer.parseInt(pageNum) : 1;
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");

        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 ");
        if(keyWord != null && keyWord.trim().length() >0){
            filter.append("and (name like '%"+keyWord+"%' or ");
            modelAndView.addObject("keywords", keyWord);
        }

        if(orderField != null && orderField.trim().length() >0){
            filter.append("order by "+orderField+" ");
        }else{
            filter.append("order by id ");
        }

        if(orderDirection != null && orderDirection.trim().length() >0){
            filter.append(orderDirection+" ");
        }else{
            filter.append("desc ");
        }
        List<BlockArticleType> list = blockArticleTypeService.listBlockArticleTypes((currentPage - 1) * numPerPage, numPerPage, filter.toString(), true);
        modelAndView.addObject("blockArticleTypeList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "blockArticleTypeList");
        modelAndView.addObject("currentPage", currentPage);
        //总数量
        modelAndView.addObject("totalCount", blockArticleTypeService.countBlockArticleTypes(filter.toString()));
        return modelAndView ;
    }

    @RequestMapping("/goBlockArticleTypeJSP")
    public ModelAndView goAricleJSP(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        ModelAndView modelAndView = new ModelAndView() ;
        modelAndView.setViewName(url) ;
        if(request.getParameter("id") != null){
            int id = Integer.parseInt(request.getParameter("id"));
            BlockArticleType blockArticleType = blockArticleTypeService.findBlockArticleTypeById(id);
            modelAndView.addObject("blockArticleType", blockArticleType);
        }
        return modelAndView;
    }

    @RequestMapping("/saveBlockArticleType")
    @RequiresPermissions("/ssadmin/saveBlockArticleType.html")
    public ModelAndView saveArticleType(HttpServletRequest request) throws Exception{
        BlockArticleType blockArticleType = new BlockArticleType();
        blockArticleType.setCreateTime(Utils.getTimestamp());
        blockArticleType.setCreateTime(Utils.getTimestamp());
        blockArticleType.setName(request.getParameter("name"));
        blockArticleTypeService.saveBlockArticleType(blockArticleType);
        ModelAndView modelAndView = new ModelAndView() ;
        modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","新增成功");
        modelAndView.addObject("callbackType","closeCurrent");
        return modelAndView;
    }

    @RequestMapping("/updateBlockArticleType")
    @RequiresPermissions("/ssadmin/updateBlockArticleType.html")
    public ModelAndView updateArticleType(HttpServletRequest request) throws Exception{
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        BlockArticleType blockArticleType = blockArticleTypeService.findBlockArticleTypeById(id);
        blockArticleType.setName(name);
        blockArticleType.setLastUpdateTime(Utils.getTimestamp());
        blockArticleTypeService.updateBlockArticleType(blockArticleType);
        ModelAndView modelAndView = new ModelAndView() ;
        modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","更新成功");
        modelAndView.addObject("callbackType","closeCurrent");
        return modelAndView;
    }

    @RequestMapping("/deleteBlockArticleType")
    @RequiresPermissions("/ssadmin/deleteBlockArticleType.html")
    public ModelAndView deleteArticleType(HttpServletRequest request) throws Exception{
        int id = Integer.parseInt(request.getParameter("id"));
        ModelAndView modelAndView = new ModelAndView() ;
        int count = blockArticleService.countBlockArticleByType(id);
        if(count > 0){
            modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","删除失败，该文章类型已被引用");
            return modelAndView;
        }
        blockArticleTypeService.deleteBlockArticleTypeById(id);
        modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","删除成功");
        return modelAndView;
    }

    @RequestMapping("/blockArticleTypeLookup")
    public ModelAndView forLookUp(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView = new ModelAndView() ;
        modelAndView.setViewName("ssadmin/blockArticleTypeLookup");

        String keyWord = request.getParameter("keywords");
        String pageNum = request.getParameter("pageNum");
        int currentPage = pageNum != null ? Integer.parseInt(pageNum) : 1;
        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 ");
        if(keyWord != null && keyWord.trim().length() >0){
            filter.append("and (name like '%"+keyWord+"%' ");
            modelAndView.addObject("keywords", keyWord);
        }
        List<BlockArticleType> list = blockArticleTypeService.listBlockArticleTypes((currentPage - 1) * numPerPage, numPerPage, filter.toString(), true);
        modelAndView.addObject("blockArticleTypeList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "blockArticleTypeList");
        modelAndView.addObject("currentPage", currentPage);
        //总数量
        modelAndView.addObject("totalCount", blockArticleTypeService.countBlockArticleTypes(filter.toString()));
        return modelAndView ;
    }
}
