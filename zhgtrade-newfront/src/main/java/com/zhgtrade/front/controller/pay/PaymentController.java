package com.zhgtrade.front.controller.pay;

import com.ruizton.main.Enum.*;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.model.Fcapitaloperation;
import com.ruizton.main.model.Fsystemargs;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.admin.CapitaloperationService;
import com.ruizton.main.service.admin.SystemArgsService;
import com.ruizton.main.service.front.FrontAccountService;
import com.ruizton.main.service.front.FrontUserService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * DESC: 人民币充值
 * <p/>
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp(xxly68@qq.com)
 * Date： 2016-05-23 19:02
 */
@Controller
@RequestMapping("/epay")
public class PaymentController extends BaseController {
    @Autowired
    private SystemArgsService systemArgsService;
    @Autowired
    private FrontAccountService frontAccountService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private CapitaloperationService capitaloperationService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private MessageQueueService messageQueueService;
    private final static Logger logger = LoggerFactory.getLogger(PaymentController.class);


    private final static String _95EPAY_PREFIX_KEY      = "_95epay_";
    private final static String _95EPAY_MERNO_KEY       = "MerNo";
    private final static String _95EPAY_MD5KEY_KEY      = "MD5key";
    private final static String _95EPAY_AMOUNT_KEY      = "Amount";
    private final static String _95EPAY_BILLNO_KEY      = "BillNo";
    private final static String _95EPAY_RETURNURL_KEY   = "ReturnURL";
    private final static String _95EPAY_NOTIFYURL_KEY   = "NotifyURL";
    private final static String _95EPAY_MD5INFO_KEY     = "MD5info";
    private final static String _95EPAY_PAYTYPE_KEY     = "PayType";
    private final static String _95EPAY_PAYMENTTYPE_KEY = "PaymentType";
    private final static String _95EPAY_SUCCEED_KEY     = "Succeed";
    // 双乾网银支付
    private final static String _95EPAY_EBANK_PAY_TYPE  = "CSPAY";

    @RequestMapping(value = "/ebank", method = RequestMethod.POST)
    public String _95epayEbank(HttpServletRequest request,
                               @RequestParam(required = true) String bank,
                               @RequestParam(required = true) double amount){
        //最小充值金额
        double min_double = Double.parseDouble(constantMap.getString("minrechargecny"));
        if (amount < min_double) {
            //最小金额不嫩小于
            request.setAttribute("code", 2);
            request.setAttribute("minVal", min_double);
            return "pay/_95epay";
        }

        Fuser fuser = getSessionUser(request);
        fuser = frontUserService.findById(fuser.getFid());
        double maxRMB = constantMap.getDouble(ConstantKeys.MAX_CHARGE_RMB_IDENTIFY);
        if(amount > maxRMB && 2 != fuser.getfIdentityStatus()){
            // 超过额度 需要手持身份认证
            request.setAttribute("code", -30);
            request.setAttribute("maxRMB", maxRMB);
            return "pay/_95epay";
        }

        Fcapitaloperation fcapitaloperation = new Fcapitaloperation();
        fcapitaloperation.setFamount(amount);
        fcapitaloperation.setFcreateTime(Utils.getTimestamp());
        fcapitaloperation.setFtype(CapitalOperationTypeEnum.RMB_IN);
        fcapitaloperation.setFuser(fuser);
        fcapitaloperation.setfLastUpdateTime(Utils.getTimestamp());

        EpayBankEnum epayBank = EpayBankEnum.get(bank);
        fcapitaloperation.setfBank(epayBank.getName());
        fcapitaloperation.setFstatus(CapitalOperationInStatus.WaitForComing);
        fcapitaloperation.setPayType(ChargePayTypeEnum._95epay_EBank);
        if(StringUtils.hasText(fuser.getFrealName())){
            fcapitaloperation.setfPayee(fuser.getFrealName());
        }
        fcapitaloperation.setfPhone(this.getSessionUser(request).getFtelephone());
        this.frontAccountService.addFcapitaloperation(fcapitaloperation);

        // 组装参数
        Map<String, String> params = new TreeMap<>();
        params.put(_95EPAY_MERNO_KEY, constantMap.getString(ConstantKeys._59EPAY_MERNO));
        params.put(_95EPAY_AMOUNT_KEY, String.valueOf(amount));
        params.put(_95EPAY_BILLNO_KEY, StringUtils.encodeBase64(String.valueOf(fcapitaloperation.getFid())));
        params.put(_95EPAY_RETURNURL_KEY, constantMap.getString(ConstantKeys._59EPAY_RESULT_URL));

        // 签名
        params.put(_95EPAY_MD5INFO_KEY, _95epaySignature(params));
        params.put(_95EPAY_NOTIFYURL_KEY, constantMap.getString(ConstantKeys._59EPAY_NOTIFY_URL));
        params.put(_95EPAY_PAYTYPE_KEY, _95EPAY_EBANK_PAY_TYPE);
        params.put(_95EPAY_PAYMENTTYPE_KEY, bank);

        request.setAttribute("code", 200);
        request.setAttribute("payKeys", params);
        return "pay/_95epay";
    }

