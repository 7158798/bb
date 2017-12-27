package com.zhgtrade.front.controller.account;

import com.ruizton.main.Enum.CountLimitTypeEnum;
import com.ruizton.main.Enum.VouchersStatusEnum;
import com.ruizton.main.code.AuthCode;
import com.ruizton.main.code.Captcha;
import com.ruizton.main.code.EmailMessageCaptcha;
import com.ruizton.main.code.SmsMessageCaptcha;
import com.ruizton.main.model.Fapi;
import com.ruizton.main.model.Fintrolinfo;
import com.ruizton.main.model.Fregionconf;
import com.ruizton.main.model.Fscore;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fvouchers;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.SystemArgsService;
import com.ruizton.main.service.admin.UserService;
import com.ruizton.main.service.admin.VirtualWalletService;
import com.ruizton.main.service.front.FrontRegionConfService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.FrontValidateService;
import com.ruizton.util.Constants;
import com.ruizton.util.GoogleAuth;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import com.ruizton.util.ZhgUserSynUtils;
import com.zhgtrade.front.controller.BaseController;
import com.zhgtrade.front.wx.controller.WXAccountController;
import com.zhguser.UserClient;
import com.zhguser.service.User;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DESC:
 * <p/>
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin(1186270005@qq.com)
 * Date： 2016-05-11 13:22
 */
@Controller
@RequestMapping("/account")
public class SecurityController extends BaseController {
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private SystemArgsService systemArgsService;
    @Autowired
    private UserService userService;
    @Autowired
    private VirtualWalletService virtualWalletService;
    @Autowired
    private FrontRegionConfService frontRegionConfService;
    /**
     * 安全中心
     *
     * @return
     */
    @RequestMapping("/security")
    public String securityCenter(HttpServletRequest request, Map<String, Object> map) {
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid());
        String device_name = Constants.GoogleAuthName + ":" + fuser.getFemail();

        boolean isBindGoogle = fuser.getFgoogleBind();
        boolean isBindTelephone = fuser.isFisTelephoneBind();
        int bindType = 0;
        if (isBindGoogle && isBindTelephone) {
            bindType = 3;
        } else if (isBindGoogle) {
            bindType = 2;
        } else if (isBindTelephone) {
            bindType = 1;
        }

