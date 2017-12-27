package com.ruizton.main.service.front;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import com.ruizton.main.service.admin.SystemArgsService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Preconditions;
import com.ruizton.main.Enum.EntrustPlanStatusEnum;
import com.ruizton.main.Enum.EntrustPlanTypeEnum;
import com.ruizton.main.Enum.EntrustRobotStatusEnum;
import com.ruizton.main.Enum.EntrustStatusEnum;
import com.ruizton.main.Enum.EntrustTypeEnum;
import com.ruizton.main.auto.RealTimeData;
import com.ruizton.main.dao.FentrustDAO;
import com.ruizton.main.dao.FentrustlogDAO;
import com.ruizton.main.dao.FentrustplanDAO;
import com.ruizton.main.dao.FfeesDAO;
import com.ruizton.main.dao.FsubscriptionDAO;
import com.ruizton.main.dao.FsubscriptionlogDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FvirtualcointypeDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Fentrust;
import com.ruizton.main.model.Fentrustlog;
import com.ruizton.main.model.Fentrustplan;
import com.ruizton.main.model.Fsubscription;
import com.ruizton.main.model.Fsubscriptionlog;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;
import com.ruizton.main.service.BaseService;
import com.ruizton.util.MathUtils;
import com.ruizton.util.Utils;

@Service
public class FrontTradeService {

