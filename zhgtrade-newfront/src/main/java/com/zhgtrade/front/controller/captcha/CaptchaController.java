package com.zhgtrade.front.controller.captcha;

import com.ruizton.main.Enum.CountLimitTypeEnum;
import com.ruizton.main.Enum.ValidateMailStatusEnum;
import com.ruizton.main.Enum.ValidateMessageStatusEnum;
import com.ruizton.main.code.AuthCode;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvalidateemail;
import com.ruizton.main.model.Fvalidatemessage;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.FrontValidateService;
import com.ruizton.util.ConstantKeys;
import com.ruizton.util.Constants;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import com.zhgtrade.front.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin（1186270005@qq.com）
 * Date：
 */

@RestController
public class CaptchaController extends BaseController {
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private FrontUserService frontUserService;

    /**
     * 新版发送短信认证码
     *
     * @param request
     * @param mobile
     * @param areaCode
     * @param isBind
     * @return
     */
    @RequestMapping(value = "/captcha/sendSMSAuthCode", method = RequestMethod.POST)
    public Object sendSMSAuthCode(HttpServletRequest request,
                                  @RequestParam(required = false)String mobile,
                                  @RequestParam(required = false, defaultValue = "86")String areaCode,
                                  @RequestParam(required = false, defaultValue = "false")boolean isBind,
                                  @RequestParam(required = false, defaultValue = "false")boolean isLogin,
                                  @RequestParam(required = false, defaultValue = "")String code){
        if(isBind && !StringUtils.isMobile(mobile)){
            return 101;
        }

        Fuser fuser = getSessionUser(request);

        //进入认为是登录
        if(isLogin && fuser == null){
            Object loginNameObject = request.getSession().getAttribute(Constants.NOT_LASTDEVICE_LOGINNAME);
            Object passwprdObject = request.getSession().getAttribute(Constants.NOT_LASTDEVICE_PASSWORD);
            if(loginNameObject != null && passwprdObject != null){
                Fuser tempUser = new Fuser();
                tempUser.setFloginName(String.valueOf(loginNameObject));
                tempUser.setFemail(String.valueOf(loginNameObject));
                tempUser.setFloginPassword(String.valueOf(passwprdObject));
                fuser = frontUserService.updateCheckLogin(tempUser, null);
            }
             if(fuser != null){
                String imageCode = (String) request.getSession().getAttribute(Constants.IMAGE_CODE_KEY);
                if(!code.equalsIgnoreCase(imageCode)){
                    // 图形验证码错误
                    return 106;
                }
            }else{
                return 107;
            }
        }
        // 特例
        Boolean canSend = (Boolean) request.getSession().getAttribute(Constants.NONMEMBER_SEND_CODE);

        if(isBind && null == fuser){
            String imageCode = (String) request.getSession().getAttribute(Constants.IMAGE_CODE_KEY);
            if(!code.equalsIgnoreCase(imageCode)){
                // 图形验证码错误
                return 106;
            }
        }



        if(null != fuser){
            fuser = frontUserService.findById(fuser.getFid());
        }
        if(!isBind && StringUtils.isMobile(mobile) && !frontUserService.isTelephoneExists(mobile) && (null == canSend || !canSend)){
            // 非会员手机号
            return 102;
        }else if(!isBind && !StringUtils.isMobile(mobile) && (null == fuser || !fuser.isFisTelephoneBind())){
            // 未绑定
            return 103;
        }else if(!isBind && !StringUtils.isMobile(mobile)){
            mobile = fuser.getFtelephone();
        }

        if(isBind && frontUserService.isTelephoneExists(mobile)){
            // 手机号存在
            return 104;
        }

        int limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), CountLimitTypeEnum.TELEPHONE);
        if(limit <= 0){
            return 105;
        }

        // 生成验证码
        AuthCode authCode = new AuthCode(mobile, Utils.randomInteger(6), CountLimitTypeEnum.TELEPHONE);
        request.getSession().setAttribute(Constants.SESSION_NEW_CAPTCHA_CODE, authCode);

        // 保存发送记录
        Fvalidatemessage fvalidatemessage = new Fvalidatemessage() ;
        fvalidatemessage.setFcontent(this.constantMap.getString(ConstantKeys.VALIDATE_MESSAGE_CONTENT).replace("#code#", authCode.getCode())) ;
        fvalidatemessage.setFcreateTime(authCode.getDateTime()) ;
        fvalidatemessage.setFphone(mobile) ;
        fvalidatemessage.setFstatus(ValidateMessageStatusEnum.Not_send) ;
        if(null != fuser){
            Fuser user = new Fuser();
            user.setFid(fuser.getFid());
            fvalidatemessage.setFuser(user);
        }
        this.frontValidateService.addFvalidateMessage(fvalidatemessage) ;
        //发送短信队列通知
        messageQueueService.publish(QueueConstants.MESSAGE_COMMON_QUEUE, fvalidatemessage);

        request.getSession().removeAttribute(Constants.IMAGE_CODE_KEY);
        return 200;
    }

    /**
     * 语音验证码
     *
     * @param request
     * @param mobile
     * @param areaCode
     * @param isBind
     * @return
     */
    @RequestMapping(value = "/captcha/sendVoiceAuthCode", method = RequestMethod.POST)
    public Object sendVoiceAuthCode(HttpServletRequest request,
                                   @RequestParam(required = false)String mobile,
                                   @RequestParam(required = false, defaultValue = "86")String areaCode,
                                   @RequestParam(required = false, defaultValue = "false")boolean isBind,
                                    @RequestParam(required = false, defaultValue = "false")boolean isLogin,
                                    @RequestParam(required = false, defaultValue = "")String code){
        if(isBind && !StringUtils.isMobile(mobile)){
            return 101;
        }
        Fuser fuser = getSessionUser(request);

        if(isLogin && fuser == null){
            Object loginNameObject = request.getSession().getAttribute(Constants.NOT_LASTDEVICE_LOGINNAME);
            Object passwprdObject = request.getSession().getAttribute(Constants.NOT_LASTDEVICE_PASSWORD);
            if(loginNameObject != null && passwprdObject != null){
                Fuser tempUser = new Fuser();
                tempUser.setFloginName(String.valueOf(loginNameObject));
                tempUser.setFemail(String.valueOf(loginNameObject));
                tempUser.setFloginPassword(String.valueOf(passwprdObject));
                fuser = frontUserService.updateCheckLogin(tempUser, null);
            }
            if(fuser != null){
                String imageCode = (String) request.getSession().getAttribute(Constants.IMAGE_CODE_KEY);
                if(!code.equalsIgnoreCase(imageCode)){
                    // 图形验证码错误
                    return 106;
                }
            }else{
                return 107;
            }
        }
        // 特例
        Boolean canSend = (Boolean) request.getSession().getAttribute(Constants.NONMEMBER_SEND_CODE);

        if(isBind && null == fuser){
            String imageCode = (String) request.getSession().getAttribute(Constants.IMAGE_CODE_KEY);
            if(!code.equalsIgnoreCase(imageCode)){
                // 图形验证码错误
                return 106;
            }
        }

        if(null != fuser){
            fuser = frontUserService.findById(fuser.getFid());
        }
        if(!isBind && StringUtils.isMobile(mobile) && !frontUserService.isTelephoneExists(mobile) && (null == canSend || !canSend)){
            // 非会员手机号
            return 102;
        }else if(!isBind && !StringUtils.isMobile(mobile) && (null == fuser || !fuser.isFisTelephoneBind())){
            // 未绑定
            return 103;
        }else if(!isBind && !StringUtils.isMobile(mobile)){
            mobile = fuser.getFtelephone();
        }

        if(isBind && frontUserService.isTelephoneExists(mobile)){
            // 手机号存在
            return 104;
        }

        int limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), CountLimitTypeEnum.VOICE_CAPTCHA);
        if(limit <= 0){
            return 105;
        }

        // 生成验证码
        AuthCode authCode = new AuthCode(mobile, Utils.randomInteger(6), CountLimitTypeEnum.VOICE_CAPTCHA);
        request.getSession().setAttribute(Constants.SESSION_NEW_CAPTCHA_CODE, authCode);

        // 保存发送记录
        Fvalidatemessage fvalidatemessage = new Fvalidatemessage() ;
        fvalidatemessage.setType(1);
        fvalidatemessage.setFcontent(authCode.getCode()) ;
        fvalidatemessage.setFcreateTime(authCode.getDateTime()) ;
        fvalidatemessage.setFphone(mobile) ;
        fvalidatemessage.setFstatus(ValidateMessageStatusEnum.Not_send) ;
        if(null != fuser){
            Fuser user = new Fuser();
            user.setFid(fuser.getFid());
            user.setFhasRealValidate(fuser.getFhasRealValidate());
            user.setFrealName(fuser.getFrealName());
            fvalidatemessage.setFuser(user);
        }
        this.frontValidateService.addFvalidateMessage(fvalidatemessage) ;
        //发送语音队列通知
        messageQueueService.publish(QueueConstants.VOICE_COMMON_QUEUE, fvalidatemessage);

        request.getSession().removeAttribute(Constants.IMAGE_CODE_KEY);
        return 200;
    }

    /**
     * 新版发送邮箱验证码
     *
     * @param request
     * @param email
     * @param isBind
     * @return
     */
    @RequestMapping(value = "/captcha/sendEmailAuthCode", method = RequestMethod.POST)
    public Object sendEmailAuthCode(HttpServletRequest request,
                                    @RequestParam(required = false)String email,
                                    @RequestParam(required = false, defaultValue = "false")boolean isBind,
                                    @RequestParam(required = false, defaultValue = "false")boolean isLogin,
                                    @RequestParam(required = false, defaultValue = "")String code){
        if(isBind && !StringUtils.isEmail(email)){
            return 101;
        }
        Fuser fuser = getSessionUser(request);
        // 特例
        Boolean canSend = (Boolean) request.getSession().getAttribute(Constants.NONMEMBER_SEND_CODE);
        if(isBind && null == fuser){
            String imageCode = (String) request.getSession().getAttribute(Constants.IMAGE_CODE_KEY);
            if(!code.equalsIgnoreCase(imageCode)){
                // 图形验证码错误
                return 106;
            }
        }

        // 登录时发送邮箱验证码。
        if(isLogin && fuser == null){
            Object loginNameObject = request.getSession().getAttribute(Constants.NOT_LASTDEVICE_LOGINNAME);
            Object passwprdObject = request.getSession().getAttribute(Constants.NOT_LASTDEVICE_PASSWORD);
            if(loginNameObject != null && passwprdObject != null){
                Fuser tempUser = new Fuser();
                tempUser.setFloginName(String.valueOf(loginNameObject));
                tempUser.setFemail(String.valueOf(loginNameObject));
                tempUser.setFloginPassword(String.valueOf(passwprdObject));
                fuser = frontUserService.updateCheckLogin(tempUser, null);
            }
            if(fuser != null){
                String imageCode = (String) request.getSession().getAttribute(Constants.IMAGE_CODE_KEY);
                if(!code.equalsIgnoreCase(imageCode)){
                    // 图形验证码错误
                    return 106;
                }
            }else{
                return 107;
            }
        }

        if(null != fuser){
            fuser = frontUserService.findById(fuser.getFid());
        }
        if(!isBind && StringUtils.isEmail(email) && !frontUserService.isEmailExists(email) && (null == canSend || !canSend)){
            // 非会员邮箱号
            return 102;
        }else if(!isBind && !StringUtils.isEmail(email) && (null == fuser || !fuser.getFisMailValidate())){
            // 未绑定
            return 103;
        }else if(!isBind && !StringUtils.isEmail(email)){
            email = fuser.getFemail();
        }

        if(isBind && frontUserService.isEmailExists(email)){
            // 邮箱号存在
            return 104;
        }

        int limit = this.frontValidateService.getLimitCount(Utils.getIpAddr(request), CountLimitTypeEnum.EMAIL);
        if(limit <= 0){
            return 105;
        }

        // 生成验证码
        AuthCode authCode = new AuthCode(email, Utils.randomInteger(6), CountLimitTypeEnum.EMAIL);
        request.getSession().setAttribute(Constants.SESSION_NEW_CAPTCHA_CODE, authCode);

        // 保存发送记录
        Fvalidateemail fvalidateemail = new Fvalidateemail();
        fvalidateemail.setEmail(email);
        fvalidateemail.setFtitle(this.constantMap.getString(ConstantKeys.englishName)+"验证码") ;
        fvalidateemail.setFcontent(
                this.constantMap.getString(ConstantKeys.mailCodeValidateContent)
                        .replace("#firstLevelDomain#", this.constantMap.getString(ConstantKeys.firstLevelDomain))
                        .replace("#code#", authCode.getCode())
                        .replace("#fulldomain#", this.constantMap.getString(ConstantKeys.fulldomain))
                        .replace("#englishName#", this.constantMap.getString(ConstantKeys.englishName))) ;
        fvalidateemail.setFcreateTime(authCode.getDateTime()) ;
        fvalidateemail.setFstatus(ValidateMailStatusEnum.Not_send) ;
        if(null != fuser){
            Fuser user = new Fuser();
            user.setFid(fuser.getFid());
            user.setFemail(email);
            fvalidateemail.setFuser(user);
        }
        this.frontValidateService.addFvalidateemail(fvalidateemail);

        //加入邮件队列
        messageQueueService.publish(QueueConstants.EMAIL_COMMON_QUEUE, fvalidateemail);

        request.getSession().removeAttribute(Constants.IMAGE_CODE_KEY);
        return 200;
    }
}
