package com.ruizton.main.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.model.KeywordIndexInfo;
import com.ruizton.util.ConstantKeys;
import com.ruizton.util.Constants;
import com.ruizton.util.GuavaUtils;
import com.ruizton.util.InformationApiUtils;
import com.ruizton.util.Utils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 项目库
 *
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/9/27
 */
@Controller
@RequestMapping("/ssadmin")
public class ProjectLibraryController {
    @Autowired
    private ConstantMap constantMap;

    public String getSendUrl(String uri){
        return constantMap.getString(ConstantKeys.INFORMATION_URL) + uri;
    }

    @RequestMapping("/projectList")
    @RequiresPermissions("ssadmin/projectList.html")
    public Object projectList(ModelMap map,
                              @RequestParam(required = false, defaultValue = "1")int pageNum,
                              @RequestParam(required = false)String orderField,
                              @RequestParam(required = false)String orderDirection){
        Map params = new HashMap<>();
        params.put("start", String.valueOf((pageNum - 1) * Constants.PAGE_ITEM_COUNT_40));
        params.put("size", String.valueOf(Constants.PAGE_ITEM_COUNT_40));
        if(StringUtils.hasText(orderField)){
            params.put("modelName", orderField);
        }
        if("desc".equalsIgnoreCase(orderDirection)){
            params.put("isDesc", Boolean.TRUE.toString());
        }else{
            params.put("isDesc", Boolean.FALSE.toString());
        }

        Optional<JSONArray> arrOptional = InformationApiUtils.sendGetRequest(getSendUrl("/backstage/getProjectList"), params);
        arrOptional.ifPresent((jsonArr) -> {
            jsonArr.forEach(e -> {
                JSONObject obj = (JSONObject) e;
                Optional.ofNullable(obj.getLong("createdTime")).ifPresent((time) -> obj.put("createdTime", new Date(time)));
                Optional.ofNullable(obj.getLong("companyCreatedTime")).ifPresent((time) -> obj.put("companyCreatedTime", new Date(time)));
            });
            map.put("list", jsonArr);
        });

        int totalCount = 0;
        Optional<Integer> countOptional = InformationApiUtils.sendGetRequest(getSendUrl("/backstage/countProject"), params);
        if(countOptional.isPresent()){
            totalCount = countOptional.get();
        }

        map.put("pageNum", pageNum);
        map.put(ConstantKeys.NEWS_WEB_URL, constantMap.getString(ConstantKeys.NEWS_WEB_URL));
        map.put("totalCount", totalCount);
        map.put("numPerPage", Constants.PAGE_ITEM_COUNT_40);
        return "ssadmin/projectList";
    }

    @RequestMapping("/editProject")
    @RequiresPermissions(value = {"ssadmin/addProject.html", "ssadmin/updateProject.html"})
    public Object editProject(@RequestParam(required = false, defaultValue = "0")int articleId, ModelMap map){
        if(articleId > 0){
            // 获取项目信息
            Map params = new HashMap<>();
            params.put("articleId", String.valueOf(articleId));
            Optional<JSONObject> optional = InformationApiUtils.sendGetRequest(getSendUrl("/backstage/getProjectByArticleId"), params);
            optional.ifPresent(e -> {
                Optional.ofNullable(e.getLong("createdTime")).ifPresent((time) -> e.put("createdTime", new Date(time)));
                Optional.ofNullable(e.getLong("companyCreatedTime")).ifPresent((time) -> e.put("companyCreatedTime", new Date(time)));
                map.put("pro", e);
            });

            // 已关联关键字
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("articleId", articleId + "");
            Optional<JSONArray> keywordIdOptional = InformationApiUtils.sendGetRequest(getSendUrl("/backstage/getKeywordIndexByArticleId"), paramMap);
            keywordIdOptional.ifPresent(e -> {
                Map idMap = new HashMap();
                e.forEach(obj -> {
                    idMap.put(((JSONObject) obj).getLongValue("keywordId"), true);
                });
                map.put("keywordMap", idMap);
            });
        }

        // 获取关键字
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("classId", "15");
        Optional<JSONArray> keywordOptional = InformationApiUtils.sendGetRequest(getSendUrl("/api/getKeywordList"), paramMap);
        keywordOptional.ifPresent(e -> map.put("keywordList", e));

        return "ssadmin/editProject";
    }

