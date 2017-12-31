package com.ruizton.main.service.front;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.Enum.MessageStatusEnum;
import com.ruizton.main.Enum.MoneyType;
import com.ruizton.main.Enum.WalletTransferStatus;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.dao.*;
import com.ruizton.main.model.*;
import com.ruizton.util.CollectionUtils;
import com.ruizton.util.ConstantKeys;
import com.ruizton.util.FormatUtils;
import com.ruizton.util.HttpUtils;
import com.ruizton.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/7/25
 */
@Service("walletTransferService")
public class WalletTransferService {
    private static Logger logger = LoggerFactory.getLogger(WalletTransferService.class);

    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private WalletTransferDao walletTransferDao;
    @Autowired
    private FwalletDAO fwalletDAO;
    @Autowired
    private FvirtualwalletDAO fvirtualwalletDAO;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FmessageDAO fmessageDAO;
    @Autowired
    private FvirtualcointypeDAO fvirtualcointypeDAO;

    private List<Map<String, Object>> apps = Collections.emptyList();
    // 可转账币 币类型:币id
    private Set<String> canTransfer = new HashSet<>();

    public void save(WalletTransfer entity){
        entity.setCreateTime(Utils.getTimestamp());
        walletTransferDao.save(entity);
    }

    public void update(WalletTransfer entity){
        walletTransferDao.update(entity);
    }

    public WalletTransfer findById(int id){
        return walletTransferDao.findById(id);
    }

    public List<WalletTransfer> findByUser(int userId, int firstResult, int maxResult){
        return walletTransferDao.findByUser(userId, firstResult, maxResult);
    }

    public List<WalletTransfer> find(Integer userId, Integer coinId, Short moneyType, Short status, int firstResult, int maxResult){
        return walletTransferDao.find(userId, coinId, moneyType, status, firstResult, maxResult);
    }

    public int count(Integer userId, Integer coinId, Short moneyType, Short status){
        return walletTransferDao.count(userId, coinId, moneyType, status);
    }

    /**
     * 人民币转账
     *
     * @param fuser
     * @param transfer
     */
    public boolean insertSendRMBTransfer(Fuser fuser, WalletTransfer transfer){
        Fwallet fwallet = new Fwallet();
        fwallet.setFid(fuser.getFwallet().getFid());
        fwallet.setFtotalRmb(transfer.getAmount());
        fwallet.setFlastUpdateTime(Utils.getTimestamp());
        int count = fwalletDAO.updateRmb(fwallet);
        if(count <= 0){
            logger.error("转账：人民币余额不足");
            return false;
        }

        transfer.setActive(true);
        transfer.setMoneyType(MoneyType.RMB);
        transfer.setStatus(WalletTransferStatus.Paid);
        transfer.setFromSystem(constantMap.getString(ConstantKeys.USER_TRANSFER_SYSTEM_TAG));
        this.save(transfer);

        return true;
    }

    /**
     * 虚拟币转账
     *
     * @param fuser
     * @param transfer
     */
    public boolean insertSendCoinTransfer(Fuser fuser, WalletTransfer transfer){
        int count = fvirtualwalletDAO.updateRmb(fuser.getFid(), transfer.getVirtualCoinId(), transfer.getAmount(), Utils.getTimestamp());
        if(count <= 0){
            logger.error("转账：虚拟币余额不足，虚拟币编号为：" + transfer.getVirtualCoinId());
            return false;
        }

        transfer.setActive(true);
        transfer.setMoneyType(MoneyType.Virtual_Coin);
        transfer.setStatus(WalletTransferStatus.Paid);
        transfer.setFromSystem(constantMap.getString(ConstantKeys.USER_TRANSFER_SYSTEM_TAG));
        this.save(transfer);

        return true;
    }

