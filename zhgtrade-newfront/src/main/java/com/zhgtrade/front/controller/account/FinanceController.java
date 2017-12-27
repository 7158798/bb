package com.zhgtrade.front.controller.account;

import com.ruizton.main.Enum.*;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.code.AuthCode;
import com.ruizton.main.code.Captcha;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.comm.ValidateMap;
import com.ruizton.main.dto.LatestDealData;
import com.ruizton.main.model.*;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.service.admin.CapitaloperationService;
import com.ruizton.main.service.admin.SystemArgsService;
import com.ruizton.main.service.front.*;
import com.ruizton.main.service.winthdraw.FbankinfoWinthdrawService;
import com.ruizton.util.ConstantKeys;
import com.ruizton.util.Constants;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import com.zhgtrade.front.controller.BaseController;
import com.zhguser.service.User;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin(1186270005@qq.com)
 * Date： 2016-05-07 13:38
 */
@Controller
@RequestMapping("/account")
public class FinanceController extends BaseController {
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontBankInfoService frontBankInfoService;
    @Autowired
    private FrontAccountService frontAccountService;
    @Autowired
    private SystemArgsService systemArgsService;
    @Autowired
    private FrontRegionConfService frontRegionConfService;
    @Autowired
    private FbankinfoWinthdrawService fbankinfoWinthdrawService;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private ValidateMap messageValidateMap;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private RealTimeDataService realTimeDataService;
    @Autowired
    private WalletTransferService walletTransferService;

    private Comparator<Fvirtualwallet> comparator = (o1, o2) -> {
        //0总是在非0的后面
        LatestDealData f1 = realTimeDataService.getLatestDealData(o1.getFvirtualcointype().getFid());
        LatestDealData f2 = realTimeDataService.getLatestDealData(o2.getFvirtualcointype().getFid());
        if(f1.getTotalOrder() == 0 && f2.getTotalOrder() != 0){
            return 1;
        }else if(f1.getTotalOrder() != 0 && f2.getTotalOrder() == 0){
            return -1;
        } else {
            return Integer.valueOf(f1.getTotalOrder()).compareTo(f2.getTotalOrder());
        }
    };


    /**
     * 个人财务
     *
     * @param map
     * @return
     */
    @RequestMapping("/fund")
    public String fundAccount(Map<String, Object> map, HttpServletRequest request) {
        Fuser fuser = getSessionUser(request);
        // 现金
        Fwallet fwallet = frontUserService.findFwalletById(fuser.getFwallet().getFid());
        map.put("wallet", fwallet);
        // 虚拟币资金
        Map<Integer, Fvirtualwallet> coinWallet = frontUserService.findVirtualWallet(fuser.getFid());
        List<Fvirtualwallet> wallets = new ArrayList<>(coinWallet.values());
        Collections.sort(wallets, comparator);

        map.put("coinWallet", wallets);
        map.put("staticUrl", getStaticServer());
        return "account/account_finance";
    }

    /**
     * 人民币充值
     *
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/chargermb", method = RequestMethod.GET)
    public String chargeRMB(Map<String, Object> map, HttpServletRequest request,
                            @RequestParam(value = "type", required = false, defaultValue = "0") int type,
                            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int page) throws Exception {
        //系统银行账号
        List<Systembankinfo> systembankinfos = this.frontBankInfoService.findAllSystemBankInfo();

        // 金额尾数
        map.put("random", new Random().nextInt(80) + 11);
        map.put("bankInfo", systembankinfos);
        map.put("banks", EpayBankEnum.banks);
        map.put("type", type);

        // 充值记录
        Fuser fuser = this.getSessionUser(request);
        Map<String, Object> param = new HashMap<>();
        param.put("fuser.fid", fuser.getFid());
        param.put("ftype", CapitalOperationTypeEnum.RMB_IN);

        if(3 == type){
            // 转账记录
            int totalCount = walletTransferService.count(fuser.getFid(), null, MoneyType.RMB.getIndex(), null);
            List<WalletTransfer> lines = walletTransferService.find(fuser.getFid(), null, MoneyType.RMB.getIndex(), null, (page - 1) * Constants.PAGE_ITEM_COUNT_20, Constants.PAGE_ITEM_COUNT_20);
            map.put("lines", lines);
            map.put("total", totalCount);
        }else{
            List<Fcapitaloperation> list = this.frontAccountService.listCapitalList(page, Constants.PAGE_ITEM_COUNT_20, param, "fid desc");
            int totalCount = this.frontAccountService.countCapitalCount(param);
            map.put("lines", list);
            map.put("total", totalCount);
        }

        //最小充值金额
        double minRecharge = Double.parseDouble(constantMap.getString("minrechargecny").trim());
        map.put("minRecharge", minRecharge);
        map.put("maxRMB", constantMap.getDouble(ConstantKeys.MAX_CHARGE_RMB_IDENTIFY));

        // 分页信息
        map.put("pageSize", Constants.PAGE_ITEM_COUNT_20);
        map.put("pageNow", page);
        map.put("curTime", new Date());

        return "account/account_rechargecny";
    }

    /*
     * @增加一条充值记录
	 * */
    /*
	 * type:银行类型，暂时只支持4
	 * */

