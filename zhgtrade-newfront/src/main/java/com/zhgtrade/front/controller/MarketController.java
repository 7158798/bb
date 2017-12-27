package com.zhgtrade.front.controller;

import com.alibaba.fastjson.JSONArray;
import com.ruizton.main.Enum.EntrustRobotStatusEnum;
import com.ruizton.main.Enum.EntrustStatusEnum;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.auto.OneDayData;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.dto.FentrustData;
import com.ruizton.main.dto.FentrustlogData;
import com.ruizton.main.dto.LatestDealData;
import com.ruizton.main.model.*;
import com.ruizton.main.service.admin.LimittradeService;
import com.ruizton.main.service.admin.SystemArgsService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.main.service.front.*;
import com.ruizton.util.*;
import com.ruizton.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/market")
public class MarketController extends BaseController {

    @Autowired
    private RealTimeDataService realTimeDataService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private SystemArgsService systemArgsService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontTradeService frontTradeService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private LimittradeService limittradeService;
    @Autowired
    private ProjectCollectionService projectCollectionService;
    @Autowired
    private FrontOthersService frontOthersService;

    @Autowired
    private MarketService marketService;
    @Autowired
    private OneDayData oneDayData;
    @Autowired
    private ConstantMap constantMap;
    private Comparator<LatestDealData> comparator = (f1, f2) -> {
        //0总是在非0的后面
        if(f1.getTotalOrder() == 0 && f2.getTotalOrder() != 0){
            return 1;
        }else if(f1.getTotalOrder() != 0 && f2.getTotalOrder() == 0){
            return -1;
        } else {
            return Integer.valueOf(f1.getTotalOrder()).compareTo(f2.getTotalOrder());
        }
    };

    private static final int NUMBER_PER_PAGE = 10;
    /* 实时行情页面 */
    @RequestMapping("/trade")
    public String account(HttpServletRequest request, @RequestParam(value = "symbol", required = false, defaultValue = "0") int symbol, Model model) {
        List<LatestDealData> vlist = realTimeDataService.getLatestDealDataList();
        List immutableList = Utils.deepCopy(vlist);
        immutableList.sort(comparator);
        LatestDealData vdata = realTimeDataService.getLatestDealData(symbol);

        Fuser fuser = getSessionUser(request);
        if(fuser != null){
            ProjectCollection projectCollection = projectCollectionService.findByUserAndCoin(fuser.getFid(), symbol);
            if(projectCollection == null){
                model.addAttribute("isCollected", 0);
            }else {
                model.addAttribute("isCollected", 1);
            }
        }else{
            model.addAttribute("isCollected", 0);
        }
        if (vdata == null || vdata.getFid() == 0) {
            vdata = vlist.get(0);
            symbol = vdata.getFid();
        }

        model.addAllAttributes(real(symbol));
        model.addAttribute("symbol", symbol);
        model.addAttribute("vlist", immutableList);
        model.addAttribute("vdata", vdata);

        Fvirtualcointype fvirtualcointype = virtualCoinService.findById(symbol);
        model.addAttribute("tradeTips", null != fvirtualcointype && StringUtils.hasText(fvirtualcointype.getFdescription())?fvirtualcointype.getFdescription():constantMap.getString(ConstantKeys.COIN_TRADE_TIPS));
        return "market/trade";
    }

    /**
     * 实时行情
     *
     * @param id
     * @return
     */
    @RequestMapping("/real")
    @ResponseBody
    public Map<String, Object> real(@RequestParam("symbol") int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("last", String.valueOf(this.realTimeDataService.getLatestDealPrize(id)));
        map.put("buy", String.valueOf(this.realTimeDataService.getHighestBuyPrize(id)));
        map.put("sell", String.valueOf(this.realTimeDataService.getLowestSellPrize(id)));
        map.put("high", String.valueOf(this.oneDayData.getHighest(id)));
        map.put("low", String.valueOf(this.oneDayData.getLowest(id)));
        map.put("vol", String.valueOf(this.oneDayData.getTotal(id)));
        return map;
    }

