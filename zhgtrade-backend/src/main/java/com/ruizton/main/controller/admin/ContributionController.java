package com.ruizton.main.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.service.admin.UserService;
import com.ruizton.util.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

/**
 * Created by sunpeng on 2017/1/4.
 */
@Controller
@RequestMapping("/ssadmin")public class ContributionController {
    private static final int COIN_VENTURE_ID = 5;
    private static final int PROJECT_ID = 15;
    private String informationUrl;
    private String pageUrl;
    private static final int NUM_PER_PAGE = 40;
    @Autowired
    private UserService userService;
    @Autowired
    private ConstantMap constantMap;

    public String getInformationUrl(){
        return constantMap.getString("informationUrl");
//        return "http://192.168.0.233:8088";
    }
    public String getPageUrl(){
        return constantMap.getString("pageUrl");
    }

    @RequiresPermissions("ssadmin/contributionList.html")
    @RequestMapping("/contributionList")
    public ModelAndView list(
            @RequestParam(required = false, defaultValue = "0") int status,
            @RequestParam(required = false) String authorId,
            @RequestParam(required = false) String loginName,
            @RequestParam(name = "pageNum", required = false, defaultValue = "0") int currentPage,
            @RequestParam(required = false) String orderField,
            @RequestParam(required = false) String orderDirection
    ) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/contributionList");
        String url = getInformationUrl() + "/backstage/getContributionPath";
        Map<String, String> map = new HashMap<String, String>();
        if (status != 0) {
            map.put("status", String.valueOf(status));
            modelAndView.addObject("status", status);
        }
        if(StringUtils.hasText(loginName)){
            List<Fuser> userList = userService.findByProperty("floginName", loginName.trim());
            if(userList.size() == 1){
                map.put("authorId", String.valueOf(userList.get(0).getFid()));
                modelAndView.addObject("loginName", loginName);
            }else {
                map.put("authorId", String.valueOf(-1));
                modelAndView.addObject("loginName", loginName);
            }
        }
        if (StringUtils.hasText(authorId)) {
            map.put("authorId", authorId);
            modelAndView.addObject("authorId", authorId);
        }
        map.put("size", String.valueOf(NUM_PER_PAGE));
        modelAndView.addObject("numPerPage", NUM_PER_PAGE);
        if (currentPage != 0) {
            map.put("start", String.valueOf((currentPage - 1) * NUM_PER_PAGE));
            modelAndView.addObject("currentPage", currentPage);
        }
        if (StringUtils.hasText(orderField) && StringUtils.hasText(orderDirection)) {
            map.put("modelName", orderField);
            map.put("isDesc", orderDirection.equals("desc") ? "true" : "false");
            modelAndView.addObject("orderField", orderField);
            modelAndView.addObject("orderDirection", orderDirection);
        }
        Optional<JSONArray> optional = InformationApiUtils.sendGetRequest(url, map);
        optional.ifPresent(jsonArray -> {
            jsonArray.parallelStream().forEach(jsonObject -> {
                JSONObject contribution = (JSONObject) jsonObject;
                int userId = contribution.getInteger("userId");
                if (userId != 0) {
                    contribution.put("loginName", userService.findById(userId).getFloginName());
                    contribution.put("nickName", userService.findById(userId).getFnickName());
                } else {
                    contribution.put("loginName", "匿名");
                    contribution.put("nickName", "匿名");
                }
                contribution.put("createdTime", new Date(contribution.getLong("createdTime")));
                int contributionStatus = contribution.getIntValue("status");
                if(contributionStatus == 1){
                    contribution.put("status", "未审核");
                }else if(contributionStatus == 2){
                    contribution.put("status", "通过");
                }else if (contributionStatus == 3){
                    contribution.put("status", "不通过");
                }else if (contributionStatus == -2){
                    contribution.put("status", "已删除");
                }
            });
        });
        String countUrl = getInformationUrl() + "/backstage/countContributionPath";
        Optional<Integer> optionalCount = InformationApiUtils.sendGetRequest(countUrl, map);
        modelAndView.addObject("totalCount", optionalCount.get());
        modelAndView.addObject("list", optional.get());
        modelAndView.addObject("pageUrl", getPageUrl());
        return modelAndView;
    }

    @RequestMapping("/goContributionJSP")
    public ModelAndView goArticleJsp(int id, String url){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(url);
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(id));
        Optional<JSONObject> optional = InformationApiUtils.sendGetRequest(getInformationUrl() + "/backstage/getContributionPath", map);
        optional.ifPresent(contribution -> {
            int userId = contribution.getInteger("userId");
            if (userId != 0) {
                String nickName = userService.findById(userId).getFnickName();
                contribution.put("nickName", nickName);
            } else {
                contribution.put("nickName", "匿名");
            }
        });
        modelAndView.addObject("contribution", optional.get());
        Map<Integer, String> typeNameMap = InformationApiUtils.getTypeNameMap(getInformationUrl() + "backsta");
        //过滤项目库
        if(typeNameMap.get(PROJECT_ID) != null){
            typeNameMap.remove(PROJECT_ID);
        }
        //过滤币创投
        if(typeNameMap.get(COIN_VENTURE_ID) != null){
            typeNameMap.remove(COIN_VENTURE_ID);
        }
        modelAndView.addObject("typeNameMap", typeNameMap);
        return modelAndView;
    }


    @RequiresPermissions("ssadmin/adoptContribution.html")
    @RequestMapping("/adoptContribution")
    public ModelAndView adoptContribution(
            String  contributionId,
            String title,
            String classId,
            String authorId,
            String authorName,
            String content,
            String sources,
            String originalText,
            @RequestParam(required = false) String[] keywords,
            @RequestParam(required = false) String readingNum,
            @RequestParam(required = false) String commentNum,
            @RequestParam(required = false) String imgPath,
            @RequestParam(required = false) MultipartFile imgFile
    ) {

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("contributionId", contributionId);
        paramMap.put("title", title);
        paramMap.put("classId", classId);
        if(StringUtils.hasText(authorId)){
            paramMap.put("authorId", authorId);
        }
        paramMap.put("authorName", String.valueOf(authorName));
        paramMap.put("content", content);
        paramMap.put("sources", sources);
        paramMap.put("originalText", originalText);
        if(StringUtils.hasText(readingNum)){
            paramMap.put("readingNum", readingNum);
        }
        if(StringUtils.hasText(commentNum)){
            paramMap.put("commentNum", commentNum);
        }
        if(StringUtils.hasText(imgPath)){
            paramMap.put("imgPath", imgPath);
        }

        ModelAndView modelAndView = new ModelAndView();
        if(imgFile != null && !imgFile.isEmpty()){
            try {
                byte[] bytes = imgFile.getBytes();
                if(!Utils.isImage(bytes)){
                    // 不是有效图片文件
                    modelAndView.addObject("statusCode",300);
                    modelAndView.addObject("message","非法图片");
                    modelAndView.setViewName("ssadmin/comm/ajaxDone");
                    return modelAndView;
                }
                String ext = com.ruizton.util.StringUtils.getFilenameExtension(imgFile.getOriginalFilename());
                String filePath = Constants.AdminUploadInformationDirectory + Utils.getRelativeFilePath(ext, bytes);
                boolean flag = Utils.uploadFileToOss(imgFile.getInputStream(), filePath);
                if(flag){
                    paramMap.put("imgPath", filePath);
                }else{
                    modelAndView.addObject("statusCode",300);
                    modelAndView.addObject("message","上传图片失败");
                    modelAndView.setViewName("ssadmin/comm/ajaxDone");
                    return modelAndView;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        Optional<Integer> optionalArticleId = InformationApiUtils.sendPostRequest(getInformationUrl() + "/backstage/adoptContribution", paramMap);
        //关键字
        int articleId = optionalArticleId.get();
        if(keywords != null){
            for (int i = 0; i < keywords.length; i++) {
                Map<String, String> keywordParam = new HashMap<String, String>();
                keywordParam.put("articleId", String.valueOf(articleId));
                keywordParam.put("keywordId", keywords[i]);
                keywordParam.put("content", content);
                keywordParam.put("title", title);
                Optional<Object> optional = InformationApiUtils.sendPostRequest(getInformationUrl() + "/backstage/insertKeywordIndex", keywordParam);
                System.out.println(optional.get());

            }
        }
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("callbackType","closeCurrent");
        return modelAndView;
    }


    @RequiresPermissions("ssadmin/notAdoptContribution.html")
    @RequestMapping("/notAdoptContribution")
    public ModelAndView notAdopt(String ids){
        Map<String, String> map = new HashMap<String, String>();
        map.put("contributionId", String.valueOf(ids));
        Optional<Integer> optional = InformationApiUtils.sendPostRequest(getInformationUrl() + "/backstage/notAdopt", map);
        ModelAndView modelAndView = new ModelAndView();
        if(optional.get() > 0){
            modelAndView.addObject("statusCode",200);
            modelAndView.addObject("message", "审核不通过成功");
        }else{
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message", "审核失败");
        }
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        return modelAndView;
    }
}
