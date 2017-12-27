package com.ruizton.main.cache.data.impl;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.Enum.EntrustRobotStatusEnum;
import com.ruizton.main.Enum.EntrustTypeEnum;
import com.ruizton.main.cache.data.RealTimeCenter;
import com.ruizton.main.cache.data.RealTimeEntrustService;
import com.ruizton.main.dto.FentrustData;
import com.ruizton.main.dto.FentrustlogData;
import com.ruizton.main.model.Fentrust;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-01 14:37
 */
@Service("realTimeEntrustService")
public class RealTimeEntrustServiceImpl implements RealTimeEntrustService {

    @Resource
    private JedisPool jedisPool;
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private ExecutorService executorService;
    @Resource
    private RealTimeCenter realTimeCenter;

    @Override
    public Set<FentrustlogData> getEntrustSuccessMap(final int id) {
        return realTimeCenter.getEntrustLog(id);
    }

    @Override
    public Set<FentrustlogData> getEntrustSuccessMapLimit(final int id, final int limit) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] mkey = ("cache:fentrustlog:" + id).getBytes();
            Set<byte[]> bytes = jedis.zrevrange(mkey, 0, limit - 1);
            Set<FentrustlogData> set = new TreeSet<FentrustlogData>();
            int i = bytes.size();
            for (byte[] b : bytes) {
                FentrustlogData data = JSON.parseObject(b, FentrustlogData.class);
                data.setFid(i--);
                set.add(data);
            }
            return set;
        }
    }

    @Override
    public void addEntrustBuyMap(int id, Fentrust fentrust) {
        FentrustData data = new FentrustData();
        Fuser fuser = fentrust.getFuser();
        data.setFuid(fuser.getFid());
        data.setWalletId(fuser.getFwallet().getFid());
        data.setFsuccessAmount(fentrust.getFsuccessAmount());
        data.setFamount(fentrust.getFamount());
        data.setFcreateTime(fentrust.getFcreateTime());
        data.setFfees(fentrust.getFfees());
        data.setFleftfees(fentrust.getFleftfees());
        data.setFcount(fentrust.getFcount());
        data.setFid(fentrust.getFid());
        data.setFviFid(id);
        data.setFentrustType(fentrust.getFentrustType());
        data.setFleftCount(fentrust.getFleftCount());
        data.setFprize(fentrust.getFprize());
        data.setFstatus(fentrust.getFstatus());
        data.setRobotStatus(fentrust.getRobotStatus());
        if (fentrust.getFentrustType() == EntrustTypeEnum.BUY) {
            data.setFneedFee(false);
        } else {
            data.setFneedFee(fentrust.getFuser().getFneedFee());
            if (data.isFneedFee()) {
                data.setFlevel(fentrust.getFuser().getFscore().getFlevel());
            }
        }
//        mongoTemplate.insert(data, Constants.FENTRUST_DATA);
//        messageQueueService.publish(QueueConstants.SYNC_BUY_SELL_PRICE_DATA_QUEUE, data);
//        depthCalculateQueue.calculateDepthEntrust(data);
//        messageQueueService.publish(QueueConstants.DEPTH_ENTRUST_QUEUE, data);
//        System.out.println("进来了吗。。。");
        if (fentrust.getRobotStatus() == EntrustRobotStatusEnum.Normal) {
            messageQueueService.publish(QueueConstants.SOLVE_ENTRUST_QUEUE + id, data);
        } else {
            messageQueueService.publish(QueueConstants.ROBOT_ENTRUST_QUEUE + id, data);
        }
    }

    @Override
    public void removeEntrustBuyMap(int id, Fentrust fentrust) {
        FentrustData data=new FentrustData();
        data.setFentrustType(fentrust.getFentrustType());
        data.setFid(fentrust.getFid());
        data.setFleftCount(fentrust.getFleftCount());
        data.setFprize(fentrust.getFprize());
        data.setFviFid(id);
        data.setFstatus(fentrust.getFstatus());
        removeEntrustBuyMap(id, data);
    }

    @Override
    public void addEntrustSellMap(int id, Fentrust fentrust) {
    	addEntrustBuyMap(id, fentrust);
    }

    @Override
    public void removeEntrustSellMap(int id, Fentrust fentrust) {
        removeEntrustBuyMap(id, fentrust);
    }

	@Override
	public void removeEntrustSellMap(final int id, final FentrustData data) {
		executorService.execute(new Runnable() {
            @Override
            public void run() {
                messageQueueService.publish(QueueConstants.SOLVE_ENTRUST_QUEUE + id, data);
            }
        });
	}

	@Override
	public void removeEntrustBuyMap(int id, FentrustData fentrustData) {
		removeEntrustSellMap(id, fentrustData);

	}

}
