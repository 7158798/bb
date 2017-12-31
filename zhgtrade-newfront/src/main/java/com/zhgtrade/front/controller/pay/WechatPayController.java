package com.zhgtrade.front.controller.pay;

import com.ruizton.main.Enum.CapitalOperationInStatus;
import com.ruizton.main.Enum.CapitalOperationTypeEnum;
import com.ruizton.main.Enum.ChargePayTypeEnum;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.model.Fcapitaloperation;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.service.admin.CapitaloperationService;
import com.ruizton.main.service.front.FrontAccountService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.util.ConstantKeys;
import com.ruizton.util.Constants;
import com.ruizton.util.HttpUtils;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import com.ruizton.util.XmlUtils;
import com.zhgtrade.front.controller.BaseController;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2017/1/12
 */
@Controller
public class WechatPayController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(WechatPayController.class);

    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontAccountService frontAccountService;
    @Autowired
    private CapitaloperationService capitaloperationService;

    @ResponseBody
    @RequestMapping("/account/wechatScanCodePay")
    public Object scanCodePay(HttpServletRequest request, int amount) throws IOException, DocumentException {
        Map retMap = new HashMap<>();

        //最小充值金额
        double min_double = Double.parseDouble(constantMap.getString("minrechargecny"));
        if (amount < min_double) {
            //最小金额不嫩小于
            retMap.put("code", 101);
            retMap.put("minVal", min_double);
            return retMap;
        }

        Fuser fuser = getSessionUser(request);
        fuser = frontUserService.findById(fuser.getFid());
        double maxRMB = constantMap.getDouble(ConstantKeys.MAX_CHARGE_RMB_IDENTIFY);
        if(amount > maxRMB && 2 != fuser.getfIdentityStatus()){
            // 超过额度 需要手持身份认证
            retMap.put("code", 102);
            retMap.put("maxRMB", maxRMB);
            return retMap;
        }

        Fcapitaloperation fcapitaloperation = new Fcapitaloperation();
        fcapitaloperation.setFamount(amount);
        fcapitaloperation.setFcreateTime(Utils.getTimestamp());
        fcapitaloperation.setFtype(CapitalOperationTypeEnum.RMB_IN);
        fcapitaloperation.setFuser(fuser);
        fcapitaloperation.setfLastUpdateTime(Utils.getTimestamp());

        fcapitaloperation.setFfees(Utils.getDouble(amount * 0.005, 2));
        fcapitaloperation.setfBank("微信扫码");
        fcapitaloperation.setFstatus(CapitalOperationInStatus.WaitForComing);
        fcapitaloperation.setPayType(ChargePayTypeEnum.Wechat_Scan_Code);
        if(StringUtils.hasText(fuser.getFrealName())){
            fcapitaloperation.setfPayee(fuser.getFrealName());
        }
        fcapitaloperation.setfPhone(this.getSessionUser(request).getFtelephone());
        this.frontAccountService.addFcapitaloperation(fcapitaloperation);

        // 微信下单
        Map payMap = sendPayOrder(fcapitaloperation.getFid(), amount, request);
        if(!"SUCCESS".equals(payMap.get("return_code"))){
            // 微信下单失败
            retMap.put("code", 103);
            logger.warn("wechat pay failure for order id " + fcapitaloperation.getFid() + ", return_msg is " + payMap.get("return_msg"));
            return retMap;
        }

        String codeUrl = payMap.get("code_url").toString();
        retMap.put("code", 200);
        retMap.put("codeUrl", codeUrl);
        retMap.put("orderId", fcapitaloperation.getFid());
        return retMap;
    }

    private Map sendPayOrder(int orderId, int amount, HttpServletRequest request) throws IOException, DocumentException {
        Map paramMap = new TreeMap<>();
        paramMap.put("appid", constantMap.getString("weChatNewAppId"));
        paramMap.put("mch_id", constantMap.getString("weChatMchId"));
        paramMap.put("nonce_str", Utils.randomString(15));
        paramMap.put("body", "比特家充值");
        paramMap.put("out_trade_no", Constants.WECHAT_ORDER_NO_PREFIX + orderId);
        paramMap.put("total_fee", String.valueOf(amount * 100));
        paramMap.put("spbill_create_ip", Utils.getIpAddr(request));
        paramMap.put("notify_url", constantMap.getString("weChatPayNotifyUrl"));
        paramMap.put("trade_type", "NATIVE");
        paramMap.put("sign", sign(paramMap));

        Document document = XmlUtils.map2xml(paramMap, "xml");
        String xml = XmlUtils.formatXml(document);
        PostMethod method = new PostMethod("https://api.mch.weixin.qq.com/pay/unifiedorder");
        method.setRequestEntity(new StringRequestEntity(xml, "text/xml", "UTF-8"));
        HttpUtils.getInstance().executeMethod(method);
        byte[] respByes = method.getResponseBody();
        return XmlUtils.xml2map(new String(respByes, Constants.UTF8_CHARSET), false);
    }

    private String sign(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            if(StringUtils.hasText(v) && !"sign".equals(k)) {
                sb.append(k);
                sb.append("=");
                sb.append(v);
                sb.append("&");
            }

        });
        sb.append("key=").append(constantMap.getString("weChatPayKey"));
        System.out.println(sb.toString());
        return Utils.MD5(sb.toString()).toUpperCase();
    }

    @ResponseBody
    @RequestMapping(value = "/wechat/payNotify", method = RequestMethod.POST)
    public Object payNotify(@RequestBody String xml) {
        Map map;
        try {
            map = XmlUtils.xml2map(xml, false);
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.error("wechat pay notify parse xml fail, result is " + xml, e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[解析xml失败]]></return_msg></xml>";
        }

        if(!"SUCCESS".equals(map.get("return_code"))){
            // 通信不成功
            logger.debug("wechat pay notify not success, result is " + xml);
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[通信失败]]></return_msg></xml>";
        }

        TreeMap<String, String> paramsMap = new TreeMap<>(map);
        paramsMap.put("appid", constantMap.getString("weChatNewAppId"));
        paramsMap.put("mch_id", constantMap.getString("weChatMchId"));

        // 签名
        String signStr = sign(paramsMap);
        if(!map.get("sign").equals(signStr)){
            logger.warn("wechat pay notify sign error, order id is " + map.get("out_trade_no").toString() + ", result is " + xml);
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[验签失败]]></return_msg></xml>";
        }

        // 订单号解码
        int orderId = Integer.valueOf(map.get("out_trade_no").toString().replaceFirst(Constants.WECHAT_ORDER_NO_PREFIX, ""));
        double amount = Double.valueOf(map.get("total_fee").toString()) / 100;

        if(!"SUCCESS".equals(map.get("result_code"))){
            // 交易不成功
            logger.warn("wechat pay notify trade fail, order id is " + map.get("out_trade_no").toString() + ", result is " + xml);
            capitaloperationService.updateChargeFailure(orderId, amount);
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[交易失败]]></return_msg></xml>";
        }

        double fee = Utils.getDouble(amount * 0.005, 2);
        capitaloperationService.updateChargeSuccess(orderId, amount, fee, map.get("transaction_id").toString());
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[ok]]></return_msg></xml>";
    }

    @ResponseBody
    @RequestMapping("/account/payOrderInfo")
    public Object payOrderInfo(HttpServletRequest request, int orderId){
        Fuser fuser = getSessionUser(request);
        Fcapitaloperation fcapitaloperation = capitaloperationService.findById(orderId);

        Map retMap = new HashMap<>();

        if(null == fcapitaloperation || fuser.getFid() != fcapitaloperation.getFuser().getFid() || CapitalOperationTypeEnum.RMB_IN != fcapitaloperation.getFtype()){
            retMap.put("code", 101);
            return retMap;
        }

        retMap.put("code", 200);
        retMap.put("status", fcapitaloperation.getFstatus());
        return retMap;
    }
}
