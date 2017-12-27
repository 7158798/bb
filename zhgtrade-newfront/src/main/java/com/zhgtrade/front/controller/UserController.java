package com.zhgtrade.front.controller;

import com.ruizton.main.Enum.CountLimitTypeEnum;
import com.ruizton.main.Enum.UserStatusEnum;
import com.ruizton.main.code.AuthCode;
import com.ruizton.main.code.Captcha;
import com.ruizton.main.code.EmailMessageCaptcha;
import com.ruizton.main.code.SmsMessageCaptcha;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.dto.UserDto;
import com.ruizton.main.model.Flog;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.admin.LogService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.FrontValidateService;
import com.ruizton.util.CollectionUtils;
import com.ruizton.util.Constants;
import com.ruizton.util.HttpUtils;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import com.ruizton.util.ZhgUserSynUtils;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringValueResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private LogService logService;

    /**
     * 登录
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/oldLogin", method = RequestMethod.POST)
    public Object login(HttpServletRequest request,
                        @RequestParam(required = true) String loginName,
                        @RequestParam(required = true) String password) {
        JSONObject jsonObject = new JSONObject();
        /*
         * result.resultCode == -1//用户名或密码错误 result.resultCode ==
		 * -2//此ip登录频繁，请2小时后再试 result.resultCode == -3
		 * result.errorNum=0://此ip登录频繁，请2小时后再试
		 * result.errorNum!=0://用户名或密码错误，您还有"+result.errorNum+"次机会"
		 * result.resultCode == -4//您的浏览器还未开启COOKIE,请设置启用COOKIE功能
		 * result.resultCode == 1//正常 result.resultCode ==
		 * 2//账户出现安全隐患被冻结，请尽快联系客服。
		 */

        // 两个小时内有5次登陆失败的机会
        Fuser fuser = new Fuser();
        fuser.setFloginName(loginName);
        fuser.setFemail(loginName);
        fuser.setFloginPassword(password);
        String ip = Utils.getIpAddr(request);
        int limitedCount = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
        if (limitedCount > 0) {
			/*try {
				if (Utils.getCookie(request.getCookies(), "open") == null) {
					jsonObject.accumulate("resultCode", -4);
					return jsonObject.toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.accumulate("resultCode", -4);
				return jsonObject.toString();
			}*/

            fuser = this.frontUserService.updateCheckLogin(fuser, ip);
            if (fuser != null) {
                if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                    jsonObject.accumulate("resultCode", 1);
                    userLogined(request, fuser);
                } else {
                    jsonObject.accumulate("resultCode", 2);
                }
            } else {
                // 错误的用户名或密码
                if (limitedCount == Constants.ErrorCountLimit) {
                    jsonObject.accumulate("resultCode", -1);
                } else {
                    jsonObject.accumulate("resultCode", -3);
                    jsonObject.accumulate("errorNum", limitedCount - 1);
                }
                this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
            }
        } else {
            jsonObject.accumulate("resultCode", -2);
        }

        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object newLogin(HttpServletRequest request,
                           HttpServletResponse response,
                           @RequestParam(required = false) String loginName,
                           @RequestParam(required = false) String password,
                           @RequestParam(required = false) String phoneCode,
                           @RequestParam(required = false, defaultValue = "false") boolean wxBind) {


        JSONObject jsonObject = new JSONObject();
        if(!StringUtils.hasText(loginName)){
            Object object = request.getSession().getAttribute(Constants.NOT_LASTDEVICE_LOGINNAME);
            if(object == null){
                jsonObject.accumulate("resultCode", -10);
                return jsonObject;
            }
            loginName = String.valueOf(object);
        }

        if(!StringUtils.hasText(password)){
            Object object = request.getSession().getAttribute(Constants.NOT_LASTDEVICE_PASSWORD);
            if(object == null){
                jsonObject.accumulate("resultCode", -10);
                return jsonObject;
            }
            password = String.valueOf(object);
        }

        /*
         * result.resultCode == -1//用户名或密码错误 result.resultCode ==
		 * -2//此ip登录频繁，请2小时后再试 result.resultCode == -3
		 * result.errorNum=0://此ip登录频繁，请2小时后再试
		 * result.errorNum!=0://用户名或密码错误，您还有"+result.errorNum+"次机会"
		 * result.resultCode == -4//您的浏览器还未开启COOKIE,请设置启用COOKIE功能
		 * result.resultCode == 1//正常 result.resultCode ==
		 * 2//账户出现安全隐患被冻结，请尽快联系客服。
		 */

        // 两个小时内有5次登陆失败的机会
        Fuser fuser = new Fuser();
        fuser.setFloginName(loginName);
        fuser.setFemail(loginName);
        fuser.setFloginPassword(password);
        String ip = Utils.getIpAddr(request);
        int limitedCount = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.LOGIN_PASSWORD);

        //获取cookie是否是常用设备登录
        if (limitedCount > 0) {
            // 优先远程登录
            // 众股用户系统登录
            Object retObj = ZhgUserSynUtils.synUserLogin(loginName, password);
            if(retObj instanceof Fuser){
                fuser = (Fuser) retObj;
                if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE){
                    // 验证设备，返回null表示通过，返回一个JSONObject对象，说明不通过，把JSONObject对象返回给前段。
                    JSONObject validateObject = validateDevice(request, response, loginName, password, phoneCode, jsonObject, fuser);
                    if(validateObject != null){
                        return validateObject;
                    }
                    jsonObject.accumulate("resultCode", 1);
                    /*add by yujie */
                    if(!StringUtils.hasText(fuser.getFtelephone()) && !StringUtils.hasText(fuser.getFemail())){
                        jsonObject.accumulate("code", 1);
                    }
                    userLogined(request, fuser);

                    // 微信绑定
                    String wxOpenId = (String) request.getSession().getAttribute(Constants.WECHAT_OPEN_ID);
                    if(wxBind && StringUtils.hasText(wxOpenId)){
                        fuser.setWxOpenId(wxOpenId);
                        frontUserService.updateFUser(fuser, request.getSession());
                        request.getSession().removeAttribute(Constants.WECHAT_OPEN_ID);
                    }
                    // 登录后台处理线程
                    messageQueueService.publish(QueueConstants.USER_LOGINED_BACK_QUEUE, new UserDto(fuser.getFid(), new Date()));

                }else{
                    jsonObject.accumulate("resultCode", 2);
                }
                return jsonObject;
            }

            fuser = this.frontUserService.updateCheckLogin(fuser, ip);
            if (fuser != null) {
                if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {

                    // 验证设备，返回null表示通过，返回一个JSONObject对象，说明不通过，把JSONObject对象返回给前段。
                    JSONObject validateObject = validateDevice(request, response, loginName, password, phoneCode, jsonObject, fuser);
                    if(validateObject != null){
                        return validateObject;
                    }
                    jsonObject.accumulate("resultCode", 1);
                    /*add by yujie */
                    if(!StringUtils.hasText(fuser.getFtelephone()) && !StringUtils.hasText(fuser.getFemail())){
                        jsonObject.accumulate("code", 1);
                    }
                    userLogined(request, fuser);

                    // 从用户系统同步信息
                    if(StringUtils.hasText(fuser.getZhgOpenId())){
                        ZhgUserSynUtils.synUserInfo(fuser.getZhgOpenId());
                    }else{
                        ZhgUserSynUtils.synUserRegister(fuser);
                    }

                    // 微信绑定
                    String wxOpenId = (String) request.getSession().getAttribute(Constants.WECHAT_OPEN_ID);
                    if(wxBind && StringUtils.hasText(wxOpenId)){
                        fuser.setWxOpenId(wxOpenId);
                        frontUserService.updateFUser(fuser, request.getSession());
                        request.getSession().removeAttribute(Constants.WECHAT_OPEN_ID);
                    }

                    // 登录后台处理线程
                    messageQueueService.publish(QueueConstants.USER_LOGINED_BACK_QUEUE, new UserDto(fuser.getFid(), new Date()));

                } else {
                    jsonObject.accumulate("resultCode", 2);
                }
            }else{
                // 错误的用户名或密码
                if (limitedCount == Constants.ErrorCountLimit) {
                    jsonObject.accumulate("resultCode", -1);
                } else {
                    jsonObject.accumulate("resultCode", -3);
                    jsonObject.accumulate("errorNum", limitedCount - 1);
                }
                this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
            }
        } else {
            jsonObject.accumulate("resultCode", -2);
        }
        return jsonObject;
    }


    /**
     * 判断上次登录设备和此次登录的设备是否相同。允许登录，写入cookie并返回null，不允许登录返回一个JSONObject对象，并把状态码返回。
     * @param request
     * @param response
     * @param loginName
     * @param password
     * @param phoneCode
     * @param jsonObject
     * @param fuser
     * @return
     */
    private JSONObject validateDevice(HttpServletRequest request, HttpServletResponse response, String loginName, String password, String phoneCode, JSONObject jsonObject, Fuser fuser) {
        try{
            String deviceCodeKey = String.valueOf(fuser.getFid() * 3 );
            String deviceBeforeCode = Utils.getCookie(request.getCookies(), deviceCodeKey);
            String lastDeviceBeforeCode = logService.lastDeviceBeforeCode(fuser.getFid());
            //当前账号在其他设备上登录过并记录在flog，假如当前账号的上一次登录设备没有记录在数据库，直接登录。
            if(StringUtils.hasText(lastDeviceBeforeCode)){
                // 此设备没有登录过，或者当前设备的登录信息和该用户最后一次登录的信息不同
                if(deviceBeforeCode == null || !deviceBeforeCode.equals(lastDeviceBeforeCode)){
                    if(StringUtils.hasText(phoneCode)){
                        //验证码不正确，直接返回。
                        JSONObject validateResult = validateAuthCode(request, fuser, phoneCode, jsonObject);
                        if(validateResult != null){
                            jsonObject = validateResult;
                            return jsonObject;
                        }
                    }else {
                        //不在上一个设备上登录，又没有发送验证码。把用户名和密码保存在session中。
                        request.getSession().setAttribute(Constants.NOT_LASTDEVICE_LOGINNAME, loginName);
                        request.getSession().setAttribute(Constants.NOT_LASTDEVICE_PASSWORD, password);
                        if(StringUtils.hasText(fuser.getFtelephone())){
                            jsonObject.accumulate("resultCode", -5);
                            return jsonObject;
                        }
                        if(StringUtils.hasText(fuser.getFemail())){
                            jsonObject.accumulate("resultCode", -6);
                            return jsonObject;
                        }
                    }
                }
            }
            // 将此次的登录加入常用设备
            deviceBeforeCode = DigestUtils.md5Hex(fuser.getFloginName());
            Cookie loginCookie = new Cookie(deviceCodeKey, deviceBeforeCode);
            loginCookie.setPath("/");
            loginCookie.setDomain(".zhgtrade.com");
            loginCookie.setMaxAge(7 * 24 * 60 * 60);
            loginCookie.setHttpOnly(true);
            response.addCookie(loginCookie);
            // 登录日志 记录在客户端的value值为md5（登录名）
            logService.addLoginLog(fuser, Utils.getIpAddr(request), "", deviceBeforeCode);
            request.getSession().removeAttribute(Constants.NOT_LASTDEVICE_LOGINNAME);
            request.getSession().removeAttribute(Constants.NOT_LASTDEVICE_LOGINNAME);
        }catch(Exception e){
            e.printStackTrace();
            //服务器错误，不能登录
            jsonObject.accumulate("resultCode", -20);
            return jsonObject;
        }
        return null;
    }

    //登录设备记为最后登录设备，并写入到flog中
    public void markLastDevice(HttpServletRequest request, Fuser fuser){

    }



    //验证验证码
    private JSONObject validateAuthCode(HttpServletRequest request, Fuser fuser, String phoneCode, JSONObject jsonObject){
        try {
            AuthCode authCode = (AuthCode) request.getSession().getAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
            if(authCode == null){
                jsonObject.accumulate("code", -15);
                return jsonObject;
            }
            int _limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), authCode.getActionType());
            if (_limit <= 0) {
                jsonObject.accumulate("code", 110);
                jsonObject.accumulate("leftCount", 0);
                return jsonObject;
            } else {
                if (!authCode.isEnabled(fuser.getFemail(), phoneCode) && !authCode.isEnabled(fuser.getFtelephone(), phoneCode)) {
                    jsonObject.accumulate("code", 111);
                    jsonObject.accumulate("leftCount", _limit - 1);
                    this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), authCode.getActionType());
                    return jsonObject;
                } else {
                    this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), authCode.getActionType());
                }
                request.getSession().removeAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
            }
        }catch (Exception e){
            e.printStackTrace();
            //服务器错误
            jsonObject.accumulate("resultCode", -20);
            return jsonObject;
        }

        return null;
    }

    protected void userLogined(HttpServletRequest request, Fuser fuser){
        // 清理估值缓存
        request.getSession().removeAttribute(Constants.USER_ASSET);

        String ip = Utils.getIpAddr(request);
        this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
        if (fuser.getFopenSecondValidate()) {
            setSecondLoginSession(request, fuser);
        } else {
            setLoginSession(request, fuser);
        }

        // 登录通知
        frontUserService.sendLoginNotice(request.getSession().getId(), fuser.getFid(), fuser);
    }

    /**
     * 登出
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/logout")
    public Object logout(HttpServletRequest request) {
        Fuser fuser = getSessionUser(request);
        if(null != fuser){
            request.getSession().removeAttribute(getSessionUser(request).getFid() + "trade");
            request.getSession().removeAttribute(Constants.USER_LOGIN_SESSION);
            frontUserService.sendLogoutNotice(request.getSession().getId(), fuser.getFid());
        }
        return "ok";
    }

    /**
     * 去注册
     *
     * @return
     */
    @RequestMapping(value = "/to_reg", method = RequestMethod.GET)
    public String toRegister() {
        return "user/register_risk";
    }

    /**
     * 注册
     *
     * @return
     */
    @RequestMapping(value = "/reg", method = RequestMethod.GET)
    public String register(HttpServletRequest request) {
        try {
            setFormToken(request);
            String introStr = Utils.getCookie(request.getCookies(), Constants.INTRO_COOKIE);
            if(StringUtils.hasText(introStr)){
                request.setAttribute("intro", introStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "user/register";
    }

    /**
     * 用户认证
     *
     * @return
     */
    @RequestMapping(value = "/auth")
    public String userAuth(HttpServletRequest request, Map<String, Object> map) {
        Fuser fuser = getSessionUser(request);
        if(null == fuser || fuser.getFpostRealValidate() || fuser.getFhasRealValidate()){
            return "redirect:/";
        }
        setFormToken(request);
        return "user/register_real";
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
        if(null == getSessionUser(request)){
            ret.put("msg", "未登录");
            return ret;
        }

        List<Fuser> list = frontUserService.findUserByProperty("fidentityNo", identityNo);
        if(!CollectionUtils.isEmpty(list)){
            ret.put("msg", "证件号已被绑定");
            return ret;
        }

        Fuser fuser = this.frontUserService.findById(getSessionUser(request).getFid()) ;
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

        // 同步用户信息
//        ZhgUserSynUtils.synUserAuth(fuser.getZhgOpenId(), realName, identityNo, identityType + "");
        return ret ;
    }

    /**
     * 注册用户
     *
     * @return
     */
    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public
    @ResponseBody
    Object doRegister(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam(value = "type", required = true) short type,
                      @RequestParam(value = "areaCode", required = false, defaultValue = "86") String areaCode,
                      @RequestParam(value = "cellphone", required = false) String cellphone,
                      @RequestParam(value = "email", required = false) String email,
                      @RequestParam(value = "password", required = true) String password,
                      @RequestParam(value = "inviteCode", required = false) String inviteCode,
                      @RequestParam(value = "vetifyCode", required = true) String vetifyCode,
                      @RequestParam(value = "authType", required = false, defaultValue = "0") short authType,
                      @RequestParam(value = "realName", required = false) String realName,
                      @RequestParam(value = "cardId", required = false) String cardId,
                      @RequestParam(value = "token", defaultValue = "") String token) {

        Map<String, Object> map = new HashMap<String, Object>(5);
        try {
            Captcha captcha = (Captcha) request.getSession().getAttribute(Constants.SESSION_CAPTCHA_CODE);
            String sToken = (String) request.getSession().getAttribute(Constants.FORM_TOKEN);

            if(captcha instanceof SmsMessageCaptcha && !((SmsMessageCaptcha)captcha).getCellphone().equals(cellphone)){
                map.put("code", 6);
                map.put("msg", "验证码不正确或已过期");
                updateCaptchaLimitedCount(request, captcha);
            }else if(captcha instanceof EmailMessageCaptcha && !((EmailMessageCaptcha)captcha).getEmail().equals(email)){
                map.put("code", 6);
                map.put("msg", "验证码不正确或已过期");
                updateCaptchaLimitedCount(request, captcha);
            }else if (1 == type && !StringUtils.isMobile(cellphone)) {
                // 手机格式不对
                map.put("code", 2);
                map.put("msg", "手机格式不正确");
            } else if (2 == type && !StringUtils.isEmail(email)) {
                // 邮箱格式不对
                map.put("code", 3);
                map.put("msg", "邮箱格式不正确");
            } else if (StringUtils.hasText(cellphone) && (boolean)userNameIsExist(cellphone)) {
                // 手机存在
                map.put("code", 4);
                map.put("msg", "手机号已被注册");
            } else if (StringUtils.hasText(email) && (boolean)userNameIsExist(email)) {
                // 邮箱存在
                map.put("code", 5);
                map.put("msg", "邮箱已被注册");
            } else if (!StringUtils.hasText(vetifyCode) || null == captcha || !vetifyCode.equals(captcha.getCode()) || captcha.isExpire()) {
                // 验证码不正确
                map.put("code", 6);
                map.put("msg", "验证码不正确或已过期");
                updateCaptchaLimitedCount(request, captcha);
            }
            else if (!StringUtils.hasText(password)) {
                map.put("code", 9);
                map.put("msg", "请输入您的密码");
            } else {
                // 不让重复提交
                cleanFormToken(request);
                // 注册
                Fuser fuser = new Fuser();
                if (StringUtils.hasText(email)) {
                    fuser.setFemail(email);
                    fuser.setFnickName(email.split("@")[0]);
                    fuser.setFloginName(email);
                    fuser.setFisMailValidate(true);
                } else if (StringUtils.hasText(cellphone)) {
                    fuser.setFtelephone(cellphone);
                    fuser.setFnickName(cellphone);
                    fuser.setFloginName(cellphone);
                    fuser.setFisTelephoneBind(true);
                    fuser.setFisTelValidate(true);
                    fuser.setFareaCode(areaCode);
                }
                fuser.setFloginPassword(Utils.MD5(password));
                fuser.setFregisterIp(Utils.getIpAddr(request));// 注册IP
                fuser.setFsourceUrl(request.getHeader("Referer"));// 来源url
                /*fuser.setFpostRealValidate(true);
                fuser.setFpostRealValidateTime(Utils.getTimestamp());
                fuser.setFidentityType(authType);
                fuser.setFrealName(realName);
                fuser.setFidentityNo(cardId);*/
                // 推广
                if (StringUtils.hasText(inviteCode)) {
                    inviteCode = new String(Base64Utils.decode(inviteCode.getBytes()));
                    Fuser user = frontUserService.findById(Integer.valueOf(inviteCode));
                    if (null != user) {
                        fuser.setfIntroUser_id(user);
                    }
                    Cookie cookie = new Cookie(Constants.INTRO_COOKIE, null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }

                frontUserService.saveRegister(fuser);
                setLoginSession(request, fuser);
                // 成功
                map.put("code", 200);

                // 删除验证码
                deleteCaptchaLimitedCount(request, captcha);

                // 同步用户系统
                ZhgUserSynUtils.synUserRegister(fuser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setFormToken(request);
            map.put("code", -1);
            map.put("msg", "系统异常");
            map.put("token", request.getSession().getAttribute(Constants.FORM_TOKEN));
        }

        return map;
    }

    /**
     * 新版注册用户
     *
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public
    @ResponseBody
    Object doRegister(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam(value = "areaCode", required = false, defaultValue = "86") String areaCode,
                      @RequestParam(value = "cellphone", required = false) String cellphone,
                      @RequestParam(value = "email", required = false) String email,
                      @RequestParam(value = "password") String password,
                      @RequestParam(value = "inviteCode", required = false) String inviteCode,
                      @RequestParam(value = "vetifyCode") String vetifyCode,
                      @RequestParam(required = false, defaultValue = "false") boolean wxBind) {

        Map map = new HashMap();
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);

        if(null == authCode){
            map.put("code", 101);
            return map;
        }
        if(CountLimitTypeEnum.TELEPHONE != authCode.getActionType() && CountLimitTypeEnum.VOICE_CAPTCHA != authCode.getActionType() && CountLimitTypeEnum.EMAIL != authCode.getActionType()){
            map.put("code", 102);
            return map;
        }
        if(StringUtils.isMobile(cellphone) && (boolean)userNameIsExist(cellphone)){
            map.put("code", 103);
            return map;
        }
        if(StringUtils.isEmail(email) && (boolean)userNameIsExist(email)){
            map.put("code", 104);
            return map;
        }
        if(!StringUtils.hasText(password)){
            map.put("code", 105);
            return map;
        }

        int _limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), authCode.getActionType());
        if (_limit <= 0) {
            map.put("code", 106);
            map.put("leftCount", 0);
            return map;
        } else {
            if (!authCode.isEnabled(cellphone, vetifyCode) && !authCode.isEnabled(email, vetifyCode)) {
                map.put("code", 107);
                map.put("leftCount", _limit - 1);
                this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), authCode.getActionType());
                return map;
            } else {
                this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), authCode.getActionType());
            }
            request.getSession().removeAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        }

        // 注册
        Fuser fuser = new Fuser();
        if (StringUtils.hasText(email)) {
            fuser.setFemail(email);
            fuser.setFnickName(email.split("@")[0]);
            fuser.setFloginName(email);
            fuser.setFisMailValidate(true);
        } else if (StringUtils.hasText(cellphone)) {
            fuser.setFtelephone(cellphone);
            fuser.setFnickName(cellphone);
            fuser.setFloginName(cellphone);
            fuser.setFisTelephoneBind(true);
            fuser.setFisTelValidate(true);
            fuser.setFareaCode(areaCode);
        }
        fuser.setFloginPassword(Utils.MD5(password));
        fuser.setFregisterIp(Utils.getIpAddr(request));// 注册IP
        fuser.setFsourceUrl(request.getHeader("Referer"));// 来源url

        try {
            // 推广
            if (StringUtils.hasText(inviteCode)) {
                inviteCode = new String(Base64Utils.decode(inviteCode.getBytes()));
                Fuser user = frontUserService.findById(Integer.valueOf(inviteCode));
                if (null != user) {
                    fuser.setfIntroUser_id(user);
                }
                Cookie cookie = new Cookie(Constants.INTRO_COOKIE, null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }

            // 微信绑定
            String wxOpenId = (String) request.getSession().getAttribute(Constants.WECHAT_OPEN_ID);
            if(wxBind && StringUtils.hasText(wxOpenId)){
                fuser.setWxOpenId(wxOpenId);
                request.getSession().removeAttribute(Constants.WECHAT_OPEN_ID);
            }

            frontUserService.saveRegister(fuser);

            setLoginSession(request, fuser);
            // 成功
            map.put("code", 200);

            // 同步用户系统
            ZhgUserSynUtils.synUserRegister(fuser);

            String deviceCodeKey = fuser.getFid() * 3 + "";
            String deviceBeforeCode = DigestUtils.md5Hex(fuser.getFloginName());
            Cookie loginCookie = new Cookie(deviceCodeKey, deviceBeforeCode);
            loginCookie.setPath("/");
            loginCookie.setMaxAge(7 * 24 * 60 * 60);
            loginCookie.setHttpOnly(true);
            response.addCookie(loginCookie);
            // 登录日志 记录在客户端的value值为md5（登录名）
            logService.addLoginLog(fuser, Utils.getIpAddr(request), "", deviceBeforeCode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    @RequestMapping("/reg_ok")
    public String registerSuccess() {
        return "user/register_success";
    }

    @RequestMapping(value = "/find_pwd", method = RequestMethod.GET)
    public String findPassword(HttpServletRequest request) {
        setFormToken(request);
        return "user/find_password";
    }

    /**
     * 发起找回密码
     *
     * @param request
     * @param email
     * @param mobile
     * @param code
     * @return
     */
    @RequestMapping(value = "/find_pwd", method = RequestMethod.POST)
    public
    @ResponseBody
    Object findPassword(HttpServletRequest request, @RequestParam(value = "email", required = false) String email,
                        @RequestParam(value = "mobile", required = false) String mobile,
                        @RequestParam(value = "captcha", required = true) String code,
                        @RequestParam(value = "token", required = true) String token) {
        Map<String, Object> map = new HashMap<>(5);
        if(!StringUtils.hasText(token) || !token.equals(getFormToken(request))){
            map.put("msg", "请勿重复提交");
            return map;
        }
        if (!StringUtils.hasText(mobile) && !StringUtils.hasText(email)) {
            map.put("msg", "请输入您的手机号/邮箱号");
            return map;
        }
        if (!StringUtils.hasText(code)) {
            map.put("msg", "请输入验证码");
            return map;
        }
        Captcha captcha = (Captcha) request.getSession().getAttribute(Constants.SESSION_CAPTCHA_CODE);
        if(captcha instanceof SmsMessageCaptcha && !((SmsMessageCaptcha)captcha).getCellphone().equals(mobile)){
            map.put("msg", "您输入的验证码不正确或已过期");
            return map;
        }else if(captcha instanceof EmailMessageCaptcha && !((EmailMessageCaptcha)captcha).getEmail().equals(email)){
            map.put("msg", "您输入的验证码不正确或已过期");
            return map;
        }
        if (null == captcha || captcha.isExpire() || !code.equals(captcha.getCode())) {
            map.put("msg", "您输入的验证码不正确或已过期");
            updateCaptchaLimitedCount(request, captcha);
            return map;
        }

        Fuser fuser = null;
        if (StringUtils.hasText(mobile)) {
            fuser = frontUserService.findByMobile(mobile);
        } else if (StringUtils.hasText(email)) {
            fuser = frontUserService.findByEmail(email);
        }
        if (null == fuser) {
            map.put("msg", "该用户不存在");
            return map;
        }

        if (StringUtils.hasText(mobile) && !fuser.isFisTelephoneBind()) {
            // 手机号未认证
            map.put("msg", "您输入的手机号未认证");
            return map;
        }
        if (StringUtils.hasText(email) && !fuser.getFisMailValidate()) {
            // 邮箱未认证
            map.put("msg", "您输入的邮箱未认证");
            return map;
        }

        cleanFormToken(request);
        deleteCaptchaLimitedCount(request, captcha);

        // 保存用户信息
        request.getSession().setAttribute(Constants.FIND_PASSWORD_USER, fuser);
        map.put("code", 200);
        return map;
    }

    /**
     * 新版找回密码
     *
     * @param request
     * @param email
     * @param mobile
     * @param code
     * @return
     */
    @RequestMapping(value = "/findPassword", method = RequestMethod.POST)
    public
    @ResponseBody
    Object findPassword_(HttpServletRequest request, @RequestParam(value = "email", required = false) String email,
                        @RequestParam(value = "mobile", required = false) String mobile,
                        @RequestParam(value = "captcha") String code) {
        Map<String, Object> map = new HashMap<>();
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);

        if(null == authCode){
            map.put("code", 101);
            return map;
        }
        if(CountLimitTypeEnum.TELEPHONE != authCode.getActionType() && CountLimitTypeEnum.VOICE_CAPTCHA != authCode.getActionType() && CountLimitTypeEnum.EMAIL != authCode.getActionType()){
            map.put("code", 102);
            return map;
        }
        if(StringUtils.isMobile(mobile) && !frontUserService.isTelephoneExists(mobile)){
            map.put("code", 103);
            return map;
        }
        if(StringUtils.isEmail(email) && !frontUserService.isEmailExists(email)){
            map.put("code", 104);
            return map;
        }

        int _limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), authCode.getActionType());
        if (_limit <= 0) {
            map.put("code", 105);
            map.put("leftCount", 0);
            return map;
        } else {
            if (!authCode.isEnabled(mobile, code) && !authCode.isEnabled(email, code)) {
                map.put("code", 106);
                map.put("leftCount", _limit - 1);
                this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), authCode.getActionType());
                return map;
            } else {
                this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), authCode.getActionType());
            }
            request.getSession().removeAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        }

        Fuser fuser = null;
        if (StringUtils.hasText(mobile)) {
            fuser = frontUserService.findByMobile(mobile);
        } else if (StringUtils.hasText(email)) {
            fuser = frontUserService.findByEmail(email);
        }

        // 保存用户信息
        request.getSession().setAttribute(Constants.FIND_PASSWORD_USER, fuser);
        map.put("code", 200);
        return map;
    }

    /**
     * 进入密码重置页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/reset_pwd", method = RequestMethod.GET)
    public String resetPassword(HttpServletRequest request){
        if(null == request.getSession().getAttribute(Constants.FIND_PASSWORD_USER)){
            return "redirect:/user/find_pwd.html";
        }
        setFormToken(request);
        return "user/password_reset";
    }

    /**
     *  重置密码
     *
     * @param request
     * @param loginName
     * @param password
     * @param token
     * @return
     */
    @RequestMapping(value = "/reset_pwd", method = RequestMethod.POST)
    public @ResponseBody Object resetPassword(HttpServletRequest request,
                                              @RequestParam(value = "loginName", required = true)String loginName,
                                              @RequestParam(value = "password", required = true)String password,
                                              @RequestParam(value = "token", required = true)String token){
        Map<String, Object> map = new HashMap<>(5);
        if(null == request.getSession().getAttribute(Constants.FIND_PASSWORD_USER)){
            // 不合理请求
            map.put("code", "-1");
            return map;
        }
        if(!StringUtils.hasText(token) || !token.equals(getFormToken(request))){
            map.put("msg", "请勿重复提交");
            return map;
        }
        if(!StringUtils.hasText(loginName)){
            map.put("msg", "请输入您的登录名");
            return map;
        }
        if(!StringUtils.hasText(password)){
            map.put("msg", "请输入您的新密码");
            return map;
        }
        Fuser fuser = (Fuser) request.getSession().getAttribute(Constants.FIND_PASSWORD_USER);
        if(!loginName.equals(fuser.getFloginName())){
            map.put("msg", "您输入的登录名不正确");
            return map;
        }

        fuser = frontUserService.findById(fuser.getFid());
        fuser.setFloginPassword(Utils.MD5(password));
        frontUserService.updateFuser(fuser);

        // 修改用户系统密码
        ZhgUserSynUtils.synUserPassword(fuser.getZhgOpenId(), password);

        cleanFormToken(request);
        request.getSession().removeAttribute(Constants.FIND_PASSWORD_USER);
        map.put("code", 200);
        return map;
    }

    @RequestMapping("/reset_ok")
    public String resetSuccess(HttpServletRequest request){
        if(null != getSessionUser(request)){
            return "redirect:/";
        }
        return "user/fail_success";
    }

    /**
     * 手机号是否存在
     *
     * @param mobile
     * @return
     */
    @ResponseBody
    @RequestMapping("/mobile_exist")
    public Object mobileIsExist(@RequestParam(value = "mobile", required = true)String mobile){
        Map<String, Object> map = new HashMap<>(5);
        if(!StringUtils.isMobile(mobile)){
            map.put("msg", "请输入正确的手机号");
            return map;
        }

        if(frontUserService.isTelephoneExists(mobile)){
            map.put("msg", "手机号已存在");
            return map;
        }

        // 众股用户系统
        /*if(ZhgUserSynUtils.mobileIsExist(mobile)){
            map.put("msg", "手机号已存在");
            return map;
        }*/
        map.put("code", 200);
        return map;
    }

    /**
     * 邮箱号是否存在
     *
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping("/email_exist")
    public Object emailIsExist(@RequestParam(value = "email", required = true)String email){
        Map<String, Object> map = new HashMap<>(5);
        if(!StringUtils.isEmail(email)){
            map.put("msg", "请输入正确的邮箱号");
            return map;
        }

        if(frontUserService.isEmailExists(email)){
            map.put("msg", "邮箱已存在");
            return map;
        }

        // 众股用户系统
        /*if(ZhgUserSynUtils.emailIsExist(email)){
            map.put("msg", "邮箱已存在");
            return map;
        }*/
        map.put("code", 200);
        return map;
    }

    @ResponseBody
    @RequestMapping("/user_exist")
    public Object userNameIsExist(@RequestParam(value = "loginName", required = true)String loginName){
        boolean flag = false;

        if(StringUtils.isMobile(loginName)){
            flag = frontUserService.isTelephoneExists(loginName);
//            flag = frontUserService.isTelephoneExists(loginName) || ZhgUserSynUtils.mobileIsExist(loginName);
        }
        if(!flag && StringUtils.isEmail(loginName)){
            flag = frontUserService.isEmailExists(loginName);
//            flag = frontUserService.isEmailExists(loginName) || ZhgUserSynUtils.emailIsExist(loginName);
        }
        if(!flag && StringUtils.hasText(loginName)){
            flag = frontUserService.isUserNameExists(loginName) || ZhgUserSynUtils.usernameIsExist(loginName);
        }

        return flag;
    }

    /**
     * 微信绑定(注册)
     *
     * @param request
     * @param mobile
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/wechatBind", method = RequestMethod.POST)
    public Object wechatBind(HttpServletRequest request,HttpServletResponse response, String mobile, String code){
        Map retMap = new HashMap<>();
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        if(null == authCode){
            retMap.put("code", 101);
            return retMap;
        }
        // 短信|语音
        if(CountLimitTypeEnum.TELEPHONE != authCode.getActionType() && CountLimitTypeEnum.VOICE_CAPTCHA != authCode.getActionType()){
            retMap.put("code", 102);
            return retMap;
        }
        String wxOpenId = (String) request.getSession().getAttribute(Constants.WECHAT_OPEN_ID);
        if(StringUtils.isEmpty(wxOpenId)){
            retMap.put("code", 103);
            return retMap;
        }
        // 验证码验证
        int mobil_limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), authCode.getActionType());
        if (mobil_limit <= 0) {
            retMap.put("code", 104);
            retMap.put("leftCount", 0);
            return retMap;
        } else {
            if (!authCode.isEnabled(mobile, code)) {
                retMap.put("code", 105);
                retMap.put("leftCount", mobil_limit - 1);
                this.frontValidateService.updateLimitCount(Utils.getIpAddr(request), authCode.getActionType());
                return retMap;
            }
        }

        Fuser fuser = frontUserService.findByMobile(mobile);
        if(null != fuser){
            if(StringUtils.hasText(fuser.getWxOpenId())){
                // 已绑定
                retMap.put("code", 106);
                return retMap;
            }

                // 登录
            fuser.setWxOpenId(wxOpenId);
            frontUserService.updateFUser(fuser, request.getSession());
            doLogin(request, fuser,response);
            retMap.put("code", 200);
        }else{
            // 注册新用户
            if((boolean)userNameIsExist(mobile)){
                // 已被注册
                retMap.put("code", 151);
                return retMap;
            }

            // 随机密码
            String password = Utils.randomString(8);
            try {
                doRegister(request, response,mobile, "86", null, password, null, wxOpenId);
                retMap.put("code", 202);
            } catch (Exception e) {
                e.printStackTrace();
                retMap.put("code", 152);
                return retMap;
            }
        }

        this.frontValidateService.deleteCountLimite(Utils.getIpAddr(request), authCode.getActionType());
        request.getSession().removeAttribute(Constants.SESSION_NEW_CAPTCHA_CODE);
        request.getSession().removeAttribute(Constants.WECHAT_OPEN_ID);
        return retMap;
    }

    /**
     * 微信登录
     *
     * @param request
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/wxLogin")
    public Object wxLogin(HttpServletRequest request, String code,HttpServletResponse response) {
        Map retMap = new HashMap<>();
        if(StringUtils.isEmpty(code)){
            retMap.put("code", 101);
            return retMap;
        }

        // 网页授权
        Map map = new HashMap<>();
        map.put("appid", constantMap.get("weChatAppId"));
        map.put("secret", constantMap.get("weChatSecret"));
        map.put("code", code);
        map.put("grant_type", "authorization_code");
        com.alibaba.fastjson.JSONObject tokenJson = HttpUtils.sendGetRequestForJson("https://api.weixin.qq.com/sns/oauth2/access_token", map);

        String openId = tokenJson.getString("openid");
        Fuser fuser = frontUserService.findByWxOpenId(openId);
        if(null == fuser){
            // 保存openid
            request.getSession().setAttribute(Constants.WECHAT_OPEN_ID, openId);
            request.getSession().setAttribute(Constants.NONMEMBER_SEND_CODE, true);
            // 未绑定或注册
            retMap.put("code", 102);
            return retMap;
        }

        doLogin(request, fuser,response);

        retMap.put("code", 200);
        return retMap;
    }

    private void doLogin(HttpServletRequest request, Fuser fuser,HttpServletResponse response){
        // 直接给用户登录
        // 登录日志
        String deviceBeforeCode = "";
        try{
            deviceBeforeCode = URLEncoder.encode(fuser.getFloginName() + Utils.getIpAddr(request),"UTF-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        //        添加Cookie
        response.addCookie(new Cookie(fuser.getFid() * 3 + "",deviceBeforeCode));

        logService.addLoginLog(fuser, Utils.getIpAddr(request), "", deviceBeforeCode);

        // 清理估值缓存
        request.getSession().removeAttribute(Constants.USER_ASSET);

        if (fuser.getFopenSecondValidate()) {
            setSecondLoginSession(request, fuser);
        } else {
            setLoginSession(request, fuser);
        }

        // 登录通知
        frontUserService.sendLoginNotice(request.getSession().getId(), fuser.getFid(), fuser);

        // 从用户系统同步信息
        if(StringUtils.hasText(fuser.getZhgOpenId())){
            ZhgUserSynUtils.synUserInfo(fuser.getZhgOpenId());
        }else{
            ZhgUserSynUtils.synUserRegister(fuser);
        }
    }

    private void doRegister(HttpServletRequest request,HttpServletResponse response, String mobile, String areaCode, String email, String password, String inviteCode, String wxOpenId) throws Exception {
        Fuser fuser = new Fuser();
        if (StringUtils.hasText(email)) {
            fuser.setFemail(email);
            fuser.setFnickName(email.split("@")[0]);
            fuser.setFloginName(email);
            fuser.setFisMailValidate(true);
        } else if (StringUtils.hasText(mobile)) {
            fuser.setFtelephone(mobile);
            fuser.setFnickName(mobile);
            fuser.setFloginName(mobile);
            fuser.setFisTelephoneBind(true);
            fuser.setFisTelValidate(true);
            fuser.setFareaCode(areaCode);
        }
        fuser.setWxOpenId(wxOpenId);
        fuser.setFloginPassword(Utils.MD5(password));
        fuser.setFregisterIp(Utils.getIpAddr(request));// 注册IP
        fuser.setFsourceUrl(request.getHeader("Referer"));// 来源url
                /*fuser.setFpostRealValidate(true);
                fuser.setFpostRealValidateTime(Utils.getTimestamp());
                fuser.setFidentityType(authType);
                fuser.setFrealName(realName);
                fuser.setFidentityNo(cardId);*/

        if (StringUtils.hasText(inviteCode)) {
            inviteCode = new String(Base64Utils.decode(inviteCode.getBytes()));
            Fuser user = frontUserService.findById(Integer.valueOf(inviteCode));
            if (null != user) {
                fuser.setfIntroUser_id(user);
            }
        }

        frontUserService.saveRegister(fuser);

        doLogin(request, fuser,response);
    }
}