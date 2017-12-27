package com.ruizton.main.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.model.KeywordInfo;
import com.ruizton.util.GuavaUtils;
import com.ruizton.util.HttpClientUtils;
import com.ruizton.util.InformationApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by sunpeng on 2016/9/22.
 */
@Controller
@RequestMapping("/ssadmin")
public class KeywordController {

//    private static final String url = "http://192.168.0.233:8088";
    private static final String KEYWORD_LIST_URL = "/api/getKeywordList";
    private static final String KEYWORD_COUNT_URL = "/api/countKeywordList";
    private static final String TYPE_LIST_URL =  "/api/getClassList?isMapList=true";
    private static final String INSERT_URL =  "/backstage/insertKeyword";
    private static final String UPDATE_URL =  "/backstage/updateKeyword";
    private static final String DELETE_URL =  "/backstage/deleteKeyword";
    private static final String FIND_BY_ID_URL =  "/backstage/getKeywordById";
    private static final int NUM_PER_PAGE = 40;

    @Autowired
    private ConstantMap constantMap;

    @RequestMapping("/keywordList")
    public ModelAndView list(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer pageNum
    ){
        String url = constantMap.getString("informationUrl");
        ModelAndView modelAndView = new ModelAndView();
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        if(type != null){
            modelAndView.addObject("type", type);
            paramMap.put("classId", String.valueOf(type));
        }

        if(pageNum != null){
            paramMap.put("start", String.valueOf((pageNum - 1) * NUM_PER_PAGE));
        }else {
            pageNum = 1;
        }
        paramMap.put("size", String.valueOf(NUM_PER_PAGE));
        modelAndView.addObject("currentPage", pageNum);
        modelAndView.addObject("numPerPage", NUM_PER_PAGE);
        String response = HttpClientUtils.get(url + KEYWORD_LIST_URL, paramMap);
        JSONArray jsonArray = JSON.parseObject(response).getJSONArray("data");
        List<KeywordInfo> keywordList = JSON.parseArray(jsonArray.toString(), KeywordInfo.class);
        int totolCount = InformationApiUtils.countKeyword(url, paramMap);
        modelAndView.addObject("totalCount", totolCount);
        Map<Integer, String> typeNameMap = getTypeMap();
        for (KeywordInfo keywordInfo : keywordList) {
            keywordInfo.setClassName(typeNameMap.get(keywordInfo.getClassId()));
        }
        modelAndView.addObject("keywordList", keywordList);
        modelAndView.addObject("typeNameMap", typeNameMap);
        modelAndView.setViewName("ssadmin/keywordList");
        return modelAndView;
    }

    /**
     * 跳转
     * @param request
     * @return
     */
    @RequestMapping("/goKeywordJSP")
    public ModelAndView goArticleJsp(HttpServletRequest request){

        String url = constantMap.getString("informationUrl");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(request.getParameter("url"));
        Optional<String> optional = Optional.ofNullable(request.getParameter("id"));
        if(optional.isPresent()){
            Map<Integer, String> typeMap = InformationApiUtils.getTypeNameMap(url);
            Map<String, String> paramMap = GuavaUtils.newHashMap();
            paramMap.put("id", optional.get());
            String keywordResponse = HttpClientUtils.get(url + FIND_BY_ID_URL, paramMap);
            JSONObject keywordResponseObject = JSON.parseObject(keywordResponse);
            JSONObject keywordObject = keywordResponseObject.getJSONObject("data");
            KeywordInfo keywordInfo = JSON.parseObject(keywordObject.toString(), KeywordInfo.class);
            keywordInfo.setClassName(typeMap.get(keywordInfo.getClassId()));
            modelAndView.addObject("keywordInfo", keywordInfo);
        }
        Optional<String> optional1 = Optional.ofNullable(request.getParameter("classId"));
        if(optional1.isPresent()){
            Map<Integer, String> typeMap = getTypeMap();
            modelAndView.addObject("typeId", optional1.get());
            modelAndView.addObject("typeName",typeMap.get(optional1.get()));
        }

        return modelAndView;
    }

