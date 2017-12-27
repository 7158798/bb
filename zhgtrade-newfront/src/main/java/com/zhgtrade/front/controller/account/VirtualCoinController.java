package com.zhgtrade.front.controller.account;

import com.ruizton.main.Enum.CountLimitTypeEnum;
import com.ruizton.main.Enum.VirtualCapitalOperationOutStatusEnum;
import com.ruizton.main.Enum.VirtualCapitalOperationTypeEnum;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.code.AuthCode;
import com.ruizton.main.code.Captcha;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.dto.LatestDealData;
import com.ruizton.main.model.*;
import com.ruizton.main.service.admin.PoolService;
import com.ruizton.main.service.admin.SystemArgsService;
import com.ruizton.main.service.admin.VirtualWalletService;
import com.ruizton.main.service.front.FrontAccountService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.FrontValidateService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.util.CollectionUtils;
import com.ruizton.util.ConstantKeys;
import com.ruizton.util.Constants;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import com.zhgtrade.front.controller.BaseController;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DESC:
 * <p/>
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin(1186270005@qq.com)
 * Date： 2016-05-10 19:20
 */
@Controller
@RequestMapping("/account")
public class VirtualCoinController extends BaseController {
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private VirtualWalletService virtualWalletService;
    @Autowired
    private SystemArgsService systemArgsService;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private FrontAccountService frontAccountService;
    @Autowired
    private PoolService poolService;
    @Autowired
    private ConstantMap constantMap;
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
     * 虚拟币充值页面
     *
     * @param symbol
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/chargeBtc", method = RequestMethod.GET)
    public ModelAndView rechargeBtc(HttpServletRequest request,
                                    @RequestParam(required = false, defaultValue = "0") int sub,
                                    @RequestParam(required = false, defaultValue = "0") int symbol,
                                    @RequestParam(value = "currentPage", required = false, defaultValue = "1") int page) {
        ModelAndView modelAndView = new ModelAndView();
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid());
        Fvirtualcointype fvirtualcointype = symbol > 0 ? this.frontVirtualCoinService.findFvirtualCoinById(symbol) : null;

        List<Fvirtualcointype> fvirtualcointypes = this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
        fvirtualcointypes.sort(comparator);
        if(symbol == 0){
            for (Fvirtualcointype t : fvirtualcointypes) {
                if (t.isFIsWithDraw()) {
                    fvirtualcointype = t;
                    break;
                }
            }
        }

//        if (fvirtualcointype == null || fvirtualcointype.getFstatus() == VirtualCoinTypeStatusEnum.Abnormal) {
//            fvirtualcointype = this.frontVirtualCoinService.findFirstFirtualCoin_Wallet();
//        }

        if (fvirtualcointype == null || !fvirtualcointype.isFIsWithDraw()) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        // 虚拟钱包
        List<Fvirtualwallet> wallets = virtualWalletService.findByTwoProperty("fvirtualcointype.fid", fvirtualcointype.getFid(), "fuser.fid", fuser.getFid());
        if (!CollectionUtils.isEmpty(wallets)) {
            modelAndView.addObject("wallet", wallets.get(0));
        }


//        List<Fvirtualcointype> fvirtualcointypes = frontVirtualCoinService.listByTotalOrder();
        Fvirtualaddress fvirtualaddress = this.frontVirtualCoinService.findFvirtualaddress(fuser, fvirtualcointype);

        // 若没有地址 则分配地址
        if(null == fvirtualaddress || StringUtils.isEmpty(fvirtualaddress.getFadderess())){
            try {
                fvirtualaddress = frontVirtualCoinService.updateAssignWalletAddress(fuser, fvirtualcointype);
            } catch (Exception e) {
                fvirtualaddress = this.frontVirtualCoinService.findFvirtualaddress(fuser, fvirtualcointype);
                e.printStackTrace();
            }
        }

        //充值记录
        List<Fvirtualcaptualoperation> fvirtualcaptualoperations =
                this.frontVirtualCoinService.findFvirtualcaptualoperation(
                        fuser,
                        new int[]{VirtualCapitalOperationTypeEnum.COIN_IN},
                        null,
                        new Fvirtualcointype[]{fvirtualcointype},
                        "fcreateTime desc", (page - 1) * Constants.PAGE_ITEM_COUNT_20, Constants.PAGE_ITEM_COUNT_20);

