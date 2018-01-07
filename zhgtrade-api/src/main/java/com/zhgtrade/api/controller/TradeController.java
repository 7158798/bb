//package com.zhgtrade.api.controller;
//
//import com.ruizton.main.Enum.EntrustRobotStatusEnum;
//import com.ruizton.main.Enum.EntrustStatusEnum;
//import com.ruizton.main.Enum.EntrustTypeEnum;
//import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
//import com.ruizton.main.api.APIResultCode;
//import com.ruizton.main.cache.data.RealTimeDataService;
//import com.ruizton.main.comm.ConstantMap;
//import com.ruizton.main.dto.FentrustData;
//import com.ruizton.main.dto.FentrustlogData;
//import com.ruizton.main.dto.LatestDealData;
//import com.ruizton.main.model.Fentrust;
//import com.ruizton.main.model.Flimittrade;
//import com.ruizton.main.model.Fuser;
//import com.ruizton.main.model.Fvirtualaddress;
//import com.ruizton.main.model.Fvirtualcointype;
//import com.ruizton.main.model.Fvirtualwallet;
//import com.ruizton.main.model.Fwallet;
//import com.ruizton.main.service.admin.LimittradeService;
//import com.ruizton.main.service.admin.VirtualWalletService;
//import com.ruizton.main.service.front.FrontTradeService;
//import com.ruizton.main.service.front.FrontUserService;
//import com.ruizton.main.service.front.FrontVirtualCoinService;
//import com.ruizton.util.ConstantKeys;
//import com.ruizton.util.Constants;
//import com.ruizton.util.DateUtils;
//import com.ruizton.util.FormatUtils;
//import com.ruizton.util.MarketUtils;
//import com.ruizton.util.Utils;
//import com.zhgtrade.api.utils.ApiConstants;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// * 比特家
// * CopyRight : www.btc58.cc
// * Author : xxp
// * Date： 2016-04-12 14:02
// */
//@Controller
//@RequestMapping("/api/v1")
//public class TradeController extends ApiBaseController {
//
//    @Autowired
//    private RealTimeDataService realTimeDataService;
//    @Autowired
//    private VirtualWalletService virtualWalletService;
//    @Autowired
//    private FrontTradeService frontTradeService;
//    @Autowired
//    private FrontUserService frontUserService;
//    @Autowired
//    private FrontVirtualCoinService frontVirtualCoinService;
//    @Autowired
//    private LimittradeService limittradeService;
//    @Autowired
//    private ConstantMap constantMap;
//
//    /**
//     * 400/s
//     * 获取zhgtrade行情
//     *
//     * @param symbol
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/ticker")
//    public Object ticker(@RequestParam(value = "symbol") int symbol) {
//        Map<String, Object> ret = new HashMap<>(5);
//        if (symbol <= 0) {
//            ret.put(ApiConstants.RESULT_KEY, false);
//            ret.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_201.getCode());
//            return ret;
//        }
//
//        LatestDealData dealData = realTimeDataService.getLatestDealData(symbol);
//        Map<String, Object> ticker = new HashMap<>();
//        ticker.put("last", Utils.getDouble(dealData.getLastDealPrize(), 4));
//        ticker.put("buy", Utils.getDouble(dealData.getHigestBuyPrize(), 4));
//        ticker.put("sell", Utils.getDouble(dealData.getLowestSellPrize(), 4));
//        ticker.put("high", Utils.getDouble(dealData.getHighestPrize24(), 4));
//        ticker.put("low", Utils.getDouble(dealData.getLowestPrize24(), 4));
//        ticker.put("vol", Utils.getDouble(dealData.getVolumn(), 4));
//
//        ret.put("ticker", ticker);
//        ret.put(ApiConstants.RESULT_KEY, true);
//        return ret;
//    }
//
//    /**
//     * 400/s
//     * 获取zhgtrade行情
//     *
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/tickers")
//    public Object tickers() {
//        Map<String, Object> ret = new HashMap<>(5);
//
//        List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
//        List list = new ArrayList<>(dealDatas.size());
//
//        dealDatas.forEach(dealData -> {
//            Map<String, Object> ticker = new HashMap<>();
//            ticker.put("symbol", dealData.getFid());
//            ticker.put("last", Utils.getDouble(dealData.getLastDealPrize(), 4));
//            ticker.put("buy", Utils.getDouble(dealData.getHigestBuyPrize(), 4));
//            ticker.put("sell", Utils.getDouble(dealData.getLowestSellPrize(), 4));
//            ticker.put("high", Utils.getDouble(dealData.getHighestPrize24(), 4));
//            ticker.put("low", Utils.getDouble(dealData.getLowestPrize24(), 4));
//            ticker.put("vol", Utils.getDouble(dealData.getVolumn(), 4));
//
//            list.add(ticker);
//        });
//
//        ret.put("tickers", list);
//        ret.put(ApiConstants.RESULT_KEY, true);
//        return ret;
//    }
//
////    /**
////     * 500/s
////     * 获取市场深度
////     *
////     * @return
////     */
////    @ResponseBody
////    @RequestMapping(value = "/depth")
////    public Object depth(HttpServletRequest request, @RequestParam(value = "symbol") int symbol,
////                        @RequestParam(value = "merge", required = false, defaultValue = "0") int merge) {
////        Map<String, Object> result = new HashMap<>(5);
////        if (symbol <= 0) {
////            result.put(ApiConstants.RESULT_KEY, false);
////            result.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_201.getCode());
////            return result;
////        }
////
////        List<FentrustData> buyData = realTimeDataService.getBuyDepthMap(symbol, merge);
////        List<FentrustData> sellData = realTimeDataService.getSellDepthMap(symbol, merge);
////
////        List buyList = new ArrayList<>(buyData.size());
////        List sellList = new ArrayList<>(sellData.size());
////        for (FentrustData data : buyData) {
////            buyList.add(new Object[]{data.getFprize(), Utils.getDouble(data.getFleftCount(), 4)});
////        }
////        for (FentrustData data : sellData) {
////            sellList.add(new Object[]{data.getFprize(), Utils.getDouble(data.getFleftCount(), 4)});
////        }
////
////        result.put("asks", buyList);
////        result.put("bids", sellList);
////        result.put(ApiConstants.RESULT_KEY, true);
////        return result;
////    }
//
//    /**
//     * 400/s
//     * 获取最近100交易信息
//     *
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/orders")
//    public Object orders(HttpServletRequest request, @RequestParam(value = "symbol") int symbol,
//                         @RequestParam(value = "size", required = false, defaultValue = "100") int size) {
//        Map<String, Object> result = new HashMap<>(5);
//        if (symbol <= 0) {
//            result.put(ApiConstants.RESULT_KEY, false);
//            result.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_201.getCode());
//            return result;
//        }
//
//        if(size > 100){
//            size = 100;
//        }
//
//        Set<FentrustlogData> logData = realTimeDataService.getEntrustSuccessMapLimit(symbol, size);
//        List<Object> dataList = new ArrayList<>(logData.size());
//        for (FentrustlogData fentrustlog : logData) {
//            Map<String, Object> map = new HashMap<>(6);
//            map.put("date", fentrustlog.getFcreateTime().getTime() / 1000);
//            map.put("price", Utils.getDouble(fentrustlog.getFprize(), 4));
//            map.put("amount", Utils.getDouble(fentrustlog.getFamount(), 4));
////            map.put("tid", fentrustlog.getFid());
//            map.put("type", fentrustlog.getfEntrustType() == EntrustTypeEnum.BUY ? "buy" : "sell");
//            dataList.add(map);
//        }
//
//        result.put(ApiConstants.RESULT_KEY, true);
//        result.put("orders", dataList);
//        return result;
//    }
//
//    /**
//     * 250/s
//     * 获取用户信息
//     *
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/balance")
//    public Object userInfo(HttpServletRequest request) {
//        Fuser fuser = getFuser(request);
//        Fwallet fwallet = fuser.getFwallet();
//
//        Map<String, Object> info = new HashMap();
//        // 人民币
//        info.put("cny_total", FormatUtils.formatCNY(fwallet.getFtotalRmb() + fwallet.getFfrozenRmb()));
//        info.put("cny_frozen", FormatUtils.formatCNY(fwallet.getFfrozenRmb()));
//
//        // 虚拟币
//        List<Fvirtualwallet> viWallets = virtualWalletService.list(0, 0, "where fuser.fid=" + fuser.getFid(), false);
//        for (Fvirtualwallet wallet : viWallets) {
//            String name = wallet.getFvirtualcointype().getfShortName().toLowerCase();
//            info.put(name + "_total", FormatUtils.formatCoin(wallet.getFtotal() + wallet.getFfrozen()));
//            info.put(name + "_frozen", FormatUtils.formatCoin(wallet.getFfrozen()));
//        }
//
//        Map<String, Object> result = new HashMap<>(2);
//        result.put("info", info);
//        result.put(ApiConstants.RESULT_KEY, true);
//
//        return result;
//    }
//
//    /**
//     * 300/s
//     * @param request
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/wallet")
//    public Object walletAddress(HttpServletRequest request,
//                                @RequestParam(value = "symbol") int symbol) {
//        Map<String, Object> result = new HashMap<>(5);
//        if (symbol <= 0) {
//            result.put(ApiConstants.RESULT_KEY, false);
//            result.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_201.getCode());
//            return result;
//        }
//
//        Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(symbol);
//
//        if (null == fvirtualcointype) {
//            result.put(ApiConstants.RESULT_KEY, false);
//            result.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_201.getCode());
//            return result;
//        }
//
//        if (!fvirtualcointype.isFIsWithDraw()) {
//            // 不能充值
//            result.put(ApiConstants.RESULT_KEY, false);
//            result.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_202.getCode());
//            return result;
//        }
//
//        Fuser fuser = getFuser(request);
//        Fvirtualaddress fvirtualaddress = this.frontVirtualCoinService.findFvirtualaddress(fuser, fvirtualcointype);
//
//        // 若没有地址 则分配地址
//        if (null == fvirtualaddress) {
//            try {
//                fvirtualaddress = frontVirtualCoinService.updateAssignWalletAddress(fuser, fvirtualcointype);
//            } catch (Exception e) {
//                fvirtualaddress = this.frontVirtualCoinService.findFvirtualaddress(fuser, fvirtualcointype);
//                e.printStackTrace();
//            }
//        }
//
//        if (null == fvirtualaddress) {
//            // 没有分配到地址
//            result.put(ApiConstants.RESULT_KEY, false);
//            result.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_203.getCode());
//            return result;
//        }
//
//        result.put(ApiConstants.RESULT_KEY, true);
//        result.put("address", fvirtualaddress.getFadderess());
//        return result;
//    }
//
//    /**
//     * 200/s
//     * 挂单查询 最多100条
//     *
//     * @param request
//     * @param symbol
//     * @param since
//     * @param type    0全部 1挂单中
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "trade_list")
//    public Object tradeList(HttpServletRequest request,
//                             @RequestParam(value = "symbol") int symbol,
//                             @RequestParam(value = "since", required = false, defaultValue = "0") long since,
//                             @RequestParam(value = "type", defaultValue = "0") int type) {
//        int userId = (int) request.getAttribute(Constants.USER_ID);
//        Map<String, Object> resMap = new HashMap<>(5);
//
//        if (symbol <= 0) {
//            resMap.put(ApiConstants.RESULT_KEY, false);
//            resMap.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_201.getCode());
//            return resMap;
//        }
//
//        Date beginDate = null;
//        if (since > 0) {
//            beginDate = new Date(since * 1000);
//        }
//
//        Integer[] status = 1 == type ? new Integer[]{1, 2} : null;
//
//        List<Fentrust> fentrusts = frontTradeService.findFentrustHistory(userId, symbol, null, status, beginDate, null, 1, 100);
//        List<Map<String, Object>> result = new ArrayList<>(fentrusts.size());
//        for (Fentrust fentrust : fentrusts) {
//            Map<String, Object> map = new HashMap<>(10);
//            map.put("id", fentrust.getFid());
//            map.put("datetime", DateUtils.formatDate(fentrust.getFcreateTime(), "yyyy-MM-dd HH:mm:ss"));
//            map.put("amount", Utils.getDouble(fentrust.getFamount(), 4));
//            map.put("left_amount", Utils.getDouble(fentrust.getFamount() - fentrust.getFsuccessAmount(), 4));
//            map.put("price", Utils.getDouble(fentrust.getFprize(), 4));
//            map.put("status", fentrust.getFstatus());
//            map.put("type", 0 == fentrust.getFentrustType() ? "buy" : "sell");
//            map.put("count", fentrust.getFcount());
//            map.put("left_count", fentrust.getFleftCount());
//            result.add(map);
//        }
//
//        resMap.put("orders", result);
//        resMap.put(ApiConstants.RESULT_KEY, true);
//
//        return resMap;
//    }
//
//    /**
//     * 450/s
//     * 获取用户订单
//     *
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/trade_view")
//    public Object tradeInfo(HttpServletRequest request,
//                            @RequestParam(value = "id") int id) {
//        int userId = (int) request.getAttribute(Constants.USER_ID);
//
//        Fentrust fentrust = frontTradeService.findFentrustById(id);
//
//        Map<String, Object> resMap = new HashMap<>(5);
//        if (userId != fentrust.getFuser().getFid()) {
//            // 非法操作
//            resMap.put(ApiConstants.RESULT_KEY, false);
//            resMap.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_401.getCode());
//            return resMap;
//        }
//
//        Map<String, Object> map = new HashMap<>(10);
//        map.put("id", fentrust.getFid());
//        map.put("datetime", DateUtils.formatDate(fentrust.getFcreateTime(), "yyyy-MM-dd HH:mm:ss"));
//        map.put("amount", Utils.getDouble(fentrust.getFamount(), 4));
//        map.put("left_amount", Utils.getDouble(fentrust.getFamount() - fentrust.getFsuccessAmount(), 4));
//        map.put("price", Utils.getDouble(fentrust.getFprize(), 4));
//        map.put("status", fentrust.getFstatus());
//        map.put("type", 0 == fentrust.getFentrustType() ? "buy" : "sell");
//        map.put("count", fentrust.getFcount());
//        map.put("left_count", fentrust.getFleftCount());
//
//        resMap.put(ApiConstants.RESULT_KEY, true);
//        resMap.put("order", map);
//        return resMap;
//    }
//
//    /**
//     * 200/s
//     * 取消挂单
//     *
//     * @param request
//     * @param id
//     * @return
//     * @throws Exception
//     */
//    @ResponseBody
//    @RequestMapping(value = "/cancel_trade", method = RequestMethod.POST)
//    public Object cancelEntrust(HttpServletRequest request, @RequestParam(value = "id") int id) throws Exception {
//
//        Fuser fuser = getFuser(request);
//        Map<String, Object> resMap = new HashMap<>(5);
//
//        final Fentrust fentrust = this.frontTradeService.findFentrustById(id);
//        if (fentrust.getFuser().getFid() != fuser.getFid()) {
//            // 非法操作
//            resMap.put(ApiConstants.RESULT_KEY, false);
//            resMap.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_401.getCode());
//            return resMap;
//        }
//        if (fentrust != null && (fentrust.getFstatus() == EntrustStatusEnum.Going || fentrust.getFstatus() == EntrustStatusEnum.PartDeal)) {
//            try {
//                /*更新挂单*/
//                this.frontTradeService.updateCancelFentrust(fentrust, fuser);
//                realTimeDataService.removeEntrustBuyMap(fentrust.getFvirtualcointype().getFid(), fentrust);
//                resMap.put(ApiConstants.RESULT_KEY, true);
//            } catch (Exception e) {
//                e.printStackTrace();
//                resMap.put(ApiConstants.RESULT_KEY, false);
//                resMap.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_402.getCode());
//            }
//        } else {
//            resMap.put(ApiConstants.RESULT_KEY, false);
//            resMap.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_204.getCode());
//        }
//
//        resMap.put("id", id);
//        return resMap;
//    }
//
//    /**
//     * 200/s
//     * 下单交易
//     *
//     * @return
//     * @throws Exception
//     */
//    @ResponseBody
//    @RequestMapping(value = "/trade", method = RequestMethod.POST)
//    public Object trade(HttpServletRequest request,
//                        @RequestParam(value = "symbol") int symbol,
//                        @RequestParam(value = "type") String type,
//                        @RequestParam(value = "price") double price,
//                        @RequestParam(value = "amount") double amount) throws Exception {
//        if ("buy".equals(type)) {
//            return this.buyBtcSubmit(request, 0, symbol, amount, price);
//        } else if ("sell".equals(type)) {
//            return this.sellBtcSubmit(request, 0, symbol, amount, price);
//        } else {
//            Map<String, Object> map = new HashMap<>(2);
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_401.getCode());
//            return map;
//        }
//    }
//
//    /**
//     * 挂买单
//     *
//     * @param request
//     * @param limited
//     * @param symbol
//     * @param tradeAmount
//     * @param tradeCnyPrice
//     * @return
//     * @throws Exception
//     */
//    protected Object buyBtcSubmit(
//            HttpServletRequest request,
//            @RequestParam(required = false, defaultValue = "0") final int limited,//是否按照市场价买入
//            @RequestParam(required = true) final int symbol,//币种
//            @RequestParam(required = true) double tradeAmount,//数量
//            @RequestParam(required = true) double tradeCnyPrice//单价
//    ) throws Exception {
//
//        Map<String, Object> map = new HashMap<>();
//
//        tradeAmount = Utils.getDouble(tradeAmount, 4);
//        tradeCnyPrice = Utils.getDouble(tradeCnyPrice, 4);
//
//        if (tradeAmount < 0.0001D) {
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_205.getCode());
//            return map;
//        }
//
//        if (tradeCnyPrice < 0.0001D) {
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_206.getCode());
//            return map;
//        }
//
//        // 最大交易限制
//        if(tradeAmount * tradeCnyPrice > constantMap.getDouble(ConstantKeys.MAX_TRADE_AMOUNT)){
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_212.getCode());
//            return map;
//        }
//
//        LatestDealData vdata = realTimeDataService.getLatestDealData(symbol);
//
//        if (vdata == null || vdata.getStatus() == VirtualCoinTypeStatusEnum.Abnormal) {
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_207.getCode());
//            return map;
//        }
//
//        if (!vdata.isFisShare()) {
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_207.getCode());
//            return map;
//        }
//
//        //是否开放交易
//        if (MarketUtils.openTrade(vdata.getOpenTrade()) == false) {
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_207.getCode());
//            return map;
//        }
//
//        if (checkLimitTrade(symbol, tradeCnyPrice, map))
//            return map;
//
//
//        Fuser fuser = getFuser(request);
//
//        double totalTradePrice = 0F;
//        if (limited == 0) {
//            totalTradePrice = tradeAmount * tradeCnyPrice;
//        } else {
//            totalTradePrice = tradeAmount;
//        }
//        Fwallet fwallet = fuser.getFwallet();
//        if (fwallet.getFtotalRmb() < totalTradePrice) {
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_208.getCode());
//            return map;
//        }
//
//        boolean flag = false;
//        try {
//            Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(symbol);
//            final Fentrust fentrust = this.frontTradeService.updateEntrustBuy2(tradeAmount, tradeCnyPrice, fuser, limited == 1, EntrustRobotStatusEnum.Normal, fvirtualcointype);
//            frontTradeService.sendToQueue(limited == 1, symbol, fentrust);
//            map.put("id", fentrust.getFid());
//            flag = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (flag) {
//            map.put(ApiConstants.RESULT_KEY, true);
//        } else {
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_402.getCode());
//        }
//        return map;
//    }
//
//    /**
//     * 挂卖单
//     *
//     * @param request
//     * @param limited
//     * @param symbol
//     * @param tradeAmount
//     * @param tradeCnyPrice
//     * @return
//     * @throws Exception
//     */
//    protected Object sellBtcSubmit(
//            HttpServletRequest request,
//            @RequestParam(required = false, defaultValue = "0") final int limited,//是否按照市场价买入
//            @RequestParam(required = true) final int symbol,//币种
//            @RequestParam(required = true) double tradeAmount,//数量
//            @RequestParam(required = true) double tradeCnyPrice//单价
//    ) throws Exception {
//
//        Map<String, Object> map = new HashMap<>();
//
//        tradeAmount = Utils.getDouble(tradeAmount, 4);
//        tradeCnyPrice = Utils.getDouble(tradeCnyPrice, 4);
//
//        if (tradeAmount < 0.0001D) {
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_205.getCode());
//            return map;
//        }
//
//        if (tradeCnyPrice < 0.0001D) {
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_206.getCode());
//            return map;
//        }
//
//        // 最大交易限制
//        if(tradeAmount * tradeCnyPrice > constantMap.getDouble(ConstantKeys.MAX_TRADE_AMOUNT)){
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_212.getCode());
//            return map;
//        }
//
//        LatestDealData vdata = realTimeDataService.getLatestDealData(symbol);
//
//        if (vdata == null || !vdata.isFisShare()
//                || vdata.getStatus() != VirtualCoinTypeStatusEnum.Normal) {
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_207.getCode());
//            return map;
//        }
//
//        //是否开放交易
//        if (MarketUtils.openTrade(vdata.getOpenTrade()) == false) {
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_207.getCode());
//            return map;
//        }
//
//        // 限价交易
//        if (checkLimitTrade(symbol, tradeCnyPrice, map)) {
//            return map;
//        }
//
//        Fuser fuser = getFuser(request);
//        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(), symbol);
//        if (fvirtualwallet == null) {
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_211.getCode());
//            return map;
//        }
//        if (fvirtualwallet.getFtotal() < tradeAmount) {
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_211.getCode());
//            return map;
//        }
//
//        boolean flag = false;
//        try {
//            Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);
//
//            final Fentrust fentrust = this.frontTradeService.updateEntrustSell2(symbol, tradeAmount, tradeCnyPrice, fuser, limited == 1, EntrustRobotStatusEnum.Normal, fvirtualcointype);
//            frontTradeService.sendToQueue(limited == 1, symbol, fentrust);
//            map.put("id", fentrust.getFid());
//            flag = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (flag) {
//            map.put(ApiConstants.RESULT_KEY, true);
//        } else {
//            map.put(ApiConstants.RESULT_KEY, false);
//            map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_402.getCode());
//        }
//
//        return map;
//    }
//
//    private boolean checkLimitTrade(@RequestParam(required = true) int symbol, @RequestParam(required = true) double tradeCnyPrice, Map<String, Object> map) {
//        Flimittrade limittrade = this.isLimitTrade(symbol);
//
//        double upPrice = 0d;
//        double downPrice = 0d;
//        if (limittrade != null) {
//            upPrice = limittrade.getFupprice();
//            downPrice = limittrade.getFdownprice();
//            if (downPrice < 0) downPrice = 0;
//            if (tradeCnyPrice > upPrice || tradeCnyPrice < downPrice) {
//                map.put(ApiConstants.RESULT_KEY, false);
//                map.put(ApiConstants.RESULT_CODE_KEY, APIResultCode.Code_210.getCode());
//                return true;
//            }
//        }
//        return false;
//    }
//
//    protected Flimittrade isLimitTrade(int vid) {
//        Flimittrade flimittrade = null;
//        String filter = "where fvirtualcointype.fid=" + vid;
//        List<Flimittrade> flimittrades = this.limittradeService.list(0, 0, filter, false);
//        if (flimittrades != null && flimittrades.size() > 0) {
//            flimittrade = flimittrades.get(0);
//        }
//        return flimittrade;
//    }
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
