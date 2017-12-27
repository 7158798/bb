package com.zhgtrade.front.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.dto.WeixinArticleData;
import com.ruizton.main.model.BlockArticle;
import com.ruizton.main.model.WeixinArticle;
import com.ruizton.main.service.admin.BlockArticleService;
import com.ruizton.main.service.front.WeixinArticleService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sunpeng on 2016/7/25.
 */
@RestController
@RequestMapping("/block")
public class BlockDakeController {

    private final String format = "yyyy-MM-dd";
    private final int numPerPage = 10;
    @Autowired
    private BlockArticleService blockArticleService;
    @Autowired
    private WeixinArticleService weixinArticleService;
    @RequestMapping("/test")
    public String test(){
        return "Hello, World";
    }

    @RequestMapping("/index")
    public Map<String, Object> index(){
        Map<String, Object> map = new HashMap<String, Object>();
        List<BlockArticle> announceTotalList = blockArticleService.listFiveBlockArticles(1);
        List<BlockArticle> newsTotalList = blockArticleService.listFiveBlockArticles(2);
        Map<String, Object> announceMap = new LinkedHashMap<String, Object>();
        String date = "";
        List<Map<String, Object>> timeList = new ArrayList<Map<String, Object>>();
        for (BlockArticle blockArticle : announceTotalList) {
            long lastUpdateTime = blockArticle.getLastUpdateTime().getTime();
            String lastDateStr = dateToString(new Date(lastUpdateTime));
            Map<String, Object> articleMap = new HashMap<String, Object>();
            articleMap.put("lastUpdateTime", dateToString(new Date(blockArticle.getLastUpdateTime().getTime())));
            articleMap.put("id", blockArticle.getId());
            articleMap.put("title", blockArticle.getTitle());
            if(date.equals(lastDateStr)){
                timeList.add(articleMap);
            }else {
                if(!date.equals("")){
                    announceMap.put(date, timeList);
                }
                timeList = new ArrayList<Map<String, Object>>();
                timeList.add(articleMap);
                date = lastDateStr;
            }
        }
        if(!date.equals("")){
            announceMap.put(date, timeList);
        }
        map.put("announceMap", announceMap);
        List<Map<String, Object>> newsList = new ArrayList<Map<String, Object>>();
        for (BlockArticle blockArticle : newsTotalList) {
            Map<String, Object> newsMap = new HashMap<>();
            newsMap.put("lastUpdateTime", dateToString(new Date(blockArticle.getLastUpdateTime().getTime())));
            newsMap.put("id", blockArticle.getId());
            newsMap.put("title", blockArticle.getTitle());
            newsMap.put("content", blockArticle.getContent());
            newsMap.put("imgUrl", blockArticle.getImgUrl());
            newsList.add(newsMap);
        }
        map.put("newsList", newsList);

        List<WeixinArticle> weixinList = weixinArticleService.list(1, 6);
        if(weixinList.size() > 6){
            weixinList = weixinList.subList(0, 6);
        }
        JSONArray jsonArray = new JSONArray();
        for (WeixinArticle weixinArticle : weixinList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", weixinArticle.getUrl());
            jsonObject.put("imgUrl", weixinArticle.getImgUrl());
            jsonObject.put("id", weixinArticle.getId());
            jsonObject.put("title", weixinArticle.getTitle());
            jsonObject.put("author", weixinArticle.getAuthor());
            jsonObject.put("time", dateToString(new Date(weixinArticle.getCreateTime().getTime())));
            jsonObject.put("intro", weixinArticle.getIntroduction());
            jsonArray.add(jsonObject);
        }
        map.put("weixinList", jsonArray);
        return map;
    }