    @RequestMapping(value = "/chargermb", method = RequestMethod.POST)
    @ResponseBody
    public Object alipayManual(
            HttpServletRequest request,
            @RequestParam(required = true) double money,
            @RequestParam(required = true) String sbank,
            @RequestParam(required = true) String account,
            @RequestParam(required = true) String payee,
            @RequestParam(required = true) int bankType
    ) throws Exception {
        JSONObject jsonObject = new JSONObject();
        if (money < 0) {
            //非法
            return String.valueOf(-2);
        }

        //最小充值金额
        double min_double = Double.parseDouble(constantMap.getString("minrechargecny"));
        if (money < min_double) {
            //最小金额不嫩小于
            return String.valueOf(-1);
        }

        Fuser fuser = this.getSessionUser(request);
        fuser = frontUserService.findById(fuser.getFid());
        double maxRMB = constantMap.getDouble(ConstantKeys.MAX_CHARGE_RMB_IDENTIFY);
        if(money > maxRMB && 2 != fuser.getfIdentityStatus()){
            // 超过额度 需要手持身份认证
            return -30;
        }

        Systembankinfo systembankinfo = frontBankInfoService.findSystembankinfoById(bankType);
        /*List<Systembankinfo> systembankinfos = this.frontBankInfoService.findSystembankinfoByStatus(SystemBankInfoEnum.NORMAL_VALUE);
        Systembankinfo systembankinfo = null;
        if (systembankinfos != null && systembankinfos.size() > 0) {
            if (type == 6) {//支付宝
                for (Systembankinfo sys : systembankinfos) {
                    if (sys.getFbankName().indexOf("支付宝") != -1) {
                        systembankinfo = sys;
                    }
                }
            } else if (type == 4) {//银行
                for (Systembankinfo sys : systembankinfos) {
                    if (sys.getFbankName().indexOf("银行") != -1) {
                        systembankinfo = sys;
                    }
                }
            }
        }*/
        if (systembankinfo == null) {
            jsonObject.accumulate("isHasBankAccount", false);
            return jsonObject.toString();
        }

        Fcapitaloperation fcapitaloperation = new Fcapitaloperation();
        fcapitaloperation.setFamount(money);
        fcapitaloperation.setSystembankinfo(systembankinfo);
        fcapitaloperation.setFcreateTime(Utils.getTimestamp());
        fcapitaloperation.setfLastUpdateTime(Utils.getTimestamp());
        fcapitaloperation.setFtype(CapitalOperationTypeEnum.RMB_IN);
        fcapitaloperation.setFuser(fuser);
        fcapitaloperation.setFstatus(CapitalOperationInStatus.WaitForComing);
        fcapitaloperation.setfBank(sbank);
        fcapitaloperation.setfAccount(account);
        fcapitaloperation.setfPayee(payee);
        fcapitaloperation.setPayType(ChargePayTypeEnum.Default);
        fcapitaloperation.setfPhone(this.getSessionUser(request).getFtelephone());
        this.frontAccountService.addFcapitaloperation(fcapitaloperation);

        jsonObject.accumulate("money", String.valueOf(fcapitaloperation.getFamount()));
        jsonObject.accumulate("tradeId", fcapitaloperation.getFid());
        jsonObject.accumulate("userId", fcapitaloperation.getFuser().getFid());

        //bank info
        jsonObject.accumulate("fbankName", systembankinfo.getFbankName());
        jsonObject.accumulate("fownerName", systembankinfo.getFownerName());
        jsonObject.accumulate("fbankAddress", systembankinfo.getFbankAddress());
        jsonObject.accumulate("fbankNumber", systembankinfo.getFbankNumber());

//        String activityTime = constantMap.getString("activityTime");

        return jsonObject;
    }

