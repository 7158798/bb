package com.ruizton.main.controller.admin;

import com.alibaba.druid.stat.TableStat;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.oss.AliyunService;
import com.ruizton.util.GuavaUtils;
import com.ruizton.util.HttpClientUtils;
import com.ruizton.util.InformationApiUtils;
import com.ruizton.util.Utils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharsetUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.*;

/**
 * Created by sunpeng on 2016/10/9.
 */
@Controller
@RequestMapping("/ssadmin")
public class RoadShowController {

    @Autowired
    private AliyunService aliyunService;
    private static final String ROADSHOW_LIST_URL = "/api/getRoadShowList";
    private static final String ROADSHOW_COUNT_URL = "/api/countRoadShowList";
    private static final String ROADSHOW_UPDATE_URL = "/backstage/updateRoadShow";
    private static final String GET_ROADSHOW_BY_ID_URL = "/api/getRoadShow";
    private static final String DEL_ROADSHOW_BY_ID_URL = "/api/delRoadShow";
    private static final String GET_FILE_PATH_URL = "/api/getFilePath";
    private static final String DET_FILE_PATH_URL = "/backstage/delFilePath";
    private static final String COUNT_FILE_PATH_URL = "/api/countFilePath";
    private static final String UPLOAD_URL = "/api/uploadFile";
    private static final int NUM_PER_PAGE = 40;

    @Autowired
    private ConstantMap constantMap;
    @Value("${oss.cdn}")
    private String cdn;

    @RequestMapping("/roadShowList")
    @RequiresPermissions("/ssadmin/roadShowList.html")
    public ModelAndView list(@RequestParam(required = false) String orderField,
                             @RequestParam(required = false) String orderDirection,
                             @RequestParam(required = false, defaultValue = "1") int pageNum,
                             @RequestParam(required = false, defaultValue = "0") int checkStatus,
                             @RequestParam(required = false) String title,
                             @RequestParam(required = false) Integer articleId,
                             @RequestParam(required = false, defaultValue = "false") boolean isAudit,
                             @RequestParam(required = false, defaultValue = "false") boolean isDialog) {

        String url = constantMap.getString("informationUrl");
        ModelAndView modelAndView = new ModelAndView();
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        if(isAudit){
            modelAndView.addObject("isAudit", isAudit);
        }
        if(isDialog){
            modelAndView.addObject("isDialog", isDialog);
        }
        if(checkStatus != 0){
            paramMap.put("checkStatus", String.valueOf(checkStatus));
            modelAndView.addObject("checkStatus",checkStatus);
        }
        if(articleId != null){
            paramMap.put("articleId", String.valueOf(articleId));
            modelAndView.addObject("articleId", articleId);
        }
        Optional<Integer> intOptional = InformationApiUtils.sendGetRequest(url + ROADSHOW_COUNT_URL, paramMap);
        intOptional.ifPresent(count -> modelAndView.addObject("totalCount", count));
        if(StringUtils.hasText(orderField)){
            paramMap.put("modelName", orderField);
            modelAndView.addObject("orderField", orderField);
        }
        if(StringUtils.hasText(orderDirection)){
            paramMap.put("isDesc", orderDirection.equals("desc") ? "true" : "false");
            modelAndView.addObject("orderDirection", orderDirection);
        }
        if(StringUtils.hasText(title)){
            paramMap.put("title", title);
        }
        paramMap.put("size", String.valueOf(NUM_PER_PAGE));
        int start = (pageNum - 1) * NUM_PER_PAGE;
        paramMap.put("start", String.valueOf(start));
        Optional<JSONArray> listOptional = InformationApiUtils.sendGetRequest(url + ROADSHOW_LIST_URL, paramMap);
        listOptional.ifPresent( jsonArray -> {
            for (int i = 0; i < jsonArray.size(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Optional.ofNullable(jsonObject.getLong("createdTime")).ifPresent(createdTime -> jsonObject.put("createdTime", new Date(createdTime)));
                Optional.ofNullable(jsonObject.getLong("startTime")).ifPresent(createdTime -> jsonObject.put("startTime", new Date(createdTime)));
            }
            modelAndView.addObject("list", jsonArray);
        });

        modelAndView.addObject("title", title);
        modelAndView.addObject("currentPage", pageNum);
        modelAndView.addObject("numPerPage", NUM_PER_PAGE);
        modelAndView.setViewName("ssadmin/roadShowList");
        return modelAndView;

    }

    @RequestMapping("/goRoadshowJsp")
    public ModelAndView goRoadShowJsp(
            String url,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false, defaultValue = "false") boolean isAudit,
            @RequestParam(required = false, defaultValue = "false") boolean isDialog){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("isAudit", isAudit);
        String informationUrl = constantMap.getString("informationUrl");
        if(id != null){
            Map<String, String> map = GuavaUtils.newHashMap();
            map.put("id", String.valueOf(id));
            Optional<JSONObject> optionalObject = InformationApiUtils.sendGetRequest(informationUrl + GET_ROADSHOW_BY_ID_URL, map);
            optionalObject.ifPresent(jsonObject -> {
                Optional.ofNullable(jsonObject.getLong("startTime")).ifPresent(createdTime -> jsonObject.put("startTime", new Date(createdTime)));
                modelAndView.addObject("roadshow", jsonObject);
            });
        }
        if(isDialog){
            modelAndView.addObject("isDialog", isDialog);
        }
        modelAndView.setViewName(url);
        return modelAndView;

    }