        map.put("bindType", bindType);
        map.put("device_name", device_name);
        map.put("fuser", fuser);
        return "account/account_safecenter";
    }

    /**
     * 解绑/绑定手机
     *
     * @param request
     * @param code
     * @return
     * @throws Exception
     */
    @Deprecated
    @ResponseBody
    @RequestMapping("/unMobile")
    public Object unbindMobile(HttpServletRequest request, @RequestParam(required = true) String code) throws Exception {
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid());
        JSONObject jsonObject = new JSONObject();
        if (fuser.isFisTelephoneBind()) {
            String ip = Utils.getIpAddr(request);
            int mobil_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
            if (mobil_limit <= 0) {
                jsonObject.accumulate("resultCode", -9);
                jsonObject.accumulate("errorNum", mobil_limit);
                return jsonObject;
            } else {
                SmsMessageCaptcha captcha = (SmsMessageCaptcha) getCaptcha(request);
                if (null != captcha && code.equals(captcha.getCode()) && !captcha.isExpire() && captcha.getCellphone().equals(fuser.getFtelephone())) {
                    boolean flag = false;
                    try {
                        if (Constants.ConnectUserDbFlag) {
                            User user = UserClient.updateMobile(fuser.getGlobalUserId(), null);
                            if (user == null) {
                                jsonObject.accumulate("resultCode", -2);
                                return jsonObject;
                            }
                        }

                        fuser.setFisTelephoneBind(false);
                        fuser.setFtelephone(null);
                        fuser.setFlastUpdateTime(Utils.getTimestamp());
                        this.frontUserService.updateFUser(fuser, request.getSession());

                        //推广数量-1
                        Fuser introFuser = fuser.getfIntroUser_id();
                        if (introFuser != null) {
                            introFuser = this.frontUserService.findById(fuser.getfIntroUser_id().getFid());
                            introFuser.setfInvalidateIntroCount(introFuser.getfInvalidateIntroCount() - 1);
                            this.frontUserService.updateFuser(introFuser);
                        }

                        flag = true;
                        deleteCaptchaLimitedCount(request, captcha);

                        // 同步用户系统
//                        ZhgUserSynUtils.synDelUserMobile(fuser.getZhgOpenId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (flag) {
                        jsonObject.accumulate("resultCode", -1);
                    } else {
                        jsonObject.accumulate("resultCode", 0);
                    }
                    return jsonObject;
                } else {
                    updateCaptchaLimitedCount(request, captcha);
                    jsonObject.accumulate("resultCode", -9);
                    jsonObject.accumulate("errorNum", mobil_limit - 1);
                    return jsonObject;
                }
            }
        } else {
            jsonObject.accumulate("resultCode", -1);
            return jsonObject;
        }

    }

    /**
     * 绑定手机
     *
     * @param request
     * @param areaCode
     * @param phone
     * @param code
     * @param totpCode
     * @return
     * @throws Exception
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(value = "/bindMobile")
    public Object validatePhone(HttpServletRequest request,
                                @RequestParam(required = true, defaultValue = "86") String areaCode,
                                @RequestParam(value = "mobile", required = true) String phone,
                                @RequestParam(required = true) String code,
                                @RequestParam(required = false, defaultValue = "0") String totpCode
    ) throws Exception {
        JSONObject jsonObject = new JSONObject();

        if (!phone.matches("^\\d{10,14}$")) {//手機格式不對
            jsonObject.accumulate("resultCode", -6);
            return jsonObject;
        }
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid());

        if (fuser.isFisTelephoneBind()) {//已經綁定過手機了
            jsonObject.accumulate("resultCode", -5);
            return jsonObject;
        }

        String ip = Utils.getIpAddr(request);
        int google_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);
        int tel_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
        if (google_limit <= 0) {
            jsonObject.accumulate("resultCode", -8);
            jsonObject.accumulate("errorNum", 0);
            return jsonObject;
        }
        if (tel_limit <= 0) {
            jsonObject.accumulate("resultCode", -9);
            jsonObject.accumulate("errorNum", 0);
            return jsonObject;
        }

        if (fuser.getFgoogleBind()) {
            boolean googleAuth = GoogleAuth.auth(Long.parseLong(totpCode), fuser.getFgoogleAuthenticator());
            if (!googleAuth) {
                //谷歌驗證失敗
                this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE);
                jsonObject.accumulate("resultCode", -8);
                jsonObject.accumulate("errorNum", google_limit - 1);
                return jsonObject;
            } else {
                this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE);
            }
        }

        Captcha captcha = (Captcha) request.getSession().getAttribute(Constants.SESSION_CAPTCHA_CODE);
        if(captcha instanceof SmsMessageCaptcha && !((SmsMessageCaptcha)captcha).getCellphone().equals(phone)){
            updateCaptchaLimitedCount(request, captcha);
            jsonObject.accumulate("resultCode", -9);
            jsonObject.accumulate("errorNum", tel_limit - 1);
            return jsonObject;
        }
        if (null != captcha && code.equals(captcha.getCode())) {
            cleanCaptcha(request);
            //判斷手機是否被綁定了
            List<Fuser> fusers = this.frontUserService.findUserByProperty("ftelephone", phone);
            if (fusers.size() > 0) {//手機號碼已經被綁定了
                jsonObject.accumulate("resultCode", -7);
                return jsonObject;
            }
            if (Constants.ConnectUserDbFlag) {
                if (UserClient.isMobileExists(phone)) {//手機號碼已經被綁定了
                    jsonObject.accumulate("resultCode", -7);
                    return jsonObject;
                }

            }

            fuser.setFareaCode(areaCode);
            fuser.setFtelephone(phone);
            fuser.setFisTelephoneBind(true);
            fuser.setFlastUpdateTime(Utils.getTimestamp());

            if (Constants.ConnectUserDbFlag) {
                User user = UserClient.updateMobile(fuser.getGlobalUserId(), phone);//远程更新
                if (user == null) {
                    jsonObject.accumulate("resultCode", -9);
                    return jsonObject;
                }
            }

            Fvouchers vouchers = null;
            int cc = this.adminService.getAllCount("Fvouchers", "where fuser.fid=" + fuser.getFid());
            int vouchersDates = Double.valueOf(this.systemArgsService.getValue("vouchersDates")).intValue();
            if (cc == 0 && vouchersDates > 0) {
                vouchers = new Fvouchers();
                vouchers.setFuser(fuser);
                vouchers.setFcreatetime(Utils.getTimestamp());

                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, vouchersDates);
                vouchers.setFendate(c.getTime());
                vouchers.setFamount(50d);
                vouchers.setFlastamount(50d);
                vouchers.setFstatus(VouchersStatusEnum.NO);
            }

            this.frontUserService.updateFUser(fuser, vouchers, request.getSession());

            //推广数量+1
            Fuser introFuser = fuser.getfIntroUser_id();
            Fintrolinfo introlInfo = null;
            Fvirtualwallet fvirtualWallet = null;
            Fscore fscore = fuser.getFscore();
            String[] args = this.systemArgsService.getValue("introlSendCoin").split("#");
            if (introFuser != null && args.length == 3) {
                int introlUserId = fuser.getfIntroUser_id().getFid();
                Fuser fintroluser = this.userService.findById(introlUserId);
                String filter = "where fuser.fid=" + introlUserId + " and fvirtualcointype.fid=" + args[0];
                fvirtualWallet = this.virtualWalletService.list(0, 0, filter, false).get(0);
                double introlScore = Double.valueOf(args[2]);
                if (!fscore.isFissend() && introlScore > 0) {
                    fvirtualWallet.setFtotal(fvirtualWallet.getFtotal() + introlScore);
                    fscore.setFissend(true);
                    introlInfo = new Fintrolinfo();
                    introlInfo.setFcreatetime(Utils.getTimestamp());
                    introlInfo.setFiscny(false);
                    introlInfo.setFqty(introlScore);
                    introlInfo.setFuser(fintroluser);
                    introlInfo.setFtitle("下线UID" + fuser.getFid() + "绑定手机，奖励" + args[1] + introlScore + "个");
                }
                introFuser = this.frontUserService.findById(fuser.getfIntroUser_id().getFid());
                introFuser.setfInvalidateIntroCount(introFuser.getfInvalidateIntroCount() + 1);

            }
            String[] signSendScore = this.systemArgsService.getValue("vipScore").split("#");
            fscore.setFscore(fscore.getFscore() + Double.valueOf(signSendScore[0]));
            this.frontUserService.updateFuser(introFuser, introlInfo, fvirtualWallet, fscore);

            //成功
            deleteCaptchaLimitedCount(request, captcha);
            jsonObject.accumulate("resultCode", 0);

//			//推广奖励
//			try {
//				Factivitytype factivitytype = this.frontActivityService.findFactivityTypeById(ActivityTypeEnum.BINNDING_MOBIL) ;
//				this.frontActivityService.updateCompleteOneActivity(factivitytype, fuser) ;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

            // 同步用户系统
//            ZhgUserSynUtils.synUserMobile(fuser.getZhgOpenId(), fuser.getFtelephone());

            return jsonObject;
        } else {
            updateCaptchaLimitedCount(request, captcha);
            jsonObject.accumulate("resultCode", -9);
            jsonObject.accumulate("errorNum", tel_limit - 1);
            return jsonObject;
        }
    }

    /**
     * 修改登录密码/交易密码
     *
     * @param request
     * @param newPwd
     * @param originPwd
     * @param phoneCode
     * @param pwdType
     * @param reNewPwd
     * @param totpCode
     * @return
     * @throws Exception
     */
    @Deprecated
    @ResponseBody
    @RequestMapping("/modPassword")
    public Object modifyPwd(HttpServletRequest request,
                            @RequestParam(value = "newPassword", required = true) String newPwd,
                            @RequestParam(value = "oldPassword", required = false) String originPwd,
                            @RequestParam(value = "code", required = false, defaultValue = "0") String phoneCode,
                            @RequestParam(value = "type", required = false, defaultValue = "0") int pwdType,
                            @RequestParam(value = "rePassword", required = true) String reNewPwd,
                            @RequestParam(required = false, defaultValue = "0") String totpCode
    ) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("pwdType", pwdType);
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid());

        if (!newPwd.equals(reNewPwd)) {
            jsonObject.accumulate("resultCode", -3);
            return jsonObject;//两次输入密码不一样
        }

        if (!fuser.isFisTelephoneBind() && !fuser.getFgoogleBind()) {
            jsonObject.accumulate("resultCode", -13);
            return jsonObject;//需要绑定绑定谷歌或者电话才能修改密码
        }

        String ip = Utils.getIpAddr(request);
        if (fuser.isFisTelephoneBind()) {
            int mobil_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
            if (mobil_limit <= 0) {
                jsonObject.accumulate("resultCode", -7);
                jsonObject.accumulate("errorNum", 0);
                return jsonObject;
            } else {
                Captcha captcha = getCaptcha(request);
                if (null == captcha || !phoneCode.equals(captcha.getCode()) || captcha.isExpire()) {
                    jsonObject.accumulate("resultCode", -7);
                    jsonObject.accumulate("errorNum", mobil_limit - 1);
                    if(null != captcha){
                        updateCaptchaLimitedCount(request, captcha);
                    }
                    return jsonObject;
                } else if(null != captcha) {
                    deleteCaptchaLimitedCount(request, captcha);
                }
            }
        }

        if (fuser.getFgoogleBind()) {
            int google_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);
            if (google_limit <= 0) {
                jsonObject.accumulate("resultCode", -6);
                jsonObject.accumulate("errorNum", 0);
                return jsonObject;
            } else {
                if (!GoogleAuth.auth(Long.parseLong(totpCode), fuser.getFgoogleAuthenticator())) {
                    jsonObject.accumulate("resultCode", -6);
                    jsonObject.accumulate("errorNum", google_limit - 1);
                    this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE);
                    return jsonObject;
                } else {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE);
                }
            }

        }

        if (pwdType == 0) {
            //修改登陆密码
            if (StringUtils.hasText(fuser.getFloginPassword()) && !fuser.getFloginPassword().equals(Utils.MD5(originPwd))) {
                jsonObject.accumulate("resultCode", -5);
                return jsonObject;//原始密码错误
            }

        } else {
            //修改交易密码
//            if (fuser.getFtradePassword() != null && fuser.getFtradePassword().length() > 0) {
//                if (!fuser.getFtradePassword().equals(Utils.MD5(originPwd))) {
//                    jsonObject.accumulate("resultCode", -5);
//                    return jsonObject;//原始密码错误
//                }
//            }
        }

        if (pwdType == 0) {
            if (fuser.getFtradePassword() != null
                    && fuser.getFtradePassword().equals(Utils.MD5(newPwd))) {
                jsonObject.accumulate("resultCode", -10);
                return jsonObject;
            }

            if (Constants.ConnectUserDbFlag) {
                User user = UserClient.updatePassword(fuser.getGlobalUserId(), Utils.MD5(newPwd));
                if (user == null) {
                    jsonObject.accumulate("resultCode", -10);
                    return jsonObject;
                }
            }

            fuser.setFloginPassword(Utils.MD5(newPwd));
            this.frontUserService.updateFUser(fuser, request.getSession());

            // 同步用户系统
            ZhgUserSynUtils.synUserPassword(fuser.getZhgOpenId(), newPwd);
        } else {
            if (fuser.getFloginPassword().equals(Utils.MD5(newPwd))) {
                jsonObject.accumulate("resultCode", -10);
                return jsonObject;
            }

            fuser.setFtradePassword(Utils.MD5(newPwd));
            this.frontUserService.updateFUser(fuser, request.getSession());
        }

        jsonObject.accumulate("resultCode", 0);
        return jsonObject;
    }

    /**
     * 绑定邮箱
     *
     * @param request
     * @param email
     * @param code
     * @return
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(value = "/bindEmail", method = RequestMethod.POST)
    public Object bindEmail(HttpServletRequest request,
                            @RequestParam(value = "email")String email,
                            @RequestParam(value = "code")String code){
        Map<String, Object> map = new HashMap<>(5);
        if(!StringUtils.hasText(email)){
            map.put("msg", "请输入正确的邮箱号");
            return map;
        }
        if(!StringUtils.hasText(code)){
            map.put("msg", "请输入验证码");
            return map;
        }

        Captcha captcha = (Captcha) request.getSession().getAttribute(Constants.SESSION_CAPTCHA_CODE);
        if(captcha instanceof EmailMessageCaptcha && !((EmailMessageCaptcha)captcha).getEmail().equals(email)){
            map.put("msg", "验证码错误或已过期");
            updateCaptchaLimitedCount(request, captcha);
            return map;
        }
        if(null == captcha || !code.equals(captcha.getCode()) || captcha.isExpire()){
            map.put("msg", "验证码错误或已过期");
            updateCaptchaLimitedCount(request, captcha);
            return map;
        }
        if(frontUserService.isEmailExists(email)){
            map.put("msg", "邮箱已存在");
            return map;
        }

        deleteCaptchaLimitedCount(request, captcha);

        Fuser fuser = frontUserService.findById(getSessionUser(request).getFid());
        fuser.setFemail(email);
        fuser.setFisMailValidate(true);
        frontUserService.updateFUser(fuser, request.getSession());

        map.put("code", 200);

        // 同步用户系统
//        ZhgUserSynUtils.synUserEmail(fuser.getZhgOpenId(), email);
        return map;
    }


    /**
     * 解绑邮箱
     *
     * @param request
     * @param code
     * @return
     * @throws Exception
     */
    @Deprecated
    @ResponseBody
    @RequestMapping("/unEmail")
    public Object unbindEmail(HttpServletRequest request, @RequestParam(required = true) String code) throws Exception {
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid());
        JSONObject jsonObject = new JSONObject();
        if (fuser.getFisMailValidate()) {
            String ip = Utils.getIpAddr(request);
            int mobil_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.EMAIL);
            if (mobil_limit <= 0) {
                jsonObject.accumulate("resultCode", -9);
                jsonObject.accumulate("errorNum", mobil_limit);
                return jsonObject;
            } else {
                EmailMessageCaptcha captcha = (EmailMessageCaptcha) getCaptcha(request);
                if (null != captcha && code.equals(captcha.getCode()) && !captcha.isExpire() && captcha.getEmail().equals(fuser.getFemail())) {
                    boolean flag = false;
                    try {
                        fuser.setFisMailValidate(false);
                        fuser.setFemail(null);
                        fuser.setFlastUpdateTime(Utils.getTimestamp());
                        this.frontUserService.updateFUser(fuser, request.getSession());

                        //推广数量-1
                        Fuser introFuser = fuser.getfIntroUser_id();
                        if (introFuser != null) {
                            introFuser = this.frontUserService.findById(fuser.getfIntroUser_id().getFid());
                            introFuser.setfInvalidateIntroCount(introFuser.getfInvalidateIntroCount() - 1);
                            this.frontUserService.updateFuser(introFuser);
                        }

                        flag = true;
                        deleteCaptchaLimitedCount(request, captcha);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (flag) {
                        jsonObject.accumulate("resultCode", -1);
                    } else {
                        jsonObject.accumulate("resultCode", 0);
                    }
                    return jsonObject;
                } else {
                    updateCaptchaLimitedCount(request, captcha);
                    jsonObject.accumulate("resultCode", -9);
                    jsonObject.accumulate("errorNum", mobil_limit - 1);
                    return jsonObject;
                }
            }
        } else {
            jsonObject.accumulate("resultCode", -1);
            return jsonObject;
        }
    }
    /**
     * 个人信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/personalinfo")
    public String personalInfo(HttpServletRequest request, Map<String, Object> map) throws Exception {
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid());
        Fapi fapi = fuser.getFapi();
        boolean openApi = fapi != null;

        int level = fuser.getFscore().getFlevel();

        List<Fregionconf> provinces = this.frontRegionConfService.findRegionByLevel(2);

        map.put("headImgUrl", fuser.getHeadImgUrl());
        map.put("level", level);
        map.put("openApi", openApi);
        map.put("fuser", fuser);
        map.put("provinces", provinces);
        if(StringUtils.hasText(fuser.getFaddress()) && fuser.getFaddress().contains("_")){
            String[] vals = fuser.getFaddress().split("_");
            map.put("province", vals[0]);
            map.put("city", vals[1]);
            map.put("address", vals[2]);
        }else{
            map.put("address", fuser.getFaddress());
        }
        return "account/account_personal";
    }

    /**
     *
     * @param request
     * @param nickname
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateNickname", method = RequestMethod.POST)
    public Object updateNickname(HttpServletRequest request, String nickname){
        Map map = new HashMap<>();
        if(StringUtils.isEmpty(nickname)){
            map.put("code", 0);
            map.put("data", nickname);
            map.put("message", "");
            return map;
        }
        if(nickname.length() > 20){
            map.put("code", -1);
            map.put("data", nickname);
            map.put("message", "昵称名称最长10位");
            return map;
        }

        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid());
        fuser.setFnickName(nickname);
        frontUserService.updateFUser(fuser, request.getSession());
        map.put("data", nickname);
        map.put("code", 200);
        return map;
    }

    /**
     * 修改个人信息
     *
     * @param request
     * @param nickName
     * @param address
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/modUserinfo", method = RequestMethod.POST)
    public Object updateUserinfo(HttpServletRequest request,
                                 @RequestParam(required=false,defaultValue="")String nickName,
                                 @RequestParam(required=false,defaultValue="")String province,
                                 @RequestParam(required=false,defaultValue="")String city,
                                 @RequestParam(required=false,defaultValue="")String address
    ) throws Exception{
        Map<String, Object> ret = new HashMap<>(5);
//        if(StringUtils.isEmpty(nickName)){
//            ret.put("msg", "请输入昵称");
//            return ret;
//        }
        if(StringUtils.isEmpty(province)){
            ret.put("msg", "请输入省份");
            return ret;
        }
        if(StringUtils.isEmpty(city)){
            ret.put("msg", "请输入城市");
            return ret;
        }
        if(StringUtils.isEmpty(address)){
            ret.put("msg", "请输入详细地址");
            return ret;
        }
        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid()) ;
//        fuser.setFnickName(nickName) ;
        fuser.setFaddress(province + "_" + city + "_" + address);
        this.frontUserService.updateFUser(fuser, request.getSession()) ;
        ret.put("code", 200);
        return ret ;
    }

    /**
     *  提交实名信息
     *
     * @param request
     * @param identityNo
     * @param identityType
     * @param realName
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/auth", method = RequestMethod.POST)
    public Object submitAuthInfo(HttpServletRequest request,
                                 @RequestParam(value = "cardId", required=true)String identityNo,
                                 @RequestParam(value = "authType", required=false, defaultValue="0")int identityType,
                                 @RequestParam(required=true)String realName
    ) throws Exception {
        Map<String, Object> ret = new HashMap<>(5);
        if(StringUtils.isEmpty(realName)){
            ret.put("msg", "请输入真实姓名");
            return ret;
        }
        if(StringUtils.isEmpty(identityNo)){
            ret.put("msg", "请输入证件号码");
            return ret;
        }

        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid());
        if(fuser.getFpostRealValidate() || fuser.getFhasRealValidate()){
            ret.put("msg", "已提交过认证信息");
            return ret;
        }

        realName = HtmlUtils.htmlEscape(realName);
        fuser.setFidentityNo(identityNo) ;
        fuser.setFrealName(realName) ;
        fuser.setFidentityType(identityType) ;
        fuser.setFpostRealValidate(true) ;
        fuser.setFhasRealValidate(false);
        fuser.setFpostRealValidateTime(Utils.getTimestamp()) ;
        this.frontUserService.updateFUser(fuser, request.getSession()) ;
        ret.put("code", 200);

        return ret ;
    }

    /**
     * 上传头像
     *
     * 不能大于3MB
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = "/upload_head_img")
    public String uploadHeadImg(HttpServletRequest request, @RequestParam(value = "head_img") MultipartFile multipartFile, Map paramMap) throws IOException {
        if(null == multipartFile){
            // 上传文件不存在
            paramMap.put("code", 101);
        }else if(multipartFile.getSize() > 3 * 1024 * 1024){
            // 上传文件过大
            paramMap.put("code", 102);
            paramMap.put("max", "3MB");
        }else{
            byte[] bytes = multipartFile.getBytes();
            if(!Utils.isImage(bytes)){
                // 不是有效图片文件
                paramMap.put("code", 103);
            }else{
                String ext = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
                String filePath = Constants.HeadImgDirectory + Utils.getRelativeFilePath(ext, bytes);
                boolean flag = Utils.uploadFileToOss(multipartFile.getInputStream(), filePath);
                if(flag){
                    Fuser fuser = getSessionUser(request);
                    fuser = frontUserService.findById(fuser.getFid());
                    fuser.setHeadImgUrl(filePath);
                    setLoginSession(request, fuser);
                    frontUserService.updateFuser(fuser);
                    paramMap.put("img_url", filePath);
                    paramMap.put("code", 200);
                }else{
                    // 上传失败
                    paramMap.put("code", 104);
                }
            }
        }
        return "common/upload_img_result";
    }

    @RequestMapping(value = "/upload_auth_img")
    public String uploadAuthImage(HttpServletRequest request, @RequestParam(value = "img") MultipartFile multipartFile, Map paramMap) throws IOException {
        if(null == multipartFile){
            // 上传文件不存在
            paramMap.put("code", 101);
        }else if(multipartFile.getSize() > 3 * 1024 * 1024){
            // 上传文件过大
            paramMap.put("code", 102);
            paramMap.put("max", "3MB");
        }else{
            byte[] bytes = multipartFile.getBytes();
            if(!Utils.isImage(bytes)){
                // 不是有效图片文件
                paramMap.put("code", 103);
            }else{
                Fuser fuser = getSessionUser(request);
                String ext = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
                String filePath = Constants.IdentityPicDirectory + Utils.getRelativeFilePath(ext, bytes, fuser.getFid());
                boolean flag = Utils.uploadFileToOss(multipartFile.getInputStream(), filePath);
                if(flag){
                    paramMap.put("img_url", filePath);
                    paramMap.put("code", 200);
                }else{
                    // 上传失败
                    paramMap.put("code", 104);
                }
            }
        }
        return "common/upload_img_result";
    }

    @RequestMapping(value = "/uploadIdentifyPic", method = RequestMethod.GET)
    public Object uploadIdentifyPic(HttpServletRequest request){
        Fuser fuser = getSessionUser(request);
        fuser = frontUserService.findById(fuser.getFid());
        if(1 == fuser.getfIdentityStatus() || 2 == fuser.getfIdentityStatus()){
            return "redirect:/account/personalinfo.html";
        }
        return "account/account_identify";
    }

    @ResponseBody
    @RequestMapping(value = "/uploadIdentifyPic", method = RequestMethod.POST)
    public Object uploadIdentifyPic(HttpServletRequest request, String fIdentityPath, String fIdentityPath2){
        if(!StringUtils.hasText(fIdentityPath) || !StringUtils.hasText(fIdentityPath2)){
            return "no";
        }

        Fuser fuser = getSessionUser(request);
        fuser = frontUserService.findById(fuser.getFid());

        if(1 == fuser.getfIdentityStatus() || 2 == fuser.getfIdentityStatus()){
            return "no";
        }

        fuser.setfIdentityPath(fIdentityPath);
        fuser.setfIdentityPath2(fIdentityPath2);
        fuser.setfIdentityStatus(1);
        frontUserService.updateFUser(fuser, request.getSession());
        // 更新状态
        setLoginSession(request, fuser);
        return "ok";
    }

    /**
     * 新版修改交易密码
     *
     * @param request
     * @param newPassword
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modTradePassword", method = RequestMethod.POST)
    public Object modTradePassword(HttpServletRequest request, String newPassword, String code){
        Fuser fuser = getSessionUser(request);

        Map map = new HashMap<>();
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);

        if(null == authCode){
            map.put("code", 101);
            return map;
        }
        // 短信|语音
        if(CountLimitTypeEnum.TELEPHONE != authCode.getActionType() && CountLimitTypeEnum.VOICE_CAPTCHA != authCode.getActionType()){
            map.put("code", 102);
            return map;
        }

        if(fuser.getFloginPassword().equals(Utils.MD5(newPassword))){
            map.put("code", 103);
            return map;
        }

        int mobil_limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), authCode.getActionType());
        if (mobil_limit <= 0) {
            map.put("code", 104);
            map.put("leftCount", 0);
            return map;
        } else {
            if (!authCode.isEnabled(fuser.getFtelephone(), code)) {
                map.put("code", 105);
                map.put("leftCount", mobil_limit - 1);
                this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), authCode.getActionType());
                return map;
            } else {
                this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), authCode.getActionType());
            }
            request.getSession().removeAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        }

        fuser = this.frontUserService.findById(fuser.getFid());
        fuser.setFtradePassword(Utils.MD5(newPassword));
        this.frontUserService.updateFUser(fuser, request.getSession());
        map.put("code", 200);
        return map;
    }

    /**
     * 新版修改登录密码
     *
     * @param request
     * @param newPassword
     * @param oldPassword
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modLoginPassword", method = RequestMethod.POST)
    public Object modLoginPassword(HttpServletRequest request, String newPassword, String oldPassword, String code){
        Fuser fuser = getSessionUser(request);

        Map map = new HashMap<>();
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);

        if(null == authCode){
            map.put("code", 101);
            return map;
        }
        // 短信|语音
        if(CountLimitTypeEnum.TELEPHONE != authCode.getActionType() && CountLimitTypeEnum.VOICE_CAPTCHA != authCode.getActionType()){
            map.put("code", 102);
            return map;
        }

        if(!fuser.getFloginPassword().equals(Utils.MD5(oldPassword))){
            map.put("code", 103);
            return map;
        }

        if(StringUtils.hasText(fuser.getFtradePassword()) && fuser.getFtradePassword().equals(Utils.MD5(newPassword))){
            map.put("code", 104);
            return map;
        }

        int mobil_limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), authCode.getActionType());
        if (mobil_limit <= 0) {
            map.put("code", 105);
            map.put("leftCount", 0);
            return map;
        } else {
            if (!authCode.isEnabled(fuser.getFtelephone(), code)) {
                map.put("code", 106);
                map.put("leftCount", mobil_limit - 1);
                this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), authCode.getActionType());
                return map;
            } else {
                this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), authCode.getActionType());
            }
            request.getSession().removeAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        }

        fuser = this.frontUserService.findById(fuser.getFid());
        fuser.setFloginPassword(Utils.MD5(newPassword));
        this.frontUserService.updateFUser(fuser, request.getSession());
        // 同步用户系统
        ZhgUserSynUtils.synUserPassword(fuser.getZhgOpenId(), newPassword);
        map.put("code", 200);
        return map;
    }

    /**
     * 新版解绑手机号
     *
     * @param request
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/unbindMobile", method = RequestMethod.POST)
    public Object _unbindMobile(HttpServletRequest request, String code){
        Fuser fuser = frontUserService.findById(getSessionUser(request).getFid());

        Map map = new HashMap<>();
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);

        if(null == authCode){
            map.put("code", 101);
            return map;
        }
        // 短信|语音
        if(CountLimitTypeEnum.TELEPHONE != authCode.getActionType() && CountLimitTypeEnum.VOICE_CAPTCHA != authCode.getActionType()){
            map.put("code", 102);
            return map;
        }

        if(!fuser.isFisTelephoneBind() || StringUtils.isEmpty(fuser.getFtelephone())){
            map.put("code", 103);
            return map;
        }

        int mobil_limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), authCode.getActionType());
        if (mobil_limit <= 0) {
            map.put("code", 104);
            map.put("leftCount", 0);
            return map;
        } else {
            if (!authCode.isEnabled(fuser.getFtelephone(), code)) {
                map.put("code", 105);
                map.put("leftCount", mobil_limit - 1);
                this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), authCode.getActionType());
                return map;
            } else {
                this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), authCode.getActionType());
            }
            request.getSession().removeAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        }

        fuser.setFisTelValidate(false);
        fuser.setFisTelephoneBind(false);
        fuser.setFtelephone(null);
        this.frontUserService.updateFUser(fuser, request.getSession());
        map.put("code", 200);
        return map;
    }

    /**
     * 绑定手机号
     *
     * @param request
     * @param mobile
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/bindMobile_", method = RequestMethod.POST)
    public Object bindMobile(HttpServletRequest request, String mobile, String code){
        Fuser fuser = frontUserService.findById(getSessionUser(request).getFid());

        Map map = new HashMap<>();
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);

        if(null == authCode){
            map.put("code", 101);
            return map;
        }
        if(fuser.isFisTelephoneBind()){
            map.put("code", 106);
            return map;
        }
        // 短信|语音
        if(CountLimitTypeEnum.TELEPHONE != authCode.getActionType() && CountLimitTypeEnum.VOICE_CAPTCHA != authCode.getActionType()){
            map.put("code", 102);
            return map;
        }

        // 手机已被绑定
        if(frontUserService.isTelephoneExists(authCode.getAuthStr())){
            map.put("code", 103);
            return map;
        }

        int mobil_limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), authCode.getActionType());
        if (mobil_limit <= 0) {
            map.put("code", 104);
            map.put("leftCount", 0);
            return map;
        } else {
            if (!authCode.isEnabled(mobile, code)) {
                map.put("code", 105);
                map.put("leftCount", mobil_limit - 1);
                this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), authCode.getActionType());
                return map;
            } else {
                this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), authCode.getActionType());
            }
            request.getSession().removeAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        }

        fuser.setFisTelValidate(true);
        fuser.setFisTelephoneBind(true);
        fuser.setFtelephone(mobile);
        this.frontUserService.updateFUser(fuser, request.getSession());
        map.put("code", 200);
        return map;
    }

    /**
     * 新版解绑邮箱
     *
     * @param request
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/unbindEmail", method = RequestMethod.POST)
    public Object unbindEmail_(HttpServletRequest request, String code){
        Fuser fuser = frontUserService.findById(getSessionUser(request).getFid());

        Map map = new HashMap<>();
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);

        if(null == authCode){
            map.put("code", 101);
            return map;
        }
        // 邮箱
        if(CountLimitTypeEnum.EMAIL != authCode.getActionType()){
            map.put("code", 102);
            return map;
        }

        if(!fuser.getFisMailValidate() || StringUtils.isEmpty(fuser.getFemail())){
            map.put("code", 103);
            return map;
        }

        int mobil_limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), authCode.getActionType());
        if (mobil_limit <= 0) {
            map.put("code", 104);
            map.put("leftCount", 0);
            return map;
        } else {
            if (!authCode.isEnabled(fuser.getFemail(), code)) {
                map.put("code", 105);
                map.put("leftCount", mobil_limit - 1);
                this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), authCode.getActionType());
                return map;
            } else {
                this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), authCode.getActionType());
            }
            request.getSession().removeAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        }

        fuser = this.frontUserService.findById(fuser.getFid());
        fuser.setFisMailValidate(false);
        fuser.setFemail(null);
        this.frontUserService.updateFUser(fuser, request.getSession());
        map.put("code", 200);
        return map;
    }

    /**
     * 新版绑定邮箱
     *
     * @param request
     * @param email
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/bindEmail_", method = RequestMethod.POST)
    public Object bindEmail_(HttpServletRequest request, String email, String code){
        Fuser fuser = frontUserService.findById(getSessionUser(request).getFid());

        Map map = new HashMap<>();
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);

        if(null == authCode){
            map.put("code", 101);
            return map;
        }
        if(fuser.getFisMailValidate()){
            map.put("code", 106);
            return map;
        }
        // 邮箱
        if(CountLimitTypeEnum.EMAIL != authCode.getActionType()){
            map.put("code", 102);
            return map;
        }

        // 邮箱已被绑定
        if(frontUserService.isEmailExists(authCode.getAuthStr())){
            map.put("code", 103);
            return map;
        }

        int mobil_limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), authCode.getActionType());
        if (mobil_limit <= 0) {
            map.put("code", 104);
            map.put("leftCount", 0);
            return map;
        } else {
            if (!authCode.isEnabled(email, code)) {
                map.put("code", 105);
                map.put("leftCount", mobil_limit - 1);
                this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), authCode.getActionType());
                return map;
            } else {
                this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), authCode.getActionType());
            }
            request.getSession().removeAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        }

        fuser = this.frontUserService.findById(fuser.getFid());
        fuser.setFisMailValidate(true);
        fuser.setFemail(email);
        this.frontUserService.updateFUser(fuser, request.getSession());
        map.put("code", 200);
        return map;
    }
}











