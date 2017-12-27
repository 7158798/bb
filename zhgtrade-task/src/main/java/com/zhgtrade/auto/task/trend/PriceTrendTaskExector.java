package com.zhgtrade.auto.task.trend;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.cache.data.impl.RealTimePriceServiceImpl;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.EntrustlogService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.util.DateUtils;
import com.zhgtrade.auto.task.TaskExecutor;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * DESC:    首页3天价格趋势图
 * <p/>
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin(1186270005@qq.com)
 * Date： 2016-05-18 16:43
 */
public class PriceTrendTaskExector implements TaskExecutor{
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private EntrustlogService entrustlogService;
    @Autowired
    private RealTimePriceServiceImpl realTimePriceService;

    private final static String TIME_FORMAT = "yyyy-MM-dd HH";

    public void init(){
        this.execute();
    }

    @Override
    public void execute() {
        List<Fvirtualcointype> types = virtualCoinService.findAll();
        Date startDate = DateUtils.getDaysBefore(3);
//        System.out.println("---------------------开始统计3天每小时成交价格趋势---------------------");
        Map<String, Object> datas = new HashedMap(types.size());
        for(final Fvirtualcointype type : types){
            if(type.isFisShare() && VirtualCoinTypeStatusEnum.Normal == type.getFstatus()){
                Map map = entrustlogService.findAvgPriceByHour(type.getFid(), startDate, null);
                List<Object[]> list = repairSample(startDate, map, type.getFid());
//                System.out.println(JSON.toJSONString(list));

                datas.put(type.getFid().toString(), list);
//                System.out.println("---------------------缓存统" + type.getFname() + "成交价格趋势---------------------");
            }
        }
        realTimePriceService.pushHourPriceTrendData(datas);
//        System.out.println("---------------------结束统计3天每小时成交价格趋势---------------------");
    }

    private List<Object[]> repairSample(Date startDate, Map<String, Object> map, int symbol){
        int k = 1;          // 采样区间的坐标数
        int repairIndex = 0;
        double start = 0;   // 采样区间开始价格
        double end = 0;     // 采样区间结尾价格
        boolean lastFlag = false;

        Object nextPriceObj = null;

        // 获取区间结束价格
        Date curTime = new Date();

        List<Object[]> datas = new ArrayList<>(72);
        for(;startDate.before(curTime);){
            startDate = DateUtils.getHoursAfter(startDate, 1);
            long timePoint = DateUtils.formatDate(DateUtils.formatDate(startDate, TIME_FORMAT), TIME_FORMAT).getTime();
            Object curPrice = map.get(timePoint);
            if(null != curPrice){
                // 有数据
                k = 1;
                repairIndex = 0;
                start = Double.parseDouble(curPrice.toString());
                datas.add(new Object[]{timePoint, curPrice});
            }else if(0 == start && !lastFlag){
                // 没有数据获前几天最后交易价格
                start = entrustlogService.getLastestDealPrice(symbol, null, startDate);
                datas.add(new Object[]{timePoint, start});
                lastFlag = true;
            }else{
                // 采样
                if(1 == k){
                    Date time = startDate;
                    while(time.before(curTime)){
                        time = DateUtils.getHoursAfter(time, 1);
                        long timeVal = DateUtils.formatDate(DateUtils.formatDate(time, TIME_FORMAT), TIME_FORMAT).getTime();
                        nextPriceObj = map.get(timeVal);
                        if(null != nextPriceObj){
                            break;
                        }else{
                            nextPriceObj = null;
                        }
                        k ++;
                    }
                }

                if(null == nextPriceObj){
                    // 后面没有数据
                    datas.add(new Object[]{timePoint, start});
                }else{
                    // 补样
                    repairIndex ++;
                    end = Double.parseDouble(nextPriceObj.toString());
                    double d = (end - start) / k;
                    double price = start + Math.abs(d) * (1 - Math.random() * 2) + d * repairIndex;
                    datas.add(new Object[]{timePoint, price});
                }
            }
        }
        return  datas;
    }

    public static void main(String[] args) {
        System.out.println(1 - Math.random() * 2);
    }
}
