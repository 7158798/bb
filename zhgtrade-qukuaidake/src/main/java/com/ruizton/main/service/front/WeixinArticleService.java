package com.ruizton.main.service.front;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.dao.WeixinArticleDao;
import com.ruizton.main.dto.WeixinArticleData;
import com.ruizton.main.model.WeixinArticle;
import com.ruizton.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * Created by sunpeng on 2016/7/29.
 */
@Service
public class WeixinArticleService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private WeixinArticleDao weixinArticleDao;

    public void addWeixinArticleData(WeixinArticleData weixinArticleData){
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForHash().put(Constants.REDIS_WEIXIN_ARTICLE, weixinArticleData.getId() + "", JSON.toJSONString(weixinArticleData));
    }

    public void addWeixinArticleDatas(List<WeixinArticleData> list){
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        Map<String, String> map = new HashMap<String, String>();
        for (WeixinArticleData weixinArticleData : list){
            map.put(weixinArticleData.getId() + "", JSON.toJSONString(weixinArticleData));
        }
        redisTemplate.opsForHash().putAll(Constants.REDIS_WEIXIN_ARTICLE, map);
    }

    public List<WeixinArticle> list(int firstResult, int maxResult){
        return weixinArticleDao.list(firstResult, maxResult);
    }


    public void saveWeixinArticle(WeixinArticle weixinArticle){
        boolean isExist = weixinArticleDao.isExist(weixinArticle.getWeixinId());
        if(!isExist){
            weixinArticleDao.save(weixinArticle);
        }
    }

    public int count(){
        return weixinArticleDao.count();
    }
//
//    public void deleteWeixinArticle(WeixinArticle weixinArticle){
//        weixinArticleDao.delete(weixinArticle);
//    }
//
//    public void updateWeixinArticle(WeixinArticle weixinArticle){
//        weixinArticleDao.update(weixinArticle);
//    }
//
//    public List<WeixinArticle> listWeixinArticles(int firstResult, int maxResult){
//        return weixinArticleDao.list(firstResult, maxResult);
//    }
//


}