    /**
     * 人民币已经到账
     *
     * @param transfer
     */
    public boolean updateTransferred(WalletTransfer transfer){
        if (walletTransferDao.updateForSuccess(transfer.getId()) <= 0) {
            logger.error("资金不能重复到账，转账订单号：" + transfer.getId());
            return false;
        }

        Fuser fuser = frontUserService.findById(transfer.getUserId());

        if(transfer.isRMB()){
            // 人民币解冻
            int count = fwalletDAO.updateFwalletFrozen(fuser.getFwallet().getFid(), transfer.getAmount());
            if(count <= 0){
                logger.error("人民币主动转账冻结资金余额不足，转账订单号：" + transfer.getId());
                throw new RuntimeException();
            }
        }else if(transfer.isVirtualCoin()){
            // 虚拟币解冻
            Fvirtualwallet fvirtualwallet = fvirtualwalletDAO.findVirtualWallet(transfer.getUserId(), transfer.getVirtualCoinId());
            int count = fvirtualwalletDAO.updateVirtualWalletFrozen(fvirtualwallet.getFid(), transfer.getAmount());
            if(count <= 0){
                logger.error("虚拟币主动转账冻结资金余额不足，转账订单号：" + transfer.getId());
                throw new RuntimeException();
            }
        }else{
            return false;
        }

        return true;
    }

    /**
     * 人民币到账
     *
     * @param fuser
     * @param transfer
     */
    public void insertReceiveRMBTransfer(Fuser fuser, WalletTransfer transfer){
        transfer.setActive(false);
        transfer.setStatus(WalletTransferStatus.Success);
        transfer.setUpdateTime(Utils.getTimestamp());
        this.save(transfer);

        fwalletDAO.updateFwalletMoney(fuser.getFwallet().getFid(), transfer.getAmount());

        Fmessage fmessage = new Fmessage();
        fmessage.setFcontent("您从众创园接收到人民币转账" + FormatUtils.formatCNY(transfer.getAmount()) + "元");
        fmessage.setFcreateTime(Utils.getTimestamp());
        fmessage.setFreceiver(fuser);
        fmessage.setFstatus(MessageStatusEnum.NOREAD_VALUE);
        fmessage.setFtitle("转账通知");
        this.fmessageDAO.save(fmessage);
    }

    /**
     * 虚拟币到账
     *
     * @param transfer
     * @return
     */
    public void insertReceiveCoinTransfer(Fuser fuser, WalletTransfer transfer){
        transfer.setActive(false);
        transfer.setStatus(WalletTransferStatus.Success);
        transfer.setUpdateTime(Utils.getTimestamp());
        this.save(transfer);

        fvirtualwalletDAO.updateFwalletMoney(transfer.getVirtualCoinId(), transfer.getUserId(), transfer.getAmount(), 0, 0);

        Fvirtualcointype fvirtualcointype = fvirtualcointypeDAO.findById(transfer.getVirtualCoinId());

        Fmessage fmessage = new Fmessage();
        fmessage.setFcontent("您从众创园接收到虚拟货币(" + fvirtualcointype.getFname() + ")转账" + FormatUtils.formatCoin(transfer.getAmount()) + "个");
        fmessage.setFcreateTime(Utils.getTimestamp());
        fmessage.setFreceiver(fuser);
        fmessage.setFstatus(MessageStatusEnum.NOREAD_VALUE);
        fmessage.setFtitle("转账通知");
        this.fmessageDAO.save(fmessage);
    }