    /**
     * 人民币提现
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/withdrawCny")
    public ModelAndView withdrawCny(HttpServletRequest request, @RequestParam(value = "currentPage", defaultValue = "1") int page) throws Exception {
        ModelAndView modelAndView = new ModelAndView();

        Fuser fuser = this.frontUserService.findById4WithDraw(getSessionUser(request).getFid());
        List<FbankinfoWithdraw> bankCards = this.frontUserService.findByFuser(fuser.getFid());

        List<String> bankTypes = BankTypeEnum.bankNames;

        //最大提现人民币
        double max_double = Double.parseDouble(constantMap.getString("maxwithdrawcny").trim());
        double min_double = Double.parseDouble(constantMap.getString("minwithdrawcny").trim());
        modelAndView.addObject("max_double", max_double);
        modelAndView.addObject("min_double", min_double);

        Map<String, Object> param = new HashMap<>();
        param.put("fuser.fid", fuser.getFid());
        param.put("ftype", CapitalOperationTypeEnum.RMB_OUT);
        List<Fcapitaloperation> fcapitaloperations = this.frontAccountService.findCapitalList(page, Constants.PAGE_ITEM_COUNT_10, param, " fcreateTime desc");

        List<Fregionconf> provinces = this.frontRegionConfService.findRegionByLevel(2);

        modelAndView.addObject("lines", fcapitaloperations);
        modelAndView.addObject("bankTypes", bankTypes);
        modelAndView.addObject("provinces", provinces);
        modelAndView.addObject("fuser", fuser);
        modelAndView.addObject("bankCards", bankCards);
        modelAndView.setViewName("account/account_withdrawcny");

        int totalCount = this.frontAccountService.findCapitalCount(param);

        // 分页信息
        modelAndView.addObject("pageSize", Constants.PAGE_ITEM_COUNT_10);
        modelAndView.addObject("pageNow", page);
        modelAndView.addObject("total", totalCount);
        return modelAndView;
    }

    /**
     * 删除提款银行卡
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/del_bank", method = RequestMethod.POST)
    public
    @ResponseBody
    Object deleteWinthdrawBankinfo(HttpServletRequest request, @RequestParam(value = "id", required = true) int id) {
        Fuser fuser = getSessionUser(request);
        FbankinfoWithdraw info = fbankinfoWinthdrawService.findById(id);
        if (null != info && info.getFuser().getFid() == fuser.getFid()) {
            fbankinfoWinthdrawService.delete(info);
            return "ok";
        }

        return "no";
    }

    /**
     * 添加银行卡
     *
     * @param account
     * @param openBankType
     * @param pId
     * @param cId
     * @param branch
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add_bank", method = RequestMethod.POST)
    public
    @ResponseBody
    String addWinthdrawBankinfo(HttpServletRequest request,
                                @RequestParam(required = true) String account,
                                @RequestParam(required = true) int openBankType,
                                @RequestParam(required = true) int pId,
                                @RequestParam(required = true) int cId,
                                @RequestParam(required = true) String branch) throws Exception {
        JSONObject jsonObject = new JSONObject();

        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid());
        boolean isGoogleBind = fuser.getFgoogleBind();
        boolean isTelephoneBind = fuser.isFisTelephoneBind();
        if (!isGoogleBind && !isTelephoneBind) {
            jsonObject.accumulate("resultCode", -13);//非正常提交
            return jsonObject.toString();
        }

		/*String ip = Utils.getIpAddr(getRequest()) ;
		int google_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE) ;
		int tel_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE) ;

		if(fuser.getFgoogleBind()){
			if(google_limit<=0){
				jsonObject.accumulate("resultCode", -8) ;
				jsonObject.accumulate("errorNum", 0) ;
				return jsonObject.toString() ;
			}else{
				boolean googleAuth = GoogleAuth.auth(Long.parseLong(totpCode),fuser.getFgoogleAuthenticator()) ;

				if(!googleAuth){
					//谷歌驗證失敗
					this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE) ;
					jsonObject.accumulate("resultCode", -8) ;
					jsonObject.accumulate("errorNum", google_limit-1) ;
					return jsonObject.toString() ;
				}else{
					this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE) ;
				}
			}

		}

		if(fuser.isFisTelephoneBind()){
			if(tel_limit<=0){
				jsonObject.accumulate("resultCode", -9) ;
				jsonObject.accumulate("errorNum", 0) ;
				return jsonObject.toString() ;
			}else{
				if(!validateMessageCode(fuser, fuser.getFareaCode(), fuser.getFtelephone(), MessageTypeEnum.CNY_ACCOUNT_WITHDRAW, phoneCode)){
					//手機驗證錯誤
					this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE) ;
					jsonObject.accumulate("resultCode", -9) ;
					jsonObject.accumulate("errorNum", tel_limit-1) ;
					return jsonObject.toString() ;
				}else{
					this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE) ;
				}
			}

		}*/

