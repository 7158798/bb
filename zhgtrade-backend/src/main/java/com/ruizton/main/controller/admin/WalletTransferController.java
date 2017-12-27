package com.ruizton.main.controller.admin;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.Enum.MoneyType;
import com.ruizton.main.Enum.WalletTransferStatus;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.WalletTransfer;
import com.ruizton.main.service.admin.UserService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.main.service.front.WalletTransferService;
import com.ruizton.util.DateUtils;
import com.ruizton.util.StringUtils;
import com.ruizton.util.XlsExport;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunpeng on 2016/11/29.
 */
@Controller
@RequestMapping("/ssadmin")
public class WalletTransferController {
    private static final int NUM_PER_PAGE = 40;
    @Autowired
    private WalletTransferService walletTransferService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private UserService userService;
    @Autowired
    private RealTimeDataService realTimeDataService;

    @RequestMapping("/walletTransferList")
    @RequiresPermissions("ssadmin/walletTransferList.html")
    public ModelAndView list(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer coinId,
            @RequestParam(required = false) String active,
            @RequestParam(required = false) Short moneyType,
            @RequestParam(required = false) Short status,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false) String orderField,
            @RequestParam(required = false) String orderDirection
    ){
        Boolean bool;
        if(!StringUtils.hasText(active)){
            bool = null;
        }else{
            bool = Boolean.parseBoolean(active);
        }
        ModelAndView modelAndView = new ModelAndView();
        List<Fvirtualcointype> coins = virtualCoinService.findAll();
        Map<Integer, String> coinMap = new HashMap<>();
        for(Fvirtualcointype coin : coins){
            coinMap.put(coin.getFid(), coin.getFname());
        }
        coinMap.put(0, "人民币");
        modelAndView.setViewName("ssadmin/walletTransferList");
        List<WalletTransfer> list = walletTransferService.listForAdmin(userId, coinId, bool, moneyType, status, (pageNum - 1) * NUM_PER_PAGE, NUM_PER_PAGE, orderField, orderDirection);
        int totalCount = walletTransferService.countForAdmin(userId, coinId, bool, moneyType, status);

        Map map = new HashMap<>();
        list.forEach(e -> {
            if(!map.containsKey(e.getUserId())){
                Fuser fuser = userService.findById(e.getUserId());
                map.put(e.getUserId(), fuser);
            }
        });

        modelAndView.addObject("list", list);
        modelAndView.addObject("numPerPage", NUM_PER_PAGE);
        modelAndView.addObject("rel", "blockArticleTypeList");
        modelAndView.addObject("currentPage", pageNum);
        modelAndView.addObject("total", totalCount);
        modelAndView.addObject("coinMap", coinMap);
        modelAndView.addObject("coinId", coinId);
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("active", active);
        modelAndView.addObject("users", map);
        return modelAndView;
    }

    @RequestMapping(value = "/exportWalletTransferList", method = RequestMethod.GET)
    @RequiresPermissions("ssadmin/exportWalletTransferList.html")
    public void exportWalletTransferList(HttpServletResponse response,
                                         @RequestParam(required = false) Integer userId,
                                         @RequestParam(required = false) Integer coinId,
                                         @RequestParam(required = false) String active){

        Boolean bool;
        if(!StringUtils.hasText(active)){
            bool = null;
        }else{
            bool = Boolean.parseBoolean(active);
        }
        List<WalletTransfer> list = walletTransferService.listForAdmin(userId, coinId, bool, null, null, 0, 0, null, null);
        List<Fvirtualcointype> coins = virtualCoinService.findAll();
        Map<Integer, String> coinMap = new HashMap<>();
        for(Fvirtualcointype coin : coins){
            coinMap.put(coin.getFid(), coin.getFname());
        }
        coinMap.put(0, "人民币");

        XlsExport xls = new XlsExport();

        // 标题
        xls.createRow(0);
        xls.setCell(0, "用户ID");
        xls.setCell(1, "登录名");
        xls.setCell(2, "用户邮箱");
        xls.setCell(3, "用户手机号");
        xls.setCell(4, "交易种类");
        xls.setCell(5, "交易号");
        xls.setCell(6, "转入/转出");
        xls.setCell(7, "From");
        xls.setCell(8, "To");
        xls.setCell(9, "金额");
        xls.setCell(10, "状态");
        xls.setCell(11, "创建时间");
        xls.setCell(12, "更新时间");

        // 填入数据

        for(int i = 0; i < list.size(); i ++){
            WalletTransfer walletTransfer = list.get(i);

            Fuser fuser = userService.findById(walletTransfer.getUserId());

            xls.createRow(i + 1);
            xls.setCell(0, StringUtils.null2EmptyString(walletTransfer.getUserId()));
            xls.setCell(1, fuser.getFloginName());
            xls.setCell(2, fuser.getFemail());
            xls.setCell(3, fuser.getFtelephone());

            xls.setCell(4, StringUtils.null2EmptyString(coinMap.get(walletTransfer.getVirtualCoinId())));
            xls.setCell(5, StringUtils.null2EmptyString(walletTransfer.getTradeNo()));
            xls.setCell(6, StringUtils.null2EmptyString(walletTransfer.isActive() ? "转出" : "转入"));
            xls.setCell(7, StringUtils.null2EmptyString(walletTransfer.getFromSystem()));
            xls.setCell(8, StringUtils.null2EmptyString(walletTransfer.getToSystem()));
            double amount = walletTransfer.getAmount();
            if(walletTransfer.isActive()){
                amount = -amount;
            }
            xls.setCell(9, amount);
            xls.setCell(10, StringUtils.null2EmptyString(walletTransfer.getStatus().getName()));
            xls.setCell(11, StringUtils.null2EmptyString(walletTransfer.getCreateTime()));
            xls.setCell(12, StringUtils.null2EmptyString(walletTransfer.getUpdateTime()));
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("资金转移列表-", "utf-8") + format.format(new Date()) + ".xls");
            xls.exportXls(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转入资金统计
     *
     * @param startDate
     * @param endDate
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/transferInReport")
    @RequiresPermissions(value = {"ssadmin/transferInReport.html", "ssadmin/exportTransferInReport.html"}, logical = Logical.OR)
    public String transferInReport(@RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date startDate,
                                   @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date endDate,
                                   @RequestParam(required = false, defaultValue = "0")String coinType,
                                   @RequestParam(required = false)String url,
                                   ModelMap modelMap){
        if(null != endDate){
            endDate = DateUtils.getDateLastTime(endDate);
        }

        if(null == startDate && null == endDate){
            startDate = DateUtils.getMonthBefore();
            endDate = DateUtils.getDateLastTime(new Date());
        }

        MoneyType moneyType;
        Integer virtualCoinId = null;
        if("0".equals(coinType)){
            moneyType = MoneyType.RMB;
        }else{
            virtualCoinId = Integer.valueOf(coinType);
            moneyType = MoneyType.Virtual_Coin;
        }

        Object[] dataArr = this.walletTransferService.transferReport(startDate, endDate, moneyType, virtualCoinId, false, WalletTransferStatus.Success);

        if("ssadmin/excel/exportTransferInReport".equals(url)){
            modelMap.put("xAxis", dataArr[0]);
            modelMap.put("yAxis", dataArr[1]);
            if(null != virtualCoinId){
                modelMap.put("coin", realTimeDataService.getLatestDealData(virtualCoinId));
            }
            return url;
        }

        modelMap.put("xAxis", JSON.toJSONString(dataArr[0]));
        modelMap.put("yAxis", JSON.toJSONString(dataArr[1]));
        modelMap.put("startDate", startDate);
        modelMap.put("endDate", endDate);
        modelMap.put("coins", realTimeDataService.getLatestDealDataList());

        return "ssadmin/charts/transferInReport";
    }
}