    @Autowired
    private FentrustDAO fentrustDAO;
    @Autowired
    private FentrustlogDAO fentrustlogDAO;
    @Autowired
    private FwalletDAO fwalletDAO;
    @Autowired
    private FvirtualcointypeDAO fvirtualcointypeDAO;
    @Autowired
    private RealTimeData realTimeData;
    @Autowired
    private FuserDAO fuserDAO;
    @Autowired
    private FvirtualwalletDAO fvirtualwalletDAO;
    @Autowired
    private FentrustplanDAO fentrustplanDAO;
    @Autowired
    private FfeesDAO ffeesDAO;
    @Autowired
    private FsubscriptionDAO fsubscriptionDAO;
    @Autowired
    private FsubscriptionlogDAO fsubscriptionlogDAO;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    BaseService baseService;

//    public double updateDealMaking(Fentrust buyFentrust, Fentrust sellFentrust, int fviFid) {
//
//        long startNewLog = System.currentTimeMillis();
//        double successPrice;    // 成交价
//        double successCount;    // 成交量
//
//        if (buyFentrust.getFid() > sellFentrust.getFid()) {
//            successPrice = sellFentrust.getFprize();
//        } else {
//            successPrice = buyFentrust.getFprize();
//        }
//        if (buyFentrust.getFleftCount() > sellFentrust.getFleftCount()) {
//            successCount = sellFentrust.getFleftCount();
//        } else {
//            successCount = buyFentrust.getFleftCount();
//        }
//
//        Fvirtualcointype fvirtualcointype = new Fvirtualcointype();
//        fvirtualcointype.setFid(fviFid);
//
//        //买单日志
//        Fentrustlog buyFentrustlog = new Fentrustlog();
//        buyFentrustlog.setFamount(MathUtils.multiply(successCount, successPrice));
//        buyFentrustlog.setFprize(successPrice);
//        buyFentrustlog.setFcount(successCount);
//        buyFentrustlog.setFcreateTime(Utils.getTimestamp());
//        buyFentrustlog.setIsactive(buyFentrust.getFcreateTime().getTime() > sellFentrust.getFcreateTime().getTime());
//        buyFentrustlog.setFentrust(buyFentrust);
//        buyFentrustlog.setfEntrustType(EntrustTypeEnum.BUY);
//        buyFentrustlog.setFvirtualcointype(fvirtualcointype);
//
//        //卖单日志
//        Fentrustlog sellFentrustlog = new Fentrustlog();
//        sellFentrustlog.setFamount(MathUtils.multiply(successCount, successPrice));
//        sellFentrustlog.setFcount(successCount);
//        sellFentrustlog.setFprize(successPrice);
//        sellFentrustlog.setFcreateTime(Utils.getTimestamp());
//        sellFentrustlog.setIsactive(sellFentrust.getFcreateTime().getTime() > buyFentrust.getFcreateTime().getTime());
//        sellFentrustlog.setFentrust(sellFentrust);
//        sellFentrustlog.setfEntrustType(EntrustTypeEnum.SELL);
//        sellFentrustlog.setFvirtualcointype(fvirtualcointype);
//
//        System.out.println("newLog用时：" + (System.currentTimeMillis() - startNewLog));
//        long startMysql = System.currentTimeMillis();
//        boolean isSuccesss = updateDealMaking(buyFentrust, sellFentrust, buyFentrustlog, sellFentrustlog, fviFid);
//        System.out.println("dealmaking方法一共用时：" + (System.currentTimeMillis() - startMysql));
//        // 如果不成功，返回成交量为0
//        if (!isSuccesss) {
//            successCount = 0D;
//        }
//
//        return successCount;
//    }

//    public boolean updateDealMaking(int buyFid, int sellFid, int fviFid, double successCount, double successPrice, FentrustData tradeData) {
//
////		System.out.println("ppp: " + buyFid + "," + sellFid + "," + fviFid + "," + successCount + "," + successPrice);
//
//        Fentrust buyFentrust = baseService.findByFid(buyFid);
//        Fentrust sellFentrust = baseService.findByFid(sellFid);
//        successPrice = 0;
//        successCount = 0;
//        if (buyFentrust.getFid() > sellFentrust.getFid()) {
//            successPrice = sellFentrust.getFprize();
//        } else {
//            successPrice = buyFentrust.getFprize();
//        }
//
//        if (buyFentrust.getFleftCount() > sellFentrust.getFleftCount()) {
//            successCount = sellFentrust.getFleftCount();
//        } else {
//            successCount = buyFentrust.getFleftCount();
//        }
//
//        tradeData.setFleftCount(successCount);
//
//        Preconditions.checkNotNull(buyFentrust, "buy is null");
//        Preconditions.checkNotNull(sellFentrust, "sell is null");
//
//        Fvirtualcointype fvirtualcointype = new Fvirtualcointype();
//        fvirtualcointype.setFid(fviFid);
//
//        //买单日志
//        Fentrustlog buyFentrustlog = new Fentrustlog();
//        buyFentrustlog.setFamount(MathUtils.multiply(successCount, successPrice));
//        buyFentrustlog.setFprize(successPrice);
//        buyFentrustlog.setFcount(successCount);
//        buyFentrustlog.setFcreateTime(Utils.getTimestamp());
//        buyFentrustlog.setIsactive(buyFentrust.getFcreateTime().getTime() > sellFentrust.getFcreateTime().getTime());
//        buyFentrustlog.setFentrust(buyFentrust);
//        buyFentrustlog.setfEntrustType(EntrustTypeEnum.BUY);
//        buyFentrustlog.setFvirtualcointype(fvirtualcointype);
//
//        //卖单日志
//        Fentrustlog sellFentrustlog = new Fentrustlog();
//        sellFentrustlog.setFamount(MathUtils.multiply(successCount, successPrice));
//        sellFentrustlog.setFcount(successCount);
//        sellFentrustlog.setFprize(successPrice);
//        sellFentrustlog.setFcreateTime(Utils.getTimestamp());
//        sellFentrustlog.setIsactive(sellFentrust.getFcreateTime().getTime() > buyFentrust.getFcreateTime().getTime());
//        sellFentrustlog.setFentrust(sellFentrust);
//        sellFentrustlog.setfEntrustType(EntrustTypeEnum.SELL);
//        sellFentrustlog.setFvirtualcointype(fvirtualcointype);
//        return updateDealMaking(buyFentrust, sellFentrust, buyFentrustlog, sellFentrustlog, fviFid);
//
//    }

//    public boolean updateDealMaking(Fentrust buy, Fentrust sell, Fentrustlog buyLog, Fentrustlog sellLog, int id) {
//
//        try {
//
//            // 状态检查，如果买单，或者买单，已经取消，或者已经完全成交，则不能进行撮合更新动作
//            if (buy.getFstatus() == EntrustStatusEnum.Cancel
//                    || buy.getFstatus() == EntrustStatusEnum.AllDeal
//                    || sell.getFstatus() == EntrustStatusEnum.Cancel
//                    || sell.getFstatus() == EntrustStatusEnum.AllDeal) {
////				throw new IllegalStateException("撮合失败，买单或者买单已取消或已完全成交");
////				System.out.println("撮合失败，买单或者买单已取消或已完全成交");
//                return false;
//            }
//
//            long updateEntrust = System.currentTimeMillis();
//            if (buy.isFisLimit()) {
//                buy.setFcount(buy.getFcount() + buyLog.getFcount());
//                buy.setFsuccessAmount(buy.getFsuccessAmount() + (buyLog.getFamount()));
//                buy.setFleftfees(buy.getFleftfees());
//                buy.setFlastUpdatTime(Utils.getTimestamp());
//                if (buy.getFamount() - buy.getFsuccessAmount() < 0.0001F) {
//                    buy.setFstatus(EntrustStatusEnum.AllDeal);
//                    buy.setFleftCount(0);
//                } else {
//                    buy.setFstatus(EntrustStatusEnum.PartDeal);
//                }
//                fentrustDAO.attachDirty(buy);
//            } else {
//
//                buy.setFleftCount(MathUtils.subtract(buy.getFleftCount(), buyLog.getFcount()));
//                buy.setFsuccessAmount(MathUtils.add(buy.getFsuccessAmount(), buyLog.getFamount()));
//                buy.setFlastUpdatTime(Utils.getTimestamp());
//                if (buy.getFleftCount() < 0.0001F) {
//                    buy.setFstatus(EntrustStatusEnum.AllDeal);
//                    buy.setFleftCount(0);
//                } else {
//                    buy.setFstatus(EntrustStatusEnum.PartDeal);
//                }
//                fentrustDAO.attachDirty(buy);
//            }
//
//
//            double sellFee = sellLog.getFcount() / sell.getFcount() * sell.getFfees();
//            if (sell.isFisLimit()) {
//                sell.setFsuccessAmount(sell.getFsuccessAmount() + buyLog.getFamount());
//                sell.setFamount(sell.getFamount() + sellLog.getFamount());
//                sell.setFleftCount(sell.getFleftCount() - sellLog.getFcount());
//                sell.setFleftfees(sell.getFleftfees() - sellFee);
//                sell.setFlastUpdatTime(Utils.getTimestamp());
//                if (sell.getFleftCount() < 0.0001F) {
//                    sell.setFstatus(EntrustStatusEnum.AllDeal);
//                    sell.setFcount(0);
//                } else {
//                    sell.setFstatus(EntrustStatusEnum.PartDeal);
//                }
//
//                fentrustDAO.attachDirty(sell);
//            } else {
//
//                sell.setFleftCount(MathUtils.subtract(sell.getFleftCount(), sellLog.getFcount()));
//                sell.setFsuccessAmount(MathUtils.add(sell.getFsuccessAmount(), sellLog.getFamount()));
//                sell.setFleftfees(sell.getFleftfees() - sellFee);
//                sell.setFlastUpdatTime(Utils.getTimestamp());
//                if (sell.getFleftCount() < 0.0001F) {
//                    sell.setFstatus(EntrustStatusEnum.AllDeal);
//                    sell.setFleftCount(0);
//                } else {
//                    sell.setFstatus(EntrustStatusEnum.PartDeal);
//                }
//                fentrustDAO.attachDirty(sell);
//            }
//            System.out.println("更新挂单用时：" + (System.currentTimeMillis() - updateEntrust));
//            long startLog = System.currentTimeMillis();
//            fentrustlogDAO.addFentrustLogBat(buyLog, sellLog);
//            System.out.println("更新Log用时：" + (System.currentTimeMillis() - startLog));
////			System.out.println("卖单更新成功");
////			System.out.println("买单更新成功");
//
//            // 更新钱包
//            if (sell.getRobotStatus() == EntrustRobotStatusEnum.Normal && buy.getRobotStatus() == EntrustRobotStatusEnum.Normal) {
//
//				long startFindFee = System.currentTimeMillis();
//                double feeRate = 0d;
//                if (sell.getFuser().getFneedFee()) {
//                	feeRate = ffeesDAO.findFfee(id, sell.getFuser().getFscore().getFlevel()).getFfee();
//                }
//				System.out.println("查询费用用时：" + (System.currentTimeMillis() - startFindFee));
//
//				long startFindWalletID = System.currentTimeMillis();
//                int fbuyWalletId = buy.getFuser().getFwallet().getFid();
//                int fsellWalletId = sell.getFuser().getFwallet().getFid();
//				System.out.println("查询钱包ID用时：" + (System.currentTimeMillis() - startFindWalletID));
//
//                //更新钱包，买家解冻资金，卖家获得资金
//				long startUpdateWallet = System.currentTimeMillis();
//                int walletFrozenRow = fwalletDAO.updateFwalletFrozen(fbuyWalletId, buyLog.getFamount());
//                Preconditions.checkArgument(walletFrozenRow == 1, fbuyWalletId + " unfrozen buyer's wallet throw exception");
////				System.out.println("买家钱包更新成功");
//                int walletMoneyRow = fwalletDAO.updateFwalletMoney(fsellWalletId, MathUtils.multiply(sellLog.getFamount(), MathUtils.subtract(1, feeRate)));
//                Preconditions.checkArgument(walletMoneyRow == 1, fsellWalletId + " transfer to seller's wallet throw exception");
////				System.out.println("卖家虚拟钱包更新成功");
//				System.out.println("更新钱包用时：" + (System.currentTimeMillis() - startUpdateWallet));
//
//                //更新虚拟钱包，买家获得虚拟币，卖家解冻虚拟币
//				long startUpdateVirtualWallet = System.currentTimeMillis();
//                int virtualWalletFrozenRow = fvirtualwalletDAO.updateBuyAndSellVirtualWalletFrozen(sell.getFuser().getFid(), id, sellLog.getFcount());
//                Preconditions.checkArgument(virtualWalletFrozenRow == 1, sell.getFuser().getFid() + " , " + sellLog.getFcount() + " unfrozen seller's virtualwallet throw exception");
////				System.out.println("卖家虚拟钱包更新成功");
//                int virtualWalletCountRow = fvirtualwalletDAO.updateBuyAndSellVirtualWalletCount(buy.getFuser().getFid(), id, buyLog.getFcount());
////				System.out.println(virtualWalletCountRow);
//                Preconditions.checkArgument(virtualWalletCountRow == 1, buy.getFuser().getFid() + "," + buyLog.getFcount() + " transfer to buyer's virtualwallet throw exception");
////				System.out.println("买家虚拟钱包更新成功");
//				System.out.println("更新虚拟钱包用时：" + (System.currentTimeMillis() - startUpdateVirtualWallet));
//
//				long startUpdateReturn = System.currentTimeMillis();
//                if (buy.getFstatus() == EntrustStatusEnum.AllDeal) {
//
//                    double left_amount = MathUtils.subtract(buy.getFamount(), buy.getFsuccessAmount());
//                    if (left_amount > 0) {
//                        int retureMoneyFromFrozen = fwalletDAO.updateFwalletMoneyAndFrozen(buy.getFuser().getFwallet().getFid(), left_amount);
//                        Preconditions.checkArgument(retureMoneyFromFrozen == 1, buy.getFuser().getFwallet().getFid() + "," + left_amount + "解除冻结，并把冻结的钱转入买家钱包时抛出异常");
//                    }
//                }
//				System.out.println("更新返还未使用金额用时：" + (System.currentTimeMillis() - startUpdateReturn));
//            }
//
//            long successStart = System.currentTimeMillis();
//            //加入成功交易
//            this.realTimeData.addEntrustSuccessMap(id, sellLog);
//            //加入成功交易
//            this.realTimeData.addEntrustSuccessMap(id, buyLog);
//            System.out.println("加入成功交易用时：" + (System.currentTimeMillis() - successStart));
//            return true;
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//
//    }