        List<FbankinfoWithdraw> bankCards = this.frontUserService.findByFuser(fuser.getFid());
        if(bankCards.stream().filter(e -> e.isInit()).count() >= 3){
            jsonObject.accumulate("resultCode", -1);
            return jsonObject.toString();
        }

        //成功
        try {
            FbankinfoWithdraw fbankinfoWithdraw = new FbankinfoWithdraw();
            fbankinfoWithdraw.setFbankNumber(account);
            fbankinfoWithdraw.setFbankType(openBankType);
            fbankinfoWithdraw.setFcreateTime(Utils.getTimestamp());
            fbankinfoWithdraw.setFname(BankTypeEnum.getEnumString(openBankType));
            fbankinfoWithdraw.setFstatus(BankInfoStatusEnum.NORMAL_VALUE);
            fbankinfoWithdraw.setInit(true);
            String fprovince = this.frontRegionConfService.findRegionById(pId).getName();
            String fcity = this.frontRegionConfService.findRegionById(cId).getName();
            fbankinfoWithdraw.setFprovince(fprovince);
            fbankinfoWithdraw.setFcity(fcity);
            fbankinfoWithdraw.setFbranch(branch);
            fbankinfoWithdraw.setFuser(fuser);

            this.frontUserService.updateBankInfoWithdraw(fbankinfoWithdraw);
            jsonObject.accumulate("resultCode", 0);
        } catch (Exception e) {
            jsonObject.accumulate("resultCode", -13);
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/withdrawAmountBlur")
    public String withdrawAmountBlur(HttpServletRequest request,
                                     @RequestParam(required = true) double withdrawAmount
    ) throws Exception {
        //最大提现人民币
        double max_double = Double.parseDouble(this.systemArgsService.getValue("maxwithdrawcny").trim());
        double min_double = Double.parseDouble(this.systemArgsService.getValue("minwithdrawcny").trim());

        Fwallet fwallet = this.frontUserService.findFwalletById(getSessionUser(request).getFwallet().getFid());
        double total = fwallet.getFtotalRmb();
        if (total < withdrawAmount) {
            return String.valueOf(-5);
        }

        //不能小于最小
        if (withdrawAmount < min_double) {
            return String.valueOf(-3);
        }
        //不能大于指定值
        if (withdrawAmount > max_double) {
            return String.valueOf(-200);
        }

        return String.valueOf(0);
    }

    /**
     * 提现
     *
     * @param request
     * @param tradePwd
     * @param withdrawBalance
     * @param phoneCode
     * @param totpCode
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/withdrawCnySubmit")
    public String withdrawCnySubmit(HttpServletRequest request,
                                    @RequestParam(required = false, defaultValue = "0") String tradePwd,
                                    @RequestParam(required = true) double withdrawBalance,
                                    @RequestParam(required = false, defaultValue = "0") String phoneCode,
                                    @RequestParam(required = true) int cardId,
                                    @RequestParam(required = false, defaultValue = "0") String totpCode
    ) throws Exception {
        JSONObject jsonObject = new JSONObject();
        //最大提现人民币
        double max_double = Double.parseDouble(this.systemArgsService.getValue("maxwithdrawcny").trim());
        double min_double = Double.parseDouble(this.systemArgsService.getValue("minwithdrawcny").trim());

        if (withdrawBalance < min_double) {
            //提现金额不能小于10
            jsonObject.accumulate("code", 101);
            return jsonObject.toString();
        }

        if (withdrawBalance > max_double) {
            //提现金额不能大于指定值
            jsonObject.accumulate("code", 102);
            return jsonObject.toString();
        }

        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid());
        if (fuser.getFwallet().getFtotalRmb() < withdrawBalance) {
            //资金不足
            jsonObject.accumulate("code", 103);
            return jsonObject.toString();
        }

        FbankinfoWithdraw fbankinfoWithdraw = this.frontUserService.findFbankinfoWithdrawByFid(cardId);
        if (!fbankinfoWithdraw.isInit()) {
            jsonObject.accumulate("code", 104);
            return jsonObject.toString();
        }

        if (!Utils.MD5(tradePwd).equals(fuser.getFtradePassword())) {
            //交易密码密码错误
            jsonObject.accumulate("code", 105);
            return jsonObject.toString();
        }

//        double maxRMB = constantMap.getDouble(ConstantKeys.MAX_DRAW_RMB_IDENTIFY);
        if(2 != fuser.getfIdentityStatus()){
            // 超过额度 需要手持身份认证
            jsonObject.accumulate("code", 106);
            return jsonObject.toString();
        }

        int time = this.frontAccountService.getTodayCnyWithdrawTimes(fuser);
        String times = constantMap.getString(ConstantKeys.DAY_DRAW_RMB_TIMES);
        if (StringUtils.hasText(times) && time >= Integer.valueOf(times)) {
            jsonObject.accumulate("code", 107);
            jsonObject.accumulate("count", times);
            return jsonObject.toString();
        }

//		//是否有借款
//		Map allMap = this.lendEntrustService.getNetInfo(fuser, CnyOrCoinEnum.CNY);
//		double totalApply = Double.valueOf(allMap.get("totalApply").toString());
//		double totalBorrow = Double.valueOf(allMap.get("totalBorrow").toString());
//		if(totalApply >0){
//			jsonObject.accumulate("resultCode", -30) ;
//			return jsonObject.toString() ;
//		}else if(totalBorrow >0){
//			jsonObject.accumulate("resultCode", -31) ;
//			return jsonObject.toString() ;
//		}

        // 验证码匹配
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        if(null == authCode){
            jsonObject.accumulate("code", 108);
            return jsonObject.toString();
        }
        int _limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), authCode.getActionType());
        if (_limit <= 0) {
            jsonObject.accumulate("code", 110);
            jsonObject.accumulate("leftCount", 0);
            return jsonObject.toString();
        } else {
            if (null == authCode || !authCode.isEnabled(fuser.getFtelephone(), phoneCode) || (CountLimitTypeEnum.TELEPHONE != authCode.getActionType() && CountLimitTypeEnum.VOICE_CAPTCHA != authCode.getActionType())) {
                jsonObject.accumulate("code", 111);
                jsonObject.accumulate("leftCount", _limit - 1);
                this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), authCode.getActionType());
                return jsonObject.toString();
            } else {
                this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), authCode.getActionType());
            }
            request.getSession().removeAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        }

        boolean withdraw = false;
        try {
            withdraw = this.frontAccountService.updateWithdrawCNY(withdrawBalance, fuser, fbankinfoWithdraw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (withdraw) {
            jsonObject.accumulate("code", 200);
            this.messageValidateMap.removeMessageMap(MessageTypeEnum.getEnumString(MessageTypeEnum.CNY_TIXIAN));
        } else {
            jsonObject.accumulate("code", 112);
        }

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping("/cancelChargeCny")
    public String cancelRechargeCnySubmit(HttpServletRequest request,
            int id
    ) throws Exception{
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid()) ;
        Fcapitaloperation fcapitaloperation = this.frontAccountService.findCapitalOperationById(id) ;
        if(
                fcapitaloperation.getFuser().getFid() ==fuser.getFid()
                        &&fcapitaloperation.getFtype()==CapitalOperationTypeEnum.RMB_IN
                        &&(fcapitaloperation.getFstatus()==CapitalOperationInStatus.NoGiven||fcapitaloperation.getFstatus()==CapitalOperationInStatus.WaitForComing)
                ){
            fcapitaloperation.setFstatus(CapitalOperationInStatus.Invalidate) ;
            fcapitaloperation.setfLastUpdateTime(Utils.getTimestamp()) ;
            try {
                this.frontAccountService.updateCapitalOperation(fcapitaloperation) ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return String.valueOf(0) ;
    }

    @ResponseBody
    @RequestMapping("/cancelWithdrawCny")
    public String cancelWithdrawcny(HttpServletRequest request,
            int id
    ) throws Exception{
        Fcapitaloperation fcapitaloperation = this.frontAccountService.findCapitalOperationById(id) ;
        if(fcapitaloperation!=null
                &&fcapitaloperation.getFuser().getFid() ==getSessionUser(request).getFid()
                &&fcapitaloperation.getFtype()==CapitalOperationTypeEnum.RMB_OUT
                &&fcapitaloperation.getFstatus()==CapitalOperationOutStatus.WaitForOperation){
            try {
                this.frontAccountService.updateCancelWithdrawCny(fcapitaloperation, this.frontUserService.findById(getSessionUser(request).getFid())) ;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return String.valueOf(0) ;
    }

}
