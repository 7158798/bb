package com.zhgtrade.front.controller.account;

import com.alibaba.fastjson.JSONArray;
import com.ruizton.main.Enum.CountLimitTypeEnum;
import com.ruizton.main.Enum.MoneyType;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.code.AuthCode;
import com.ruizton.main.code.Captcha;
import com.ruizton.main.code.SmsMessageCaptcha;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.dto.LatestDealData;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;
import com.ruizton.main.model.WalletTransfer;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.main.service.admin.VirtualWalletService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.FrontValidateService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.main.service.front.WalletTransferService;
import com.ruizton.util.*;
import com.zhgtrade.front.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/7/26
 */
@Controller
public class WalletTransferController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(WalletTransferController.class);

    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private WalletTransferService walletTransferService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private RealTimeDataService realTimeDataService;
    @Autowired
    private VirtualWalletService virtualWalletService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private FrontValidateService frontValidateService;

    private Comparator<Fvirtualcointype> comparator = (f1, f2) -> {
        //0总是在非0的后面
        if(f1.getTotalOrder() == 0 && f2.getTotalOrder() != 0){
            return 1;
        }else if(f1.getTotalOrder() != 0 && f2.getTotalOrder() == 0){
            return -1;
        } else {
            return Integer.valueOf(f1.getTotalOrder()).compareTo(f2.getTotalOrder());
        }
    };

    /**
     * 转账
     *
     * @param modelMap
     */
    @RequestMapping("/account/transfer")
    public String transfer(HttpServletRequest request,
                           @RequestParam(required = false, defaultValue = "0") int symbol,
                           @RequestParam(value = "currentPage", required = false, defaultValue = "1")int page,
                           @RequestParam(value = "total", required = false, defaultValue = "0")int total,
                           Map<String, Object> modelMap) {
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid());
        List<Fvirtualcointype> fvirtualcointypes = this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
        for(int i=0; i<fvirtualcointypes.size(); i++){
            Fvirtualcointype type = fvirtualcointypes.get(i);
            if(!walletTransferService.coinEnableToTransfer(type.getFid())){
                i --;
                i %= fvirtualcointypes.size();
                fvirtualcointypes.remove(type);
            }
        }
        Fvirtualcointype fvirtualcointype = null;
        fvirtualcointypes.sort(comparator);
        if(symbol == 0){
            if(!CollectionUtils.isEmpty(fvirtualcointypes)){
                fvirtualcointype = fvirtualcointypes.get(0);
            }
        }else{
            if(walletTransferService.coinEnableToTransfer(symbol)){
                for (Fvirtualcointype t : fvirtualcointypes) {
                    if (symbol == t.getFid()) {
                        fvirtualcointype = t;
                        break;
                    }
                }
            }else{
                if(!CollectionUtils.isEmpty(fvirtualcointypes)){
                    fvirtualcointype = fvirtualcointypes.get(0);
                }
            }
        }

        if(null == fvirtualcointype){
            return "redirect:/";
        }

        // 虚拟钱包
        List<Fvirtualwallet> wallets = virtualWalletService.findByTwoProperty("fvirtualcointype.fid", fvirtualcointype.getFid(), "fuser.fid", fuser.getFid());
        if (!CollectionUtils.isEmpty(wallets)) {
            modelMap.put("wallet", wallets.get(0));
        }

        // 转账记录
        if(total <= 0){
            total = walletTransferService.count(fuser.getFid(), fvirtualcointype.getFid(), MoneyType.Virtual_Coin.getIndex(), null);
        }
        List<WalletTransfer> lines = walletTransferService.find(fuser.getFid(), fvirtualcointype.getFid(), MoneyType.Virtual_Coin.getIndex(), null, (page - 1) * Constants.PAGE_ITEM_COUNT_20, Constants.PAGE_ITEM_COUNT_20);

        modelMap.put("pageSize", Constants.PAGE_ITEM_COUNT_20);
        modelMap.put("pageNow", page);
        modelMap.put("total", total);

        modelMap.put("lines", lines);
        modelMap.put("fvirtualcointype", fvirtualcointype);
        modelMap.put("fvirtualcointypes", fvirtualcointypes);

        return "account/coin_wallet_transfer";
    }

    /**
     * 人民币转账
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/account/transfer_cny", method = RequestMethod.POST)
    public Object transferRMB(HttpServletRequest request,
                              @RequestParam(value = "amount") double amount,
                              @RequestParam(value = "password") String tradePassword,
                              @RequestParam(value = "app") String toApp) {
        Map<String, Object> retMap = new HashMap<>(2);
        /*Fuser fuser = getSessionUser(request);

        if(!walletTransferService.appIsExist(toApp)){
            // 非法参数
            retMap.put("code", 201);
            return retMap;
        }

        if(toApp.equals(constantMap.getString(ConstantKeys.USER_TRANSFER_SYSTEM_TAG))){
            retMap.put("code", 202);
            return retMap;
        }

        if (amount < 0.01) {
            retMap.put("code", 203);
            return retMap;
        }

        if (!Utils.MD5(tradePassword).equals(fuser.getFtradePassword())) {
            retMap.put("code", 204);
            return retMap;
        }

        if(StringUtils.isEmpty(fuser.getZhgOpenId())){
            fuser = frontUserService.findUserByProperty("fid", fuser.getFid()).get(0);
            if(StringUtils.isEmpty(fuser.getZhgOpenId())){
                // 没有用户系统openid
                retMap.put("code", 205);
                return retMap;
            }
        }

        Fwallet fwallet = frontUserService.findFwalletById(fuser.getFwallet().getFid());
        if (null == fwallet || fwallet.getFtotalRmb() < amount) {
            retMap.put("code", 206);
            return retMap;
        }

        WalletTransfer walletTransfer = new WalletTransfer();
        walletTransfer.setUserId(fuser.getFid());
        walletTransfer.setAmount(amount);
        walletTransfer.setToSystem(toApp);

        if (walletTransferService.insertSendRMBTransfer(fuser, walletTransfer)) {
            retMap.put("code", 200);
        } else {
            retMap.put("code", 206);
        }*/

        return retMap;
    }

    /**
     * 虚拟币转账
     *
     * @param request
     * @param amount
     * @param tradePassword
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/account/transfer_coin", method = RequestMethod.POST)
    public Object transferCoin(HttpServletRequest request,
                               @RequestParam(value = "symbol") int symbol,
                               @RequestParam(value = "amount") double amount,
                               @RequestParam(value = "password") String tradePassword,
                               @RequestParam(value = "phoneCode") String phoneCode,
                               @RequestParam(value = "app", required = false, defaultValue = "zcfunding") String toApp) {
        Map<String, Object> retMap = new HashMap<>(2);
        Fuser fuser = getSessionUser(request);

        /*if(!walletTransferService.appIsExist(toApp)){
            retMap.put("code", 201);
            return retMap;
        }*/

        if(toApp.equals(constantMap.getString(ConstantKeys.USER_TRANSFER_SYSTEM_TAG))){
            retMap.put("code", 202);
            return retMap;
        }

        if(!walletTransferService.coinEnableToTransfer(symbol)){
            retMap.put("code", 202);
            return retMap;
        }

        if (amount < 0.0001) {
            retMap.put("code", 203);
            return retMap;
        }

        if (!Utils.MD5(tradePassword).equals(fuser.getFtradePassword())) {
            retMap.put("code", 204);
            return retMap;
        }

        if(null == realTimeDataService.getLatestDealData(symbol)){
            retMap.put("code", 205);
            return retMap;
        }

        if(StringUtils.isEmpty(fuser.getZhgOpenId())){
            fuser = frontUserService.findUserByProperty("fid", fuser.getFid()).get(0);
            if(StringUtils.isEmpty(fuser.getZhgOpenId())){
                // 没有用户系统openid
                retMap.put("code", 206);
                return retMap;
            }
        }

        Fvirtualwallet fvirtualwallet = frontUserService.findVirtualWalletByUser(fuser.getFid(), symbol);
        if (null == fvirtualwallet || fvirtualwallet.getFtotal() < amount) {
            retMap.put("code", 207);
            return retMap;
        }

        // 验证码匹配
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        int _limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), authCode.getActionType());
        if (_limit <= 0) {
            retMap.put("code", 208);
            retMap.put("leftCount", 0);
            return retMap;
        } else {
            if (null == authCode || !authCode.isEnabled(fuser.getFtelephone(), phoneCode) || (CountLimitTypeEnum.TELEPHONE != authCode.getActionType() && CountLimitTypeEnum.VOICE_CAPTCHA != authCode.getActionType())) {
                retMap.put("code", 209);
                retMap.put("leftCount", _limit - 1);
                this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), authCode.getActionType());
                return retMap;
            } else {
                this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), authCode.getActionType());
            }
            request.getSession().removeAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        }

        WalletTransfer walletTransfer = new WalletTransfer();
        walletTransfer.setUserId(fuser.getFid());
        walletTransfer.setVirtualCoinId(symbol);
        walletTransfer.setAmount(amount);
        walletTransfer.setToSystem(toApp);

        if (walletTransferService.insertSendCoinTransfer(fuser, walletTransfer)) {
            retMap.put("code", 200);
        } else {
            retMap.put("code", 207);
        }

        cleanCaptcha(request);

        return retMap;
    }

    /**
     * 成功返回yes
     * <p>
     * 转账到账回调
     *
     * @param request
     * @param id
     * @param tradeNo
     * @param sign
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/funds/transfer_notify", method = RequestMethod.POST)
    public Object transferNotify(HttpServletRequest request,
                                 @RequestParam(value = "orderId") int id,
                                 @RequestParam(value = "tradeNo") String tradeNo,
                                 @RequestParam(value = "sign") String sign) {
        Map<String, Object> retMap = new HashMap<>(2);

        // ip验证
        String ip = Utils.getIpAddr(request);
        if (!validateSystemIp(ip)) {
            logger.error("转账通知ip有误，ip:" + ip);
            retMap.put("ret", false);
            return retMap;
        }

        // 签名
        WalletTransfer walletTransfer = walletTransferService.findById(id);
        Map<String, Object> map = new TreeMap<>();
        map.put("orderId", walletTransfer.getId());
        map.put("tradeNo", walletTransfer.getTradeNo());
        map.put("sysSecret", constantMap.getString(ConstantKeys.USER_TRANSFER_SYSTEM_SECRET));
        if(!SignatureUtil.getSign(map).equals(sign)){
            logger.error("转账通知签名不匹配");
            retMap.put("ret", false);
            return retMap;
        }

        if(walletTransferService.updateTransferred(walletTransfer)){
            retMap.put("ret", true);
            return retMap;
        }

        retMap.put("ret", false);
        return retMap;
    }

    /**
     * 接收转账
     *
     * @param request
     * @param openId
     * @param tradeNo
     * @param amount
     * @param sign
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/funds/receive_transfer", method = RequestMethod.POST)
    public Object receiveTransfer(HttpServletRequest request,
                                  @RequestParam(value = "openId")String openId,
                                  @RequestParam(value = "tradeNo") String tradeNo,
                                  @RequestParam(value = "amount") String amount,
                                  @RequestParam(value = "from") String fromApp,
                                  @RequestParam(value = "to") String toApp,
                                  @RequestParam(value = "moneyType")int moneyType,
                                  @RequestParam(value = "virtualCoinId", required = false, defaultValue = "0")int coinId,
                                  @RequestParam(value = "sign") String sign){

        Map<String, Object> retMap = new HashMap<>(2);

        // 参数验证
        String toTag = constantMap.getString(ConstantKeys.USER_TRANSFER_SYSTEM_TAG);
        if(fromApp.equals(toApp) || !toApp.equals(toTag)){
            logger.error("到账系统有无,to:" + toApp);
            retMap.put("ret", false);
            return retMap;
        }

        // ip验证
        String ip = Utils.getIpAddr(request);
        if (!validateSystemIp(ip)) {
            logger.error("请求ip有误, ip:" + ip);
            retMap.put("ret", false);
            return retMap;
        }

        // 签名
        Map<String, Object> map = new TreeMap<>();
        map.put("openId", openId);
        map.put("tradeNo", tradeNo);
        map.put("amount", amount);
        map.put("from", fromApp);
        map.put("to", toTag);
        map.put("sysSecret", constantMap.getString(ConstantKeys.USER_TRANSFER_SYSTEM_SECRET));
        map.put("moneyType", moneyType);
        if(0 != coinId){
            map.put("virtualCoinId", coinId);
        }
        if(!SignatureUtil.getSign(map).equals(sign)){
            logger.error("签名不匹配");
            retMap.put("ret", false);
            return retMap;
        }

        Fuser fuser = frontUserService.findByZhgOpenId(openId);
        if(null == fuser){
            logger.error("用户未同步到本地...");
            retMap.put("ret", false);
            return retMap;
        }

        WalletTransfer walletTransfer = new WalletTransfer();
        walletTransfer.setUserId(fuser.getFid());
        walletTransfer.setToSystem(toApp);
        walletTransfer.setFromSystem(fromApp);
        walletTransfer.setMoneyType(MoneyType.get(moneyType));
        walletTransfer.setVirtualCoinId(coinId);
        walletTransfer.setAmount(Double.valueOf(amount));
        walletTransfer.setTradeNo(tradeNo);
        walletTransfer.setUserId(fuser.getFid());

        if(walletTransfer.isRMB()){
            walletTransferService.insertReceiveRMBTransfer(fuser, walletTransfer);
        }else if(walletTransfer.isVirtualCoin()){
            walletTransferService.insertReceiveCoinTransfer(fuser, walletTransfer);
        }else{
            retMap.put("ret", false);
            return retMap;
        }

        retMap.put("ret", true);
        return retMap;
    }

    /**
     * 获取所有虚拟币
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/founds/conis")
    public Object virtualCoins(){
        Map<String, Object> retMap = new HashMap<>();

        List<Fvirtualcointype> conis = virtualCoinService.findAll();
        List<Map<String, Object>> coinList = new ArrayList<>(conis.size());
        for(Fvirtualcointype fvirtualcointype : conis){
            Map<String, Object> map = new HashMap<>();
            map.put("id", fvirtualcointype.getFid());
            map.put("name", fvirtualcointype.getFname());
            map.put("shortName", fvirtualcointype.getfShortName());
            map.put("symbol", fvirtualcointype.getfSymbol());
            map.put("status", fvirtualcointype.getFstatus());
            map.put("isShare", fvirtualcointype.isFisShare());
            map.put("isWithdraw", fvirtualcointype.isFIsWithDraw());
            map.put("imgUrl", fvirtualcointype.getFurl());
            map.put("introUrl", fvirtualcointype.getFintroUrl());
            map.put("createTime", fvirtualcointype.getFaddTime());
            map.put("moneyType", MoneyType.Virtual_Coin.getIndex());
            coinList.add(map);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", 0);
        map.put("name", "人民币");
        map.put("shortName", "RMB");
        map.put("status", 1);
        map.put("isShare", true);
        map.put("isWithdraw", true);
        map.put("createTime", Utils.getTimestamp());
        map.put("moneyType", MoneyType.RMB.getIndex());
        coinList.add(map);

        retMap.put("ret", true);
        retMap.put("coins", coinList);
        return retMap;
    }

    /**
     * 校验用户系统的ip
     *
     * @param ip
     * @return
     */
    protected boolean validateSystemIp(String ip){
        List<String> ipList = new ArrayList<>(Arrays.asList(constantMap.getString(ConstantKeys.USER_SYS_IPS).split(",")));
        return ipList.contains(ip);
    }
}
