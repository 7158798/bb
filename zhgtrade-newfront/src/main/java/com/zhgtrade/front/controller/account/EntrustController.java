package com.zhgtrade.front.controller.account;

import com.ruizton.main.Enum.EntrustStatusEnum;
import com.ruizton.main.model.Fentrust;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.front.FrontTradeService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.util.Constants;
import com.ruizton.util.DateUtils;
import com.ruizton.util.StringUtils;
import com.zhgtrade.front.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * DESC:
 * 委托管理
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp(xxly68@qq.com)
 * Date： 2016-05-09 16:11
 */
@Controller
@RequestMapping("/account")
public class EntrustController extends BaseController {
    @Autowired
    private FrontTradeService frontTradeService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;

    /**
     * 委托列表
     *
     * @param request
     * @param symbol
     * @param status
     * @param page
     * @return
     */
    @RequestMapping("/entrusts")
    public String entrustList(HttpServletRequest request, Map<String, Object> map,
                              @RequestParam(value = "symbol", required = false)Integer symbol,
                              @RequestParam(value = "type", required = false, defaultValue = "-1")int type,
                              @RequestParam(value = "startDate", required = false)String startDate,
                              @RequestParam(value = "endDate", required = false)String endDate,
                              @RequestParam(value = "status", required = false, defaultValue = "-1")int status,
                              @RequestParam(value = "total", required = false, defaultValue = "0")int total,
                              @RequestParam(value = "currentPage", required = false, defaultValue = "1")int page){
        // 虚拟币列表
        List<Fvirtualcointype> coins = frontVirtualCoinService.list();
        map.put("coins", coins);

        Fvirtualcointype curCoin = null;
        if(null == symbol){
            curCoin = frontVirtualCoinService.findFirstFirtualCoin();
        }else{
            curCoin = frontVirtualCoinService.findFvirtualCoinById(symbol);
        }
        if(null != curCoin){
            // 委托列表
            symbol = curCoin.getFid();
            Integer[] types = -1 == type ? null : new Integer[]{type};
            Integer[] statuses = -1 == status ? null : new Integer[]{status};
            Fuser fuser = getSessionUser(request);
            Date start = StringUtils.hasText(startDate) ? DateUtils.formatDate(startDate) : null;
            Date end = StringUtils.hasText(endDate) ? DateUtils.formatDate(endDate) : null;
            List<Fentrust> entrusts = frontTradeService.findSuccessHistory(fuser.getFid(), symbol, type, null, null, -1, 20);
            Map<Integer, Object> avgPriceMap = new HashMap<>(entrusts.size());
            for(Fentrust fentrust : entrusts){
                if(EntrustStatusEnum.AllDeal == fentrust.getFstatus() || EntrustStatusEnum.PartDeal == fentrust.getFstatus()){
                    avgPriceMap.put(fentrust.getFid(), frontTradeService.avgSuccessPrice(fentrust.getFid()));
                }
            }
            if(0 == total){
                total = frontTradeService.countFentrustHistory(fuser.getFid(), curCoin.getFid(), types, statuses, start, end);
            }
            map.put("avgs", avgPriceMap);
            map.put("entrusts", entrusts);

            map.put("pageSize", Constants.PAGE_ITEM_COUNT_20);
            map.put("pageNow", page);
            map.put("total", total);
        }else{
            map.put("entrusts", Collections.emptyList());
        }

        map.put("type", type);
        map.put("symbol", symbol);
        map.put("status", status);
        return "account/account_entrust";
    }

}
