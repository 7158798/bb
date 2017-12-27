package com.ruizton.task.queue;

import com.ruizton.main.auto.KlinePeriodData2;
import com.ruizton.main.dto.FentrustlogData;
import com.ruizton.main.model.Fperiod;
import com.ruizton.main.mq.MessageListener;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 同步成交数据
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-01 14:37
 */
public class SyncFentrustLogDataQueue implements MessageListener<FentrustlogData> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageQueueService messageQueueService;

    @Autowired
    private KlinePeriodData2 klinePeriodData2;

    @PostConstruct
    public void run() {
        messageQueueService.subscribe(QueueConstants.SYNC_FENTRUST_LOG_DATA_QUEUE, this, FentrustlogData.class);
    }

    @Override
    public void onMessage(FentrustlogData message) {

        double count = message.getFcount();
        double prize = message.getFprize();

        Fperiod fperiod = new Fperiod() ;
        fperiod.setFid(message.getFid());
        fperiod.setFdi(prize) ;
        fperiod.setFgao(prize) ;
        fperiod.setFkai(prize) ;
        fperiod.setFliang(count) ;
        fperiod.setFshou(prize) ;
        fperiod.setFtime(Utils.getTimestamp()) ;

        klinePeriodData2.setOneMiniteData(message.getFviFid(), fperiod);
    }

}
