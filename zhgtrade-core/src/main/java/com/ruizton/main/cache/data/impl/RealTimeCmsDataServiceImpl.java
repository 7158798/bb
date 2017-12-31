package com.ruizton.main.cache.data.impl;

import com.ruizton.main.cache.data.RealTimeCmsDataService;
import com.ruizton.main.model.Farticle;
import com.ruizton.main.service.admin.ArticleService;
import com.ruizton.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * DESC:
 * <p/>
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp(xxly68@qq.com)
 * Date： 2016-05-27 10:30
 */
public class RealTimeCmsDataServiceImpl implements RealTimeCmsDataService {
    @Autowired
    private ArticleService articleService;
    /**
     * 获取资讯列表
     * @return
     */
    @Override
    public Map<String, List<Farticle>> getCmsContentList(){
        // 官方公告
        List<Farticle> ggItems = articleService.list(0, 6, " where farticletype.fid = 2 order by isTop desc, fid desc", true);
        // 市场动态
        List<Farticle> newerItems = articleService.list(0, 6, " where farticletype.fid = 9 order by isTop desc, fid desc", true);
        // 项目展示
        List<Farticle> xmItems = articleService.list(0, 6, " where farticletype.fid = 8 order by isTop desc, fid desc", true);

        Map<String, List<Farticle>> map = new HashMap<>(3);
        if(CollectionUtils.isEmpty(ggItems)){
            map.put("2", Collections.emptyList());
        }else{
            map.put("2", ggItems);
        }
        if(CollectionUtils.isEmpty(ggItems)){
            map.put("9", Collections.emptyList());
        }else{
            map.put("9", newerItems);
        }
        if(CollectionUtils.isEmpty(ggItems)){
            map.put("8", Collections.emptyList());
        }else{
            map.put("8", xmItems);
        }

        return map;

        /*return (List<CmsContentItem>) redisTemplate.execute(new RedisCallback() {
            @Override
            public List<CmsContentItem> doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] json = connection.get(key.getBytes());
                if(!ObjectUtils.isEmpty(json)){
                    return Arrays.asList(JSON.parseObject(json, CmsContentItem[].class));
                }
                return Collections.emptyList();
            }
        });*/
    }

    /*@Override
    public void cacheCmsContentList(List<CmsContentItem> items) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                String key = Constants.CMS_CONTENT_LIST_PREFIX + PinyinHelper.getShortPinyin(items.get(0).getTypeName());
                connection.set(key.getBytes(), JSON.toJSONBytes(items));
                return null;
            }
        });
    }*/

    /**
     *  初始化缓存信息
     *
     */
    /*public void init(){
        // 缓存列表
        System.out.println("-------------开始缓存文章列表------------");
        List<Farticletype> types = articleTypeService.findAll();
        for(Farticletype type : types){
            Farticle farticle = articleService.findOne(type);
            if(null != farticle){
                cmsContentBuilderWatcher.notify(farticle);
            }
        }
        System.out.println("-------------结束缓存文章列表------------");
    }*/
}
