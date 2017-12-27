package com.ruizton.main.controller.admin;

import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.model.*;
import com.ruizton.util.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by sunpeng on 2016/9/9.
 */
@Controller
@RequestMapping("/ssadmin")
public class InformationArticleController {

    private static final int PAGE_PER_SIZE = 40;
    private static final int COIN_VENTURE_ID = 5;
    private static final int PROJECT_ID = 15;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private HttpServletRequest request;

    @RequiresPermissions("/ssadmin/informationArticleList.html")
    @RequestMapping("/informationArticleList")
    public ModelAndView listArticle(HttpServletRequest request){
        String url = constantMap.getString("informationUrl");
        String pageUrl = constantMap.getString("pageUrl") + "details.html?id=";
        Map<Integer, String> typeMap = InformationApiUtils.getTypeNameMap(url);
        typeMap.put(0, "全部");
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/informationArticleList");
        //页码
        Optional<String> currentPageOptional = Optional.ofNullable(request.getParameter("pageNum"));
        String currentPage = currentPageOptional.orElse("1");
        paramMap.put("pageNum", String.valueOf(Integer.valueOf(currentPage) - 1));
        paramMap.put("size", String.valueOf(PAGE_PER_SIZE));
        //搜索关键字
        Optional<String> keyWordOptional = Optional.ofNullable(request.getParameter("keywords"));
        if(keyWordOptional.isPresent() && StringUtils.hasText(keyWordOptional.get())){
            paramMap.put("title", keyWordOptional.get());
            modelAndView.addObject("keywords", keyWordOptional.get());
        }
        //排序
        Optional<String> orderFieldOptional = Optional.ofNullable(request.getParameter("orderField"));
        if(orderFieldOptional.isPresent() && !orderFieldOptional.get().equals("")){
            paramMap.put("modelName", orderFieldOptional.get());
        }
        //方向
        Optional<String> orderDirectionOptional = Optional.ofNullable(request.getParameter("orderDirection"));
        if(orderDirectionOptional.isPresent() && !orderDirectionOptional.get().equals("")){
            paramMap.put("isDesc", orderDirectionOptional.get().equals("desc") ? "true" : "false");
        }
        //类型
        Optional<String> typeOptional = Optional.ofNullable(request.getParameter("type"));
        if(typeOptional.isPresent()){
            paramMap.put("classId", typeOptional.get());
            modelAndView.addObject("type", typeOptional.get());
        }
        List<InformationArticle> list = InformationApiUtils.listArticle(url, paramMap, false);
        for (InformationArticle informationArticle : list) {
            informationArticle.setClassName(typeMap.get(informationArticle.getClassId()));
        }

        //假如排序字段是capital或者financingRound，就先过滤articleList中币创投的文章，然后把币创投的文章追加前面或者后面；否则，
        //直接通过articleId获取币创投文章
//        if("capital".equals(paramMap.get("modelName")) || "financingRound".equals(paramMap.get("modelName"))){
//            String response = HttpClientUtils.get(url + ventureArticleListUrl, paramMap);
//            JSONObject responseObject = JSON.parseObject(response);
//            JSONArray dataArray = responseObject.getJSONArray("data");
//            List<InformationArticle> ventureList = JSON.parseArray(dataArray.toString(), InformationArticle.class);
//            for (InformationArticle informationArticle : ventureList) {
//                informationArticle.setClassName(typeMap.get(informationArticle.getClassId()));
//            }
//            articleList = GuavaUtils.newArrayList(articleList.parallelStream().filter(informationArticle -> informationArticle.getClassId() != COIN_VENTURE_ID).iterator());
//            if (paramMap.get("isDesc").equals("true")) {
//                articleList.addAll(0, ventureList);
//            }else {
//                articleList.addAll(ventureList);
//            }
//        }else{
//             Iterator<InformationArticle> iterator = articleList.parallelStream().map((informationArticle) -> {
//                if(informationArticle.getClassId() == COIN_VENTURE_ID){
//                   convertToVentureArticle(informationArticle);
//                }
//                return informationArticle;
//            }).iterator();
//            articleList = GuavaUtils.newArrayList(iterator);
//        }
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("numPerPage", PAGE_PER_SIZE);

        //总数
        int totolCount = InformationApiUtils.countArticle(url, paramMap);
//        if(paramMap.get("classId") == null){
//            paramMap.put("classId", String.valueOf(COIN_VENTURE_ID));
//            int ventureCount = InformationApiUtils.countArticle(url, paramMap);
//            totolCount -= ventureCount;
//        }
        modelAndView.addObject("totalCount", totolCount);
        //过滤项目库
        if(typeMap.get(PROJECT_ID) != null){
            typeMap.remove(PROJECT_ID);
        }
        //过滤币创投
        if(typeMap.get(COIN_VENTURE_ID) != null){
            typeMap.remove(COIN_VENTURE_ID);
        }
        modelAndView.addObject("typeMap", typeMap);
        modelAndView.addObject("articleList", list);
        modelAndView.addObject("pageUrl", pageUrl);
        return modelAndView;
    }

