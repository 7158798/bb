package com.ruizton.main.cache.data.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.Enum.EntrustTypeEnum;
import com.ruizton.main.cache.data.RealTimeCenter;
import com.ruizton.main.cache.data.RealTimeEntrustDepthService;
import com.ruizton.main.dto.FentrustData;
import com.ruizton.main.model.Fentrust;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
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

    public double getHighestBuyPrizeExceptRobot(int id){
        String string = realTimeCenter.getEntrustList(String.format("%d:%d:%d", id, EntrustTypeEnum.BUY, 4));
        JSONArray data = JSONArray.fromObject(string);

        List<List> list = JSONArray.toList(data, String.class);
        if(list!= null && list.size()>0){
            return Double.valueOf(list.get(0).get(1).toString()) ;
//            return 0;
        }else {
            return 0;
        }
    }
    public double getLowestSellPrizeExceptRobot(int id){
        String string = realTimeCenter.getEntrustList(String.format("%d:%d:%d", id, EntrustTypeEnum.SELL, 4));
        JSONArray data = JSONArray.fromObject(string);
        List<List> list = JSONArray.toList(data, String.class);
        if(list!= null && list.size()>0){
            return Double.valueOf(list.get(0).get(1).toString()) ;
//            return 0;
        }else {
            return 0;
        }
    }

    public double getLastestNBuyPrice(int id,int N){
        String string = realTimeCenter.getEntrustList(String.format("%d:%d:%d", id, EntrustTypeEnum.BUY, 4));
        JSONArray data = JSONArray.fromObject(string);
        List<List> list = JSONArray.toList(data, String.class);
        if(list.size() == 0){
            return 0 ;
        }else{
            double price = Double.MAX_VALUE;
            if(list.size()<N)
                N =list.size();
            for ( int i = 0;i<N;i++){
                double price0 = Double.valueOf(list.get(i).get(1).toString());
                if( price0 < price ){
                    price = price0;
                    i++;
                }
                if(i>=N)
                    break;
            }
            return price;
        }
    }

    public double getLastestNSellPrice(int id,int N){
        String string = realTimeCenter.getEntrustList(String.format("%d:%d:%d", id, EntrustTypeEnum.SELL, 4));
        JSONArray data = JSONArray.fromObject(string);
        List<List> list = JSONArray.toList(data, String.class);
        if(list.size()==0){
            return 0 ;
        }else{
            double price = 0;
            if(list.size()<N)
                N =list.size();
            for ( int i = 0;i<N;i++){
                double price0 = Double.valueOf(list.get(i).get(1).toString());
                if( price0 > price ){
                    price = price0;
                    i++;
                }
                if(i>=N)
                    break;
            }
            return price;
        }
    }
}
