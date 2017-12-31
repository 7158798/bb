package com.ruizton.main.cache.data;

import com.ruizton.main.dto.FentrustData;
import com.ruizton.main.dto.FentrustlogData;
import com.ruizton.main.model.Fentrust;

import java.util.Set;

/**
 * 买入/卖出委托单
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-03-23 10:02
 */
public interface RealTimeEntrustService {

    /**
     * 委托成功日志
     * @param id
     * @return
     */
    Set<FentrustlogData> getEntrustSuccessMap(int id);

    /**
     * 委托成功日志
     * @param id
     * @param limit
     * @return
     */
    Set<FentrustlogData> getEntrustSuccessMapLimit(int id, int limit);

    /**
     * 新增委托买入单
     * @param id
     * @param fentrust
     */
    void addEntrustBuyMap(int id, Fentrust fentrust);

    /**
     * 取消委托买入单
     * @param id
     * @param fentrust
     */
    void removeEntrustBuyMap(int id, Fentrust fentrust);

    void removeEntrustSellMap(int id, FentrustData fentrustData);

    void removeEntrustBuyMap(int id, FentrustData fentrustData);

    /**
     * 添加委托卖出单
     * @param id
     * @param fentrust
     */
    void addEntrustSellMap(int id, Fentrust fentrust);

    /**
     * 移除委托买入单
     * @param id
     * @param fentrust
     */
    void removeEntrustSellMap(int id, Fentrust fentrust);

}