    @RequestMapping("/informationVentureArticleList")
    public ModelAndView listVentureArticle(HttpServletRequest request){
        String url = constantMap.getString("informationUrl");
        String pageUrl = constantMap.getString("pageUrl") + "details.html?id=";
        Map<Integer, String> typeMap = InformationApiUtils.getTypeNameMap(url);
        String ventureName = typeMap.get(COIN_VENTURE_ID);
        typeMap.clear();
        typeMap.put(COIN_VENTURE_ID, ventureName);

        Map<String, String> paramMap = GuavaUtils.newHashMap();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/informationVentureArticleList");
        //页码
        Optional<String> currentPageOptional = Optional.ofNullable(request.getParameter("pageNum"));
        String currentPage = currentPageOptional.orElse("1");
        paramMap.put("pageNum", String.valueOf(Integer.valueOf(currentPage) - 1));
        modelAndView.addObject("currentPage", currentPage);
        paramMap.put("size", String.valueOf(PAGE_PER_SIZE));
        modelAndView.addObject("numPerPage", 40);

        //搜索关键字
        Optional<String> keyWordOptional = Optional.ofNullable(request.getParameter("keywords"));
        if(keyWordOptional.isPresent() && StringUtils.hasText(keyWordOptional.get())){
            paramMap.put("title", keyWordOptional.get());
            modelAndView.addObject("keywords", keyWordOptional.get());
        }
        //排序
        Optional<String> orderFieldOptional = Optional.ofNullable(request.getParameter("orderField"));
        if(orderFieldOptional.isPresent() && !orderFieldOptional.get().equals("")){
            paramMap.put("modelName", orderFieldOptional.get());
        }
        //方向
        Optional<String> orderDirectionOptional = Optional.ofNullable(request.getParameter("orderDirection"));
        if(orderDirectionOptional.isPresent() && !orderDirectionOptional.get().equals("")){
            paramMap.put("isDesc", orderDirectionOptional.get().equals("desc") ? "true" : "false");
        }
        // 类型
//        Optional<String> typeOptional = Optional.ofNullable(request.getParameter("type"));
//        if(typeOptional.isPresent()){
//            paramMap.put("classId", typeOptional.get());
//            modelAndView.addObject("type", typeOptional.get());
//        }

        paramMap.put("classId", String.valueOf(COIN_VENTURE_ID));
        List<InformationArticle> list = InformationApiUtils.listArticle(url, paramMap, true);
        for (InformationArticle informationArticle : list) {
            informationArticle.setClassName(typeMap.get(COIN_VENTURE_ID));
            String financingRound = informationArticle.getFinancingRound();
            if(StringUtils.hasText(financingRound) && financingRound.length() > 1){
                informationArticle.setFinancingRound(informationArticle.getFinancingRound().substring(1));
            }
        }

        //总数
        int totolCount = InformationApiUtils.countArticle(url, paramMap);
        modelAndView.addObject("totalCount", totolCount);
        modelAndView.addObject("typeMap", typeMap);
        modelAndView.addObject("articleList", list);
        modelAndView.addObject("pageUrl", pageUrl);
        return modelAndView;
    }



