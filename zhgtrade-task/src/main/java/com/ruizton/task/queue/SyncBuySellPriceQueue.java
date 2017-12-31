package com.ruizton.task.queue;

import com.ruizton.main.Enum.EntrustTypeEnum;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.dto.FentrustData;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.mq.MessageListener;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.main.cache.data.LatestDealDataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-08 14:57
 */
public class SyncBuySellPriceQueue implements MessageListener<FentrustData>, Runnable {

    @Autowired
    private MessageQueueService messageQueueService;

    @Autowired
    private LatestDealDataService latestDealDataService;

    @Autowired
    private RealTimeDataService realTimeDataService;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;

    private Logger logger = Logger.getLogger(getClass());

    @PostConstruct
    public void init() {
        messageQueueService.subscribe(QueueConstants.SYNC_BUY_SELL_PRICE_DATA_QUEUE, this, FentrustData.class);
        executorService.execute(this);
    }

    @Override
    public void run() {
        /**
         * 每隔几秒同步一次最高买、最低卖
         */
        while (true) {
            try {
                Thread.sleep(1000 * 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                List<Fvirtualcointype> fvirtualcointypes = frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
                for (final Fvirtualcointype fvirtualcointype : fvirtualcointypes) {
                    updateHighestPrice(fvirtualcointype.getFid());
                    updateLowestPrice(fvirtualcointype.getFid());
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onMessage(FentrustData message) {
        if (message.getFentrustType() == EntrustTypeEnum.BUY) {
            updateHighestPrice(message.getFviFid());
        } else {
            updateLowestPrice(message.getFviFid());
        }
    }

    private void updateHighestPrice(int fvifid) {
        // 更新最高买价
        double prize = realTimeDataService.getHighestBuyPrize(fvifid);
        latestDealDataService.updateHighestPrice(fvifid, prize);
    }

    private void updateLowestPrice(int fvifid) {
        // 更新最低卖价
        double prize = realTimeDataService.getLowestSellPrize(fvifid);
        latestDealDataService.updateLowestPrice(fvifid, prize);
    }



}