    public Fentrust findFentrustById(int id) {
        return this.fentrustDAO.findById(id);
    }

    public List<Fentrustlog> findFentrustLogByFentrust(Fentrust fentrust) {
        return fentrustlogDAO.findByProperty("fentrust.fid", fentrust.getFid());
    }

    // 最新成交记录
    public List<Fentrust> findLatestSuccessDeal(int coinTypeId,
                                                int fentrustType, int count) {
        return this.fentrustDAO.findLatestSuccessDeal(coinTypeId, fentrustType,
                count);
    }

    public List<Fentrust> findAllGoingFentrust(int coinTypeId,
                                               int fentrustType, boolean isLimit) {
        return this.fentrustDAO.findAllGoingFentrust(coinTypeId, fentrustType,
                isLimit);
    }

    public List<Fentrust> findAllGoingFentrust(int coinTypeId, boolean isLimit) {
        return this.fentrustDAO.findAllGoingFentrust(coinTypeId, isLimit);
    }

    // 获得24小时内的成交记录
    public List<Fentrustlog> findLatestSuccessDeal24(int coinTypeId, int hour) {
        List<Fentrustlog> list = this.fentrustlogDAO.findLatestSuccessDeal24(coinTypeId, 24);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list;
    }

    public Fentrust findLatestDeal(int coinTypeId) {
        Fentrust fentrust = this.fentrustDAO.findLatestDeal(coinTypeId);
        if (fentrust == null) return null;
        return fentrust;
    }

