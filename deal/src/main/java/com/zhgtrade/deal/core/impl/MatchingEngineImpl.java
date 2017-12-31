package com.zhgtrade.deal.core.impl;

import com.zhgtrade.deal.Enum.EntrustTypeEnum;
import com.zhgtrade.deal.core.MatchingEngine;
import com.zhgtrade.deal.core.TradeService;
import com.zhgtrade.deal.listener.DealMarkingListener;
import com.zhgtrade.deal.model.FentrustData;
import com.zhgtrade.deal.model.FentrustlogData;
import com.zhgtrade.deal.util.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * 撮合引擎
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-05-10 14:32
 */
public class MatchingEngineImpl implements MatchingEngine {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private DealMarkingListener dealMarkingListener;

    @Resource
    private TradeService tradeService;

    public MatchingEngineImpl() {
    }

    public MatchingEngineImpl(DealMarkingListener dealMarkingListener) {
        this.dealMarkingListener = dealMarkingListener;
    }

    public double updateDealMaking(FentrustData _buyFentrust, FentrustData _sellFentrust, int fviFid) {

        double successPrice;    // 成交价
        double successCount;    // 成交量

        // 传过来的对象是缓存对象，不能直接修改，只有操作成功了才能修改，不可靠，可以在更新的sql上面加上状态检查
        FentrustData buyFentrust = _buyFentrust.clone();
        FentrustData sellFentrust = _sellFentrust.clone();

        // 传过来的是缓存对象，不以缓存为主，重新查询数据库，比较可靠
//        FentrustData buyFentrust = findByFid(_buyFentrust.getFid());
//        FentrustData sellFentrust = findByFid(_sellFentrust.getFid());

        if (buyFentrust.getFid() > sellFentrust.getFid()) {
            successPrice = sellFentrust.getFprize();
        } else {
            successPrice = buyFentrust.getFprize();
        }
        if (buyFentrust.getFleftCount() > sellFentrust.getFleftCount()) {
            successCount = sellFentrust.getFleftCount();
        } else {
            successCount = buyFentrust.getFleftCount();
        }

        Timestamp updateTime = new Timestamp(System.currentTimeMillis());

        //买单日志
        FentrustlogData buyFentrustlog = new FentrustlogData();
        buyFentrustlog.setFamount(MathUtils.multiply(successCount, successPrice));
        buyFentrustlog.setFprize(successPrice);
        buyFentrustlog.setFcount(successCount);
        buyFentrustlog.setFcreateTime(updateTime);
        buyFentrustlog.setIsactive(buyFentrust.getFid() > sellFentrust.getFid());
        buyFentrustlog.setFviFid(fviFid);
        buyFentrustlog.setfEntrustType(EntrustTypeEnum.BUY);

        //卖单日志
        FentrustlogData sellFentrustlog = new FentrustlogData();
        sellFentrustlog.setFamount(MathUtils.multiply(successCount, successPrice));
        sellFentrustlog.setFcount(successCount);
        sellFentrustlog.setFprize(successPrice);
        sellFentrustlog.setFcreateTime(updateTime);
        sellFentrustlog.setIsactive(sellFentrust.getFid() > buyFentrust.getFid());
        sellFentrustlog.setFviFid(fviFid);
        sellFentrustlog.setfEntrustType(EntrustTypeEnum.SELL);

        log.trace("updateDealMaking buy = {}, sell = {}, successPrice = {}, successCount = {}, buy active = {}, sell active = {}", buyFentrust.getFid(), sellFentrust.getFid(), successPrice, successCount, buyFentrustlog.isactive(), sellFentrustlog.isactive());

        boolean isSuccesss = tradeService.updateDealMaking(buyFentrust, sellFentrust, buyFentrustlog, sellFentrustlog);
        // 如果不成功，返回成交量为0
        if (!isSuccesss) {
            log.error("update unsuccessful.");
            successCount = 0D;
        } else {
            dealMarkingListener.writeLog(buyFentrustlog);
            dealMarkingListener.writeLog(sellFentrustlog);
            log.trace("update successful.");
            log.trace("sync entrust cache buy = {}, sell = {}", _buyFentrust.getFid(), _sellFentrust.getFid());
            // 同步缓存
            _buyFentrust.setFleftCount(buyFentrust.getFleftCount());
            _buyFentrust.setFsuccessAmount(buyFentrust.getFsuccessAmount());
            _buyFentrust.setFleftfees(buyFentrust.getFleftfees());
            _buyFentrust.setFstatus(buyFentrust.getFstatus());
            _buyFentrust.setFlastUpdatTime(buyFentrust.getFlastUpdatTime());

            _sellFentrust.setFleftCount(sellFentrust.getFleftCount());
            _sellFentrust.setFsuccessAmount(sellFentrust.getFsuccessAmount());
            _sellFentrust.setFleftfees(sellFentrust.getFleftfees());
            _sellFentrust.setFstatus(sellFentrust.getFstatus());
            _sellFentrust.setFlastUpdatTime(sellFentrust.getFlastUpdatTime());
        }

        return successCount;
    }

}
