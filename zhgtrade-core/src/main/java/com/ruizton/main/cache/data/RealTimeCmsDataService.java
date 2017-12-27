package com.ruizton.main.cache.data;

import com.ruizton.main.model.Farticle;

import java.util.List;
import java.util.Map;

/**
 * DESC: 资讯内容
 * <p/>
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin(1186270005@qq.com)
 * Date： 2016-05-27 10:23
 */
public interface RealTimeCmsDataService {

    /**
     * 获取资讯列表
     * @return
     */
    Map<String, List<Farticle>> getCmsContentList();

    /**
     * 缓存资讯列表
     *
     * @param items
     */
//    void cacheCmsContentList(List<CmsContentItem> items);

}