    @RequestMapping("/goInformationArticleJSP")
    public ModelAndView goArticleJsp(HttpServletRequest request){
        String url = constantMap.getString("informationUrl");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(request.getParameter("url"));
        Map<Integer, String> typeNameMap = InformationApiUtils.getTypeNameMap(url);
        //过滤项目库
        if(typeNameMap.get(PROJECT_ID) != null){
            typeNameMap.remove(PROJECT_ID);
        }
        //过滤币创投
        if(typeNameMap.get(COIN_VENTURE_ID) != null){
            typeNameMap.remove(COIN_VENTURE_ID);
        }
        //列出所有的Frame
//        List<FrameInfo> frameInfoList = InformationApiUtils.listFrames(url);
//        for (FrameInfo frameInfo : frameInfoList) {
//            frameInfo.setChecked(false);
//        }

        Optional<String> optional = Optional.ofNullable(request.getParameter("id"));
        if(optional.isPresent()){
            Map<Integer, String> typeMap = InformationApiUtils.getTypeNameMap(url);
            Map<String, String> paramMap = GuavaUtils.newHashMap();
            paramMap.put("id", optional.get());
            InformationArticle informationArticle = InformationApiUtils.findArticleById(url, paramMap);
            int classId = informationArticle.getClassId();
            informationArticle.setClassName(typeMap.get(classId));
            if(classId == COIN_VENTURE_ID){
                informationArticle = InformationApiUtils.findVentureArticleById(url, paramMap);
                informationArticle.setClassName(typeMap.get(informationArticle.getClassId()));
                String financingRound = informationArticle.getFinancingRound();
                if(StringUtils.hasText(financingRound) && financingRound.length() > 1){
                    informationArticle.setFinancingRound(informationArticle.getFinancingRound().substring(1));
                }
            }
            modelAndView.addObject("informationArticle", informationArticle);
            paramMap.put("articleId", optional.get());

            //布局
//            List<LayoutInfo> layoutInfoList = InformationApiUtils.listLayoutByArticle(url, paramMap);
//            for (LayoutInfo layoutInfo : layoutInfoList) {
//                int frameId = layoutInfo.getFrameId();
//                frameInfoList.forEach(frameInfo -> {
//                    if(frameId == frameInfo.getId()){
//                        frameInfo.setChecked(true);
//                    }
//                });
//            }

            //关键字
            paramMap.put("classId", String.valueOf(informationArticle.getClassId()));
            List<KeywordInfo> keywordInfoList = InformationApiUtils.listKeyword(url, paramMap);
            List<KeywordIndexInfo> keywordIndexInfoList = InformationApiUtils.findKeywordsByArticleId(url, paramMap);
            for (KeywordIndexInfo keywordIndexInfo : keywordIndexInfoList) {
                int keywordId = keywordIndexInfo.getKeywordId();
                keywordInfoList.forEach(keywordInfo -> {
                    if(keywordInfo.getId() == keywordId){
                        keywordInfo.setChecked(true);
                    }
                });
            }
            modelAndView.addObject("keywordInfoList", keywordInfoList);
        }
        modelAndView.addObject("typeNameMap", typeNameMap);
        return modelAndView;
    }