    @RequestMapping("/announce")
    public Map<String, Object> announce(@RequestParam (required = false, defaultValue = "1") int currentPage){
        List<BlockArticle> list = blockArticleService.listBlockArticlesByType(1, (currentPage - 1) * numPerPage, numPerPage);
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> announceMap = new LinkedHashMap<String, Object>();
        String date = "";
        List<Map<String, Object>> timeList = new ArrayList<Map<String, Object>>();
        for (BlockArticle blockArticle : list) {
            long lastUpdateTime = blockArticle.getLastUpdateTime().getTime();
            String lastDateStr = dateToString(new Date(lastUpdateTime));
            Map<String, Object> articleMap = new HashMap<String, Object>();
            articleMap.put("lastUpdateTime", dateToString(new Date(blockArticle.getLastUpdateTime().getTime())));
            articleMap.put("id", blockArticle.getId());
            articleMap.put("title", blockArticle.getTitle());
            if(date.equals(lastDateStr)){
                timeList.add(articleMap);
            }else {
                if(!date.equals("")){
                    announceMap.put(date, timeList);
                }
                timeList = new ArrayList<Map<String, Object>>();
                timeList.add(articleMap);
                date = lastDateStr;
            }
        }
        if(!date.equals("")){
            announceMap.put(date, timeList);
        }
        map.put("announceMap", announceMap);
        map.put("currentPage", currentPage);
        map.put("total", blockArticleService.countBlockArticleByType(1));
        return map;
    }

    @RequestMapping("/news")
    public Map<String,Object> news(@RequestParam (required = false, defaultValue = "1") int currentPage){
        Map<String, Object> map = new HashMap<String, Object>();
        List<BlockArticle> list = blockArticleService.listBlockArticlesByType(2, (currentPage - 1) * numPerPage, numPerPage);
        List<Map<String, Object>> newsList = new ArrayList<Map<String, Object>>();
        for (BlockArticle blockArticle : list) {
            Map<String, Object> newsMap = new HashMap<>();
            newsMap.put("lastUpdateTime", dateToString(new Date(blockArticle.getLastUpdateTime().getTime())));
            newsMap.put("id", blockArticle.getId());
            newsMap.put("title", blockArticle.getTitle());
            newsMap.put("content", blockArticle.getContent());
            newsMap.put("imgUrl", blockArticle.getImgUrl());
            newsList.add(newsMap);
        }
        map.put("list", newsList);
        map.put("currentPage", currentPage);
        map.put("total", blockArticleService.countBlockArticleByType(2));
        return map;
    }

    @RequestMapping("/article")
    public Map<String, Object> findBlockArticle(@RequestParam (required = false, defaultValue = "1") int id){
        BlockArticle blockArticle = blockArticleService.findBlockArticleById(id);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", blockArticle.getId());
        map.put("time", blockArticle.getLastUpdateTime());
        map.put("content", blockArticle.getContent());
        map.put("title", blockArticle.getTitle());
        map.put("docurl", blockArticle.getDocUrl());
        return map;
    }

    @RequestMapping("/weixin")
    public Map<String, Object> weixin(
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "100") int pageSize
    ){
        Map<String, Object> map = new HashMap<String, Object>();
        List<WeixinArticle> list = weixinArticleService.list((currentPage - 1) * pageSize, pageSize);
        JSONArray jsonArray = new JSONArray();
        for (WeixinArticle weixinArticle : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", weixinArticle.getId());
            jsonObject.put("title", weixinArticle.getTitle());
            jsonObject.put("author", weixinArticle.getAuthor());
            jsonObject.put("time", dateToString(new Date(weixinArticle.getCreateTime().getTime())));
            jsonObject.put("intro", weixinArticle.getIntroduction());
            jsonObject.put("url", weixinArticle.getUrl());
            jsonObject.put("imgUrl", weixinArticle.getImgUrl());
            jsonArray.add(jsonObject);
        }
        map.put("weixin", jsonArray);
        map.put("currentPage", currentPage);
        map.put("pageSize", pageSize);
        map.put("total", weixinArticleService.count());
        return map;
    }

    private String dateToString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}

