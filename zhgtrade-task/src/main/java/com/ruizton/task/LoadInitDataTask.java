package com.ruizton.task;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.cache.data.LatestDealDataService;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.dto.LatestDealData;
import com.ruizton.main.model.Fentrust;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.EntrustlogService;
import com.ruizton.main.service.front.FrontTradeService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.util.Constants;
import com.zhguser.UserClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 加载初始化数据
 * 只需要执行一个
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-01 14:37
 */
public class LoadInitDataTask implements Runnable {

    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private FrontTradeService frontTradeService;
    @Autowired
    private LatestDealDataService latestDealDataService;
    @Autowired
    private RealTimeDataService realTimeDataService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private EntrustlogService entrustlogService;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private RedisTemplate redisTemplate;

    private Logger logger = Logger.getLogger(getClass());

    @PostConstruct
    public void init() {
        /**
         * 数据只会初始化一次
         * 要重复初始化，需要删掉 Setting 表中的init记录
         * 以及各个表的数据，否则会导致重复数据
         */
        initCountData();
        initData();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000 * 60 * 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println("sync FvirtualCoinType Data");
            List<Fvirtualcointype> list = this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
            for (Fvirtualcointype fvirtualcointype : list) {
                insertOrUpdateFvirtualCoinTypeData(fvirtualcointype);
            }
        }
    }

    /**
     * 初始化用户总数，交易总额
     */
    private void initCountData() {
        logger.info("初始化用户总数，交易总额");
        _initCountData();
        System.out.println("初始化用户总数，交易总额完成");
    }

    private void _initCountData() {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                long userCount = 0;
                if(Constants.ConnectUserDbFlag)
                    userCount = UserClient.getUserCount();
                else {
                    String filter = "where fstatus=1";
                    userCount = (long) adminService.getAllCount("Fuser", filter);
                }
                int tradeAmt = Double.valueOf(entrustlogService.getFentrustAmt()).intValue();
                connection.hSet(Constants.REDIS_CACHE_COUNT, "userQty".getBytes(), JSON.toJSONBytes(userCount));
                connection.hSet(Constants.REDIS_CACHE_COUNT, "tradeAmt".getBytes(), JSON.toJSONBytes(tradeAmt));
                return null;
            }
        });
    }

    /**
     * 初始化最近成交数据
     * 以及挂单数据
     */
    private void initData() {
        logger.info("初始化最近成交数据");
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                Boolean init = connection.hExists("cache:setting".getBytes(), "initData".getBytes());
                if (!init) {
                    connection.hSet("cache:setting".getBytes(), "initData".getBytes(), "true".getBytes());
                    _initData();
                }
                return null;
            }
        });
        executorService.execute(this);
    }

    private void _initData() {
        List<Fvirtualcointype> list = this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal) ;

        for (Fvirtualcointype fvirtualcointype : list) {

            // 初始化数据
            System.out.println("插入:" + fvirtualcointype);
            insertOrUpdateFvirtualCoinTypeData(fvirtualcointype);

            // 最新成交价
            Fentrust latestDeal = this.frontTradeService.findLatestDeal(fvirtualcointype.getFid()) ;
            if (latestDeal != null) {
                latestDealDataService.updateLastDealPrice(fvirtualcointype.getFid(), latestDeal.getFprize());
            }
        }
    }

    private void insertOrUpdateFvirtualCoinTypeData(Fvirtualcointype fvirtualcointype) {
        LatestDealData latestDealData = new LatestDealData();
        latestDealData.setStatus(fvirtualcointype.getFstatus());
        latestDealData.setFid(fvirtualcointype.getFid());
        latestDealData.setFname(fvirtualcointype.getFname());
        latestDealData.setfShortName(fvirtualcointype.getfShortName());
        latestDealData.setFisShare(fvirtualcointype.isFisShare());
        latestDealData.setFname_sn(fvirtualcointype.getFname()+"("+fvirtualcointype.getfShortName()+")");
        latestDealData.setFurl(fvirtualcointype.getFurl());
        latestDealData.setCoinTradeType(fvirtualcointype.getCoinTradeType());
        latestDealData.setEquityType(fvirtualcointype.getEquityType());
        latestDealData.setHomeShow(fvirtualcointype.isHomeShow());

        if (realTimeDataService.getLatestDealData(latestDealData.getFid()) == null) {
            latestDealDataService.addLatestDealData(latestDealData);
        } else {
            latestDealDataService.updateLatestDealDataInfo(latestDealData);
        }
    }

}