    /**
     * 挂买单
     *
     * @param request
     * @param limited
     * @param symbol
     * @param tradeAmount
     * @param tradeCnyPrice
     * @param tradePwd
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/buyBtcSubmit", method = RequestMethod.POST)
    public Object buyBtcSubmit(
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") final int limited,//是否按照市场价买入
            @RequestParam(required = true) final int symbol,//币种
            @RequestParam(required = true) double tradeAmount,//数量
            @RequestParam(required = true) double tradeCnyPrice,//单价
            @RequestParam(required = false, defaultValue = "0") String tradePwd
    ) throws Exception {

        Map<String, Object> map = new HashMap<>();
        //检查是否登录
        Fuser sessionUser = getSessionUser(request);
        if (sessionUser == null){
            map.put("isLogin", 0);
            return map;
        }

        Market market = marketService.findById(symbol);

        if (market == null || market.getStatus() == Market.STATUS_Abnormal) {
            map.put("resultCode", -100);
            return map;
        }

        if (market.getTradeStatus() == Market.TRADE_STATUS_Abnormal) {
            map.put("resultCode", -101);
            return map;
        }

        //是否开放交易
        if (MarketUtils.openTrade(market.getTradeTime()) == false) {
            map.put("resultCode", -400);
            return map;
        }

        tradeAmount = Utils.getDouble(tradeAmount, market.getDecimals());
        tradeCnyPrice = Utils.getDouble(tradeCnyPrice, market.getDecimals());
//        System.out.println("1、" + System.currentTimeMillis());

        if (tradeAmount < market.getMinCount()) {
            map.put("resultCode", -1);
            map.put("msg", market.getMinCount());
            return map;
        }

        if (market.getMinMoney() != -1 && tradeAmount * tradeCnyPrice < market.getMinMoney()) {
            map.put("resultCode", -3);
            map.put("msg", market.getMinMoney());
            return map;
        }
        // 最大交易限制
        if(market.getMaxMoney() != -1 && tradeAmount * tradeCnyPrice > market.getMaxMoney()){
            map.put("resultCode", -35);
            map.put("msg", market.getMaxMoney());
            return map;
        }

        if (checkLimitTrade(market, tradeCnyPrice, map)) return map;

        Fuser fuser = this.frontUserService.findById(sessionUser.getFid());

        double totalTradePrice = 0F;
        if (limited == 0) {
            totalTradePrice = tradeAmount * tradeCnyPrice;
        } else {
            totalTradePrice = tradeAmount;
        }

        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(), market.getBuyId());
        if (fvirtualwallet == null) {
            map.put("resultCode", -200);
            return map;
        }
        if (fvirtualwallet.getFtotal() < totalTradePrice) {
            map.put("resultCode", -4);
            return map;
        }

        // 限价交易
        if (checkTradePassword(request, tradePwd, map, fuser))  {
            return map;
        }

        boolean flag = false;
        try {
            final Fentrust fentrust = this.frontTradeService.updateEntrustBuy2(market, tradeAmount, tradeCnyPrice, fuser);
            frontTradeService.sendToQueue(limited == 1, symbol, fentrust);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (flag) {
            map.put("resultCode", 0);
            setNoNeedPassword(request);
        } else {
            map.put("resultCode", -200);
        }
        return map;
    }

    private boolean checkLimitTrade(Market market, @RequestParam(required = true) double tradeCnyPrice, Map<String, Object> map) {
        double upPrice = 0d;
        double downPrice = 0d;
        if (market.getMaxPrice() != -1) {
            upPrice = market.getMaxPrice();
            if (tradeCnyPrice > upPrice) {
                map.put("resultCode", -900);
                return true;
            }
        }
        if (market.getMinPrice() != -1) {
            downPrice = market.getMinPrice();
            if (tradeCnyPrice < downPrice) {
                map.put("resultCode", -900);
                return true;
            }
        }
        return false;
    }

    public Flimittrade isLimitTrade(int vid) {
        Flimittrade flimittrade = null;
        String filter = "where fvirtualcointype.fid="+vid;
        List<Flimittrade> flimittrades = this.limittradeService.list(0, 0, filter, false);
        if(flimittrades != null && flimittrades.size() >0){
            flimittrade = flimittrades.get(0);
        }
        return flimittrade;
    }

    /**
     * 挂卖单
     * @param request
     * @param limited
     * @param symbol
     * @param tradeAmount
     * @param tradeCnyPrice
     * @param tradePwd
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/sellBtcSubmit", method = RequestMethod.POST)
    public Object sellBtcSubmit(
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") final int limited,//是否按照市场价买入
            @RequestParam(required = true) final int symbol,//币种
            @RequestParam(required = true) double tradeAmount,//数量
            @RequestParam(required = true) double tradeCnyPrice,//单价
            @RequestParam(required = false, defaultValue = "0") String tradePwd
    ) throws Exception {

        //检查是否登录
        Map<String, Object> map = new HashMap<>();

        Fuser sessionUser = getSessionUser(request);
        if (sessionUser == null){
            map.put("isLogin", 0);
            return map;
        }

        Market market = marketService.findById(symbol);

        if (market == null || market.getStatus() == Market.STATUS_Abnormal) {
            map.put("resultCode", -100);
            return map;
        }

        if (market.getTradeStatus() == Market.TRADE_STATUS_Abnormal) {
            map.put("resultCode", -101);
            return map;
        }

        //是否开放交易
        if (MarketUtils.openTrade(market.getTradeTime()) == false) {
            map.put("resultCode", -400);
            return map;
        }

        tradeAmount = Utils.getDouble(tradeAmount, market.getDecimals());
        tradeCnyPrice = Utils.getDouble(tradeCnyPrice, market.getDecimals());

        if (tradeAmount < market.getMinCount()) {
            map.put("resultCode", -1);
            map.put("msg", market.getMinCount());
            return map;
        }

        if (market.getMinMoney() != -1 && tradeAmount * tradeCnyPrice < market.getMinMoney()) {
            map.put("resultCode", -3);
            map.put("msg", market.getMinMoney());
            return map;
        }

        // 最大交易限制
        if(market.getMaxMoney() != -1 && tradeAmount * tradeCnyPrice > market.getMaxMoney()){
            map.put("resultCode", -35);
            map.put("msg", market.getMaxMoney());
            return map;
        }

        if (checkLimitTrade(market, tradeCnyPrice, map)) {return map;}

        Fuser fuser = this.frontUserService.findById(sessionUser.getFid());
        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(), market.getSellId());
        if (fvirtualwallet == null) {
            map.put("resultCode", -200);
            return map;
        }
        if (fvirtualwallet.getFtotal() < tradeAmount) {
            map.put("resultCode", -4);
            return map;
        }

        if (checkTradePassword(request, tradePwd, map, fuser)) return map;

        boolean flag = false;
        try {
            final Fentrust fentrust = this.frontTradeService.updateEntrustSell2(market, tradeAmount, tradeCnyPrice, fuser);
            frontTradeService.sendToQueue(limited == 1, symbol, fentrust);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (flag) {
            map.put("resultCode", 0);
            setNoNeedPassword(request);
        } else {
            map.put("resultCode", -200);
        }

        return map;
    }
    /**
     * 取消挂单
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/cancelEntrust", method = RequestMethod.POST)
    public Object cancelEntrust(@RequestParam(required = false, defaultValue = "0") int id ,HttpServletRequest request) throws Exception {

        Map<String, Object> map = new HashMap<>();
        Fuser sessionUser = getSessionUser(request);

        if (sessionUser == null) {
            map.put("isLogin", 0);
            return map;
        }

        Fuser fuser = this.frontUserService.findById(sessionUser.getFid());
        final Fentrust fentrust = this.frontTradeService.findFentrustById(id);
        if (fentrust != null && (fentrust.getFstatus() == EntrustStatusEnum.Going || fentrust.getFstatus() == EntrustStatusEnum.PartDeal) && fentrust.getFuser().getFid() == fuser.getFid()) {
            try {
                /*更新挂单*/
                this.frontTradeService.updateCancelFentrust(fentrust, fuser);
                realTimeDataService.removeEntrustBuyMap(fentrust.getMarket().getId(), fentrust);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @RequestMapping("/getFee")
    @ResponseBody
    public Object getFee(@RequestParam("symbol") int symbol) {
        double buyFfee = 0d;
        double sellFee = 0d;
        Market market = marketService.findById(symbol);
        sellFee = market.getSellFee();
        buyFfee = market.getBuyFee();
        Map<String, Double> ret = new HashMap<>();
        ret.put("sellFee", sellFee);
        ret.put("buyFee", buyFfee);
        return ret;
    }