    // 委托记录
    public List<Fentrust> findFentrustHistory(int fuid, int fvirtualCoinTypeId,
                                              int[] entrust_type, int first_result, int max_result, String order,
                                              int entrust_status[], Date beginDate, Date endDate)
            throws Exception {
        List<Fentrust> list = this.fentrustDAO.getFentrustHistory(fuid,
                fvirtualCoinTypeId, entrust_type, first_result, max_result,
                order, entrust_status, beginDate, endDate);
        for (Fentrust fentrust : list) {
            fentrust.getFvirtualcointype().getFname();
        }
        return list;
    }

    public int findFentrustHistoryCount(int fuid, int fvirtualCoinTypeId,
                                        int[] entrust_type, int entrust_status[], Date beginDate, Date endDate)
            throws Exception {
        return this.fentrustDAO.getFentrustHistoryCount(fuid,
                fvirtualCoinTypeId, entrust_type, entrust_status, beginDate, endDate);
    }

    // 计划委托
    public List<Fentrustplan> findEntrustPlan(int type, int status[]) {
        List<Fentrustplan> list = this.fentrustplanDAO.findEntrustPlan(type,
                status);

        return list;
    }

    // 委托买入，改进版
    public Fentrust updateEntrustBuy2(double tradeAmount, double tradeCnyPrice, Fuser fuser, boolean fisLimit, int robotStatus, Fvirtualcointype fvirtualcointype) throws Exception {

//		boolean flag = false;
//		Fwallet fwallet = this.fwalletDAO.findById(fuser.getFwallet().getFid());

        double ffeeRate = 0d;/*this.ffeesDAO.findFfee(coinTypeId,
                fuser.getFscore().getFlevel()).getFfee();*/
        double ffee = 0F;

        // 买入总价格
        double totalTradePrice = 0F;
        if (fisLimit) {
            ffee = tradeAmount * ffeeRate;
            totalTradePrice = tradeAmount - ffee;
        } else {
//			ffee = tradeAmount * tradeCnyPrice * ffeeRate;
            totalTradePrice = MathUtils.multiply(tradeAmount, tradeCnyPrice);
        }

        if (robotStatus == EntrustRobotStatusEnum.Normal) {
            // 不查询直接更新，提高并发量
            // 如果更新失败，则返回，不再下单
            double tradeMonmey = totalTradePrice;
            Fwallet fwallet = new Fwallet();
            fwallet.setFid(fuser.getFwallet().getFid());
            fwallet.setFtotalRmb(tradeMonmey);
            fwallet.setFlastUpdateTime(Utils.getTimestamp());
            int updateRow = this.fwalletDAO.updateRmb(fwallet);
            if (updateRow == 0) {
                return null;
            }
        }

        Fentrust fentrust = new Fentrust();
        if (fisLimit) {
            fentrust.setFcount(0F);
            fentrust.setFleftCount(0F);
        } else {
            fentrust.setFcount(tradeAmount);
            fentrust.setFleftCount(tradeAmount);
        }
        fentrust.setFamount(totalTradePrice);
        fentrust.setFfees(ffee);
        fentrust.setFleftfees(ffee);
        fentrust.setFcreateTime(Utils.getTimestamp());
        fentrust.setFentrustType(EntrustTypeEnum.BUY);
        fentrust.setFisLimit(fisLimit);
        fentrust.setFlastUpdatTime(Utils.getTimestamp());
        fentrust.setFprize(tradeCnyPrice);
        fentrust.setFstatus(EntrustStatusEnum.Going);
        fentrust.setFsuccessAmount(0F);
        fentrust.setFhasSubscription(false);
        fentrust.setFuser(fuser);
        fentrust.setFvirtualcointype(fvirtualcointype);
        fentrust.setRobotStatus(robotStatus);
        this.fentrustDAO.save(fentrust);

//		flag = true;

        return fentrust;
    }