    /**
     * 双乾支付签名
     *
     * @param params
     * @return
     */
    private String _95epaySignature(Map<String, String> params){
        if(CollectionUtils.isEmpty(params)) return null;

        StringBuilder keyBuf = new StringBuilder();
        for(String key : params.keySet()){
            keyBuf.append(key).append("=").append(params.get(key)).append("&");
        }
        keyBuf.append(Utils.MD5(constantMap.getString(ConstantKeys._59EPAY_MD5_KEY)).toUpperCase());
        params.remove(_95EPAY_MD5KEY_KEY);

        return Utils.MD5(keyBuf.toString()).toUpperCase();
    }

    /**
     * 双乾支付结果
     *
     * @param succeed
     * @return
     */
    @RequestMapping(value = "/95result", method = RequestMethod.POST)
    public String _95epayResult(@RequestParam(value = _95EPAY_SUCCEED_KEY, required = true)String succeed){
        if("88".equals(succeed)){
            // 付款成功
            return "redirect:/account/chargermb.html?code=200";
        }
        logger.error("双乾支付失败返回，支付响应码为" + succeed);
        System.out.println("双乾支付失败返回，支付响应码为" + succeed);
        return "redirect:/account/chargermb.html?code=-1";
    }

    /**
     * 双乾支付通知
     * @param billNo
     * @param amount
     * @param succeed
     * @param md5Info
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/95notify", method = RequestMethod.POST)
    public Object _95epayNotify(HttpServletRequest request, @RequestParam(value = _95EPAY_BILLNO_KEY, required = true)String billNo,
                                @RequestParam(value = _95EPAY_AMOUNT_KEY, required = true)String amount,
                                @RequestParam(value = _95EPAY_SUCCEED_KEY, required = true)String succeed,
                                @RequestParam(value = _95EPAY_MD5INFO_KEY, required = true)String md5Info){

        Map<String, String> params = new TreeMap<>();
        params.put(_95EPAY_MERNO_KEY, constantMap.getString(ConstantKeys._59EPAY_MERNO));
        params.put(_95EPAY_AMOUNT_KEY, amount);
        params.put(_95EPAY_BILLNO_KEY, billNo);
        params.put(_95EPAY_SUCCEED_KEY, succeed);

        if(!_95epayValidateSignature(params, md5Info)){
            // 签名失败(非法操作)
            logger.error("双乾支付通知非法签名");
            return "";
        }

        // 订单号解码
        billNo = StringUtils.decodeBase64(billNo);
        if("88".equals(succeed)){
            // 付款成功
            capitaloperationService.updateChargeSuccess(Integer.valueOf(billNo), Double.valueOf(amount), 0, null);
            return "yes";
        }

        capitaloperationService.updateChargeFailure(Integer.valueOf(billNo), Double.valueOf(amount));
        logger.error("双乾支付通知支付失败，支付响应码为" + succeed);
        return "";
    }

    /**
     * 双乾支付通知验签
     * @param params
     * @return
     */
    private boolean _95epayValidateSignature(Map<String, String> params, String md5Info){
        StringBuilder keyBuf = new StringBuilder();
        for(String key : params.keySet()){
            keyBuf.append(key).append("=").append(params.get(key)).append("&");
        }
        keyBuf.append(Utils.MD5(constantMap.getString(ConstantKeys._59EPAY_MD5_KEY)).toUpperCase());
        return md5Info.equals(Utils.MD5(keyBuf.toString()).toUpperCase());
    }

}
