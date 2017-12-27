package com.ruizton.main.controller.admin;

import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.model.LayoutInfo;
import com.ruizton.util.GuavaUtils;
import com.ruizton.util.InformationApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by sunpeng on 2016/9/26.
 */
@Controller
@RequestMapping("/ssadmin")
public class LayoutController {

    private static final int NUM_PER_PAGE = 40;
    @Autowired
    private ConstantMap constantMap;

    @RequestMapping("/layoutList")
    public ModelAndView listLayouts(HttpServletRequest request){
        String url = constantMap.getString("informationUrl");
        String pageUrl = constantMap.getString("pageUrl") + "details.html?id=";
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pageUrl", pageUrl);
        Map<Integer, String> frameNameMap = InformationApiUtils.getFrameNameMap(url);
        frameNameMap.put(0, "全部");
        modelAndView.addObject("frameNameMap", frameNameMap);

        String orderField = request.getParameter("orderField");
        if(orderField != null){
            paramMap.put("modelName", orderField);
            modelAndView.addObject("orderField", orderField);
        }
//        else{
//            paramMap.put("modelName", "frameId");
//            paramMap.put("isDesc", String.valueOf(false));
//        }

        String orderDirection = request.getParameter("orderDirection");
        if(orderDirection != null){
            paramMap.put("isDesc", String.valueOf(orderDirection.equals("desc") ? true : false));
            modelAndView.addObject("orderDirection", orderDirection);
        }
        String frameId = request.getParameter("frameId");
        if(frameId != null){
            paramMap.put("frameId", frameId);
            modelAndView.addObject("frameId", frameId);
        }

        String currentPage = request.getParameter("pageNum");
        if(currentPage == null){
            currentPage = "1";
        }
        int start = (Integer.valueOf(currentPage) - 1) * NUM_PER_PAGE;
        paramMap.put("start", String.valueOf(start));
        paramMap.put("size", String.valueOf(NUM_PER_PAGE));
        List<LayoutInfo> list = InformationApiUtils.listLayout(url, paramMap);
        for (int i = 0; i < list.size(); i++) {
            LayoutInfo layoutInfo = list.get(i);
            layoutInfo.setFrameName(frameNameMap.get(layoutInfo.getFrameId()));
        }
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("numPerPage", NUM_PER_PAGE);
        int count = InformationApiUtils.countLayout(url, paramMap);
        modelAndView.addObject("totalCount", count);
        modelAndView.addObject("rel", "layoutList");
        modelAndView.addObject("list", list);
        modelAndView.setViewName("ssadmin/layoutList");
        return modelAndView;
    }

    @RequestMapping("/goLayoutJSP")
    public ModelAndView goLayoutJSP(HttpServletRequest request){
        String apiUrl = constantMap.getString("informationUrl");
        String url = request.getParameter("url");
        ModelAndView modelAndView = new ModelAndView() ;
        modelAndView.setViewName(url) ;
        if(request.getParameter("id") != null){
            Map<String, String> map = GuavaUtils.newHashMap();
            map.put("id", request.getParameter("id"));
            LayoutInfo layoutInfo = InformationApiUtils.findLayoutById(apiUrl, map);
            modelAndView.addObject("layoutInfo", layoutInfo);
        }
        modelAndView.addObject("frameNameMap", InformationApiUtils.getFrameNameMap(apiUrl));
        return modelAndView;
    }

    /**
     * 新增Layout
     * @param articleId
     * @param frameId
     * @param rank
     * @return
     */
    @RequestMapping("/saveLayout")
    public ModelAndView saveLayout(
            @RequestParam(required = false, value = "informationArticleLookup.id") String articleId,
            @RequestParam(required = false) String frameId,
            @RequestParam(required = false) String rank){
        String url = constantMap.getString("informationUrl");
        Map<String, String> map = GuavaUtils.newHashMap();
        map.put("articleId", articleId);
        map.put("frameId", frameId);
        map.put("rank", rank);
        boolean isSuccess = InformationApiUtils.insertLayout(url, map);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("statusCode",200);
        if(isSuccess){
            modelAndView.addObject("message","新增成功");
        }else{
            modelAndView.addObject("message","新增失败");
        }
        modelAndView.addObject("callbackType","closeCurrent");
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        return modelAndView;
    }

    /**
     * 删除Layout
     * @param id
     * @return
     */
    @RequestMapping("/deleteLayout")
    public ModelAndView deleteLayout(String id){
        String url = constantMap.getString("informationUrl");
        Map<String, String> map = GuavaUtils.newHashMap();
        map.put("id", id);
        boolean isSuccess = InformationApiUtils.deleteLayout(url, map);
        ModelAndView modelAndView = new ModelAndView() ;
        modelAndView.addObject("statusCode",200);
        modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
        if(isSuccess){
            modelAndView.addObject("message","删除成功");
        }else {
            modelAndView.addObject("message","删除失败");
        }
        return modelAndView;
    }

    /**
     * 更新布局
     * @param layoutId
     * @param articleId
     * @param frameId
     * @param rank
     * @return
     */
    @RequestMapping("/updateLayout")
    public ModelAndView updateLayout(String layoutId, String articleId, String frameId, String rank){
        String url = constantMap.getString("informationUrl");
        Map<String, String> map = GuavaUtils.newHashMap();
        map.put("id", layoutId);
        map.put("articleId", articleId);
        map.put("frameId", frameId);
        map.put("rank", rank);
        boolean isSuccess = InformationApiUtils.updateLayout(url, map);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("statusCode",200);
        if(isSuccess){
            modelAndView.addObject("message","修改成功");
        }else {
            modelAndView.addObject("message","修改失败");
        }
        modelAndView.addObject("callbackType","closeCurrent");
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        return modelAndView;
    }
}