    @RequestMapping("/goInformationArticleTypeJSP")
    public ModelAndView goArticleTypeJsp(HttpServletRequest request){
        String url = constantMap.getString("informationUrl");
        ModelAndView modelAndView = new ModelAndView();
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        String type = request.getParameter("classId");

        //类型
        if(type != null){
            modelAndView.addObject("type", type);
            paramMap.put("classId", String.valueOf(type));
        }
        List<KeywordInfo> keywordList = InformationApiUtils.listKeyword(url, paramMap);
        Map<Integer, String> typeNameMap = InformationApiUtils.getTypeNameMap(url);
        for (KeywordInfo keywordInfo : keywordList) {
            keywordInfo.setClassName(typeNameMap.get(keywordInfo.getClassId()));
        }

        modelAndView.addObject("keywordList", keywordList);
        modelAndView.addObject("typeNameMap", typeNameMap);
        modelAndView.setViewName("ssadmin/keywordList");
        modelAndView.setViewName(request.getParameter("url"));
        return modelAndView;
    }
    @RequiresPermissions("/ssadmin/saveInformationArticle.html")
    @RequestMapping(value = "/saveInformationArticle")
    public ModelAndView saveArticle (
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer classId,
            @RequestParam(required = false) String[] keywords,
            @RequestParam(required = false) String[] frames,
            @RequestParam(required = false) MultipartFile imgFile,
            @RequestParam(required = false) String imgPath,
            @RequestParam(required = false) String sources,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) String readingNum,
            @RequestParam(required = false) String originalText,
            @RequestParam(required = false) String capital,
            @RequestParam(required = false) String financingRound,
            @RequestParam(required = false) String roundTime,
            @RequestParam(required = false) String content) throws Exception{

        String url = constantMap.getString("informationUrl");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        if(classId == null){
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","文章类型必填");
            return modelAndView;
        }
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        paramMap.put("title", title);
        paramMap.put("classId", classId + "");
        paramMap.put("imgPath", imgPath);
        if(imgFile != null && !imgFile.isEmpty()){
            String result = saveFile(request, imgFile);
            if(result.equals("")){
                modelAndView.addObject("statusCode",300);
                modelAndView.addObject("message","非jpg、png、gif文件格式");
                return modelAndView;
            }else if(result.equals("other")){
                System.out.println("图片保存失败");
            }else {
                paramMap.put("imgPath", result);
            }
        }
        paramMap.put("sources", sources);
        paramMap.put("authorName", authorName);
        paramMap.put("content", content);
        paramMap.put("originalText", originalText);
        paramMap.put("readingNum", readingNum);
        paramMap.put("roundTime", roundTime);
        int result;
        if(classId == COIN_VENTURE_ID){
            paramMap.put("capital", capital);
            paramMap.put("financingRound", financingRound);
            result = InformationApiUtils.insertArticle(url, paramMap, true);
        }else {
            result = InformationApiUtils.insertArticle(url, paramMap, false);
        }


        //布局
        if(result != 0){
            if(frames != null){
                Map<String, String> layoutMap = GuavaUtils.newHashMap();
                layoutMap.put("articleId", String.valueOf(result));
                for (String frameId : frames){
                    layoutMap.put("frameId", frameId);
                    InformationApiUtils.insertLayout(url, layoutMap);
                }
            }
            modelAndView.addObject("message","保存成功");
        }else {
            modelAndView.addObject("message", "保存失败");
        }

        //关键字
        if(keywords != null){
            for (int i = 0; i < keywords.length; i++) {
                addArticleKeyword(url, String.valueOf(result), title, content, keywords[i]);
            }
        }

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("callbackType","closeCurrent");
        return modelAndView;
    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    @RequiresPermissions("/ssadmin/deleteInformationArticle.html")
    @RequestMapping("/deleteInformationArticle")
    public ModelAndView deleteArticle(int id){
        Map<String, String> map = GuavaUtils.newHashMap();
        String url = constantMap.getString("informationUrl");
        map.put("id", id + "");
        boolean isSuccess = InformationApiUtils.deleteArticle(url, map);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode",200);
        if(isSuccess){
            modelAndView.addObject("message","删除成功");
        }else {
            modelAndView.addObject("message", "删除失败");
        }
        return modelAndView;
    }


    @RequiresPermissions("/ssadmin/updateInformationArticle.html")
    @RequestMapping("/updateInformationArticle")
    public ModelAndView updateArticle(
            @RequestParam int id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false, value = "informationArticleLookup.id") int classId,
            @RequestParam(required = false) String[] keywords,
            @RequestParam(required = false) String[] frames,
            @RequestParam(required = false) String sources,
            @RequestParam(required = false) MultipartFile imgFile,
            @RequestParam(required = false) String imgPath,
            @RequestParam(required = false) String originalText,
            @RequestParam(required = false) String readingNum,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) String capital,
            @RequestParam(required = false) String financingRound,
            @RequestParam(required = false) String roundTime,
            @RequestParam(required = false) String content) throws Exception{
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        String url = constantMap.getString("informationUrl");
        paramMap.put("id", String.valueOf(id));
        paramMap.put("classId", String.valueOf(classId));
        paramMap.put("title", title);
        paramMap.put("imgPath", imgPath);
        paramMap.put("content", content);
        paramMap.put("readingNum", readingNum);
        paramMap.put("authorName", authorName);
        paramMap.put("sources", sources);
        paramMap.put("originalText", originalText);
        paramMap.put("roundTime", String.valueOf(roundTime));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        if(imgFile != null && !imgFile.isEmpty()){
            String result = saveFile(request,imgFile);
            if(result.equals("")){
                modelAndView.addObject("statusCode",300);
                modelAndView.addObject("message","非jpg、png、gif文件格式");
                return modelAndView;
            }else if(result.equals("other")){
                System.out.println("图片保存失败");
            }else {
                paramMap.put("imgPath", result);
            }
        }

//        //布局
//        Map<String, String> layoutMaps = GuavaUtils.newHashMap();
//        layoutMaps.put("articleId", String.valueOf(id));
//        List<LayoutInfo> layoutInfoList = InformationApiUtils.listLayoutByArticle(url, layoutMaps);
//        for (LayoutInfo layoutInfo : layoutInfoList) {
//            Map<String, String> layoutMap = GuavaUtils.newHashMap();
//            layoutMap.put("id", String.valueOf(layoutInfo.getId()));
//            InformationApiUtils.deleteLayout(url, layoutMap);
//        }
//        if(frames != null){
//            for (String frameId : frames){
//                Map<String, String> layoutMap = GuavaUtils.newHashMap();
//                layoutMap.put("articleId", String.valueOf(id));
//                layoutMap.put("frameId", frameId);
//                InformationApiUtils.insertLayout(url, layoutMap);
//            }
//        }

        //关键字
        Map<String, String> keywordMap = GuavaUtils.newHashMap();
        keywordMap.put("articleId", String.valueOf(id));
        List<KeywordIndexInfo> keywordIndexList = InformationApiUtils.findKeywordsByArticleId(url, keywordMap);
        for (KeywordIndexInfo keywordIndexInfo : keywordIndexList) {
            keywordMap.put("id", String.valueOf(keywordIndexInfo.getId()));
            InformationApiUtils.deleteKeyword(url, keywordMap);
        }
        if(keywords != null){
            for (String keywordId : keywords){
                addArticleKeyword(url, String.valueOf(id), title, content, keywordId);
            }
        }

        boolean isSuceess;
        if(classId == COIN_VENTURE_ID){
            paramMap.put("financingRound", financingRound);
            paramMap.put("capital", capital);
            isSuceess = InformationApiUtils.updateArticle(url, paramMap, true);
        }else {
            isSuceess = InformationApiUtils.updateArticle(url, paramMap, false);
        }
        modelAndView.addObject("statusCode",200);
        if(isSuceess){
            modelAndView.addObject("message","修改成功");
        }else {
            modelAndView.addObject("message", "修改失败");
        }
        modelAndView.addObject("callbackType","closeCurrent");
        return modelAndView;
    }


    /**
     * 文章类型
     * @return
     */
    @RequiresPermissions("/ssadmin/informationArticleTypeList.html")
    @RequestMapping("/informationArticleTypeList")
    public ModelAndView articleTypeList(){
        String url = constantMap.getString("informationUrl");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("list", InformationApiUtils.listArticleType(url));
        modelAndView.setViewName("ssadmin/informationArticleTypeList");
        return modelAndView;
    }

    /**
     *  修改文章，添加文章时选择类型
     * @return
     */
    @RequestMapping("/informationArticleTypeLookup")
    public ModelAndView forLookUp(){
        String url = constantMap.getString("informationUrl");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("list", InformationApiUtils.listArticleType(url));
        modelAndView.setViewName("ssadmin/informationArticleTypeLookup");
        return modelAndView;
    }


    @RequestMapping("/forInformationArticleLookup")
    public ModelAndView forInformationAricleLookup(
            @RequestParam(required = false) String orderField,
            @RequestParam(required = false) String orderDirection,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "0") int classId,
            @RequestParam(required = false) String keywords
    ){

        String url = constantMap.getString("informationUrl");
        String pageUrl = constantMap.getString("pageUrl") + "details.html?id=";
        Map<Integer, String> typeMap = InformationApiUtils.getTypeNameMap(url);
        typeMap.put(0, "全部");
        Map<String, String> paramMap = GuavaUtils.newHashMap();
        ModelAndView modelAndView = new ModelAndView();
        //页码

        paramMap.put("pageNum", String.valueOf(pageNum - 1));
        paramMap.put("size", String.valueOf(PAGE_PER_SIZE));
        //搜索关键字
        if(StringUtils.hasText(keywords)){
            paramMap.put("title", keywords);
            modelAndView.addObject("keywords", keywords);
        }
        if(orderField != null){
            paramMap.put("modelName", orderField);
        }
        if(orderDirection != null){
            paramMap.put("isDesc", orderDirection.equals("desc") ? "true" : "false");
        }

        //总数
        int totolCount = 0;
        List<InformationArticle> list;
        if(classId != 0 && classId != COIN_VENTURE_ID){
            paramMap.put("classId", String.valueOf(classId));
            list = InformationApiUtils.listArticle(url, paramMap, false);
            totolCount += InformationApiUtils.countArticle(url, paramMap);
        }else if(classId == COIN_VENTURE_ID){
            paramMap.put("classId", String.valueOf(COIN_VENTURE_ID));
            list = InformationApiUtils.listArticle(url, paramMap, true);
            totolCount += InformationApiUtils.countArticle(url, paramMap);
        }else {
            list = InformationApiUtils.listArticle(url, paramMap, false);
            totolCount += InformationApiUtils.countArticle(url, paramMap);
            //追加币创投文章
            paramMap.remove("pageNum");
            paramMap.remove("size");
            list.addAll(InformationApiUtils.listArticle(url, paramMap, true));
        }
        for (InformationArticle informationArticle : list) {
            informationArticle.setClassName(typeMap.get(informationArticle.getClassId()));
        }
        modelAndView.addObject("type", classId);
        modelAndView.addObject("currentPage", pageNum);
        modelAndView.addObject("numPerPage", PAGE_PER_SIZE);
        modelAndView.addObject("totalCount", totolCount);

        //过滤项目库
        if(typeMap.get(PROJECT_ID) != null){
            typeMap.remove(PROJECT_ID);
        }
        modelAndView.addObject("typeMap", typeMap);
        modelAndView.addObject("articleList", list);
        modelAndView.addObject("pageUrl", pageUrl);
        modelAndView.setViewName("ssadmin/informationArticleLookup");
        return modelAndView;
    }

    /**
     * 保存图片文件
     * @param file
     * @return
     * @throws IOException
     */
    public static String saveFile(HttpServletRequest request, MultipartFile file) throws IOException {
        String realName = file.getOriginalFilename();
        String[] strings = realName.split("\\.");
        String extendName = strings[strings.length - 1];
        if(extendName != null &&  !extendName.trim().toLowerCase().endsWith("jpg") && !extendName.trim().toLowerCase().endsWith("png") && !extendName.trim().toLowerCase().endsWith("gif")){
            return "";
        }
        String realPath = request.getSession().getServletContext().getRealPath("/")+Constants.AdminUploadInformationDirectory;
        String fileName = Utils.getRandomImageName() + "." + extendName;
        boolean flag = Utils.saveFile(realPath, fileName, file.getInputStream());
        if(flag){
            return Constants.AdminUploadInformationDirectory+"/"+fileName;
        }
        return "";
    }


    /**
     * 增加文章关键字
     * @param url
     * @param articleId
     * @param title
     * @param content
     * @param keywordId
     */

    private void addArticleKeyword(String url, String articleId, String title, String content, String keywordId){
        Map<String, String> keywordParam = GuavaUtils.newHashMap();
        keywordParam.put("articleId", articleId);
        keywordParam.put("keywordId", keywordId);
        keywordParam.put("content", content);
        keywordParam.put("title", title);
        InformationApiUtils.insertKeyword(url, keywordParam);
    }





}
