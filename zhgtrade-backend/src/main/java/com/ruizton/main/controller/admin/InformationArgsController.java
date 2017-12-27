package com.ruizton.main.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.util.GuavaUtils;
import com.ruizton.util.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Created by sunpeng on 2016/9/22.
 */
@Controller
@RequestMapping("/ssadmin")
public class InformationArgsController {

    private static final String ARGS_LIST_URL = "/api/getParamList";
    private static final String FIND_ARGS_BY_KEY = "/api/getParam";
    private static final String INSERT_ARGS_URL = "/backstage/insertParam";
    private static final String DELETE_ARGS_URL = "/backstage/deleteParam";
    private static final String UPDATE_ARGS_URL = "/backstage/updateParam";

    @Autowired
    private ConstantMap constantMap;

    /**
     * 参数列表
     * @return
     */
    @RequestMapping("/informationArgsList")
    public ModelAndView list(){
        String url = constantMap.getString("informationUrl");
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> map = GuavaUtils.newHashMap();
        String argsListResponse = HttpClientUtils.get(url + ARGS_LIST_URL);
        JSONObject jsonObject = JSON.parseObject(argsListResponse);
        JSONArray argsListArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < argsListArray.size(); i++) {
            JSONObject argsObject = argsListArray.getJSONObject(i);
            map.put(argsObject.getString("key"), HtmlUtils.htmlEscape(argsObject.getString("value")));
        }
        modelAndView.addObject("map", map);
        modelAndView.setViewName("ssadmin/informationArgsList");
        return modelAndView;
    }


    /**
     * 跳转
     * @param request
     * @return
     */
    @RequestMapping("/goInformationArgsJsp")
    public ModelAndView goInformationArgsJsp(HttpServletRequest request){
        String url = constantMap.getString("informationUrl");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(request.getParameter("url"));
        Optional<String> optional = Optional.ofNullable(request.getParameter("key"));
        if(optional.isPresent()){
            String key = optional.get();
            Map<String, String> paramMap = GuavaUtils.newHashMap();
            paramMap.put("key", key);
            String argsResponse = HttpClientUtils.get(url + FIND_ARGS_BY_KEY, paramMap);
            JSONObject responseObject = JSON.parseObject(argsResponse);
            JSONObject argsObject = responseObject.getJSONObject("data");
            modelAndView.addObject("key", argsObject.getString("key"));
            modelAndView.addObject("value", HtmlUtils.htmlEscape(argsObject.getString("value")));
        }
        return modelAndView;
    }

    /**
     * 插入参数
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/saveInformationArgs")
    public ModelAndView saveInformationArgs(
            HttpServletRequest request,
            @RequestParam (required = false)String key,
            @RequestParam (required = false)String value,
            @RequestParam (required = false)MultipartFile file
    ) throws IOException {

        String url = constantMap.getString("informationUrl");
        ModelAndView modelAndView = new ModelAndView();
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        paramMap.put("key", key);
        paramMap.put("value", value);
        if(file != null && !file.isEmpty()){
            String result = InformationArticleController.saveFile(request, file);
            if(result.equals("")){
                modelAndView.addObject("statusCode",300);
                modelAndView.addObject("message","非jpg、png、gif文件格式");
                return modelAndView;
            }else if(result.equals("other")){
                System.out.println("图片保存失败");
            }else {
                paramMap.put("value", result);
            }

        }
        String response = HttpClientUtils.post(url + INSERT_ARGS_URL, paramMap);
        JSONObject jsonObject = JSON.parseObject(response);
        int result = jsonObject.getIntValue("data");
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode",200);
        if(result == 1){
            modelAndView.addObject("message","保存成功");
        }else {
            modelAndView.addObject("message", "保存失败");
        }
        modelAndView.addObject("callbackType","closeCurrent");
        return modelAndView;
    }


    /**
     * 删除参数
     * @param id
     * @return
     */
    @RequestMapping("/deleteInformationArgs")
    public ModelAndView deleteInformationArgs(String id){
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        paramMap.put("id", id);
        String url = constantMap.getString("informationUrl");
        String response = HttpClientUtils.post(url + DELETE_ARGS_URL, paramMap);
        JSONObject jsonObject = JSON.parseObject(response);
        int result = jsonObject.getIntValue("data");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("statusCode",200);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        if(result == 1){
            modelAndView.addObject("message","删除成功");
        }else {
            modelAndView.addObject("message", "删除失败");
        }
        return modelAndView;
    }


    /**
     * 更新参数
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/updateInformationArgs")
    public ModelAndView updateInformationArgs(
            HttpServletRequest request,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String key,
            @RequestParam(required = false) String value
    ) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        String url = constantMap.getString("informationUrl");
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        paramMap.put("key", key);
        paramMap.put("value", value);
        if(file != null && !file.isEmpty()){
            String result = InformationArticleController.saveFile(request, file);
            if(result.equals("")){
                modelAndView.addObject("statusCode",300);
                modelAndView.addObject("message","非jpg、png、gif文件格式");
                return modelAndView;
            }else if(result.equals("other")){
                System.out.println("图片保存失败");
            }else {
                paramMap.put("value", result);
            }

        }
        String response = HttpClientUtils.post(url + UPDATE_ARGS_URL, paramMap);
        JSONObject jsonObject = JSON.parseObject(response);
        int result = jsonObject.getIntValue("data");
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode",200);
        if(result == 1){
            modelAndView.addObject("message","修改成功");
        }else {
            modelAndView.addObject("message", "修改失败");
        }
        modelAndView.addObject("callbackType","closeCurrent");
        return modelAndView;
    }

}