    /**
     * 退款
     *
     * @param transfer
     */
    public void updateRefund(WalletTransfer transfer){
        if(walletTransferDao.updateForRefund(transfer.getId()) <= 0){
            logger.error("资金不能退款，转账订单号：" + transfer.getId());
            return;
        }

        Fuser fuser = frontUserService.findById(transfer.getUserId());

        if(transfer.isRMB()){
            // 人民币解冻
            int count = fwalletDAO.updateFwalletFrozen(fuser.getFwallet().getFid(), transfer.getAmount());
            if(count <= 0){
                logger.error("人民币主动转账冻结资金余额不足，转账订单号：" + transfer.getId());
                return;
            }

            fwalletDAO.updateFwalletMoney(fuser.getFwallet().getFid(), transfer.getAmount());
        }else if(transfer.isVirtualCoin()){
            // 虚拟币解冻
            Fvirtualwallet fvirtualwallet = fvirtualwalletDAO.findVirtualWallet(transfer.getUserId(), transfer.getVirtualCoinId());
            int count = fvirtualwalletDAO.updateVirtualWalletFrozen(fvirtualwallet.getFid(), transfer.getAmount());
            if(count <= 0){
                logger.error("虚拟币主动转账冻结资金余额不足，转账订单号：" + transfer.getId());
                return;
            }

            fvirtualwalletDAO.updateFwalletMoney(transfer.getVirtualCoinId(), transfer.getUserId(), transfer.getAmount(), 0, 0);
        }
    }

    public List<WalletTransfer> findForTransfer(){
        return walletTransferDao.findForTransfer(100);
    }

    public List<Map<String, Object>> getApplications(){
        return this.apps;
    }


    public void syncApplication(){
        String response = HttpUtils.sendGetRequest(constantMap.getString(ConstantKeys.USER_TRANSFER_APPLICATION_URL), null);
        JSONArray jsonArray = JSON.parseArray(response);
        if(null == jsonArray || 0 == jsonArray.size()){
            return;
        }

        List<Map<String, Object>> list = new ArrayList<>(jsonArray.size());
        for(int i=0, len=jsonArray.size(); i<len; i++){
            Map<String, Object> map = new HashMap<>();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String app = jsonObject.getString("sysNum");

            if(!app.equals(constantMap.getString(ConstantKeys.USER_TRANSFER_SYSTEM_TAG))){
                map.put("app", app);
                map.put("appName", jsonObject.getString("sysName"));
                map.put("img", jsonObject.getString("imgUrl"));
                list.add(map);
            }
        }

        if(!CollectionUtils.isEmpty(list)){
            this.apps = list;
        }
    }

    public boolean appIsExist(String app){
        if(CollectionUtils.isEmpty(apps)){
            return false;
        }

        for(Map<String, Object> map : apps){
            if(app.equals(map.get("app"))){
                return true;
            }
        }

        return false;
    }

    public void syncTransferCoin(){
//        JSONObject json = HttpUtils.sendGetRequestForJson(constantMap.getString(ConstantKeys.USER_TRANSFER_COIN_ENABLE_URL), null);
//        if(!Integer.valueOf(1).equals(json.getInteger("ret"))){
//            logger.error("获取可转账的币出错，响应信息：" + json);
//            return;
//        }
//
//        JSONArray arr = json.getJSONArray("data");
//        Set<String> set = new HashSet<>();
//        for(int i=0, len=arr.size(); i<len; i++){
//            set.add(arr.getString(i));
//        }
//        canTransfer = set;
    }

    public boolean coinEnableToTransfer(int coinId){
        return canTransfer.contains(MoneyType.Virtual_Coin.getIndex() + ":" + coinId);
    }

    //后台需要的接口
    public List<WalletTransfer> listForAdmin(Integer userId, Integer coinId, Boolean active, Short moneyType, Short status, int firstResult, int maxResult, String orderField, String orderDirection){
        return walletTransferDao.listForAdmin(userId, coinId, active, moneyType, status, firstResult, maxResult, orderField, orderDirection);
    }
    public int countForAdmin(Integer userId, Integer coinId, Boolean active,Short moneyType, Short status){
        return walletTransferDao.countForAdmin(userId, coinId, active, moneyType, status);
    }

    public Object[] transferReport(Date startDate, Date endDate, MoneyType moneyType, Integer virtualCoinId, Boolean active, WalletTransferStatus status){
        return this.walletTransferDao.transferReport(startDate, endDate, moneyType, virtualCoinId, active, status);
    }
}