    // 委托卖出，改进版
    public Fentrust updateEntrustSell2(int coinTypeId, double tradeAmount,
                                       double tradeCnyPrice, Fuser fuser, boolean fisLimit, int robotStatus,
                                       Fvirtualcointype fvirtualcointype) throws Exception {


        if (robotStatus == EntrustRobotStatusEnum.Normal) {
            // 不查询直接更新，提高并发量
            // 如果更新失败，则返回，不再下单
            int updateRow = fvirtualwalletDAO.updateRmb(fuser.getFid(), coinTypeId, tradeAmount, Utils.getTimestamp());
            if (updateRow == 0) {
                return null;
            }
        }

        // 检查用户是否需要手续费, 2016/03/28
        double ffee = 0d;
        if (fuser.getFneedFee()) {
            double ffeeRate = this.ffeesDAO.findFfee(coinTypeId,
                    fuser.getFscore().getFlevel()).getFfee();
            ffee = MathUtils.multiply(MathUtils.multiply(tradeAmount, tradeCnyPrice), ffeeRate);
        }
//        double ffeeRate = this.ffeesDAO.findFfee(coinTypeId, fuser.getFscore().getFlevel()).getFfee();
//        double ffee = tradeAmount * tradeCnyPrice * ffeeRate;

        Fentrust fentrust = new Fentrust();
        if (fisLimit) {
            fentrust.setFamount(0F);
        } else {
            fentrust.setFamount(MathUtils.multiply(tradeAmount, tradeCnyPrice));
        }
//		fentrust.setFcount(tradeAmount - ffee);
        fentrust.setFcount(tradeAmount);
        fentrust.setFleftCount(tradeAmount);
//		fentrust.setFleftCount(tradeAmount - ffee);
        fentrust.setFfees(ffee);
        fentrust.setFleftfees(ffee);
        fentrust.setFcreateTime(Utils.getTimestamp());
        fentrust.setFentrustType(EntrustTypeEnum.SELL);
        fentrust.setFisLimit(fisLimit);
        fentrust.setFlastUpdatTime(Utils.getTimestamp());
        fentrust.setFprize(tradeCnyPrice);
        fentrust.setFstatus(EntrustStatusEnum.Going);
        fentrust.setFsuccessAmount(0F);
        fentrust.setFuser(fuser);
        fentrust.setFhasSubscription(false);
        fentrust.setFvirtualcointype(fvirtualcointype);
        fentrust.setRobotStatus(robotStatus);
        this.fentrustDAO.save(fentrust);

        return fentrust;

    }