    /**
     * 审核
     * @param id
     * @param checkStatus
     * @return
     */
    @RequestMapping("/updateRoadShow")
    @RequiresPermissions("/ssadmin/updateRoadShow.html")
    public ModelAndView updateRaodShow(
            int id,
            int checkStatus,
            @RequestParam(required = false) String speaker,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) boolean isAudit,
            @RequestParam(required = false, defaultValue = "false") boolean isDialog){
        String url = constantMap.getString("informationUrl");
        ModelAndView modelAndView = new ModelAndView();
        Map<String, String> map = GuavaUtils.newHashMap();
        map.put("id", String.valueOf(id));
        map.put("checkStatus", String.valueOf(checkStatus));
        if(startTime != null){
            map.put("startTime", startTime);
        }
        if(title != null){
            map.put("title", title);
        }
        if(speaker != null){
            map.put("speaker", speaker);
        }
        Optional<Integer> optionnal = InformationApiUtils.sendPostRequest(url + ROADSHOW_UPDATE_URL, map);
        optionnal.ifPresent(result -> {
            if(isAudit){
                if(result == 1){
                    modelAndView.addObject("message","审核成功");
                }else{
                    modelAndView.addObject("message", "审核失败");
                }
            }else{
                if(result == 1){
                    modelAndView.addObject("message","修改成功");
                }else{
                    modelAndView.addObject("message", "修改失败");
                }
            }
        });
        if(!isDialog){
            modelAndView.addObject("navTabId", isAudit ? "roadShowAuditList" : "roadShowList");
        }
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("/deleteRoadShow")
    @RequiresPermissions("/ssadmin/deleteRoadShow.html")
    public ModelAndView deleteRoadShow(
            int id,
            @RequestParam(required = false, defaultValue = "false") boolean isDialog){
        String url = constantMap.getString("informationUrl");
        ModelAndView  modelAndView = new ModelAndView();
        Map<String, String> map = GuavaUtils.newHashMap();
        map.put("id", String.valueOf(id));
        Optional<Integer> integerOptional = InformationApiUtils.sendPostRequest(url + DEL_ROADSHOW_BY_ID_URL, map);
        if(integerOptional.isPresent()){
            if(integerOptional.get() == 1){
                modelAndView.addObject("message", "删除成功");
            }else {
                modelAndView.addObject("message", "删除失败");
            }
        }else {
            modelAndView.addObject("message", "删除失败");
        }
        if(!isDialog){
            modelAndView.addObject("navTabId", "roadShowList");
        }
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode",200);
        return modelAndView;

    }

    @RequestMapping("/roadShowPictureList")
    @RequiresPermissions("/ssadmin/roadShowPictureList.html")
    public ModelAndView roadShowPictureList(
            int roadShowId,
            @RequestParam(required = false) String orderField,
            @RequestParam(required = false) String orderDirection,
            @RequestParam(required = false, defaultValue = "1") int pageNum){

        int NUM_PER_PAGE_2 = 10;
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        String url = constantMap.getString("informationUrl");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("cdn", cdn);
        paramMap.put("roadShowId", String.valueOf(roadShowId));
        modelAndView.addObject("roadShowId", roadShowId);
        Optional<Integer> intOptional = InformationApiUtils.sendGetRequest(url + COUNT_FILE_PATH_URL, paramMap);
        paramMap.put("id", String.valueOf(roadShowId));
        Optional<JSONObject> objectOptional = InformationApiUtils.sendGetRequest(url + GET_ROADSHOW_BY_ID_URL, paramMap);
        modelAndView.addObject("roadShow", objectOptional.orElse(null));
        intOptional.ifPresent(count -> modelAndView.addObject("totalCount", count));
        if(StringUtils.hasText(orderField)){
            paramMap.put("modelName", orderField);
            modelAndView.addObject("orderField", orderField);
        }
        if(StringUtils.hasText(orderDirection)){
            paramMap.put("isDesc", orderDirection.equals("desc") ? "true" : "false");
            modelAndView.addObject("orderDirection", orderDirection);
        }
        paramMap.put("size", String.valueOf(NUM_PER_PAGE_2));
        paramMap.put("start", String.valueOf((pageNum - 1) * NUM_PER_PAGE_2));

        Optional<JSONArray> listOptional = InformationApiUtils.sendGetRequest(url + GET_FILE_PATH_URL, paramMap);
        listOptional.ifPresent(jsonArray -> {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Optional.ofNullable(jsonObject.getLong("createdTime")).ifPresent(createdTime -> jsonObject.put("createdTime", new Date(createdTime)));
            }
            modelAndView.addObject("list", jsonArray);
        });
        modelAndView.addObject("pageNum", pageNum);
        modelAndView.addObject("numPerPage", NUM_PER_PAGE_2);
        modelAndView.setViewName("ssadmin/roadShowPictureList");
        return modelAndView;

    }
    @RequestMapping("/addRoadShowPictures")
    @RequiresPermissions("ssadmin/addRoadShowPictures.html")
    public ModelAndView addRoadShowPictures(int roadShowId, @RequestParam MultipartFile file) throws IOException {
        String url = constantMap.getString("informationUrl");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", "上传成功");
        String Filename = file.getOriginalFilename();
        String suffix = Filename.substring(Filename.lastIndexOf("."));
        //定义正则表达式
        String path = "upload/roadshow/" + roadShowId;
        path += "/" + getUUID() + suffix;
        try {
            aliyunService.uploadFile(file, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("indexing", String.valueOf(roadShowId));
        map.put("path", path);
        Optional<JSONArray> jsonArray = InformationApiUtils.sendPostRequest(url + "/api/uploadFilePath", map);
        jsonArray.ifPresent(array -> {
            modelAndView.addObject("message", "上传成功");
        });
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("callbackType","closeCurrent");
        return modelAndView;
    }

    @RequestMapping("/deleteRoadShowPictures")
    @RequiresPermissions("/ssadmin/deleteRoadShowPictures.html")
    public ModelAndView deleteRoadShowPictures(int[] ids){
        String url = constantMap.getString("informationUrl");
        Map<String, String> map = GuavaUtils.newHashMap();
        ModelAndView modelAndView = new ModelAndView();
        for (int id : ids) {
            map.put("id", String.valueOf(id));
            InformationApiUtils.sendPostRequest(url + DET_FILE_PATH_URL, map);
        }
        modelAndView.addObject("message", "删除成功");
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode",200);
        return modelAndView;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
        //return UUID.randomUUID().toString().toUpperCase();
    }


}
