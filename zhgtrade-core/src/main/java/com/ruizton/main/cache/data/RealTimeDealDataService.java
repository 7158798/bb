package com.ruizton.main.cache.data;

import com.ruizton.main.dto.LatestDealData;

import java.util.List;

/**
 * 实时交易数据查询服务
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-03-23 10:02
 */
public interface RealTimeDealDataService {

    /**
     * 最新成交价
     * @return
     */
    List<LatestDealData> getLatestDealDataList();

    /**
     * 获取排行统计数据
     * @param id
     * @return
     */
    LatestDealData getLatestDealData(int id);

}
