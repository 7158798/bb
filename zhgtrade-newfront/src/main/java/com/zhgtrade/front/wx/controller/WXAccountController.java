package com.zhgtrade.front.wx.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruizton.main.Enum.*;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.dto.LatestDealData;
import com.ruizton.main.model.*;
import com.ruizton.main.service.admin.VirtualWalletService;
import com.ruizton.main.service.front.FrontAccountService;
import com.ruizton.main.service.front.FrontBankInfoService;
import com.ruizton.main.service.front.FrontQuestionService;
import com.ruizton.main.service.front.FrontTradeService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.FrontValidateService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.main.service.front.ProjectCollectionService;
import com.ruizton.main.service.front.WalletTransferService;
import com.ruizton.util.CollectionUtils;
import com.ruizton.util.ConstantKeys;
import com.ruizton.util.Constants;
import com.ruizton.util.DateUtils;
import com.ruizton.util.FormatUtils;
import com.ruizton.util.HttpUtils;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import com.ruizton.util.ZhgUserSynUtils;
import com.zhgtrade.front.controller.ApiBaseController;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 财务中心
 *
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/11/2
 */
@RestController
@RequestMapping("/wx")
public class WXAccountController extends ApiBaseController {
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontQuestionService frontQuestionService;
    @Autowired
    private ProjectCollectionService projectCollectionService;
    @Autowired
    private RealTimeDataService realTimeDataService;
    @Autowired
    private FrontBankInfoService frontBankInfoService;
    @Autowired
    private FrontAccountService frontAccountService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private WalletTransferService walletTransferService;
    @Autowired
    private VirtualWalletService virtualWalletService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private FrontTradeService frontTradeService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Logger logger = LoggerFactory.getLogger(WXAccountController.class);

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
     * 个人财务
     *
     * @param session
     * @return
     */
    @RequestMapping("/personalAssets")
    public Object personalAssets(HttpSession session){
        Fuser fuser = getLoginUser(session);
        // 注意啦，这里是钱包ID，不是用户ID，钱包ID ！= 用户ID
        Fwallet fwallet = frontUserService.findFwalletById(fuser.getFwallet().getFid());

        Map retMap = new HashMap<>();
        retMap.put("ftotalRmb", FormatUtils.formatCNY(fwallet.getFtotalRmb()));
        retMap.put("ffrozenRmb", FormatUtils.formatCNY(fwallet.getFfrozenRmb()));


        Map<Integer, Fvirtualwallet> map = frontUserService.findVirtualWallet(fuser.getFid());
        List vWallets = new ArrayList<>(map.size());
        map.values().stream()
                .filter((Fvirtualwallet e) -> VirtualCoinTypeStatusEnum.Normal == e.getFvirtualcointype().getFstatus())
                .sorted((e1, e2) -> {
                    Fvirtualcointype o1 = e1.getFvirtualcointype();
                    Fvirtualcointype o2 = e2.getFvirtualcointype();
                    if(o1.getTotalOrder() == 0 && o2.getTotalOrder() != 0){
                        return 1;
                    }else if(o1.getTotalOrder() != 0 && o2.getTotalOrder() == 0){
                        return -1;
                    } else {
                        return Integer.valueOf(o1.getTotalOrder()).compareTo(o2.getTotalOrder());
                    }
                })
                .forEach(e -> {
                    Map wallet = new HashMap();
                    Fvirtualcointype fvirtualcointype = e.getFvirtualcointype();
                    wallet.put("coinId", fvirtualcointype.getFid());
                    wallet.put("furl", fvirtualcointype.getFurl());
                    wallet.put("fname", fvirtualcointype.getFname());
                    wallet.put("fShortName", fvirtualcointype.getfShortName());
                    wallet.put("fisShare", fvirtualcointype.isFisShare());
                    wallet.put("FIsWithDraw", fvirtualcointype.isFIsWithDraw());

                    wallet.put("ftotal", FormatUtils.formatCoin(e.getFtotal()));
                    wallet.put("ffrozen", FormatUtils.formatCoin(e.getFfrozen()));
                    vWallets.add(wallet);
                });

        retMap.put("vWallets", vWallets);
        return forSuccessResult(retMap);
    }

    /**
     * 消息列表
     *
     * @param session
     * @param type
     * @param currentPage
     * @return
     */
    @RequestMapping("/messages")
    public Object messages(HttpSession session,
                           @RequestParam(required=false,defaultValue="1")int type,
                           @RequestParam(required=false,defaultValue="1")int currentPage){
        Fuser fuser = getLoginUser(session);

        Map<String, Object> param = new HashMap<String, Object>() ;
        param.put("freceiver.fid", fuser.getFid()) ;
        int totalCount = this.frontQuestionService.findFmessageByParamCount(param) ;

        int totalPage = totalCount/Constants.QuestionRecordPerPage +(totalCount%Constants.QuestionRecordPerPage==0?0:1) ;
        currentPage = currentPage<1?1:currentPage ;
        currentPage = currentPage>totalPage?totalCount:currentPage ;

        List<Fmessage> list = this.frontQuestionService.findFmessageByParam(param, (currentPage-1)*Constants.QuestionRecordPerPage, Constants.QuestionRecordPerPage, "fcreateTime desc") ;

        List msgList = new ArrayList<>(list.size());
        list.forEach(e -> {
            Map map = new HashMap();
            map.put("id", e.getFid());
            map.put("title", e.getFtitle());
            map.put("short_title", e.getFtitle_short());
            map.put("time", e.getFcreateTime());
            map.put("content", e.getFcontent());
            msgList.add(map);
        });

        return forSuccessResult(msgList, totalPage);
    }

