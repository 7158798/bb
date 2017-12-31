package com.ruizton.main.cache.data;

import java.util.List;

import com.ruizton.main.dto.FentrustData;

/**
 * 买入/卖出委托单（深度）
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-03-23 10:02
 */
public interface RealTimeEntrustDepthService {

    /**
     * 委托卖出列表
     * @param id
     * @param deep
     * @return
     */
    String getSellDepthMap(int id,int deep);
    /**
     * 委托买入列表
     * @param id
     * @param deep
     * @return
     */
    String getBuyDepthMap(int id,int deep);

    /**
     * 获取行情
     * @param fid
     * @return
     */
    String getMarketJSON(int fid);
    
}
