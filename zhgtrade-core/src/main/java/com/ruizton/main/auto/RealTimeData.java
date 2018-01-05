package com.ruizton.main.auto;

import com.alibaba.fastjson.JSONArray;
import com.ruizton.main.Enum.EntrustRobotStatusEnum;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.dto.FentrustData;
import com.ruizton.main.model.Fentrust;
import com.ruizton.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

//实时数据
public class RealTimeData {

	@Autowired
    private RealTimeDataService realTimeDataService;

	public double getLatestDealPrize(int id){
		return realTimeDataService.getLatestDealPrize(id);
	}
	public double getLowestSellPrize(int id){
		return realTimeDataService.getLowestSellPrize(id);
	}

	public void init(){
	}


	public void clear(){
	}
	/**
	 * 添加买单
	* 比特家
	* CopyRight : www.btc58.cc
	* Author : xxp（xxly68@qq.com）
	* Date : 2016年3月31日 上午10:29:37
	 */
	public void addEntrustBuyMap(int id,Fentrust fentrust) {
		realTimeDataService.addEntrustBuyMap(id, fentrust);
	}
	/**
	 * 移除买单
	* 比特家
	* CopyRight : www.btc58.cc
	* Author : xxp（xxly68@qq.com）
	* Date : 2016年3月31日 上午10:28:08
	 */

	public void removeEntrustBuyMap(int id,Fentrust fentrust){
		realTimeDataService.removeEntrustBuyMap(id, fentrust);
	}

	/**
	 * 移除机器人订单
	 * @param id
	 * @param fentrustData
	 */
	public void removeEntrustBuyMap(int id, FentrustData fentrustData){
		realTimeDataService.removeEntrustBuyMap(id, fentrustData);
	}


}


