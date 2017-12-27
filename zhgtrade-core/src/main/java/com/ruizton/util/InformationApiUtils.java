package com.ruizton.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by sunpeng on 2016/9/26.
 */
public class InformationApiUtils {

    private static final String ARTICLE_LIST_URL = "/backstage/getArticleListByFuzzyQuery";
    private static final String ARTICLETYPE_LIST_URL = "/api/getClassList?isMapList=true";
    private static final String COUNT_ARTICLE_URL = "/backstage/countFuzzyQuery";
    private static final String FIND_ARTICLE_BY_ID_URL = "/api/getArticleById";
    private static final String UPDATE_ARTICLE_BY_ID_URL = "/backstage/updateArticleById";
    private static final String UPDATE_COIN_VENTURE_CAPITAL_URL = "/backstage/updateCoinVentureCapital";
    private static final String INSERT_ARTICLE_URL = "/backstage/insertArticle";
    private static final String DELETE_ARTICLE_URL = "/backstage/delArticleById";
    private static final String FIND_VENTURE_ARTICLE_BY_ID_URL = "/api/getCoinVentureCapitalById";
    private static final String VENTURE_ARTICLE_LIST_URL = "/backstage/fuzzyQueryCoinVentureCapital";
    private static final String INSERT_VENTURE_ARTICLE_URL = "/backstage/insertCoinVentureCapital";
    private static final String LIST_LAYOUT_URL = "/api/getLayoutList";
    private static final String INSERT_LAYOUT_URL = "/backstage/insertLayout";
    private static final String DELETE_LAYOUT_URL = "/backstage/deleteLayout";
    private static final String UPDATE_LAYOUT_URL = "/backstage/updateLayout";
    private static final String COUNT_LAYOUT_URL = "/api/countLayoutList";
    private static final String FIND_LAYOUT_BY_ID_URL = "/backstage/getLayoutById";
    private static final String FIND_LAYOUT_BY_ARTICLEID_URL = "/backstage/getLayoutByArticleId";
    private static final String KEYWORD_LIST_URL = "/api/getKeywordList";
    private static final String INSERT_KEYWORD_ARTICLE_URL = "/backstage/insertKeywordIndex";
    private static final String FIND_KEYWORD_BY_ARTICLE_ID_URL = "/backstage/getKeywordIndexByArticleId";
    private static final String DELETE_ARTICLE_KEYWORD_URL = "/backstage/deleteKeywordIndex";
    private static final String FIND_CLASS_BY_ID_URL = "/api/getClassById";
    private static final String UPDATE_CLASS_BY_ID_URL = "/backstage/updateClass";
    private static final String FRAME_LIST_URL = "/backstage/getFrameList";
    private static final String KEYWORD_COUNT_URL = "/api/countKeywordList";
    private static final int COIN_VENTURE_ID = 5;
    private static final int RECOMMEND_LAYOUT_ID = 1;
    private static final int PUSH_LAYOUT_ID = 2;

    private static final int OK = 200;


    /**
     * 列出article
     * @param url
     * @param map
     * @return
     */
    public static List<InformationArticle> listArticle(String url, Map<String, String> map, boolean isVenture){
        List<InformationArticle> list = GuavaUtils.newArrayList();
        String response;
        if(isVenture){
            response = HttpClientUtils.get(url + VENTURE_ARTICLE_LIST_URL, map);
        }else{
            response = HttpClientUtils.get(url + ARTICLE_LIST_URL, map);
        }
        JSONObject responseObject = JSON.parseObject(response);
        String data = responseObject.getString("data");
        if(!data.equals("[]")){
            list = JSON.parseArray(data, InformationArticle.class);
        }
        return list;
    }


    /**
     * 获得所有文章类型的一个映射
     * @param url
     * @return
     */
    public static Map<Integer, InformationArticleType> getTypeMap(String url){
        Map<Integer, InformationArticleType> map = GuavaUtils.newHashMap();
        String response = HttpClientUtils.get(url + ARTICLETYPE_LIST_URL);
        JSONObject responseObject = JSON.parseObject(response);
        String data = responseObject.getString("data");
        if(data.equals("[]")){
            List<InformationArticleType> list = JSON.parseArray(data, InformationArticleType.class);
            for (InformationArticleType informationArticleType : list) {
                map.put(informationArticleType.getId(), informationArticleType);
            }
        }
        return map;
    }

    /**
     * 获得所有文章类型ID和标题的一个映射
     * @param url
     * @return
     */
    public static Map<Integer, String> getTypeNameMap(String url){
        Map<Integer, String> map = GuavaUtils.newHashMap();
        String response = HttpClientUtils.get(url + ARTICLETYPE_LIST_URL);
        JSONObject responseObject = JSON.parseObject(response);
        String data = responseObject.getString("data");
        if(!data.equals("[]")){
            List<InformationArticleType> list = JSON.parseArray(data, InformationArticleType.class);
            for (InformationArticleType informationArticleType : list) {
                map.put(informationArticleType.getId(), informationArticleType.getClassName());
            }
        }
        return map;
    }

