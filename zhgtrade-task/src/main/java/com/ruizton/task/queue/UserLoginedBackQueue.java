package com.ruizton.task.queue;

import com.ruizton.main.Enum.ValidateMailStatusEnum;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.dto.UserDto;
import com.ruizton.main.model.Flog;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvalidateemail;
import com.ruizton.main.mq.MessageListener;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.admin.LogService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.FrontValidateService;
import com.ruizton.util.CollectionUtils;
import com.ruizton.util.DateUtils;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2017/1/18
 */
public class UserLoginedBackQueue implements MessageListener<UserDto> {
    @Autowired
    private LogService logService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private MessageQueueService messageQueueService;

    @PostConstruct
    public void init(){
        messageQueueService.subscribe(QueueConstants.USER_LOGINED_BACK_QUEUE, this, UserDto.class);
    }

    @Override
    public void onMessage(UserDto message) {
        // 检查异地登陆
        checkLoginIp(message);
    }

    protected String buildEmail(UserDto dto, String floginName, Flog curLog){
        StringBuilder emailTmpl = new StringBuilder();
        emailTmpl.append("<div style=\"border:1px solid #CCC;background:#F4F4F4;width:100%;text-align:left\">");
        emailTmpl.append("<div style=\"border:none;background:#FCFCFC;padding:20px;color:#333;font-size:14px;\">");
        emailTmpl.append("<p>尊敬的用户：</p><p>您好！</p><p>感谢您使用比特家服务。系统监测到您的帐号");
        emailTmpl.append(floginName);
        emailTmpl.append("出现异常登录行为，请对照登录时间，确认是否为您本人操作：</p>");
        emailTmpl.append("<table class=\"loginInfo\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin-top:20px;margin-bottom:20px;border-bottom:thin solid black;border-right:thin solid black;\"><tbody><tr><!--<th style=\"border-top:thin solid black;border-left:thin solid black;text-align:left;width:150px;font-weight:normal;padding:2px 0 2px 5px;font-size:12px;\">登录地点</th>--><th style=\"border-top:thin solid black;border-left:thin solid black;text-align:left;width:150px;font-weight:normal;padding:2px 0 2px 5px;font-size:12px;\">登录IP</th><th style=\"border-top:thin solid black;border-left:thin solid black;text-align:left;width:150px;font-weight:normal;padding:2px 0 2px 5px;font-size:12px;\">登录时间</th></tr><tr><!--<td style=\"border-top:thin solid black;border-left:thin solid black;text-align:left;width:150px;font-weight:normal;padding:2px 0 2px 5px;font-size:12px;\"></td>--><td style=\"border-top:thin solid black;border-left:thin solid black;text-align:left;width:150px;font-weight:normal;padding:2px 0 2px 5px;font-size:12px;\">");
        emailTmpl.append(curLog.getFkey3());
        emailTmpl.append("</td><td style=\"border-top:thin solid black;border-left:thin solid black;text-align:left;width:150px;font-weight:normal;padding:2px 0 2px 5px;font-size:12px;\"><span style=\"border-bottom: 1px dashed rgb(204, 204, 204); position: relative;\" t=\"5\" times=\" 00:57\" isout=\"0\">");
        emailTmpl.append(DateUtils.formatDate(curLog.getFcreateTime()));
        emailTmpl.append("</span> ").append(DateUtils.formatDate(curLog.getFcreateTime(), "HH:mm"));
        emailTmpl.append("</td></tr></tbody></table><p style=\"margin:0px;padding:0px;line-height:24px;font-size:12px;color:#333333;font-family:'宋体',arial,sans-serif;\">如非本人操作，则可能您的帐号存在安全风险，请点击如下链接修改密码，以保障您的帐号安全。</p><p style=\"margin:0px;padding:0px;\"><a href=\"");
        emailTmpl.append("http://www.btc58.cc/user/find_pwd.html");
        emailTmpl.append("\" style=\"line-height:24px;font-size:12px;font-family:arial,sans-serif;color:#0000cc\" target=\"_blank\">");
        emailTmpl.append("http://www.btc58.cc/user/find_pwd.html");
        emailTmpl.append("</a></p><p style=\"margin:0px;padding:0px;line-height:24px;font-size:12px;color:#979797;font-family:'宋体',arial,sans-serif;\">(如果您无法点击此链接，请将它复制到浏览器地址栏后访问)</p><p>ZHGTRADE运营团队</p><p style=\"height:20px; border-top:1px solid #CCC\"></p><p>系统发信，请勿回复</p><p>ZHGTRADE官方网站：<a href=\"");
        emailTmpl.append("http://www.btc58.cc");
        emailTmpl.append("\" target=\"_blank\">").append("http://www.btc58.cc").append("</a></p></div></div><br><br></div>");
        return emailTmpl.toString();
    }

    protected void checkLoginIp(UserDto dto){
        List<Flog> flogList = logService.list(0, 2, "where fkey1='" + dto.getUserId() + "' order by fid desc", true);

        if(CollectionUtils.isEmpty(flogList)){
            return;
        }

        Flog curLog = flogList.get(0);
        if(1 == flogList.size()){
            addDeviceCode(curLog);
            return;
        }
        Flog preLog = flogList.get(1);
        //        cookie是否一致
        if(!StringUtils.isEmpty(preLog.getFkey5())){
            if( preLog.getFkey5().equals(curLog.getFkey5())){
                return;
            }else{
//        不一致的话将此次登陆也作为常用设备，但还是会以邮件提示用户
                curLog.setFkey5(preLog.getFkey5());
                logService.updateObj(curLog);
            }
        }else{
            addDeviceCode(curLog);
        }

        if(!DateUtils.formatDate(dto.getTimestamp()).equals(DateUtils.formatDate(curLog.getFcreateTime()))){
            // 非同一天记录
            return;
        }


//        if(StringUtils.isEmpty(curLog.getFkey3()) || StringUtils.isEmpty(preLog.getFkey3())){
//            // 当前或上一次没有ip记录
//            return;
//        }
//        if(curLog.getFkey3().equals(preLog.getFkey3())){
//            return;
//        }

        // ip不一致 发送邮件
        // 保存发送记录
        Fuser fuser = frontUserService.findById(dto.getUserId());
        if(!StringUtils.isEmail(fuser.getFemail())){
            // 未绑定邮箱
            return;
        }

        Fvalidateemail fvalidateemail = new Fvalidateemail();
        fvalidateemail.setFuser(fuser);
        fvalidateemail.setEmail(fuser.getFemail());
        fvalidateemail.setFtitle("比特家账号安全提醒") ;
        fvalidateemail.setFcontent(buildEmail(dto, fuser.getFloginName(), curLog));
        fvalidateemail.setFcreateTime(Utils.getTimestamp()) ;
        fvalidateemail.setFstatus(ValidateMailStatusEnum.Not_send) ;
        this.frontValidateService.addFvalidateemail(fvalidateemail);

        //加入邮件队列
        messageQueueService.publish(QueueConstants.EMAIL_COMMON_QUEUE, fvalidateemail);
    }

    private void addDeviceCode(Flog log){
        if(log != null && !StringUtils.isEmpty(log.getFkey3())){
            String deviceBeforeCode = "";
            try{
                deviceBeforeCode = URLEncoder.encode(log.getFkey2() + log.getFkey3(),"UTF-8");
                log.setFkey5(deviceBeforeCode);
                logService.updateObj(log);
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
    }
}