    @RequestMapping("/detail")
    public String detail(Map<String, Object> map, @RequestParam(value = "symbol", required = true)int symbol) {
        Fvirtualcointype type = virtualCoinService.findById(symbol);
        FTypeDetail fTypeDetail = virtualCoinService.findDetailByFvid(symbol);

        map.put("type", type);
        map.put("detail", fTypeDetail);
        return "market/detail";
    }

    private boolean checkTradePassword(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") String tradePwd, Map<String, Object> map, Fuser fuser) throws Exception {
        if (isNeedTradePassword(request)) {
            if (tradePwd == null || tradePwd.trim().length() == 0) {
                map.put("resultCode", -50);
                return true;
            }
            if (fuser.getFtradePassword() == null) {
                map.put("resultCode", -5);
                return true;
            }
            if (!Utils.MD5(tradePwd).equals(fuser.getFtradePassword())) {
                map.put("resultCode", -2);
                return true;
            }
        }
        return false;
    }


    @ResponseBody
    @RequestMapping("/refreshUserInfo")
    public Object refreshUserInfo(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") int symbol) throws Exception {
        Map<String, Object> map = new HashMap<>();

        // 检查币ID有效性
        symbol = checkVirtualCoinId(symbol);

        if (symbol == 0) {
            return map;
        }

        map.put("recommendPrizesell", realTimeDataService.getHighestBuyPrize(symbol));//推荐卖出价
        map.put("recommendPrizebuy", realTimeDataService.getLowestSellPrize(symbol));//推荐买入价

        Fuser user = getSessionUser(request);

        if (user != null) {
            map.put("isLogin", 1);
            map.put("needTradePasswd", isNeedTradePassword(request));

            Fvirtualwallet virtualwallet = frontUserService.findVirtualWalletNative(user.getFid(), symbol);

            // 这里原来是把用户ID当成钱包ID去拿钱包，但是经查实，数据对不上，改成通过钱包ID去拿钱包
            Fwallet wallet = frontUserService.findFwalletByIdNative(user.getFwallet().getFid());

            map.put("rmbtotal", wallet.getFtotalRmb());
            map.put("rmbfrozen", wallet.getFfrozenRmb());
            map.put("virtotal", virtualwallet.getFtotal());
            map.put("virfrozen", virtualwallet.getFfrozen());

            List entrustList = frontTradeService.getFentrustHistory(user.getFid(), symbol, 0, 5);

            List entrustListLog = frontTradeService.findFentrustHistory(user.getFid(), symbol, 0, 5);

//            List entrustList = new ArrayList<>();
//            List entrustListLog = new ArrayList<>();
//
//            for (Object fentrust : fentrusts) {
//                entrustList.add(fentrust);
//            }
//
//            for (Object fentrust : fentrustsLog) {
//                entrustListLog.add(fentrust);
//            }

            map.put("entrustList", entrustList);
            map.put("entrustListLog", entrustListLog);
        } else {
            map.put("isLogin", 0);
        }

        // 返回sessionid，用来做websocket权限验证
        map.put("token", request.getSession().getId());

        return map;
    }