    // 委托买入
    public Fentrust updateEntrustBuy(int coinTypeId, double tradeAmount,
                                     double tradeCnyPrice, Fuser fuser, boolean fisLimit, int robotStatus,
                                     HttpServletRequest req) throws Exception {


//		boolean flag = false;
        Fwallet fwallet = this.fwalletDAO.findById(fuser.getFwallet().getFid());

        double ffeeRate = 0d;/*this.ffeesDAO.findFfee(coinTypeId,
				fuser.getFscore().getFlevel()).getFfee();*/
        double ffee = 0F;

        // 买入总价格
        double totalTradePrice = 0F;
        if (fisLimit) {
            ffee = tradeAmount * ffeeRate;
            totalTradePrice = tradeAmount - ffee;
        } else {
            ffee = tradeAmount * tradeCnyPrice * ffeeRate;
            totalTradePrice = tradeAmount * tradeCnyPrice - ffee;
        }

        if (robotStatus == EntrustRobotStatusEnum.Normal) {
            if (fwallet.getFtotalRmb() < (totalTradePrice + ffee)) {
                return null;
            }

            fwallet.setFtotalRmb(fwallet.getFtotalRmb() - (totalTradePrice + ffee));
            fwallet.setFfrozenRmb(fwallet.getFfrozenRmb()
                    + (totalTradePrice + ffee));
            fwallet.setFlastUpdateTime(Utils.getTimestamp());
            this.fwalletDAO.attachDirty(fwallet);

        }

        Fentrust fentrust = new Fentrust();
        if (fisLimit) {
            fentrust.setFcount(0F);
            fentrust.setFleftCount(0F);
        } else {
            fentrust.setFcount(tradeAmount * (1 - ffeeRate));
            fentrust.setFleftCount(tradeAmount * (1 - ffeeRate));
        }
        fentrust.setFamount(totalTradePrice);
        fentrust.setFfees(ffee);
        fentrust.setFleftfees(ffee);
        fentrust.setFcreateTime(Utils.getTimestamp());
        fentrust.setFentrustType(EntrustTypeEnum.BUY);
        fentrust.setFisLimit(fisLimit);
        fentrust.setFlastUpdatTime(Utils.getTimestamp());
        fentrust.setFprize(tradeCnyPrice);
        fentrust.setFstatus(EntrustStatusEnum.Going);
        fentrust.setFsuccessAmount(0F);
        fentrust.setFhasSubscription(false);
        fentrust.setFuser(fuser);
        fentrust.setFvirtualcointype(this.fvirtualcointypeDAO
                .findById(coinTypeId));
        fentrust.setRobotStatus(robotStatus);
        this.fentrustDAO.save(fentrust);

//		flag = true;

        return fentrust;
    }