    @RequestMapping("/addProject")
    @RequiresPermissions("ssadmin/addProject.html")
    public Object addProject(ModelMap map,
                             String projectName,
                             int authorId,
                             @RequestParam(required = false)String createdTime,
                             @RequestParam(required = false, defaultValue = "0")String articleId,
                             @RequestParam(required = false, defaultValue = "0")String teamSize,
                             @RequestParam(required = false)String companyName,
                             @RequestParam(required = false)String companyLocation,
                             @RequestParam(required = false)String financingRound,
                             @RequestParam(required = false)String companyCreatedTime,
                             @RequestParam(required = false)String ceo,
                             @RequestParam(required = false)String headImgPath,
                             @RequestParam(required = false)MultipartFile headImgFile,
                             @RequestParam(required = false)String ceoIntroduction,
                             @RequestParam(required = false)String introduction,
                             @RequestParam(required = false, defaultValue = "0")String projectType,

                             @RequestParam(required = false) String title,
                             @RequestParam(required = false) int classId,
                             @RequestParam(required = false) String[] keywords,
                             @RequestParam(required = false) MultipartFile imgFile,
                             @RequestParam(required = false) String imgPath,
                             @RequestParam(required = false) String sources,
                             @RequestParam(required = false) String authorName,
                             @RequestParam(required = false) String readingNum,
                             @RequestParam(required = false) String originalText,
                             @RequestParam(required = false) String content){

        Map params = new HashMap<>();
        params.put("projectName", projectName);
        params.put("authorId", String.valueOf(authorId));
        params.put("createdTime", createdTime);
        params.put("articleId", articleId);
        params.put("teamSize", teamSize);
        params.put("companyName", companyName);
        params.put("companyLocation", companyLocation);
        params.put("financingRound", financingRound);
        params.put("companyCreatedTime", companyCreatedTime);
        params.put("CEO", ceo);
        params.put("CEOIntroduction", ceoIntroduction);
        params.put("introduction", introduction);
        params.put("projectType", projectType);

        params.put("title", title);
        params.put("classId", classId + "");
        params.put("imgPath", imgPath);
        params.put("headImgPath", headImgPath);
        if(imgFile != null && !imgFile.isEmpty()){
            try {
                byte[] bytes = imgFile.getBytes();
                if(!Utils.isImage(bytes)){
                    // 不是有效图片文件
                    map.put("statusCode",300);
                    map.put("message","非法图片");
                    return "ssadmin/comm/ajaxDone";
                }
                String ext = com.ruizton.util.StringUtils.getFilenameExtension(imgFile.getOriginalFilename());
                String filePath = Constants.AdminUploadInformationDirectory + Utils.getRelativeFilePath(ext, bytes);
                boolean flag = Utils.uploadFileToOss(imgFile.getInputStream(), filePath);
                if(flag){
                    params.put("imgPath", filePath);
                }else{
                    map.put("statusCode",300);
                    map.put("message","上传图片失败");
                    return "ssadmin/comm/ajaxDone";
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if(headImgFile != null && !headImgFile.isEmpty()){
            try {
                byte[] bytes = headImgFile.getBytes();
                if(!Utils.isImage(bytes)){
                    // 不是有效图片文件
                    map.put("statusCode",300);
                    map.put("message","非法图片");
                    return "ssadmin/comm/ajaxDone";
                }
                String ext = com.ruizton.util.StringUtils.getFilenameExtension(headImgFile.getOriginalFilename());
                String imgFilePath = Constants.AdminUploadInformationDirectory + Utils.getRelativeFilePath(ext, bytes);
                boolean flag = Utils.uploadFileToOss(headImgFile.getInputStream(), imgFilePath);
                if(flag){
                    params.put("headImgPath", imgFilePath);
                }else{
                    map.put("statusCode",300);
                    map.put("message","上传图片失败");
                    return "ssadmin/comm/ajaxDone";
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        params.put("sources", sources);
        params.put("authorName", authorName);
        params.put("content", content);
        params.put("originalText", originalText);
        params.put("readingNum", readingNum);

        Optional<Integer> optional = InformationApiUtils.sendPostRequest(getSendUrl("/backstage/insertProject"), params);

        if(optional.isPresent() && optional.get() > 0){
            //关键字
            articleId = String.valueOf(optional.get());
            Map<String, String> keywordMap = GuavaUtils.newHashMap();
            keywordMap.put("articleId", articleId);
            List<KeywordIndexInfo> keywordIndexList = InformationApiUtils.findKeywordsByArticleId(constantMap.getString(ConstantKeys.INFORMATION_URL), keywordMap);
            for (KeywordIndexInfo keywordIndexInfo : keywordIndexList) {
                keywordMap.put("id", String.valueOf(keywordIndexInfo.getId()));
                InformationApiUtils.deleteKeyword(constantMap.getString(ConstantKeys.INFORMATION_URL), keywordMap);
            }
            if(keywords != null){
                for (String keywordId : keywords){
                    addArticleKeyword(constantMap.getString(ConstantKeys.INFORMATION_URL), articleId, title, content, keywordId);
                }
            }

            map.put("statusCode",200);
            map.put("message", "新增成功");
            map.put("callbackType","closeCurrent");
        }else{
            map.put("statusCode",300);
            map.put("message", "新增失败");
        }

        return "ssadmin/comm/ajaxDone";
    }

    private void addArticleKeyword(String url, String articleId, String title, String content, String keywordId){
        Map<String, String> keywordParam = GuavaUtils.newHashMap();
        keywordParam.put("articleId", articleId);
        keywordParam.put("keywordId", keywordId);
        keywordParam.put("content", content);
        keywordParam.put("title", title);
        InformationApiUtils.insertKeyword(url, keywordParam);
    }

    @RequestMapping("/updateProject")
    @RequiresPermissions("ssadmin/updateProject.html")
    public Object updateProject(ModelMap map,
                                String projectName,
                                int authorId,
                                @RequestParam(required = false)String createdTime,
                                @RequestParam(required = false, defaultValue = "0")String articleId,
                                @RequestParam(required = false, defaultValue = "0")String teamSize,
                                @RequestParam(required = false)String companyName,
                                @RequestParam(required = false)String companyLocation,
                                @RequestParam(required = false)String financingRound,
                                @RequestParam(required = false)String companyCreatedTime,
                                @RequestParam(required = false)String ceo,
                                @RequestParam(required = false)String headImgPath,
                                @RequestParam(required = false)MultipartFile headImgFile,
                                @RequestParam(required = false)String ceoIntroduction,
                                @RequestParam(required = false)String introduction,
                                @RequestParam(required = false, defaultValue = "0")String projectType,

                                @RequestParam(required = false) String title,
                                @RequestParam(required = false) int classId,
                                @RequestParam(required = false) String[] keywords,
                                @RequestParam(required = false) MultipartFile imgFile,
                                @RequestParam(required = false) String imgPath,
                                @RequestParam(required = false) String sources,
                                @RequestParam(required = false) String authorName,
                                @RequestParam(required = false) String readingNum,
                                @RequestParam(required = false) String originalText,
                                @RequestParam(required = false) String content){


        Map params = new HashMap<>();
        params.put("projectName", projectName);
        params.put("authorId", String.valueOf(authorId));
        params.put("createdTime", createdTime);
        params.put("articleId", articleId);
        params.put("teamSize", teamSize);
        params.put("companyName", companyName);
        params.put("companyLocation", companyLocation);
        params.put("financingRound", financingRound);
        params.put("companyCreatedTime", companyCreatedTime);
        params.put("CEO", ceo);
        params.put("CEOIntroduction", ceoIntroduction);
        params.put("introduction", introduction);
        params.put("projectType", projectType);

        params.put("title", title);
        params.put("classId", classId + "");
        params.put("imgPath", imgPath);
        params.put("headImgPath", headImgPath);
        if(imgFile != null && !imgFile.isEmpty()){
            try {
                byte[] bytes = imgFile.getBytes();
                if(!Utils.isImage(bytes)){
                    // 不是有效图片文件
                    map.put("statusCode",300);
                    map.put("message","非法图片");
                    return "ssadmin/comm/ajaxDone";
                }
                String ext = com.ruizton.util.StringUtils.getFilenameExtension(imgFile.getOriginalFilename());
                String filePath = Constants.AdminUploadInformationDirectory + Utils.getRelativeFilePath(ext, bytes);
                boolean flag = Utils.uploadFileToOss(imgFile.getInputStream(), filePath);
                if(flag){
                    params.put("imgPath", filePath);
                }else{
                    map.put("statusCode",300);
                    map.put("message","上传图片失败");
                    return "ssadmin/comm/ajaxDone";
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if(headImgFile != null && !headImgFile.isEmpty()){
            try {
                byte[] bytes = headImgFile.getBytes();
                if(!Utils.isImage(bytes)){
                    // 不是有效图片文件
                    map.put("statusCode",300);
                    map.put("message","非法图片");
                    return "ssadmin/comm/ajaxDone";
                }
                String ext = com.ruizton.util.StringUtils.getFilenameExtension(headImgFile.getOriginalFilename());
                String headFilePath = Constants.AdminUploadInformationDirectory + Utils.getRelativeFilePath(ext, bytes);
                boolean flag = Utils.uploadFileToOss(headImgFile.getInputStream(), headFilePath);
                if(flag){
                    params.put("headImgPath", headFilePath);
                }else{
                    map.put("statusCode",300);
                    map.put("message","上传图片失败");
                    return "ssadmin/comm/ajaxDone";
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        params.put("sources", sources);
        params.put("authorName", authorName);
        params.put("content", content);
        params.put("originalText", originalText);
        params.put("readingNum", readingNum);

        Optional<Integer> optional = InformationApiUtils.sendPostRequest(getSendUrl("/backstage/updateProject"), params);
        if(optional.isPresent() && optional.get() > 0){
            //关键字
            Map<String, String> keywordMap = GuavaUtils.newHashMap();
            keywordMap.put("articleId", articleId);
            List<KeywordIndexInfo> keywordIndexList = InformationApiUtils.findKeywordsByArticleId(constantMap.getString(ConstantKeys.INFORMATION_URL), keywordMap);
            for (KeywordIndexInfo keywordIndexInfo : keywordIndexList) {
                keywordMap.put("id", String.valueOf(keywordIndexInfo.getId()));
                InformationApiUtils.deleteKeyword(constantMap.getString(ConstantKeys.INFORMATION_URL), keywordMap);
            }
            if(keywords != null){
                for (String keywordId : keywords){
                    addArticleKeyword(constantMap.getString(ConstantKeys.INFORMATION_URL), articleId, title, content, keywordId);
                }
            }

            map.put("statusCode",200);
            map.put("message", "修改成功");
            map.put("callbackType","closeCurrent");
        }else{
            map.put("statusCode",300);
            map.put("message", "修改失败");
        }

        return "ssadmin/comm/ajaxDone";
    }

    @RequestMapping("/delProject")
    @RequiresPermissions("ssadmin/delProject.html")
    public Object delProject(ModelMap map, String ids){
        Map params = new HashMap<>();
        params.put("articleId", ids);
        Optional<Integer> optional = InformationApiUtils.sendPostRequest(getSendUrl("/backstage/deleteProject"), params);

        if(optional.isPresent() && optional.get() > 0){
            map.put("statusCode",200);
            map.put("message", "删除成功");
        }else{
            map.put("statusCode",300);
            map.put("message", "删除失败");
        }

        return "ssadmin/comm/ajaxDone";
    }

    /**
     * 项目报道
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/projectReportList")
    @RequiresPermissions("ssadmin/projectReportList.html")
    public Object projectReportList(ModelMap modelMap,
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
        Optional<JSONArray> arrOptional = InformationApiUtils.sendGetRequest(getSendUrl("/backstage/getProjectReportList"), params);
        arrOptional.ifPresent(arr -> {
            arr.forEach(e -> {
                JSONObject obj = (JSONObject) e;
                obj.put("createdTime", new Date(obj.getLongValue("createdTime")));
            });
            modelMap.put("list", arr);
        });

        int totalCount = 0;
        Optional<Integer> countOptional = InformationApiUtils.sendGetRequest(getSendUrl("/backstage/countProjectReportList"), params);
        if(countOptional.isPresent()){
            totalCount = countOptional.get();
        }

        modelMap.put("pageNum", pageNum);
        modelMap.put("articleId", articleId);
        modelMap.put("totalCount", totalCount);
        modelMap.put("numPerPage", Constants.PAGE_ITEM_COUNT_20);

        return "ssadmin/projectReportList";
    }

    @RequestMapping("/editProjectReport")
    @RequiresPermissions(value = {"ssadmin/addProjectReport.html", "ssadmin/updateProjectReport.html"})
    public Object editProjectReport(ModelMap modelMap, @RequestParam(required = false, defaultValue = "0") int id){
        if(id > 0){
            Map paramMap = new HashMap<>();
            paramMap.put("id", id + "");
            Optional<JSONObject> optional = InformationApiUtils.sendGetRequest(getSendUrl("/backstage/getProjectReportById"), paramMap);
            optional.ifPresent(e -> modelMap.put("item", e));
        }
        return "ssadmin/editProjectReport";
    }

    @RequestMapping("/addProjectReport")
    @RequiresPermissions("ssadmin/addProjectReport.html")
    public Object addProjectReport(ModelMap modelMap,
                                   String articleId,
                                   String title,
                                   String content){

        Map paramMap = new HashMap<>();
        paramMap.put("articleId", articleId);
        paramMap.put("title", title);
        paramMap.put("content", content);

        Optional<Integer> optional = InformationApiUtils.sendGetRequest(getSendUrl("/backstage/insertProjectReport"), paramMap);
        if(optional.isPresent() && optional.get() > 0){
            modelMap.put("statusCode",200);
            modelMap.put("message", "新增成功");
        }else{
            modelMap.put("statusCode",300);
            modelMap.put("message", "新增失败");
        }

        modelMap.put("callbackType", "closeCurrent");
        return "ssadmin/comm/ajaxDone";
    }

    @RequestMapping("/updateProjectReport")
    @RequiresPermissions("ssadmin/updateProjectReport.html")
    public Object updateProjectReport(ModelMap modelMap,
                                      String id,
                                      String articleId,
                                      String title,
                                      String content){

        Map paramMap = new HashMap<>();
        paramMap.put("id", id);
        paramMap.put("articleId", articleId);
        paramMap.put("title", title);
        paramMap.put("content", content);

        Optional<Integer> optional = InformationApiUtils.sendPostRequest(getSendUrl("/backstage/updateProjectReport"), paramMap);
        if(optional.isPresent() && optional.get() > 0){
            modelMap.put("statusCode",200);
            modelMap.put("message", "修改成功");
        }else{
            modelMap.put("statusCode",300);
            modelMap.put("message", "修改失败");
        }

        modelMap.put("callbackType", "closeCurrent");
        return "ssadmin/comm/ajaxDone";
    }

    /**
     * 删除项目报道
     *
     * @param modelMap
     * @param ids
     * @return
     */
    @RequestMapping("/delProjectReport")
    @RequiresPermissions("ssadmin/delProjectReport.html")
    public Object delProjectReport(ModelMap modelMap, String ids){
        Map params = new HashMap<>();
        params.put("id", ids);
        Optional<Integer> optional = InformationApiUtils.sendPostRequest(getSendUrl("/backstage/delProjectReport"), params);

        if(optional.isPresent() && optional.get() > 0){
            modelMap.put("statusCode",200);
            modelMap.put("message", "删除成功");
        }else{
            modelMap.put("statusCode",300);
            modelMap.put("message", "删除失败");
        }

        return "ssadmin/comm/ajaxDone";
    }
}