    /**
     * 消息详情
     *
     * @param session
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/messageDetail")
    public Object messagedetail(HttpSession session, int id) throws Exception{
        JSONObject jsonObject = new JSONObject() ;

        Fmessage fmessage = this.frontQuestionService.findFmessageById(id) ;
        if(fmessage==null || fmessage.getFreceiver().getFid() != getLoginUser(session).getFid()){
            jsonObject.accumulate("result", false) ;
            return jsonObject.toString() ;
        }

        if(fmessage.getFstatus()== MessageStatusEnum.NOREAD_VALUE){
            fmessage.setFstatus(MessageStatusEnum.READ_VALUE) ;
            this.frontQuestionService.updateFmessage(fmessage) ;
        }

        jsonObject.accumulate("title", fmessage.getFtitle()) ;
        jsonObject.accumulate("content", fmessage.getFcontent()) ;
        return forSuccessResult(jsonObject);
    }

    /**
     * 个人信息
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/personalInfo")
    public Object personalInfo(HttpSession session){
        Fuser fuser = getLoginUser(session);
        if (fuser == null) {
            return forFailureResult(null, 401, "未登录");
        }
        fuser = frontUserService.findById(fuser.getFid());

        Map map = new HashMap<>();
        map.put("id", fuser.getFid());
        map.put("loginName", fuser.getFloginName());
        map.put("nickname", fuser.getFnickName());
        map.put("mobile", fuser.getFtelephone());
        map.put("email", fuser.getFemail());
        if(StringUtils.hasText(fuser.getHeadImgUrl())){
            map.put("headImg", fuser.getHeadImgUrl());
        }else{
            map.put("headImg", "static/images/default_head.jpg");
        }
        map.put("realName", fuser.getFrealName());
        map.put("identifyNo", fuser.getFidentityNo());
        map.put("identifyType", fuser.getFidentityType());
        map.put("hasPayPwd", StringUtils.hasText(fuser.getFtradePassword()));
        map.put("identifyStatus", fuser.getfIdentityStatus());
        if(fuser.getFhasRealValidate()){
            // 已通过
            map.put("auth_status", 2);
            map.put("date", DateUtils.formatDate(fuser.getFhasRealValidateTime()));
        }else if(fuser.getFpostRealValidate()){
            // 已提交
            map.put("auth_status", 1);
        }else{
            // 未提交
            map.put("auth_status", 0);
        }

        return forSuccessResult(map);
    }

    /**
     * 获取Session信息
     * @param session
     * @return
     */
    @RequestMapping(value = "/sessionInfo")
    public void sessionInfo(HttpSession session, HttpServletResponse resp) {
        Fuser fuser = getLoginUser(session);
        if (fuser != null) {
//            fuser.setFloginPassword(null);
//            fuser.setFtradePassword(null);
        }
        String value;
        try {
            value = objectMapper.writeValueAsString(fuser);
        } catch (JsonProcessingException e) {
            value = "";
            e.printStackTrace();
        }
        try {
            resp.setCharacterEncoding(SimpleMessageConverter.DEFAULT_CHARSET);
            resp.getWriter().write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param session
     * @param nickname
     * @return
     */
    @RequestMapping(value = "/updateNickname", method = RequestMethod.POST)
    public Object updateNickname(HttpSession session, String nickname){
        if(StringUtils.isEmpty(nickname)){
            return forFailureResult(nickname, 0, "");
        }
        if(nickname.length() > 10){
            return forFailureResult(null, -1, "昵称名称最长10位");
        }

        Fuser fuser = this.frontUserService.findById(getLoginUser(session).getFid()) ;
        fuser.setFnickName(nickname);
        frontUserService.updateFUser(fuser, session);

        return forSuccessResult(nickname);
    }

    /**
     * 币收藏
     *
     * @param session
     * @return
     */
    @RequestMapping("/coinCollect")
    public Object coinCollect(HttpSession session){
        Fuser fuser = getLoginUser(session);
        List<ProjectCollection> projectList = projectCollectionService.listByUser(fuser.getFid());
        List list = new ArrayList<>(projectList.size());
        projectList.forEach(e -> {
            LatestDealData lastestDealData = realTimeDataService.getLatestDealData(e.getCid());
            Map map = new HashMap();
            map.put("lastDealPrize", FormatUtils.formatCoin(lastestDealData.getLastDealPrize()));
            map.put("higestBuyPrize", FormatUtils.formatCoin(lastestDealData.getHigestBuyPrize()));
            map.put("lowestSellPrize", FormatUtils.formatCoin(lastestDealData.getLowestSellPrize()));
            map.put("lowestPrize24", FormatUtils.formatCoin(lastestDealData.getLowestPrize24()));
            map.put("highestPrize24", FormatUtils.formatCoin(lastestDealData.getHighestPrize24()));
            map.put("volumn", FormatUtils.formatCoinUnit(lastestDealData.getVolumn()));

            if(lastestDealData != null){
                map.put("fupanddown", lastestDealData.getFupanddown());
            }else{
                map.put("fupanddown", lastestDealData.getFupanddown());
            }

            map.put("id", e.getId());
            map.put("cid", e.getCid());
            map.put("fname", lastestDealData.getFname());
            map.put("fShortName", lastestDealData.getfShortName());
            map.put("furl", lastestDealData.getFurl());
            list.add(map);
        });

        return  forSuccessResult(list);
    }

    /**
     * 是否已收藏币种
     *
     * @param session
     * @param symbol
     * @return
     */
    @RequestMapping("/coinIsCollected")
    public Object coinIsCollected(HttpSession session, int symbol){
        Fuser fuser = getLoginUser(session);
        if(null == fuser){
            return forFailureResult(401);
        }

        return forSuccessResult(null != projectCollectionService.findByUserAndCoin(fuser.getFid(), symbol));
    }

    /**
     *
     *
     * @param session
     * @param newPwd
     * @param oldPwd
     * @return
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public Object updatePassword(HttpSession session, String newPwd, String oldPwd){
        Fuser fuser = getLoginUser(session);
        if(!fuser.getFloginPassword().equals(Utils.MD5(oldPwd))){
            // 旧密码不匹配
            return forFailureResult(101);
        }
        if(StringUtils.isEmpty(newPwd)){
            // 新密码为空
            return forFailureResult(102);
        }
        if(newPwd.length() < 6){
            return forFailureResult(103);
        }

        fuser = frontUserService.findById(fuser.getFid());
        fuser.setFloginPassword(Utils.MD5(newPwd));
        frontUserService.updateFUser(fuser, session);
        return forSuccessResult("ok");
    }

    /**
     *
     * @param session
     * @param newPwd
     * @param oldPwd
     * @return
     */
    @RequestMapping("/updateTradePassword")
    public Object updateTradePassword(HttpSession session, String newPwd, String oldPwd){
        Fuser fuser = getLoginUser(session);
        if(StringUtils.hasText(fuser.getFtradePassword()) && !fuser.getFtradePassword().equals(Utils.MD5(oldPwd))){
            // 旧密码不匹配
            return forFailureResult(101);
        }
        if(StringUtils.isEmpty(newPwd)){
            // 新密码为空
            return forFailureResult(102);
        }
        if(newPwd.length() < 6){
            return forFailureResult(103);
        }

        fuser = frontUserService.findById(fuser.getFid());
        fuser.setFtradePassword(Utils.MD5(newPwd));
        frontUserService.updateFUser(fuser, session);
        return forSuccessResult("ok");
    }

    /**
     * 提款银行卡
     *
     * @param session
     * @return
     */
    @RequestMapping("/bankCards")
    public Object bankCards(HttpSession session){
        Fuser fuser = getLoginUser(session);
        List<FbankinfoWithdraw> bankCards = this.frontUserService.findByFuser(fuser.getFid());
        List list = new ArrayList<>(bankCards.size());
        bankCards.stream().filter(e -> e.isInit()).forEach(e -> {
            Map map = new HashMap();
            map.put("id", e.getFid());
            map.put("bank_name", e.getFname());
            map.put("card_id", e.getFbankNumber());
            list.add(map);
        });
        return forSuccessResult(list);
    }

    /**
     * 添加银行卡
     *
     * @param session
     * @param bank
     * @param province
     * @param city
     * @param branch
     * @param cardId
     * @return
     */
    @RequestMapping("/addBankCard")
    public Object addBankCard(HttpSession session, String bank, String province, String city, String branch, String cardId){
        Fuser fuser = getLoginUser(session);

        List<FbankinfoWithdraw> bankCards = this.frontUserService.findByFuser(fuser.getFid());
        if(bankCards.stream().filter(e -> e.isInit()).count() >= 3){
            return forFailureResult(-1);
        }

        FbankinfoWithdraw fbankinfoWithdraw = new FbankinfoWithdraw();
        fbankinfoWithdraw.setFbankNumber(cardId);
        fbankinfoWithdraw.setFbankType(BankTypeEnum.bankNames.indexOf(bank) + 1);
        fbankinfoWithdraw.setFcreateTime(com.ruizton.util.Utils.getTimestamp());
        fbankinfoWithdraw.setFname(bank);
        fbankinfoWithdraw.setFstatus(BankInfoStatusEnum.NORMAL_VALUE);
        fbankinfoWithdraw.setInit(true);
        fbankinfoWithdraw.setFprovince(province);
        fbankinfoWithdraw.setFcity(city);
        fbankinfoWithdraw.setFbranch(branch);
        fbankinfoWithdraw.setFuser(fuser);

        this.frontUserService.updateBankInfoWithdraw(fbankinfoWithdraw);
        return forSuccessResult("ok");
    }

    /**
     *  重置密码
     *
     * @param request
     * @param password
     * @return
     */
    @RequestMapping(value = "/reset_pwd", method = RequestMethod.POST)
    public Object resetPassword(HttpServletRequest request,
                         @RequestParam(value = "password", required = true)String password){
        Map<String, Object> map = new HashMap<>(5);
        if(null == request.getSession().getAttribute(Constants.FIND_PASSWORD_USER)){
            // 不合理请求
            map.put("code", "-1");
            return map;
        }
        if(!StringUtils.hasText(password)){
            map.put("msg", "请输入您的新密码");
            return map;
        }
        Fuser fuser = (Fuser) request.getSession().getAttribute(Constants.FIND_PASSWORD_USER);
        fuser = frontUserService.findById(fuser.getFid());
        fuser.setFloginPassword(Utils.MD5(password));
        frontUserService.updateFuser(fuser);

        // 修改用户系统密码
        ZhgUserSynUtils.synUserPassword(fuser.getZhgOpenId(), password);

        request.getSession().removeAttribute(Constants.FIND_PASSWORD_USER);
        map.put("code", 200);
        return map;
    }

    /**
     * 人民币充值
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/rechageRMB", method = RequestMethod.GET)
    public Object rechageRMB(HttpSession session){
        Fuser fuser = getLoginUser(session);
        Map map = new HashMap();
        List<Systembankinfo> systembankinfos = this.frontBankInfoService.findAllSystemBankInfo();

        List list = new ArrayList<>(systembankinfos.size());
        systembankinfos.forEach(e -> {
            Map item = new HashMap();
            item.put("id", e.getFid());
            item.put("bankName", e.getFbankName());
            item.put("ownerName", e.getFownerName());
            list.add(item);
        });

        map.put("list", list);
        map.put("realName", fuser.getFrealName());
        map.put("minRecharge",constantMap.getDouble("minrechargecny"));
        map.put("maxRechargeAuth",constantMap.getDouble(ConstantKeys.MAX_CHARGE_RMB_IDENTIFY));
        return forSuccessResult(map);
    }

    /**
     * 转账
     */
    @RequestMapping("/transfer")
    public Object transfer(HttpSession session,
                           @RequestParam (required = false, defaultValue = "0") int symbol) {
        Fuser fuser = frontUserService.findById(getLoginUser(session).getFid());
        if(fuser == null){
            return forFailureResult(401);
        }
        Map<String, Object> retMap = new HashMap<String, Object>();
        List<Fvirtualcointype> fvirtualcointypes = this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
        for(int i=0; i < fvirtualcointypes.size(); i++){
            Fvirtualcointype type = fvirtualcointypes.get(i);
            if(!walletTransferService.coinEnableToTransfer(type.getFid())){
                i --;
                i %= fvirtualcointypes.size();
                fvirtualcointypes.remove(type);
            }
        }
        fvirtualcointypes.sort(comparator);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Fvirtualcointype coinType : fvirtualcointypes) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", coinType.getFid());
            map.put("name", coinType.getFname());
            map.put("shortName", coinType.getfShortName());
            map.put("url", coinType.getFurl());
            List<Fvirtualwallet> wallets = virtualWalletService.findByTwoProperty("fvirtualcointype.fid", coinType.getFid(), "fuser.fid", fuser.getFid());
            if (!CollectionUtils.isEmpty(wallets)) {
                map.put("useable", wallets.get(0).getFtotal());
                map.put("frozen", wallets.get(0).getFfrozen());
            }
            list.add(map);
        }
        retMap.put("list", list);
        return forSuccessResult(retMap);
    }
    /**
     * 虚拟币转账
     *
     * @param session
     * @param amount
     * @param tradePassword
     * @return
     */
    @RequestMapping(value = "/transfer_coin", method = RequestMethod.POST)
    public Object transferCoin(HttpSession session,
                               @RequestParam(value = "symbol") int symbol,
                               @RequestParam(value = "amount") double amount,
                               @RequestParam(value = "password") String tradePassword,
                               @RequestParam(value = "app", required = false, defaultValue = "zcfunding") String toApp) {
        Map<String, Object> retMap = new HashMap<>(2);
        Fuser fuser = frontUserService.findById(getLoginUser(session).getFid());
        if(fuser == null){
            return forFailureResult(401);
        }


        if(toApp.equals(constantMap.getString(ConstantKeys.USER_TRANSFER_SYSTEM_TAG))){
            retMap.put("code", 202);
            return forSuccessResult(retMap);
        }

        if(!walletTransferService.coinEnableToTransfer(symbol)){
            retMap.put("code", 202);
            return forSuccessResult(retMap);
        }

        if (amount < 0.0001) {
            retMap.put("code", 203);
            return forSuccessResult(retMap);
        }

        if (!Utils.MD5(tradePassword).equals(fuser.getFtradePassword())) {
            retMap.put("code", 204);
            return forSuccessResult(retMap);
        }

        if(null == realTimeDataService.getLatestDealData(symbol)){
            retMap.put("code", 205);
            return forSuccessResult(retMap);
        }

        if(StringUtils.isEmpty(fuser.getZhgOpenId())){
            fuser = frontUserService.findUserByProperty("fid", fuser.getFid()).get(0);
            if(StringUtils.isEmpty(fuser.getZhgOpenId())){
                // 没有用户系统openid
                retMap.put("code", 206);
                return forSuccessResult(retMap);
            }
        }

        Fvirtualwallet fvirtualwallet = frontUserService.findVirtualWalletByUser(fuser.getFid(), symbol);
        if (null == fvirtualwallet || fvirtualwallet.getFtotal() < amount) {
            retMap.put("code", 207);
            return forSuccessResult(retMap);
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
        return forSuccessResult(retMap);
    }

    /**
     * 查找虚拟钱包
     * @param session
     * @param symbol
     * @return
     */
    @RequestMapping("/getVirtualWallet")
    public Object getVirualWallet(HttpSession session, int symbol){
        Map<String, Object> map = new HashMap<String, Object>();
        Fuser fuser = frontUserService.findById(getLoginUser(session).getFid());
        if(fuser == null){
            return forFailureResult(401);
        }
        List<Fvirtualwallet> wallets = virtualWalletService.findByTwoProperty("fvirtualcointype.fid", symbol, "fuser.fid", fuser.getFid());
        if (!CollectionUtils.isEmpty(wallets)) {
            map.put("useable", wallets.get(0).getFtotal());
            map.put("frozen", wallets.get(0).getFfrozen());
        }
        return forSuccessResult(map);

    }

    /**
     * 人民币提现
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/withdrawCny")
    public Object withdrawCny(HttpSession session,
                              @RequestParam(required = false, defaultValue = "0") int cardId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Fuser fuser = frontUserService.findById(getLoginUser(session).getFid());
        if(fuser == null){
            return forFailureResult(401);
        }
        FbankinfoWithdraw bankCard = null;
        if(cardId == 0){
            List<FbankinfoWithdraw> bankCards = frontUserService.findByFuser(fuser.getFid());
            if(!CollectionUtils.isEmpty(bankCards)){
                for (FbankinfoWithdraw fbankinfoWithdraw : bankCards) {
                    if(fbankinfoWithdraw.isInit()){
                        bankCard = fbankinfoWithdraw;
                    }
                }
            }
        }else{
            bankCard = frontUserService.findFbankinfoWithdrawByFid(cardId);
        }

        if(bankCard != null){
            map.put("bankName", bankCard.getFname());
            map.put("bankNumber", bankCard.getFbankNumber());
            map.put("cardId", bankCard.getFid());
            map.put("bankType", bankCard.getFbankType());
            map.put("isHasBankCard", true);
        }else {
            map.put("isHasBankCard", false);
        }

        //最大提现人民币
        double max_double = Double.parseDouble(constantMap.getString("maxwithdrawcny").trim());
        double min_double = Double.parseDouble(constantMap.getString("minwithdrawcny").trim());
        map.put("maxDouble", max_double);
        map.put("minDouble", min_double);
        Fwallet fwallet = frontUserService.findFwalletById(fuser.getFwallet().getFid());
        map.put("balance", FormatUtils.formatCNY(fwallet.getFtotalRmb()));
        return forSuccessResult(map);
    }

    /**
     * 提现
     *  这个接口没有验证码，手持身份证照片，提现不用这个接口，改用网页端的提现。
     * @param request
     * @param tradePwd
     * @param withdrawBalance
     * @return
     * @throws Exception
     */
    @RequestMapping("/withdrawCnySubmit")
    public Object withdrawCnySubmit(HttpServletRequest request, HttpSession session, String tradePwd, double withdrawBalance, int cardId) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        Fuser fuser = frontUserService.findById(getLoginUser(session).getFid());
        if(fuser == null){
            return forFailureResult(401);
        }
        //最大提现人民币
        double max_double = Double.parseDouble(constantMap.getString("maxwithdrawcny").trim());
        double min_double = Double.parseDouble(constantMap.getString("minwithdrawcny").trim());

        if (withdrawBalance < min_double) {
            //提现金额不能小于10
            map.put("resultCode", -1);
            return forSuccessResult(map);
        }

        if (withdrawBalance > max_double) {
            //提现金额不能大于指定值
            map.put("resultCode", -200);
            return forSuccessResult(map);
        }


        double maxRMB = constantMap.getDouble(ConstantKeys.MAX_DRAW_RMB_IDENTIFY);
        if(withdrawBalance > maxRMB && 2 != fuser.getfIdentityStatus()){
            // 超过额度 需要手持身份认证
            map.put("resultCode", -60);
            map.put("maxRMB", maxRMB);
            return forSuccessResult(map);
        }

        Fwallet fwallet = fuser.getFwallet();
        FbankinfoWithdraw fbankinfoWithdraw = this.frontUserService.findFbankinfoWithdrawByFid(cardId);
        if (!fbankinfoWithdraw.isInit()) {
            map.put("resultCode", -10);
            return forSuccessResult(map);
        }


        if (fwallet.getFtotalRmb() < withdrawBalance) {
            //资金不足
            map.put("resultCode", -16);
            return forSuccessResult(map);
        }

        if (fuser.getFtradePassword() == null) {
            //没有设置交易密码
            map.put("resultCode", -15);
            return forSuccessResult(map);
        }

        if (!fuser.getFgoogleBind() && !fuser.isFisTelephoneBind()) {
            //没有绑定谷歌或者手机
            map.put("resultCode", -13);
            return forSuccessResult(map);
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

        String ip = Utils.getIpAddr(request);
        if (fuser.getFtradePassword() != null) {
            int trade_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TRADE_PASSWORD);
            if (trade_limit <= 0) {
                map.put("resultCode", -3);
                map.put("errorNum", 0);
                return forSuccessResult(map);
            } else {
                boolean flag = fuser.getFtradePassword().equals(Utils.MD5(tradePwd));
                if (!flag) {
                    map.put("resultCode", -3);
                    map.put("errorNum", trade_limit - 1);
                    this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TRADE_PASSWORD);
                    return forSuccessResult(map);
                } else if (trade_limit < Constants.ErrorCountLimit) {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TRADE_PASSWORD);
                }
            }
        }

        /*if (fuser.getFgoogleBind()) {
            int google_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);
            if (google_limit <= 0) {
                jsonObject.accumulate("resultCode", -2);
                jsonObject.accumulate("errorNum", 0);
                return jsonObject.toString();
            } else {
                boolean flag = GoogleAuth.auth(Long.parseLong(totpCode.trim()), fuser.getFgoogleAuthenticator());
                if (!flag) {
                    jsonObject.accumulate("resultCode", -2);
                    jsonObject.accumulate("errorNum", google_limit - 1);
                    this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE);
                    return jsonObject.toString();
                } else if (google_limit < new Constant().ErrorCountLimit) {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE);
                }
            }
        }*/

        /*if (fuser.isFisTelephoneBind()) {
            int tel_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
            if (tel_limit <= 0) {
                jsonObject.accumulate("resultCode", -9);
                jsonObject.accumulate("errorNum", 0);
                return jsonObject.toString();
            } else {
                boolean flag = validateMessageCode(fuser, fuser.getFareaCode(), fuser.getFtelephone(), MessageTypeEnum.CNY_TIXIAN, phoneCode);

                if (!flag) {
                    jsonObject.accumulate("resultCode", -9);
                    jsonObject.accumulate("errorNum", tel_limit - 1);
                    this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
                    return jsonObject.toString();
                } else if (tel_limit < new Constant().ErrorCountLimit) {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE);
                }
            }
        }*/