    public void sendToQueue(final boolean fisLimit, final int coinTypeId, final Fentrust fentrust) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                sentToMongoAndMQBuy(fisLimit, coinTypeId, fentrust);
            }
        });
    }

    //把买单发送到mongo和消息队列中
    public void sentToMongoAndMQBuy(boolean fisLimit, int coinTypeId, Fentrust buyFentrust) {
        realTimeData.addEntrustBuyMap(coinTypeId, buyFentrust);
    }

    public List getFentrustHistory(int fuid, int fviFid, int first, int max) {
        return fentrustDAO.getFentrustHistory(fuid, fviFid, first, max);
    }

    public List<Fentrust> findFentrustHistory(int fuid, int fviFid, int first, int max) {
        return fentrustDAO.findFentrustHistory(fuid, fviFid, first, max);
    }

    // 委托记录
    public List<Fentrust> findFentrustHistory(int fuid, int fvirtualCoinTypeId,
                                              int[] entrust_type, int first_result, int max_result, String order,
                                              int entrust_status[]) throws Exception {
        List<Fentrust> list = this.fentrustDAO.getFentrustHistory(fuid,
                fvirtualCoinTypeId, entrust_type, first_result, max_result,
                order, entrust_status);
        for (Fentrust fentrust : list) {
            fentrust.getFvirtualcointype().getFname();
        }
        return list;
    }

    public int findFentrustHistoryCount(int fuid, int fvirtualCoinTypeId,
                                        int[] entrust_type, int entrust_status[]) throws Exception {
        return this.fentrustDAO.getFentrustHistoryCount(fuid,
                fvirtualCoinTypeId, entrust_type, entrust_status);
    }

    public void updateCancelFentrust(Fentrust fentrust, Fuser fuser) {

        try {

            java.sql.Timestamp now = Utils.getTimestamp();

            fentrust.setFlastUpdatTime(now);
            fentrust.setFstatus(EntrustStatusEnum.Cancel);
            fentrustDAO.attachDirty(fentrust);

            if (fentrust.getRobotStatus() == EntrustRobotStatusEnum.Normal) {

                if (fentrust.getFentrustType() == EntrustTypeEnum.BUY) {
                    Fwallet fwallet = fuser.getFwallet();
                    // 买
//					double amount = 0F;
                    double leftAmount = MathUtils.subtract(fentrust.getFamount(), fentrust.getFsuccessAmount());
//					double leftFee = fentrust.getFleftfees();
//					amount = leftAmount + leftFee;

//                    fwallet.setFtotalRmb(MathUtils.add(fwallet.getFtotalRmb(), leftAmount));
//                    fwallet.setFfrozenRmb(MathUtils.subtract(fwallet.getFfrozenRmb(), leftAmount));
//                    fwallet.setFlastUpdateTime(now);
//                    fwalletDAO.attachDirty(fwallet);
                    int count = fwalletDAO.updateFwalletMoneyAndFrozen(fwallet.getFid(), leftAmount);
                    if(1 != count){
                        throw new RuntimeException();
                    }
                } else {
                    // 卖

//					//返还未交易部分的手续费
//					double leftFee = fentrust.getFleftfees();
//					fwallet.setFtotalRmb(fwallet.getFtotalRmb() + leftFee);
//					fwallet.setFfrozenRmb(fwallet.getFfrozenRmb() - leftFee);
//					fwalletDAO.attachDirty(fwallet);

                    Fvirtualwallet fvirtualwallet = this.fvirtualwalletDAO.findVirtualWallet(fuser.getFid(), fentrust.getFvirtualcointype().getFid());

                    double leftCount = fentrust.getFleftCount();
//                    fvirtualwallet.setFtotal(MathUtils.add(fvirtualwallet.getFtotal(), leftCount));
//                    fvirtualwallet.setFfrozen(MathUtils.subtract(fvirtualwallet.getFfrozen(), leftCount));
//                    fvirtualwallet.setFlastUpdateTime(now);
//                    fvirtualwalletDAO.attachDirty(fvirtualwallet);

                    int count = fvirtualwalletDAO.updateRefund(fuser.getFid(), fvirtualwallet.getFvirtualcointype().getFid(), leftCount, Utils.getTimestamp());
                    if(1 != count){
                        throw new RuntimeException();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public List<Fentrust> findFentrustByParam(int firstResult, int maxResults,
                                              String filter, boolean isFY) {
        return this.fentrustDAO.findByParam(firstResult, maxResults, filter,
                isFY, "Fentrust");
    }

    public int findFentrustByParamCount(String filter) {
        return this.fentrustDAO.findByParamCount(filter, "Fentrust");
    }

    public void updateFeeLog(Fentrust entrust, Fvirtualwallet fvirtualwallet) {
        try {
            this.fentrustDAO.attachDirty(entrust);
            this.fvirtualwalletDAO.attachDirty(fvirtualwallet);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void updateFeeLog(Fentrust entrust, Fwallet fwallet) {
        try {
            this.fentrustDAO.attachDirty(entrust);
            this.fwalletDAO.attachDirty(fwallet);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void updateSubscription(Fvirtualwallet fvirtualwalletCost, Fvirtualwallet fvirtualwallet,
                                   Fsubscriptionlog fsubscriptionlog, Fsubscription fsubscription) {
        try {
            this.fvirtualwalletDAO.attachDirty(fvirtualwalletCost);
            this.fvirtualwalletDAO.attachDirty(fvirtualwallet);
            this.fsubscriptionDAO.attachDirty(fsubscription);
            this.fsubscriptionlogDAO.save(fsubscriptionlog);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public Fentrustlog getClosingPrice(int fvirtualcointype) {
        return this.fentrustlogDAO.getClosingEntrust(fvirtualcointype);
    }

    public Fentrustlog getOpenningPrice(int fvirtualcointype) {
        return this.fentrustlogDAO.getOpenningEntrust(fvirtualcointype);
    }

    public Fentrustlog getEntrustBeforeWeek(int fvirtualcointype) {
        return this.fentrustlogDAO.getEntrustBeforeWeek(fvirtualcointype);
    }

    // 加密
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static Key toKey(byte[] key) throws Exception {
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }

    private static String encrypt(String data, String key) throws Exception {
        Key k = toKey(Base64.decodeBase64(key.getBytes())); // 还原密钥
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM); // 实例化Cipher对象，它用于完成实际的加密操作
        cipher.init(Cipher.ENCRYPT_MODE, k); // 初始化Cipher对象，设置为加密模式
        return new String(Base64.encodeBase64(cipher.doFinal(data.getBytes()))); // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
    }

    private static String decrypt(String data, String key) throws Exception {
        Key k = toKey(Base64.decodeBase64(key.getBytes()));
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k); // 初始化Cipher对象，设置为解密模式
        return new String(cipher.doFinal(Base64.decodeBase64(data.getBytes()))); // 执行解密操作
    }

    /**
     * @param robotStatus
     * @return 返回所有机器人订单
     */
    public List<Fentrust> findByCoinTypeAndRobotStatus(int id, int robotStatus) {
        return fentrustDAO.findByRobotStatus(id, robotStatus);
    }

    /**
     * 获取订单信息
     *
     * @return
     */
    public Map<String, Object> findFentrustInfo(int orderId, int fuserId) {
        List<Fentrust> list = findFentrustByParam(0, 50, " where fid=" + orderId + " and fuser.fid=" + fuserId, true);

        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }

        Fentrust fentrust = list.get(0);
        Map<String, Object> map = new HashMap<>(10);
        map.put("order_id", fentrust.getFid());
        map.put("create_date", fentrust.getFcreateTime());
        map.put("deal_amount", Utils.getDouble(fentrust.getFsuccessAmount(), 2));
        map.put("price", Utils.getDouble(fentrust.getFprize(), 2));
        map.put("status", fentrust.getFstatus());
        map.put("symbol", fentrust.getFvirtualcointype().getfShortName());
        map.put("type", 0 == fentrust.getFentrustType() ? "buy" : "sell");

        return map;
    }

    /**
     * 批量获取订单信息
     * @return
     */
    public List<Map<String, Object>> findFentrustInfo(Set<Integer> orderIds, int fuserId) {
        List<Map<String, Object>> list = new ArrayList<>(orderIds.size());
        for (Integer orderId : orderIds) {
            Map<String, Object> map = this.findFentrustInfo(orderId, fuserId);
            if (!CollectionUtils.isEmpty(map)) {
                list.add(this.findFentrustInfo(orderId, fuserId));
            }
        }
        return list;
    }

    /**
     * 获取历史订单
     *
     * @param fuserId
     * @param viCoinTypeId
     * @param entrustType
     * @param status
     * @param beginDate
     * @param endDate
     * @param pageNow
     * @param pageSize
     * @return
     */
    public List<Fentrust> findFentrustHistory(Integer fuserId, Integer viCoinTypeId, Integer[] entrustType, Integer[] status, Date beginDate, Date endDate, int pageNow, int pageSize) {
        if (pageNow < 1) {
            pageNow = 1;
        }
        if (pageSize < 0 || pageSize > 200) {
            pageSize = 20;
        }

        List<Fentrust> list = fentrustDAO.findHistory(fuserId, viCoinTypeId, entrustType, status, beginDate, endDate, (pageNow - 1) * pageSize, pageSize);
        for (Fentrust fentrust : list) {
            fentrust.getFvirtualcointype().getfShortName();
        }
        return list;
    }

    /**
     * 统计历史订单
     *
     * @param fuserId
     * @param viCoinTypeId
     * @param entrustType
     * @param status
     * @param beginDate
     * @param endDate
     * @return
     */
    public int countFentrustHistory(Integer fuserId, Integer viCoinTypeId, Integer[] entrustType, Integer[] status, Date beginDate, Date endDate) {
        return fentrustDAO.CountHistory(fuserId, viCoinTypeId, entrustType, status, beginDate, endDate);
    }

    /**
     * 获取用户订单
     *
     * @param fuserId
     * @param fid
     * @return
     */
    public Fentrust findFentrustByUser(int fuserId, int fid) {
        List<Fentrust> list = this.findFentrustByParam(0, 0, " where fid = " + fid + " and fuser.fid = " + fuserId, false);
        for (Fentrust fentrust : list) {
            fentrust.getFvirtualcointype().getfShortName();
            return fentrust;
        }

        return null;
    }

    /**
     * 平均成交价
     *
     * @param entrustId
     * @return
     */
    public double avgSuccessPrice(int entrustId){
        return fentrustlogDAO.avgSuccessPrice(entrustId);
    }
}















