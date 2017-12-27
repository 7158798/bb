package com.ruizton.main.cache.data.impl;

import com.ruizton.main.Enum.EntrustTypeEnum;
import com.ruizton.main.cache.data.RealTimeCenter;
import com.ruizton.main.cache.data.RealTimeEntrustDepthService;
import com.ruizton.main.dto.FentrustData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-01 14:37
 */
@Service("realTimeEntrustDepthService")
public class RealTimeEntrustDepthServiceImpl implements RealTimeEntrustDepthService {

    @Resource
    private RealTimeCenter realTimeCenter;

    public RealTimeEntrustDepthServiceImpl() {
    }

    @Override
    public String getSellDepthMap(int id, int deep) {
        return realTimeCenter.getEntrustList(String.format("%d:%d:%d", id, EntrustTypeEnum.SELL, deep));
    }

    @Override
    public String getBuyDepthMap(int id, int deep) {
        return realTimeCenter.getEntrustList(String.format("%d:%d:%d", id, EntrustTypeEnum.BUY, deep));
    }

    @Override
    public String getMarketJSON(int fid) {
        return realTimeCenter.getMarketDepth(fid);
    }
}