    /**
     * 委托/成交数据
     *
     * @param symbol
     * @param deep
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/marketRefresh")
    public Object marketRefresh(@RequestParam(required = false, defaultValue = "0") int symbol, @RequestParam(required = false, defaultValue = "4") int deep) throws Exception {

        Map<String, Object> map = new HashMap<>();

        Object[] successEntrusts = this.realTimeDataService.getEntrustSuccessMap(symbol).toArray();
        Object buyEntrusts = JSONArray.parse(this.realTimeDataService.getBuyDepthMap(symbol, deep));
        Object sellEntrusts = JSONArray.parse(this.realTimeDataService.getSellDepthMap(symbol, deep));

        List<List<String>> recentDealList = toStringList2(successEntrusts);

        map.put("buyDepthList", buyEntrusts);
        map.put("sellDepthList", sellEntrusts);
        map.put("recentDealList", recentDealList);
        map.put("symbol", symbol);

        return map;
    }

    @ResponseBody
    @RequestMapping("/depthData")
    public Object depthData(@RequestParam(required = false, defaultValue = "0") int symbol) {
        return realTimeDataService.getMarketJSON(symbol);
    }

    private int checkVirtualCoinId(int symbol) {
        if (symbol == 0) {
            return symbol;
        }
        LatestDealData data = realTimeDataService.getLatestDealData(symbol);
        return data.getFid();
    }

    private List<List<String>> toStringList2(Object[] successEntrusts) {
        List<List<String>> recentDealList = new ArrayList<List<String>>();
        for (int i = 0; i < successEntrusts.length && i <= 50; i++) {
            FentrustlogData fentrust = (FentrustlogData) successEntrusts[i];
            List<String> itemList = new ArrayList<String>();
            itemList.add(String.valueOf(fentrust.getFprize()));
            itemList.add(String.valueOf(fentrust.getFcount()));
            itemList.add(String.valueOf(new SimpleDateFormat("MM-dd HH:mm:ss").format(fentrust.getFcreateTime())));
            itemList.add(String.valueOf(fentrust.getfEntrustType() + 1));
            recentDealList.add(itemList);
        }
        return recentDealList;
    }

    private List<List<String>> toStringList(@RequestParam(required = false, defaultValue = "4") int deep, List<FentrustData> list) {
        List<List<String>> stringList = new ArrayList<List<String>>();
        for (int i = 0; i < list.size(); i++) {
            FentrustData fentrustData = list.get(i);
            List<String> itemList = new ArrayList<String>();
            String format = DeepMergeUtil.deepFormat2(deep);
            itemList.add(StringUtils.doubleToString(fentrustData.getFprize(), format));
            itemList.add(String.valueOf(fentrustData.getFleftCount()));
//            itemList.add(String.valueOf(fentrustData.getFleftCount() * fentrustData.getFprize()));
            itemList.add(String.valueOf(fentrustData.getFamount()));
            stringList.add(itemList);
        }
        return stringList;
    }

    public boolean isNeedTradePassword(HttpServletRequest request) {
        Fuser user = getSessionUser(request);
        if (user == null) return true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String key = user.getFid() + "trade";
        Object obj = request.getSession().getAttribute(key);

        if (obj == null) {
            return true;
        } else {
            try {
                double hour = Double.valueOf(this.systemArgsService.getValue("tradePasswordHour"));
                double lastHour = Utils.getDouble((sdf.parse(obj.toString()).getTime() - new Date().getTime()) / 1000 / 60 / 60, 2);
                if (lastHour >= hour) {
                    request.getSession().removeAttribute(key);
                    return true;
                } else {
                    return false;
                }
            } catch (ParseException e) {
                return false;
            }
        }
    }

    public void setNoNeedPassword(HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String key = getSessionUser(request).getFid() + "trade";
        request.getSession().setAttribute(key, sdf.format(new Date()));
    }

    @RequestMapping("/news")
    public ModelAndView dynamic(
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "1") int symbol
    ){
        ModelAndView map = new ModelAndView();
        int total = frontOthersService.countArticleByCoinId(symbol);
        List<Farticle> list = frontOthersService.findArticleByCoinId(symbol, (currentPage - 1) * NUMBER_PER_PAGE, NUMBER_PER_PAGE);
        Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(symbol);
        map.addObject("pageNow", currentPage);
        map.addObject("total", total);
        map.addObject("pageSize", NUMBER_PER_PAGE);
        map.addObject("list", list);
        map.addObject("fname", fvirtualcointype.getFname());
        map.addObject("symbol", symbol);
        map.setViewName("market/news");
        return map;
    }

}
