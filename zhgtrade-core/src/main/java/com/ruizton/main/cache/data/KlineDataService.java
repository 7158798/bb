package com.ruizton.main.cache.data;

import com.ruizton.main.model.Fperiod;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-01 14:35
 */
public interface KlineDataService {

    /**
     * 添加K线数据
     * @param id
     * @param key
     * @param fperiod
     */
    void addFperiod(int id, int key, Fperiod fperiod);

    /**
     * 保存K线数据
     * @param id
     * @param key
     * @param jsonString
     */
    void setJsonString(int id, int key, String jsonString);

    /**
     * 获取K线
     * @param id
     * @param key
     * @return
     */
    String getJsonString(int id, int key);


    /**
     * 某段时间的K线
     * @param begin
     * @param end
     * @param id
     * @param key
     * @return
     */
    String getJsonString(String begin, String end, int id, int key);

    /**
     * 获取交易中心K线图JSON数据
     * @param id
     * @return
     */
    String getIndexJsonString(int id);
    
}
