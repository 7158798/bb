package com.ruizton.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ruizton.main.dto.WeixinArticleData;
import com.ruizton.main.model.WeixinArticle;
import com.ruizton.main.service.front.WeixinArticleService;
import com.ruizton.util.DateUtils;
import com.ruizton.util.HttpClientUtils;
import com.ruizton.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sunpeng on 2016/7/29.
 */
public class AutoSaveWeixinTask {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WeixinArticleService weixinArticleService;

    public static final String url = "http://www.newrank.cn/xdnphb/detail/getAccountArticle";

    public void run() {
        try{
            Map<String, String> param = new HashMap<String, String>();
            param.put("flag", "false");
            param.put("uuid", "B72CF5C3D113068FE5288FBA29D554C3");
            param.put("nonce", "0b9151ce8");
            param.put("xyz", "bbfc349f353b7811ef7c23965cd2c22b");
            String response = HttpClientUtils.get(url, param);
            List<WeixinArticle> list = parseArticles(response);
            for (WeixinArticle weixinArticle : list) {
                weixinArticleService.saveWeixinArticle(weixinArticle);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private List<WeixinArticle> parseArticles(String response){
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONArray jsonArray = jsonObject.getJSONObject("value").getJSONArray("lastestArticle");
        List<WeixinArticle> list = new ArrayList<WeixinArticle>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject articleObject = jsonArray.getJSONObject(i);
            WeixinArticle weixinArticle = new WeixinArticle();
            weixinArticle.setTitle(articleObject.getString("title"));
            weixinArticle.setAuthor(articleObject.getString("author"));
            weixinArticle.setIntroduction(articleObject.getString("summary"));
            weixinArticle.setCreateTime(Timestamp.valueOf(articleObject.getString("publicTime")));
            weixinArticle.setWeixinId(articleObject.getInteger("id"));
            weixinArticle.setUrl(articleObject.getString("url"));
            list.add(weixinArticle);
        }
        return list;
    }


    private String convertDate(String date){
        return date.replace("年", "-").replace("月", "-").replace("日", "");
    }




}