    /**
     * 列出文章类型
     * @param url
     * @return
     */
    public static List<InformationArticleType> listArticleType(String url){
        String response = HttpClientUtils.get(url + ARTICLETYPE_LIST_URL);
        JSONArray dataResponse = JSON.parseObject(response).getJSONArray("data");
        return JSON.parseArray(dataResponse.toString(), InformationArticleType.class);
    }

    /**
     * 文章总数
     * @param url
     * @param map
     * @return
     */
    public static int countArticle(String url, Map<String, String> map){
        String response = HttpClientUtils.get(url + COUNT_ARTICLE_URL, map);
        JSONObject responseObject = JSON.parseObject(response);
        return responseObject.getIntValue("data");
    }

    /**
     * 查找文章
     * @param url
     * @param map
     * @return
     */
    public static InformationArticle findArticleById(String url, Map<String, String> map){
        String response = HttpClientUtils.get(url + FIND_ARTICLE_BY_ID_URL, map);
        JSONObject responseObject = JSON.parseObject(response);
        JSONObject dataObject = responseObject.getJSONObject("data");
        return JSON.parseObject(dataObject.toString(), InformationArticle.class);
    }

    /**
     * 查找币创投文章
     * @param url
     * @param map
     * @return
     */
    public static InformationArticle findVentureArticleById(String url, Map<String, String> map){
        String response = HttpClientUtils.get(url + FIND_VENTURE_ARTICLE_BY_ID_URL, map);
        JSONObject responseObject = JSON.parseObject(response);
        JSONObject dataObject = responseObject.getJSONObject("data");
        return JSON.parseObject(dataObject.toString(), InformationArticle.class);
    }


    /**
     * 列出所有frame
     * @param url
     * @return
     */
    public static List<FrameInfo> listFrames(String url){
        List<FrameInfo> list = GuavaUtils.newArrayList();
        String response = HttpClientUtils.get(url + FRAME_LIST_URL);
        JSONObject responseObject = JSON.parseObject(response);
        String data = responseObject.getString("data");
        if(!data.equals("[]")){
            list = JSON.parseArray(data, FrameInfo.class);
        }
        return list;
    }

