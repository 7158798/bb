package com.zhgtrade.front.controller.account;

import com.ruizton.main.Enum.*;
import com.ruizton.main.comm.KeyValues;
import com.ruizton.main.model.Fdeduct;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.DeductService;
import com.ruizton.main.service.front.FrontAccountService;
import com.ruizton.main.service.front.FrontTradeService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.util.Constants;
import com.zhgtrade.front.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账单明细
 * <p/>
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin（1186270005@qq.com）
 * Date：
 */
@Controller
@RequestMapping("/account")
public class BillController extends BaseController {
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontAccountService frontAccountService;
    @Autowired
    private FrontTradeService frontTradeService;
    @Autowired
    private DeductService deductService;

    /**
     * 账单明细
     *
     * @param request
     * @param recordType
     * @param symbol
     * @param currentPage
     * @param type
     * @return
     * @throws Exception
     */
    @RequestMapping("/bills")
    public ModelAndView record(HttpServletRequest request,
                               @RequestParam(required = false, defaultValue = "1") int recordType,
                               @RequestParam(required = false, defaultValue = "0") int symbol,
                               @RequestParam(required = false, defaultValue = "1") int currentPage,
                               @RequestParam(required = false, defaultValue = "1") int type
    ) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);
        if (fvirtualcointype == null) {
            fvirtualcointype = this.frontVirtualCoinService.findFirstFirtualCoin();
            symbol = fvirtualcointype.getFid();
        }

        if (recordType > TradeRecordTypeEnum.BTC_SELL) {
            recordType = TradeRecordTypeEnum.BTC_SELL;
        }

        List<Fvirtualcointype> fvirtualcointypes = this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
        //过滤器
        List<KeyValues> filters = new ArrayList<KeyValues>();
        for (int i = 1; i <= TradeRecordTypeEnum.BTC_SELL; i++) {
            if (i == 1 || i == 2) {
                KeyValues keyValues = new KeyValues();
                String key = "/account/bills.html?recordType=" + i + "&symbol=0";
                String value = TradeRecordTypeEnum.getEnumString(i);
                keyValues.setKey(key);
                keyValues.setValue(value);
                filters.add(keyValues);
            } else {
                String key = "/account/bills.html?recordType=" + i + "&symbol=";
                for (int j = 0; j < fvirtualcointypes.size(); j++) {
                    String value = TradeRecordTypeEnum.getEnumString(i);
                    Fvirtualcointype vc = fvirtualcointypes.get(j);

                    if (i == TradeRecordTypeEnum.BTC_RECHARGE || i == TradeRecordTypeEnum.BTC_WITHDRAW) {
                        if (!vc.isFIsWithDraw()) {
                            continue;
                        }
                    }

                    value = vc.getfShortName() + value;
                    KeyValues keyValues = new KeyValues();
                    keyValues.setKey(key + vc.getFid());
                    keyValues.setValue(value);
                    filters.add(keyValues);
                }
            }
        }

        //内容
        List list = new ArrayList();
        Map<String, Object> param;
        int totalCount = 0;
        String pagin = "";
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid());
        switch (recordType) {
            case TradeRecordTypeEnum.CNY_RECHARGE:
                param = new HashMap<String, Object>();
                param.put("fuser.fid", fuser.getFid());
                param.put("ftype", CapitalOperationTypeEnum.RMB_IN);
                list = this.frontAccountService.findCapitalList(currentPage, Constants.TradeRecordPerPage, param, "fid desc");
                totalCount = this.frontAccountService.findCapitalCount(param);
                break;

            case TradeRecordTypeEnum.CNY_WITHDRAW:
                param = new HashMap<String, Object>();
                param.put("fuser.fid", fuser.getFid());
                param.put("ftype", CapitalOperationTypeEnum.RMB_OUT);
                list = this.frontAccountService.findCapitalList(currentPage, Constants.TradeRecordPerPage, param, "fid desc");
                totalCount = this.frontAccountService.findCapitalCount(param);

                break;
            case TradeRecordTypeEnum.BTC_RECHARGE:
                list =
                        this.frontVirtualCoinService.findFvirtualcaptualoperation(
                                fuser,
                                new int[]{VirtualCapitalOperationTypeEnum.COIN_IN},
                                null,
                                new Fvirtualcointype[]{fvirtualcointype},
                                "fid desc", (currentPage - 1) * Constants.TradeRecordPerPage, Constants.TradeRecordPerPage);
                totalCount =
                        this.frontVirtualCoinService.findFvirtualcaptualoperationCount(
                                fuser,
                                new int[]{VirtualCapitalOperationTypeEnum.COIN_IN},
                                null,
                                new Fvirtualcointype[]{fvirtualcointype},
                                "fid desc");

                break;
            case TradeRecordTypeEnum.BTC_WITHDRAW:
                list =
                        this.frontVirtualCoinService.findFvirtualcaptualoperation(
                                fuser,
                                new int[]{VirtualCapitalOperationTypeEnum.COIN_OUT},
                                null,
                                new Fvirtualcointype[]{fvirtualcointype},
                                "fid desc", (currentPage - 1) * Constants.TradeRecordPerPage, Constants.TradeRecordPerPage);
                totalCount =
                        this.frontVirtualCoinService.findFvirtualcaptualoperationCount(
                                fuser,
                                new int[]{VirtualCapitalOperationTypeEnum.COIN_OUT},
                                null,
                                new Fvirtualcointype[]{fvirtualcointype},
                                "fid desc");

                break;
            case TradeRecordTypeEnum.BTC_BUY:
                list = this.frontTradeService.findFentrustHistory(
                        fuser.getFid(),
                        fvirtualcointype.getFid(),
                        new int[]{EntrustTypeEnum.BUY},
                        (currentPage - 1) * Constants.TradeRecordPerPage,
                        Constants.TradeRecordPerPage,
                        " fid desc ",
                        null);
                totalCount = this.frontTradeService.findFentrustHistoryCount(
                        fuser.getFid(),
                        fvirtualcointype.getFid(),
                        new int[]{EntrustTypeEnum.BUY},
                        null);

                break;
            case TradeRecordTypeEnum.BTC_SELL:
                list = this.frontTradeService.findFentrustHistory(
                        fuser.getFid(),
                        fvirtualcointype.getFid(),
                        new int[]{EntrustTypeEnum.SELL},
                        (currentPage - 1) * Constants.TradeRecordPerPage,
                        Constants.TradeRecordPerPage,
                        " fid desc ",
                        null);
                totalCount = this.frontTradeService.findFentrustHistoryCount(
                        fuser.getFid(),
                        fvirtualcointype.getFid(),
                        new int[]{EntrustTypeEnum.SELL},
                        null);
                break;
        }

        pagin = this.generatePagin(totalCount / Constants.TradeRecordPerPage + ((totalCount % Constants.TradeRecordPerPage) == 0 ? 0 : 1), currentPage, "/account/bills.html?recordType=" + recordType + "&symbol=" + symbol + "&");
        modelAndView.addObject("type", type);
        modelAndView.addObject("list", list);
        modelAndView.addObject("pagin", pagin);
        modelAndView.addObject("recordType", recordType);
        modelAndView.addObject("symbol", symbol);
        modelAndView.addObject("filters", filters);
        modelAndView.addObject("fvirtualcointype", fvirtualcointype);
        if (recordType == 1 || recordType == 2) {
            modelAndView.addObject("select", TradeRecordTypeEnum.getEnumString(recordType));
        } else {
            modelAndView.addObject("select", fvirtualcointype.getfShortName() + TradeRecordTypeEnum.getEnumString(recordType));
        }
        modelAndView.setViewName("account/account_record");
        return modelAndView;
    }

    /**
     * 提成明细
     *
     * @param request
     * @param currentPage
     * @param type
     * @return
     * @throws Exception
     */
    @RequestMapping("/deduct")
    public ModelAndView deduct(HttpServletRequest request,
                               @RequestParam(required = false, defaultValue = "1") int currentPage,
                               @RequestParam(required = false, defaultValue = "2") int type
    ) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Fuser fuser = this.getSessionUser(request);
        String filter = "where fuser.fid=" + fuser.getFid() + " order by fcreatetime desc";
        int total = this.deductService.getAllCount("Fdeduct", filter);
        int totalPage = total / Constants.DeductPerPage + ((total % Constants.DeductPerPage) == 1 ? 1 : 0);
        List<Fdeduct> deducts = this.deductService.list((currentPage - 1) * Constants.DeductPerPage, Constants.DeductPerPage, filter, true);
        String pagin = super.generatePagin(totalPage, currentPage, "/account/deduct.html?");

        modelAndView.addObject("type", type);
        modelAndView.addObject("deducts", deducts);
        modelAndView.addObject("pagin", pagin);
        modelAndView.setViewName("account/account_deduct");
        return modelAndView;
    }

}