        // 分页
        int totalCount = this.frontVirtualCoinService.countFvirtualcaptualoperation(fuser, new int[]{VirtualCapitalOperationTypeEnum.COIN_IN}, null,
                new Fvirtualcointype[]{fvirtualcointype});
        modelAndView.addObject("pageSize", Constants.PAGE_ITEM_COUNT_20);
        modelAndView.addObject("pageNow", page);
        modelAndView.addObject("total", totalCount);

        modelAndView.addObject("fvirtualcaptualoperations", fvirtualcaptualoperations);
        modelAndView.addObject("fvirtualcointype", fvirtualcointype);
        modelAndView.addObject("fvirtualaddress", fvirtualaddress);
        modelAndView.addObject("fvirtualcointypes", fvirtualcointypes);
        modelAndView.setViewName("account/account_rechargebtc");
        return modelAndView;
    }

    /**
     * 提款页面
     *
     * @param symbol
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/withdrawBtc", method = RequestMethod.GET)
    public ModelAndView withdrawBtc(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") int symbol,
                                    @RequestParam(value = "currentPage", required = false, defaultValue = "1") int page) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid());
        Fvirtualcointype fvirtualcointype = symbol > 0 ? this.frontVirtualCoinService.findFvirtualCoinById(symbol) : null;

        List<Fvirtualcointype> fvirtualcointypes = this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
        fvirtualcointypes.sort(comparator);

        // 默认显示排序列表的第一个
        if (symbol == 0){
            for (Fvirtualcointype t : fvirtualcointypes) {
                if (t.isFIsWithDraw()) {
                    fvirtualcointype = t;
                    break;
                }
            }
        }

//        if (fvirtualcointype == null || fvirtualcointype.getFstatus() == VirtualCoinTypeStatusEnum.Abnormal) {
//            fvirtualcointype = this.frontVirtualCoinService.findFirstFirtualCoin_Wallet();
//        }

        double minbtcWithdraw = Double.parseDouble(constantMap.get("minbtcWithdraw").toString().trim());
        double maxbtcWithdraw = Double.parseDouble(constantMap.get("maxbtcWithdraw").toString().trim());

        modelAndView.addObject("minbtcWithdraw", minbtcWithdraw);
        modelAndView.addObject("maxbtcWithdraw", maxbtcWithdraw);

        if (fvirtualcointype == null || !fvirtualcointype.isFIsWithDraw()) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        // 虚拟钱包
        List<Fvirtualwallet> wallets = virtualWalletService.findByTwoProperty("fvirtualcointype.fid", fvirtualcointype.getFid(), "fuser.fid", fuser.getFid());
        if (!CollectionUtils.isEmpty(wallets)) {
            modelAndView.addObject("wallet", wallets.get(0));
        }

        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(), fvirtualcointype.getFid());
        FvirtualaddressWithdraw fvirtualaddressWithdraw = this.frontVirtualCoinService.findFvirtualaddressWithdraw(fuser, fvirtualcointype);

        //提现记录
        List<Fvirtualcaptualoperation> fvirtualcaptualoperations =
                this.frontVirtualCoinService.findFvirtualcaptualoperation(
                        fuser,
                        new int[]{VirtualCapitalOperationTypeEnum.COIN_OUT},
                        null,
                        new Fvirtualcointype[]{fvirtualcointype},
                        "fcreateTime desc", (page - 1) * Constants.PAGE_ITEM_COUNT_20, Constants.PAGE_ITEM_COUNT_20);

        // 分页
        int totalCount = this.frontVirtualCoinService.countFvirtualcaptualoperation(fuser, new int[]{VirtualCapitalOperationTypeEnum.COIN_OUT}, null,
                new Fvirtualcointype[]{fvirtualcointype});
        modelAndView.addObject("pageSize", Constants.PAGE_ITEM_COUNT_20);
        modelAndView.addObject("pageNow", page);
        modelAndView.addObject("total", totalCount);

        modelAndView.addObject("fvirtualcaptualoperations", fvirtualcaptualoperations);
        modelAndView.addObject("fvirtualwallet", fvirtualwallet);
        modelAndView.addObject("fuser", fuser);
        modelAndView.addObject("fvirtualaddressWithdraw", fvirtualaddressWithdraw);
        modelAndView.addObject("fvirtualcointype", fvirtualcointype);
        modelAndView.addObject("fvirtualcointypes", fvirtualcointypes);
        modelAndView.addObject("staticServer", getStaticServer());
        modelAndView.setViewName("account/account_withdrawbtc");
        return modelAndView;
    }

    /**
     * 修改提款地址
     *
     * @param request
     * @param phoneCode
     * @param totpCode
     * @param symbol
     * @param withdrawAddr
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/modifyWithdrawBtcAddr")
    public Object modifyWithdrawBtcAddr(HttpServletRequest request,
                                        @RequestParam(required=true,defaultValue="0")String phoneCode,
                                        @RequestParam(required=false,defaultValue="0")String totpCode,
                                        @RequestParam(required=true)int symbol,
                                        @RequestParam(required=true)String withdrawAddr
    ) throws Exception{
        Map map = new HashMap<>();
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid()) ;

        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol) ;
        if(fvirtualcointype==null || fvirtualcointype.getFstatus()==VirtualCoinTypeStatusEnum.Abnormal){
            map.put("code", 101) ;
            return map;
        }

        // 验证码匹配
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        if(null == authCode){
            map.put("code", 105);
            map.put("leftCount", 0);
            return map;
        }
        int _limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), authCode.getActionType());
        if (_limit <= 0) {
            map.put("code", 102);
            map.put("leftCount", 0);
            return map;
        } else {
            if (null == authCode || !authCode.isEnabled(fuser.getFtelephone(), phoneCode) || (CountLimitTypeEnum.TELEPHONE != authCode.getActionType() && CountLimitTypeEnum.VOICE_CAPTCHA != authCode.getActionType())) {
                map.put("code", 103);
                map.put("leftCount", _limit - 1);
                this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), authCode.getActionType());
                return map;
            } else {
                this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), authCode.getActionType());
            }
            request.getSession().removeAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        }

        // 向总要求
//        List list = frontVirtualCoinService.findFvirtualaddress(fvirtualcointype, withdrawAddr);
//        if(list.size() > 0){
//            map.put("code", 104) ;
//            return map;
//        }

        FvirtualaddressWithdraw fvirtualaddressWithdraw = this.frontVirtualCoinService.findFvirtualaddressWithdraw(fuser, fvirtualcointype) ;
        if(null == fvirtualaddressWithdraw){
            fvirtualaddressWithdraw = new FvirtualaddressWithdraw(fvirtualcointype, withdrawAddr, fuser, Utils.getTimestamp());
        }else{
            fvirtualaddressWithdraw.setFadderess(withdrawAddr) ;
        }
        fvirtualaddressWithdraw.setInit(true) ;
        try {
            this.frontVirtualCoinService.updateFvirtualaddressWithdraw(fvirtualaddressWithdraw) ;
            map.put("code", 200) ;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    @ResponseBody
    @RequestMapping(value="/withdrawBtcAmountBlur")
    public String withdrawBtcAmountBlur(HttpServletRequest request,
                                        @RequestParam(required=true)int symbol,
                                        @RequestParam(required=true)double withdrawAmount
    ) throws Exception{

        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol) ;
        if(fvirtualcointype==null ){
            return String.valueOf(-1) ;
        }

        if(withdrawAmount<0.1f){
            return String.valueOf(-3) ;
        }

        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid() ) ;
        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(), fvirtualcointype.getFid()) ;
        if(fvirtualwallet.getFtotal()<withdrawAmount){
            return String.valueOf(-5) ;
        }

        return String.valueOf(0) ;
    }

    /**
     * 提交提款申请
     *
     * @param withdrawAddr
     * @param withdrawAmount
     * @param tradePwd
     * @param totpCode
     * @param phoneCode
     * @param symbol
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/withdrawBtc", method = RequestMethod.POST)
    public Object withdrawBtcSubmit(HttpServletRequest request,
                                    @RequestParam(required=true)String withdrawAddr,
                                    @RequestParam(required=true)double withdrawAmount,
                                    @RequestParam(required=true)String tradePwd,
                                    @RequestParam(required=false,defaultValue="0")String totpCode,
                                    @RequestParam(required=false,defaultValue="0")String phoneCode,
                                    @RequestParam(required=true)int symbol
    ) throws Exception{
        Map map = new HashMap<>();
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid()) ;
        //是否认证手持身份证照片
        if(fuser.getfIdentityStatus() != 2){
            map.put("code", "111");
            return map;
        }
        if (!Utils.MD5(tradePwd).equals(fuser.getFtradePassword())) {
            //交易密码密码错误
            map.put("code", 112);
            return map;
        }
        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol) ;
        if(fvirtualcointype==null || !fvirtualcointype.isFIsWithDraw() || fvirtualcointype.getFstatus()==VirtualCoinTypeStatusEnum.Abnormal){
            map.put("code", 101);
            return map;
        }
        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(), fvirtualcointype.getFid()) ;
        FvirtualaddressWithdraw fvirtualaddressWithdraw = this.frontVirtualCoinService.findFvirtualaddressWithdraw(fuser, fvirtualcointype) ;

        Double minbtcWithdraw = Double.parseDouble(this.systemArgsService.getValue("minbtcWithdraw").trim()) ;
        Double maxbtcWithdraw = Double.parseDouble(this.systemArgsService.getValue("maxbtcWithdraw").trim()) ;

        //最少提现0.1
        if(withdrawAmount<minbtcWithdraw){
            map.put("code", 102) ;
            map.put("minbtcWithdraw",minbtcWithdraw) ;
            return map;
        }
        if(withdrawAmount>maxbtcWithdraw){
            map.put("code", 103) ;
            map.put("maxbtcWithdraw",maxbtcWithdraw) ;
            return map;
        }

        //余额不足
        if(fvirtualwallet.getFtotal()<withdrawAmount){
            map.put("code", 104) ;
            return map;
        }

        int time = this.frontAccountService.getTodayVirtualCoinWithdrawTimes(fuser) ;
        String times = constantMap.getString(ConstantKeys.DAY_DRAW_COIN_TIMES);
        if(StringUtils.hasText(times) && time >= Integer.valueOf(times)){
            map.put("code", 105);
            map.put("count", times);
            return map;
        }

        // 验证码匹配
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        if(null == authCode){
            map.put("code", 110);
            map.put("leftCount", 0);
            return map;
        }
        int _limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), authCode.getActionType());
        if (_limit <= 0) {
            map.put("code", 106);
            map.put("leftCount", 0);
            return map;
        } else {
            if (null == authCode || !authCode.isEnabled(fuser.getFtelephone(), phoneCode) || (CountLimitTypeEnum.TELEPHONE != authCode.getActionType() && CountLimitTypeEnum.VOICE_CAPTCHA != authCode.getActionType())) {
                map.put("code", 107);
                map.put("leftCount", _limit - 1);
                this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), authCode.getActionType());
                return map;
            } else {
                this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), authCode.getActionType());
            }
            request.getSession().removeAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        }

        if(fvirtualaddressWithdraw.isInit()==false ||!fvirtualaddressWithdraw.getFadderess().equals(withdrawAddr)){
            map.put("code", 108) ;
            return map;
        }

        // 向总要求
//        List list = frontVirtualCoinService.findFvirtualaddress(fvirtualcointype, fvirtualaddressWithdraw.getFadderess());
//        if(list.size() > 0){
//            map.put("code", 109) ;
//            return map;
//        }

        try{
            this.frontVirtualCoinService.updateWithdrawBtc(fvirtualaddressWithdraw,fvirtualcointype, fvirtualwallet, withdrawAmount, fuser) ;
            map.put("code", 200) ;
        }catch(Exception e){
            e.printStackTrace() ;
        }

        return map;
    }

    @ResponseBody
    @RequestMapping("/cancelWithdrawBtc")
    public String cancelWithdrawBtc(HttpServletRequest request,
                                    @RequestParam(required=true)int id
    ) throws Exception{
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid()) ;
        Fvirtualcaptualoperation fvirtualcaptualoperation = this.frontVirtualCoinService.findFvirtualcaptualoperationById(id) ;
        if(fvirtualcaptualoperation!=null
                &&fvirtualcaptualoperation.getFuser().getFid() ==fuser.getFid()
                &&fvirtualcaptualoperation.getFtype()==VirtualCapitalOperationTypeEnum.COIN_OUT
                &&fvirtualcaptualoperation.getFstatus()== VirtualCapitalOperationOutStatusEnum.WaitForOperation
                ){

            try{
                this.frontAccountService.updateCancelWithdrawBtc(fvirtualcaptualoperation, fuser) ;
            }catch(Exception e){
                e.printStackTrace() ;
            }

        }
        return String.valueOf(0) ;
    }

}