    /**
     * 获取一个Frame映射
     * @param url
     * @return
     */
    public static Map<Integer, String> getFrameNameMap(String url){
        Map<Integer, String> map = GuavaUtils.newHashMap();
        String response = HttpClientUtils.get(url + FRAME_LIST_URL);
        JSONObject responseObject = JSON.parseObject(response);
        JSONArray dataArray = responseObject.getJSONArray("data");
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject frameObject = dataArray.getJSONObject(i);
            map.put(frameObject.getInteger("id"), frameObject.getString("frameName"));
        }
        return map;
    }

    /**
     * 列出所有的布局
     * @param url
     * @param map
     * @return
     */
    public static List<LayoutInfo> listLayoutByArticle(String url, Map<String, String> map){
        List<LayoutInfo> list = GuavaUtils.newArrayList();
        String respones = HttpClientUtils.get(url + FIND_LAYOUT_BY_ARTICLEID_URL, map);
        JSONObject responseObject = JSON.parseObject(respones);
        String data = responseObject.getString("data");
        if(!data.equals("[]")){
            list = JSON.parseArray(data, LayoutInfo.class);
        }
        return list;
    }

    /**
     * 获得全部布局
     * @param url
     * @param map
     * @return
     */
    public static List<LayoutInfo> listLayout(String url, Map<String, String> map){
        List<LayoutInfo> list = GuavaUtils.newArrayList();
        map.put("isList", String.valueOf(true));
        String response = HttpClientUtils.get(url + LIST_LAYOUT_URL, map);
        JSONObject responseObject = JSON.parseObject(response);
        String data = responseObject.getString("data");
        if(!data.equals("[]")){
            list = JSON.parseArray(data, LayoutInfo.class);
        }
        return list;
    }

    /**
     * 布局计数
     * @param url
     * @param map
     * @return
     */
    public static int countLayout(String url, Map<String, String> map){
        String response = HttpClientUtils.get(url + COUNT_LAYOUT_URL, map);
        return JSON.parseObject(response).getInteger("data");
    }

    /**
     * 通过文章ID查找相关的关键字
     * @param url
     * @param map
     * @return
     */
    public static List<KeywordIndexInfo> findKeywordsByArticleId(String url, Map<String, String> map){
        List<KeywordIndexInfo> list = GuavaUtils.newArrayList();
        String keywordResponse = HttpClientUtils.get(url + FIND_KEYWORD_BY_ARTICLE_ID_URL, map);
        JSONObject keywordResponseObject = JSON.parseObject(keywordResponse);
        String data = keywordResponseObject.getString("data");
        if(!data.equals("[]")){
            list = JSON.parseArray(data, KeywordIndexInfo.class);
        }
        return list;
    }

    /**
     * 列出关键字
     * @param url
     * @param map
     * @return
     */
    public static List<KeywordInfo> listKeyword(String url, Map<String, String> map){
        List<KeywordInfo> list = GuavaUtils.newArrayList();
        String response = HttpClientUtils.get(url + KEYWORD_LIST_URL, map);
        JSONObject responseObject = JSON.parseObject(response);
        String data = responseObject.getString("data");
        if(!data.equals("[]")){
            list =  JSON.parseArray(data, KeywordInfo.class);
        }
        return list;
    }

    /**
     * 插入文章
     * @param url
     * @param map
     * @param isVenture
     * @return
     */
    public static int insertArticle(String url, Map<String, String> map, boolean isVenture){
        String response;
        if(isVenture){
            response = HttpClientUtils.post(url + INSERT_VENTURE_ARTICLE_URL, map);
        }else{
            response = HttpClientUtils.post(url + INSERT_ARTICLE_URL, map);
        }
        JSONObject jsonObject = JSON.parseObject(response);
        return jsonObject.getIntValue("data");
    }

    /**
     * 插入布局
     * @param url
     * @param map
     */
    public static boolean insertLayout(String url, Map<String, String> map){
        String response = HttpClientUtils.post(url + INSERT_LAYOUT_URL, map);
        JSONObject jsonObject = JSON.parseObject(response);
        int result = jsonObject.getIntValue("data");
        return result == 1 ? true :false;
    }

    /**
     * 插入关键字
     * @param url
     * @param map
     */
    public static void insertKeyword(String url, Map<String, String> map){
        String response = HttpClientUtils.post(url + INSERT_KEYWORD_ARTICLE_URL, map);
        System.out.println(response);
    }

    /**
     * 删除文章
     * @param url
     * @param map
     */
    public static boolean deleteArticle(String url, Map<String, String> map){
        String response = HttpClientUtils.post(url + DELETE_ARTICLE_URL, map);
        JSONObject jsonObject = JSON.parseObject(response);
        int result = jsonObject.getIntValue("data");
        return result == 1 ? true :false;
    }

    /**
     * 删除布局
     * @param url
     * @param map
     * @return
     */
    public static boolean deleteLayout(String url, Map<String, String> map){
        String response = HttpClientUtils.post(url + DELETE_LAYOUT_URL, map);
        JSONObject jsonObject = JSON.parseObject(response);
        int result = jsonObject.getIntValue("data");
        return result == 1 ? true :false;
    }

    /**
     * 删除关键字
     * @param url
     * @param map
     * @return
     */
    public static boolean deleteKeyword(String url, Map<String, String> map){
        String response = HttpClientUtils.post(url + DELETE_ARTICLE_KEYWORD_URL, map);
        JSONObject jsonObject = JSON.parseObject(response);
        int result = jsonObject.getIntValue("data");
        return result == 1 ? true :false;
    }

    /**
     * 更新文章
     * @param url
     * @param map
     * @param isVenture
     * @return
     */
    public static boolean updateArticle(String url, Map<String, String> map, boolean isVenture){
        String response;
        if(isVenture){
            response = HttpClientUtils.post(url + UPDATE_COIN_VENTURE_CAPITAL_URL, map);
        }else{
            response = HttpClientUtils.post(url + UPDATE_ARTICLE_BY_ID_URL, map);
        }
        JSONObject jsonObject = JSON.parseObject(response);
        int result = jsonObject.getIntValue("data");
        return result == 1 ? true :false;
    }

    /**
     * 查找布局
     * @param url
     * @param map
     * @return
     */
    public static LayoutInfo findLayoutById(String url, Map<String, String> map){
        String response = HttpClientUtils.get(url + FIND_LAYOUT_BY_ID_URL, map);
        JSONObject responseObject = JSON.parseObject(response);
        return responseObject.getObject("data", LayoutInfo.class);
    }

    /**
     * 更新布局
     * @param url
     * @param map
     * @return
     */
    public static boolean updateLayout(String url, Map<String, String> map){
        String response = HttpClientUtils.get(url + UPDATE_LAYOUT_URL, map);
        JSONObject jsonObject = JSON.parseObject(response);
        int result = jsonObject.getIntValue("data");
        return result == 1 ? true :false;
    }

    /**
     * 关键字计数
     * @param url
     * @param map
     * @return
     */
    public static int countKeyword(String url, Map<String, String> map){
        String response = HttpClientUtils.get(url + KEYWORD_COUNT_URL, map);
        return JSON.parseObject(response).getInteger("data");
    }

    public static Optional sendGetRequest(String url, Map<String, String> map){
        try{
            String response = HttpClientUtils.get(url, map);
            JSONObject jsonObject = JSON.parseObject(response);
            return Optional.ofNullable(jsonObject).flatMap((obj) -> {
                if(OK == obj.getIntValue("ret")){
                    return Optional.ofNullable(obj.get("data"));
                }
                return Optional.empty();
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional sendPostRequest(String url, Map<String, String> map){
        try {
            String response = HttpClientUtils.post(url, map);
            JSONObject jsonObject = JSON.parseObject(response);
            return Optional.ofNullable(jsonObject).flatMap((obj) -> {
                if(OK == obj.getIntValue("ret")){
                    return Optional.ofNullable(obj.get("data"));
                }
                return Optional.empty();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
