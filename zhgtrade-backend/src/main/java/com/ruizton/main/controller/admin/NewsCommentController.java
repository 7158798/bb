package com.ruizton.main.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.util.ConstantKeys;
import com.ruizton.util.Constants;
import com.ruizton.util.InformationApiUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 资讯评论
 *
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/9/29
 */
@Controller
@RequestMapping("/ssadmin")
public class NewsCommentController {
    @Autowired
    private ConstantMap constantMap;

    public String getSendUrl(String uri){
        return constantMap.getString(ConstantKeys.INFORMATION_URL) + uri;
    }

    /**
     * 品论列表
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/commentList")
    @RequiresPermissions(("ssadmin/commentList.html"))
    public Object commentList(ModelMap modelMap,
                              @RequestParam(required = false, defaultValue = "1")int pageNum,
                              @RequestParam(required = false)String articleId,
                              @RequestParam(required = false)String orderField,
                              @RequestParam(required = false)String orderDirection){

        Map params = new HashMap<>();
        params.put("start", String.valueOf((pageNum - 1) * Constants.PAGE_ITEM_COUNT_20));
        params.put("size", String.valueOf(Constants.PAGE_ITEM_COUNT_20));
        if(StringUtils.hasText(orderField)){
            params.put("modelName", orderField);
        }
        if(StringUtils.hasText(articleId)){
            params.put("articleId", articleId);
        }
        if("desc".equalsIgnoreCase(orderDirection)){
            params.put("isDesc", Boolean.TRUE.toString());
        }else{
            params.put("isDesc", Boolean.FALSE.toString());
        }
        Optional<JSONArray> arrOptional = InformationApiUtils.sendGetRequest(getSendUrl("/backstage/getCommentList"), params);
        arrOptional.ifPresent(arr -> {
            arr.forEach(e -> {
                JSONObject obj = (JSONObject) e;
                obj.put("createdTime", new Date(obj.getLongValue("createdTime")));
            });
            modelMap.put("list", arr);
        });

        int totalCount = 0;
        Optional<Integer> countOptional = InformationApiUtils.sendGetRequest(getSendUrl("/backstage/countComment"), params);
        if(countOptional.isPresent()){
            totalCount = countOptional.get();
        }

        modelMap.put("pageNum", pageNum);
        modelMap.put("articleId", articleId);
        modelMap.put("totalCount", totalCount);
        modelMap.put("numPerPage", Constants.PAGE_ITEM_COUNT_20);
        return "ssadmin/commentList";
    }

    /**
     * 删除评论
     *
     * @param modelMap
     * @param ids
     * @return
     */
    @RequestMapping("/delComment")
    @RequiresPermissions("ssadmin/delComment.html")
    public Object delComment(ModelMap modelMap, String ids){
        Map params = new HashMap<>();
        params.put("id", ids);
        Optional<Integer> optional = InformationApiUtils.sendPostRequest(getSendUrl("/backstage/delComment"), params);
        if(optional.isPresent() && optional.get() > 0){
            modelMap.put("statusCode",200);
            modelMap.put("message","删除成功");
        }else{
            modelMap.put("statusCode",300);
            modelMap.put("message","删除失败");
        }
        return "ssadmin/comm/ajaxDone";
    }
}
