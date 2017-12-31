package com.ruizton.task.queue;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.auto.KlinePeriodData2;
import com.ruizton.main.model.Fperiod;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.front.FrontOthersService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.util.Constants;
import com.ruizton.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 比特家
 * 计算每分钟K线
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-03-31 22:10
 */
public class CalculateMinuteKlineQueue {

    private Logger logger = Logger.getLogger(getClass());
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private FrontOthersService frontOthersService;
    @Autowired
    private KlinePeriodData2 klinePeriodData;
    @Autowired
    private RedisTemplate redisTemplate;

    private boolean isFirstInit = true;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");

    @PostConstruct
    public void run() {
    }

    private void calculateKline(Fvirtualcointype fvirtualcointype, long begin, long end) {
        try {
            Fperiod fperiod = klinePeriodData.getOneMiniteData(fvirtualcointype, begin, end);
            if (fperiod != null) {
                fperiod.setFvirtualcointype(fvirtualcointype);
                frontOthersService.addFperiod(fperiod);
                klinePeriodData.addFperiod(fvirtualcointype.getFid(), 0, fperiod);
            }
            klinePeriodData.generateJson(fvirtualcointype.getFid());
            klinePeriodData.generateIndexJson(fvirtualcointype.getFid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        try {
            long now = simpleDateFormat.parse(simpleDateFormat.format(Utils.getTimestamp())).getTime() - 60 * 1000L;

            //没打开交易，不用生成行情图
            

            isFirstInit = false;

            List<Fvirtualcointype> fvirtualcointypes = this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);

            final long begin = now - 60 * 1000L;
            final long end = now - 1;


            for (int i = 0; i < fvirtualcointypes.size(); i++) {
                final Fvirtualcointype fvirtualcointype = fvirtualcointypes.get(i);
                if (!openTrade(new Timestamp(begin), fvirtualcointype.getOpenTrade()) && !isFirstInit) {
	                continue;
	            }
                // 用多线程来计算K线数据，提高性能
//                executorService.submit(new Runnable() {
//                    @Override
//                    public void run() {
//                        calculateKline(fvirtualcointype, begin, end);
//                    }
//                });
                long beginTime = System.currentTimeMillis();
                calculateKline(fvirtualcointype, begin, end);
                long endTime = System.currentTimeMillis();
                logger.debug("calculate Kline " + fvirtualcointype.getFid() + " :" + (endTime - beginTime) + "ms.");
            }

            redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.set(Constants.REDIS_CACHE_LAST_UPDATE_TIME, JSON.toJSONBytes(begin));
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean openTrade(Timestamp now, String value) {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        try {
            Date beforeDate = df.parse(value.trim().split("-")[0]);
            Date afterDate = df.parse(value.trim().split("-")[1]);
            Date time = df.parse(df.format(now));
            if (time.getTime() > beforeDate.getTime() && time.getTime() <= afterDate.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("输入的时间区间不能进行格式化，时间格式为：HH:mm-HH:mm");
        }

        //抛出异常，返回false
        return false;
//        int nows = Integer.parseInt(new SimpleDateFormat("HH").format(now));
//
//        boolean flag = true;
////        String value = frontVirtualCoinService.getOpenTrade(symbol);
////        String value = this.systemArgsService.getValue("openTrade");
//        int min = Integer.parseInt(value.trim().split("-")[0]);
//        int max = Integer.parseInt(value.trim().split("-")[1]);
//
//        if (min == 0 && max == 24) {
//            return false;
//        }
//        if (min == 24 && max == 0) {
//            return true;
//        }
//
//        if (min <= max) {
//            if (nows >= min && nows <= max) {
//                flag = false;
//            }
//        }
//
//        if (max < min) {
//            if (!(nows > max && nows < min)) {
//                flag = false;
//            }
//        }
//
//        return flag;
    }

}