//        Captcha captcha = getCaptcha(request);
//        if(null == captcha || !phoneCode.equals(captcha.getCode()) || captcha.isExpire()){
//            int tel_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
//            jsonObject.accumulate("resultCode", -9);
//            jsonObject.accumulate("errorNum", tel_limit - 1);
//            updateCaptchaLimitedCount(request, captcha);
//            return jsonObject.toString();
//        }

        int time = this.frontAccountService.getTodayCnyWithdrawTimes(fuser);
        String times = constantMap.getString(ConstantKeys.DAY_DRAW_RMB_TIMES);
        if (StringUtils.hasText(times) && time >= Integer.valueOf(times)) {
            map.put("resultCode", -99);
            map.put("errorNum", times);
            return forSuccessResult(map);
        }

        boolean withdraw = false;
        try {
            withdraw = this.frontAccountService.updateWithdrawCNY(withdrawBalance, fuser, fbankinfoWithdraw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (withdraw) {
            map.put("resultCode", 0);
//            messageValidateMap.removeMessageMap(MessageTypeEnum.getEnumString(MessageTypeEnum.CNY_TIXIAN));
//            deleteCaptchaLimitedCount(request, captcha);
        } else {
            map.put("resultCode", -10);
        }

        return forSuccessResult(map);
    }

    /**
     *
     * @param session
     * @param cardId
     * @return
     */
    @RequestMapping("/choosebank")
    public Object chooseBank(HttpSession session,
                             @RequestParam(required = false, defaultValue = "0") int cardId){
        Fuser fuser = getLoginUser(session);
        if(fuser == null){
            return forFailureResult(401);
        }
        List<FbankinfoWithdraw> bankCards = frontUserService.findByFuser(fuser.getFid());
        List<Map<String, Object>> bankCardList = new ArrayList<Map<String, Object>>();
        bankCards.stream().filter(fbankinfoWithdraw -> fbankinfoWithdraw.isInit()).forEach(fbankinfoWithdraw -> {
            Map<String, Object> bankCardMap = new HashMap<String, Object>();
            bankCardMap.put("isSelect", fbankinfoWithdraw.getFid() == cardId ? true : false);
            bankCardMap.put("cardId", fbankinfoWithdraw.getFid());
            bankCardMap.put("bankCardName", fbankinfoWithdraw.getFname());
            bankCardMap.put("bankCardNumber", fbankinfoWithdraw.getFbankNumber());
            bankCardMap.put("bankCardType", fbankinfoWithdraw.getFbankType());
            bankCardList.add(bankCardMap);
        });
        return forSuccessResult(bankCardList);
    }

    /**
     * 提现记录
     * @param session
     * @return
     */
    @RequestMapping("/withdrawCnyLog")
    public Object withdrawCnyLog(HttpSession session,
                                 @RequestParam (required = false, defaultValue = "1") int currentPage){
        Fuser fuser = getLoginUser(session);
        if(fuser == null){
            return forFailureResult(401);
        }
        Map<String, Object> param = new HashMap<>();
        param.put("fuser.fid", fuser.getFid());
        param.put("ftype", CapitalOperationTypeEnum.RMB_OUT);
        List<Fcapitaloperation> fcapitaloperations = this.frontAccountService.findCapitalList(currentPage, Constants.AppRecordPerPage, param, " fcreateTime desc");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int totalCount = this.frontAccountService.findCapitalCount(param);
        int totalPage = totalCount/Constants.AppRecordPerPage +(totalCount % Constants.AppRecordPerPage == 0 ? 0 : 1) ;
        fcapitaloperations.forEach(fcapitaloperation -> {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", fcapitaloperation.getFid());
            map.put("createTime", Utils.dateFormat(fcapitaloperation.getFcreateTime()));
            map.put("lastUpdateTime", Utils.dateFormat(fcapitaloperation.getfLastUpdateTime()));
            map.put("status", fcapitaloperation.getFstatus());
            map.put("withdrawAccount", fcapitaloperation.getfAccount());
            map.put("amount", fcapitaloperation.getFamount());
            map.put("fee", fcapitaloperation.getFfees());
            map.put("status_s", fcapitaloperation.getFstatus_s());
            map.put("bank", fcapitaloperation.getfBank());
            list.add(map);
        });
        return forSuccessResult(list, totalPage);
    }


    /**
     * 充值确认
     *
     * @param session
     * @param id
     * @return
     */
    @RequestMapping("/rechargeCNYInfo")
    public Object rechargeCNYRecord(HttpSession session, int id){
        Fuser fuser = getLoginUser(session);
        Fcapitaloperation fcapitaloperation = frontAccountService.findCapitalOperationById(id);
        if(fcapitaloperation.getFuser().getFid() == fuser.getFid()){
            Systembankinfo systembankinfo = fcapitaloperation.getSystembankinfo();

            Map map = new HashMap<>();
            map.put("id", fcapitaloperation.getFid());
            map.put("amount", fcapitaloperation.getFamount());

            map.put("bankName", systembankinfo.getFbankName());
            map.put("ownerName", systembankinfo.getFownerName());
            map.put("bankAddress", systembankinfo.getFbankAddress());
            map.put("payAccount", systembankinfo.getFbankNumber());
            if(fcapitaloperation.getFstatus() == CapitalOperationInStatus.Come){
                map.put("arriveTime", fcapitaloperation.getfLastUpdateTime());
            }
            return forSuccessResult(map);
        }

        return forFailureResult(0);
    }

    /**
     * 充值记录
     *
     * @param session
     * @param page
     * @return
     */
    @RequestMapping("/rechargeCNYRecords")
    public Object rechargeCNYRecords(HttpSession session, @RequestParam(value = "page", required = false, defaultValue = "1")int page){
        Fuser fuser = getLoginUser(session);

        Map<String, Object> param = new HashMap<>();
        param.put("fuser.fid", fuser.getFid());
        param.put("ftype", CapitalOperationTypeEnum.RMB_IN);

        List<Fcapitaloperation> fcapitaloperations = this.frontAccountService.listCapitalList(page, Constants.PAGE_ITEM_COUNT_20, param, "fid desc");
        int totalCount = this.frontAccountService.countCapitalCount(param);

        List list = new ArrayList<>();
        fcapitaloperations.forEach(e -> {
            Map map = new HashMap();
            map.put("id", e.getFid());
            map.put("bankName", e.getfBank());
            map.put("status", e.getFstatus());
            map.put("payType", e.getPayType().getIndex());
            map.put("statusStr", e.getFstatus_s());
            map.put("dateTime", e.getFcreateTime());
            map.put("amount", FormatUtils.formatCNY(e.getFamount()));
            list.add(map);
        });

        int pageCount = totalCount % Constants.PAGE_ITEM_COUNT_20 > 0 ? totalCount / Constants.PAGE_ITEM_COUNT_20 + 1 : totalCount / Constants.PAGE_ITEM_COUNT_20;

        return forSuccessResult(list, pageCount);
    }

//    /**
//     * 上传证件照
//     *
//     * @param request
//     * @param multipartFile
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping(value = "/uploadAuthImage")
//    public Object uploadAuthImage(HttpServletRequest request, @RequestParam(value = "img") MultipartFile multipartFile) throws IOException {
//        if(null == multipartFile){
//            // 上传文件不存在
//            return  forFailureResult(101);
//        }else if(multipartFile.getSize() > 3 * 1024 * 1024){
//            // 上传文件过大
//            return forFailureResult(102);
//        }else{
//            byte[] bytes = multipartFile.getBytes();
//            if(!Utils.isImage(bytes)){
//                // 不是有效图片文件
//                return forFailureResult(103);
//            }else{
//                Fuser fuser = getSessionUser(request);
//                String ext = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
//                String filePath = Constants.IdentityPicDirectory + Utils.getRelativeFilePath(ext, bytes, fuser.getFid());
//                boolean flag = Utils.uploadFileToOss(multipartFile.getInputStream(), filePath);
//                if(flag){
//                    return forSuccessResult(filePath);
//                }
//            }
//        }
//
//        return forFailureResult(104);
//    }

    /**
     * 转移记录
     * @param session
     * @param currentPage
     * @return
     */
    @RequestMapping("/transferCoinRecord")
    public Object transferCoinRecord(HttpSession session, int coinId,
                                     @RequestParam(required = false, defaultValue = "1") int currentPage){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Fuser fuser = getLoginUser(session);
        if(fuser == null){
            return forFailureResult(401);
        }
        int totalCount = walletTransferService.count(fuser.getFid(), coinId, MoneyType.Virtual_Coin.getIndex(), null);
        int totalPage = totalCount % Constants.AppRecordPerPage > 0 ? totalCount / Constants.AppRecordPerPage + 1 : totalCount / Constants.AppRecordPerPage;
        List<WalletTransfer> lines = walletTransferService.find(fuser.getFid(), coinId, MoneyType.Virtual_Coin.getIndex(), null, (currentPage - 1) * Constants.AppRecordPerPage, Constants.AppRecordPerPage);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        lines.forEach(walletTransfer -> {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("createTime", Utils.dateFormat(new Timestamp(walletTransfer.getCreateTime().getTime())));
            map.put("lastUpdateTime", walletTransfer.getUpdateTime() != null ? Utils.dateFormat(new Timestamp(walletTransfer.getUpdateTime().getTime())) : "");
            map.put("id", walletTransfer.getId());
            map.put("active", walletTransfer.isActive() ? "转出" : "转入");
            map.put("amount", walletTransfer.getAmount());
            map.put("fee", walletTransfer.getFee());
            map.put("status", walletTransfer.getStatus().getName());
            list.add(map);
        });
        return forSuccessResult(list, totalPage);
    }


//    /**
//     * 上传头像
//     *
//     * 不能大于3MB
//     *
//     * @param multipartFile
//     * @return
//     */
//    @RequestMapping(value = "/uploadHeadImg")
//    public Object uploadHeadImg(HttpServletRequest request, @RequestParam(value = "head_img") MultipartFile multipartFile) throws IOException {
//        if(null == multipartFile){
//            // 上传文件不存在
//            return  forFailureResult(101);
//        }else if(multipartFile.getSize() > 3 * 1024 * 1024){
//            // 上传文件过大
//            return forFailureResult(102);
//        }else{
//            byte[] bytes = multipartFile.getBytes();
//            if(!Utils.isImage(bytes)){
//                // 不是有效图片文件
//                return forFailureResult(103);
//            }else{
//                String ext = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
//                String filePath = Constants.HeadImgDirectory + Utils.getRelativeFilePath(ext, bytes);
//                boolean flag = Utils.uploadFileToOss(multipartFile.getInputStream(), filePath);
//                if(flag){
//                    Fuser fuser = getSessionUser(request);
//                    fuser = frontUserService.findById(fuser.getFid());
//                    fuser.setHeadImgUrl(filePath);
//                    setLoginSession(request, fuser);
//                    frontUserService.updateFUser(fuser, request.getSession());
//                    return forSuccessResult(filePath);
//                }
//            }
//        }
//        return forFailureResult(104);
//    }

    /**
     * 委托管理
     *
     * @param session
     * @param symbol
     * @param type
     * @param status
     * @param page
     * @param count
     * @return
     */
    @RequestMapping("/entrustList")
    public Object entrustList(HttpSession session,
                              @RequestParam(value = "symbol", required = false)Integer symbol,
                              @RequestParam(value = "type", required = false, defaultValue = "-1")int type,
                              @RequestParam(value = "status", required = false, defaultValue = "-1")int status,
                              @RequestParam(value = "page", required = false, defaultValue = "1")int page,
                              @RequestParam(value = "count", required = false, defaultValue = "0")int count){

        Fvirtualcointype fvirtualcointype = null;
        if(null == symbol){
            fvirtualcointype = frontVirtualCoinService.findFirstFirtualCoin();
            symbol = fvirtualcointype.getFid();
        }else{
            fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(symbol);
            if(null != fvirtualcointype){
                symbol = fvirtualcointype.getFid();
            }else{
                return forSuccessResult(new Object[0]);
            }
        }

        // 委托列表
        Integer[] types = -1 == type ? null : new Integer[]{type};
        Integer[] statuses = -1 == status ? null : new Integer[]{status};
        Fuser fuser = getLoginUser(session);
        List<Fentrust> entrusts = frontTradeService.findFentrustHistory(fuser.getFid(), symbol, types, statuses, null, null, page, Constants.PAGE_ITEM_COUNT_20);

        DecimalFormat format = new DecimalFormat("0.##");

        List list = new ArrayList<>(entrusts.size());
        entrusts.forEach(e -> {
            Map map = new HashMap();
            map.put("id", e.getFid());
            map.put("price", FormatUtils.formatCoin(e.getFprize()));
            map.put("amount", e.getFamount());
            map.put("count", FormatUtils.formatCoin(e.getFcount()));
            map.put("leftCount", FormatUtils.formatCoin(e.getFleftCount()));
            map.put("status", e.getFstatus());
            map.put("statusStr", e.getFstatus_s());
            map.put("type", e.getFentrustType());
            map.put("typeStr", e.getFentrustType_s());

            map.put("successCount", FormatUtils.formatCoin(e.getFcount() - e.getFleftCount()));
            map.put("percent", format.format((e.getFcount() - e.getFleftCount()) / e.getFcount() * 100));

            map.put("fname", e.getFvirtualcointype().getFname());
            map.put("fShortName", e.getFvirtualcointype().getfShortName());
            map.put("furl", e.getFvirtualcointype().getFurl());
            list.add(map);
        });

        if(0 == count){
            count = frontTradeService.countFentrustHistory(fuser.getFid(), symbol, types, statuses, null, null);
        }

        int pageCount = count % Constants.PAGE_ITEM_COUNT_20 > 0 ? count / Constants.PAGE_ITEM_COUNT_20 + 1 : count / Constants.PAGE_ITEM_COUNT_20;

        Map map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("list", list);

        return forSuccessResult(map, pageCount);
    }

    /**
     * 撤单
     *
     * @param session
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/cancelEntrust", method = RequestMethod.POST)
    public Object cancelEntrust(HttpSession session, @RequestParam(value = "id") int id) throws Exception {
        Fuser fuser = getLoginUser(session);

        final Fentrust fentrust = this.frontTradeService.findFentrustById(id);
        if (fentrust.getFuser().getFid() != fuser.getFid()) {
            // 非法操作
            return forFailureResult(101);
        }
        if (fentrust != null && (fentrust.getFstatus() == EntrustStatusEnum.Going || fentrust.getFstatus() == EntrustStatusEnum.PartDeal)) {
            try {
                /*更新挂单*/
                this.frontTradeService.updateCancelFentrust(fentrust, fuser);
                realTimeDataService.removeEntrustBuyMap(fentrust.getFvirtualcointype().getFid(), fentrust);
                return forSuccessResult("ok");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return forFailureResult(-1);
    }

    /**
     * 微信上传头像
     *
     * @param request
     * @param mediaId
     * @return
     */
    @RequestMapping(value = "/uploadHeadImage", method = RequestMethod.POST)
    public Object uploadHeadImage(HttpServletRequest request, String mediaId){
        String token = realTimeDataService.getWeChatAccessToken();
        String imageUrl = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=" + token + "&media_id=" + mediaId;

        byte[] bytes = HttpUtils.downloadFile(imageUrl);
        if(ObjectUtils.isEmpty(bytes)){
            return 101;
        }
        if(!Utils.isImage(bytes)){
            // 不是有效图片文件
            return forFailureResult(103);
        }

        Fuser fuser = getSessionUser(request);
        InputStream is = new ByteArrayInputStream(bytes);
        String filePath = Constants.HeadImgDirectory + Utils.getRelativeFilePath("jpg", bytes, fuser.getFid());
        boolean flag = Utils.uploadFileToOss(is, filePath);
        if(flag){
            fuser = frontUserService.findById(fuser.getFid());
            fuser.setHeadImgUrl(filePath);
            setLoginSession(request, fuser);
            frontUserService.updateFUser(fuser, request.getSession());
            return forSuccessResult(filePath);
        }

        logger.error("上传图片到OSS失败");
        return forFailureResult(104);
    }

    /**
     * 上传证件照
     *
     * @param mediaId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/uploadAuthImage")
    public Object uploadAuthImage(HttpServletRequest request, String mediaId) {
        String token = realTimeDataService.getWeChatAccessToken();
        String imageUrl = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=" + token + "&media_id=" + mediaId;

        byte[] bytes = HttpUtils.downloadFile(imageUrl);
        if(ObjectUtils.isEmpty(bytes)){
            return 101;
        }
        if(!Utils.isImage(bytes)){
            // 不是有效图片文件
            return forFailureResult(103);
        }

        Fuser fuser = getSessionUser(request);
        InputStream is = new ByteArrayInputStream(bytes);
        String filePath = Constants.IdentityPicDirectory + Utils.getRelativeFilePath("jpg", bytes, fuser.getFid());
        boolean flag = Utils.uploadFileToOss(is, filePath);
        if(flag){
            return forSuccessResult(filePath);
        }

        return forFailureResult(104);
    }

    @RequestMapping("/contact")
    public Object contact(){
        String qq = constantMap.getString("serviceQQ");
        String telephone = constantMap.getString("telephone");
        String address = constantMap.getString("address");
        Map<String, String> map = new HashMap<String, String>();
        map.put("qq", qq);
        map.put("telephone", telephone);
        map.put("address", address);
        System.out.println(map);
        return forSuccessResult(map);
    }

    @RequestMapping("/recharge_coin")
    public Object rechargableCoin(){
        List<Fvirtualcointype> fvirtualcointypes = frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
        List<Map<String, Object>> withdrawCoins = new ArrayList<Map<String, Object>>();
        fvirtualcointypes.sort(comparator);
        fvirtualcointypes.forEach(fvirtualcointype -> {
            if(fvirtualcointype.isFIsWithDraw()){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", fvirtualcointype.getFid());
                map.put("name", fvirtualcointype.getFname());
                map.put("shortName", fvirtualcointype.getfShortName());
                map.put("url", fvirtualcointype.getFurl());
                withdrawCoins.add(map);
            }
        });
        return withdrawCoins;
    }

    @RequestMapping("recharge_coinrecord")
    public Object rechargeCoinRecord(
            HttpSession session,
            @RequestParam(required = false, defaultValue = "1") int symbol,
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ){
        Map<String, Object> map = new HashMap<String, Object>();
        Fuser fuser = getLoginUser(session);
        if(fuser == null){
            return null;
        }
        Fvirtualcointype fvirtualcointype = new Fvirtualcointype();
        fvirtualcointype.setFid(symbol);
        //充值记录
        List<Fvirtualcaptualoperation> fvirtualcaptualoperations = frontVirtualCoinService.findFvirtualcaptualoperation(fuser, new int[]{VirtualCapitalOperationTypeEnum.COIN_IN}, null, new Fvirtualcointype[]{fvirtualcointype}, "fcreateTime desc", (currentPage - 1) * pageSize, pageSize);
        fvirtualcaptualoperations.forEach(fvirtualcaptualoperation -> {
            fvirtualcaptualoperation.setFvirtualcointype(null);
            fvirtualcaptualoperation.setFuser(null);
        });
        int totalCount = this.frontVirtualCoinService.countFvirtualcaptualoperation(fuser, new int[]{VirtualCapitalOperationTypeEnum.COIN_IN}, null, new Fvirtualcointype[]{fvirtualcointype});
        map.put("fvirtualcaptualoperations", fvirtualcaptualoperations);
        map.put("currentPage", currentPage);
        map.put("pageSize", pageSize);
        map.put("totalCount", totalCount);
        return map;
    }


    //充值页面需要的数据
    @RequestMapping("/recharge_coinpage")
    public Object rechargeCoinPage(
            HttpSession session,
            @RequestParam(required = false, defaultValue = "1")int symbol){
        Map<String, Object> map = new HashMap<String, Object>();
        Fuser fuser = getLoginUser(session);
        if(fuser == null){
            map.put("400", "非法操作");
            return map;
        }
        Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(symbol);
        if(fvirtualcointype == null){
            map.put("400", "不存在币种");
            return map;
        }

        if(fvirtualcointype.isFIsWithDraw()){
            Map<String, Object> coin = new HashMap<String, Object>();
            coin.put("id", fvirtualcointype.getFid());
            coin.put("name", fvirtualcointype.getFname());
            coin.put("shortName", fvirtualcointype.getfShortName());
            coin.put("url", fvirtualcointype.getFurl());
            map.put("fvirtualcointype", coin);
        }else{
            map.put("400", "不能充提币");
            return map;
        }

        //虚拟钱包
        List<Fvirtualwallet> wallets = virtualWalletService.findByTwoProperty("fuser.fid", fuser.getFid(), "fvirtualcointype.fid", fvirtualcointype.getFid());
        Fvirtualwallet fvirtualwallet = null;
        if (!CollectionUtils.isEmpty(wallets)) {
            fvirtualwallet = wallets.get(0);
        }

        //地址
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

        //提现地址
        FvirtualaddressWithdraw fvirtualaddressWithdraw = this.frontVirtualCoinService.findFvirtualaddressWithdraw(fuser, fvirtualcointype);
        map.put("total", fvirtualwallet.getFtotal());
        map.put("frozen", fvirtualwallet.getFfrozen());
        map.put("address", fvirtualaddress.getFadderess());
        map.put("fvirtualaddressWithdraw", (fvirtualaddressWithdraw == null || fvirtualaddressWithdraw.getFadderess() == null) ? "" : fvirtualaddressWithdraw.getFadderess());
        return map;
    }

    /**
     * 提币记录
     * @param session
     * @param symbol
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/withdraw_coinrecord")
    public Object withdrawCoinRecord(
            HttpSession session,
            @RequestParam(required = false, defaultValue = "1") int symbol,
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "100") int pageSize){


        Map<String, Object> map = new HashMap<String, Object>();
        Fuser fuser = getLoginUser(session);
        if(fuser == null){
            map.put("message", "非法操作");
            return map;
        }
        Fvirtualcointype fvirtualcointype = new Fvirtualcointype();
        fvirtualcointype.setFid(symbol);
        //提现记录
        List<Fvirtualcaptualoperation> fvirtualcaptualoperations = this.frontVirtualCoinService.findFvirtualcaptualoperation(fuser, new int[]{VirtualCapitalOperationTypeEnum.COIN_OUT}, null, new Fvirtualcointype[]{fvirtualcointype}, "fcreateTime desc", (currentPage - 1) * pageSize, pageSize);
        fvirtualcaptualoperations.forEach(fvirtualcaptualoperation -> {
            fvirtualcaptualoperation.setFuser(null);
            fvirtualcaptualoperation.setFvirtualcointype(null);
        });
        // 分页
        int totalCount = this.frontVirtualCoinService.countFvirtualcaptualoperation(fuser, new int[]{VirtualCapitalOperationTypeEnum.COIN_OUT}, null, new Fvirtualcointype[]{fvirtualcointype});
        map.put("fvirtualcaptualoperations", fvirtualcaptualoperations);
        map.put("totalCount", totalCount);
        map.put("currentPage", currentPage);
        map.put("pageSize", pageSize);
        return map;
    }



}