    /**
     * 保存keyword
     * @param classId
     * @param keyName
     * @return
     */
    @RequestMapping("/saveKeyword")
    public ModelAndView saveKeyword(
            @RequestParam (required = false)Integer classId,
            @RequestParam (required = false)String keyName
    ){
        String url = constantMap.getString("informationUrl");
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        paramMap.put("classId", String.valueOf(classId));
        paramMap.put("keyName", keyName);
        String response = HttpClientUtils.post(url + INSERT_URL, paramMap);
        ModelAndView modelAndView = new ModelAndView();
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
     * 删除keyword
     * @param id
     * @return
     */
    @RequestMapping("/deleteKeyword")
    public ModelAndView deleteKeyword(String id){
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        paramMap.put("id", id);
        String url = constantMap.getString("informationUrl");
        String response = HttpClientUtils.post(url + DELETE_URL, paramMap);
        JSONObject jsonObject = JSON.parseObject(response);
        int result = jsonObject.getIntValue("data");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode",200);
        if(result == 1){
            modelAndView.addObject("message","删除成功");
        }else {
            modelAndView.addObject("message", "删除失败");
        }
        return modelAndView;
    }

    /**
     * 修改keyword
     * @param classId
     * @param keyName
     * @return
     */
    @RequestMapping("/updateKeyword")
    public ModelAndView updateKeyword(
            int id,
            @RequestParam(required = false) Integer classId,
            @RequestParam(required = false) String keyName
    ){
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        paramMap.put("classId", String.valueOf(classId));
        paramMap.put("keyName", keyName);
        paramMap.put("id", String.valueOf(id));
        String url = constantMap.getString("informationUrl");
        String response = HttpClientUtils.post(url + UPDATE_URL, paramMap);

        ModelAndView modelAndView = new ModelAndView();
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

    /**
     * 获得资讯文章的所有类型
     * @return
     */
    private Map<Integer, String> getTypeMap(){

        String url = constantMap.getString("informationUrl");
        String typeResponse = HttpClientUtils.get(url + TYPE_LIST_URL);
        JSONObject typeObject = JSON.parseObject(typeResponse);
        JSONArray typeArray = typeObject.getJSONArray("data");
        Map<Integer, String> typeMap = GuavaUtils.newHashMap();
        for (int i = 0; i < typeArray.size(); i++) {
            JSONObject jsonObject = typeArray.getJSONObject(i);
            typeMap.put(jsonObject.getIntValue("id"), jsonObject.getString("className"));
        }
        typeMap.put(0, "全部");
        return typeMap;
    }

    /**
     *
     * @return
     */
    @RequestMapping("/informationKeywordLookup")
    public ModelAndView forLookUp(
            @RequestParam(required = false) String classId
    ){
        ModelAndView modelAndView = new ModelAndView();
        String url = constantMap.getString("informationUrl");
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        if(classId != null){
            paramMap.put("classId", classId);
        }
        String response = HttpClientUtils.get(url + KEYWORD_LIST_URL, paramMap);
        JSONArray jsonArray = JSON.parseObject(response).getJSONArray("data");
        List<KeywordInfo> keywordList = JSON.parseArray(jsonArray.toString(), KeywordInfo.class);
        Map<Integer, String> typeNameMap = getTypeMap();
        for (KeywordInfo keywordInfo : keywordList) {
            keywordInfo.setClassName(typeNameMap.get(keywordInfo.getClassId()));
        }
        modelAndView.addObject("keywordList", keywordList);
        modelAndView.addObject("typeNameMap", typeNameMap);
        modelAndView.setViewName("ssadmin/keywordList");
        modelAndView.setViewName("ssadmin/informationKeyowrdLookup");
        return modelAndView;
    }


    /**
     * keyword JSON数据
     * @param classId
     * @return
     */
    @ResponseBody
    @RequestMapping("/keywordListJson")
    public List<KeywordInfo> keywordListJson(@RequestParam(required = false) String classId){
        String url = constantMap.getString("informationUrl");
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        if(classId != null){
            paramMap.put("classId", classId);
        }
        String response = HttpClientUtils.get(url + KEYWORD_LIST_URL, paramMap);
        JSONArray jsonArray = JSON.parseObject(response).getJSONArray("data");
        List<KeywordInfo> keywordList = JSON.parseArray(jsonArray.toString(), KeywordInfo.class);
        return keywordList;
    }

}
