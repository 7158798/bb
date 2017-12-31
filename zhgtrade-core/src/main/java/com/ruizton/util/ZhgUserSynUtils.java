package com.ruizton.util;

import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.syn.user.ZhgUserSyn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.TreeMap;

/**
 * 比特家 用户系统同步信息
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/6/21
 */
public class ZhgUserSynUtils {
    private static ConstantMap constantMap = null;
    private static MessageQueueService messageQueueService = null;

    private static Logger logger = LoggerFactory.getLogger(ZhgUserSynUtils.class);

    protected static ConstantMap getConstantMap() {
        if(null == constantMap){
            constantMap = SpringContextUtils.getBean(ConstantMap.class);
        }
        return constantMap;
    }

    protected static  MessageQueueService getmesMessageQueueService(){
        if(null == messageQueueService){
            messageQueueService = SpringContextUtils.getBean(MessageQueueService.class);
        }
        return messageQueueService;
    }

    public static final int MODIFY_USER_INFO = 1;
    public static final int REGISTER_USER_INFO = 2;
    public static final int SYN_USER_INFO = 3;  // 远程同步信息到本地

    public static String MOBILE_KEY = "mobile";
    public static String EMAIL_KEY = "email";
    public static String PASSWORD_KEY = "password";
    public static String OPEN_ID_KEY = "open_id";
    public static String REAL_NAME_KEY = "real_name";
    public static String CARDI_ID_KEY = "identify_no";
    public static String CARDI_TYPE_KEY = "identify_type";
    public static String USERNAME_KEY = "user_name";
    public static String DEL_MOBILE_KEY = "delMobile";

    protected static TreeMap<String, Object> getParamMap(){
        TreeMap map = new TreeMap();
        map.put("app_id", getConstantMap().getString(ConstantKeys.USER_SYS_APP_ID));
        map.put("app_secret", getConstantMap().getString(ConstantKeys.USER_SYS_APP_SECRET));
        return map;
    }

    protected static TreeMap signParamMap(TreeMap map){
        map.put("sign", SignatureUtil.getSign(map));
        map.remove("app_secret");
        return map;
    }

    public static boolean userSynIsOpened(){
        return "true".equals(getConstantMap().getString(ConstantKeys.USER_SYS_SYN_SWITCH));
    }

    protected static void addToQueue(ZhgUserSyn zhgUserSyn){
        getmesMessageQueueService().publish(QueueConstants.SYNC_ZHG_USER_SYSTEM_QUEUE, zhgUserSyn);
    }

    public static void synUserMobile(String openId, String mobile){
        if(StringUtils.isEmpty(openId) || !userSynIsOpened()) return;

        TreeMap map = getParamMap();
        map.put(OPEN_ID_KEY, openId);
        map.put(MOBILE_KEY, mobile);

        addToQueue(new ZhgUserSyn(MODIFY_USER_INFO, signParamMap(map)));
    }

    public static void synDelUserMobile(String openId){
        if(StringUtils.isEmpty(openId) || !userSynIsOpened()) return;

        TreeMap map = getParamMap();
        map.put(OPEN_ID_KEY, openId);
        map.put(DEL_MOBILE_KEY, true);

        addToQueue(new ZhgUserSyn(MODIFY_USER_INFO, signParamMap(map)));
    }

    public static void synUserAuth(String openId, String realName, String cardId, String cardType){
        if(StringUtils.isEmpty(openId) || !userSynIsOpened()) return;

        TreeMap map = getParamMap();
        map.put(OPEN_ID_KEY, openId);
        map.put(REAL_NAME_KEY, realName);
        map.put(CARDI_ID_KEY, cardId);
        map.put(CARDI_TYPE_KEY, cardType);

        addToQueue(new ZhgUserSyn(MODIFY_USER_INFO, signParamMap(map)));
    }

    public static void synUserEmail(String openId, String email){
        if(StringUtils.isEmpty(openId) || !userSynIsOpened()) return;

        TreeMap map = getParamMap();
        map.put(OPEN_ID_KEY, openId);
        map.put(EMAIL_KEY, email);

        addToQueue(new ZhgUserSyn(MODIFY_USER_INFO, signParamMap(map)));
    }

    public static void synUserPassword(String openId, String password){
        if(StringUtils.isEmpty(openId) || !userSynIsOpened()) return;

        TreeMap map = getParamMap();
        map.put(OPEN_ID_KEY, openId);
        map.put(PASSWORD_KEY, password);

        addToQueue(new ZhgUserSyn(MODIFY_USER_INFO, signParamMap(map)));
    }

    public static void synUserRegister(Fuser fuser){
        if(!userSynIsOpened()) return;

        TreeMap map = getParamMap();
        map.put(USERNAME_KEY, fuser.getFloginName());
        map.put(PASSWORD_KEY, fuser.getFloginPassword());
        /*map.put(CARDI_TYPE_KEY, fuser.getFidentityType() + "");
        if(StringUtils.hasText(fuser.getFemail())){
            map.put(EMAIL_KEY, fuser.getFemail());
        }
        if(StringUtils.hasText(fuser.getFtelephone())){
            map.put(MOBILE_KEY, fuser.getFtelephone());
        }
        if(StringUtils.hasText(fuser.getFrealName())){
            map.put(REAL_NAME_KEY, fuser.getFrealName());
        }
        if(StringUtils.hasText(fuser.getFidentityNo())){
            map.put(CARDI_ID_KEY, fuser.getFidentityNo());
        }*/

        addToQueue(new ZhgUserSyn(REGISTER_USER_INFO, signParamMap(map)));
    }

    public static void synUserInfo(String openId){
        if(StringUtils.isEmpty(openId) || !userSynIsOpened()) return;

        TreeMap map = getParamMap();
        map.put(OPEN_ID_KEY, openId);
        addToQueue(new ZhgUserSyn(SYN_USER_INFO, signParamMap(map)));
    }

    public static boolean emailIsExist(String email){
        if(StringUtils.isEmpty(email) || !userSynIsOpened()) return false;

        TreeMap map = getParamMap();
        map.put(EMAIL_KEY, email);
        String response = HttpUtils.sendPostRequest(constantMap.getString(ConstantKeys.USER_SYN_EMAIL_EXIST_URL), signParamMap(map));
        return "false".equals(response);
    }

    public static boolean mobileIsExist(String mobile){
        if(StringUtils.isEmpty(mobile) || !userSynIsOpened()) return false;

        TreeMap map = getParamMap();
        map.put(MOBILE_KEY, mobile);
        String response = HttpUtils.sendPostRequest(constantMap.getString(ConstantKeys.USER_SYN_MOBILE_EXIST_URL), signParamMap(map));
        return "false".equals(response);
    }

    public static boolean usernameIsExist(String username){
        if(StringUtils.isEmpty(username) || !userSynIsOpened()) return false;

        TreeMap map = getParamMap();
        map.put("username", username);
        String response = HttpUtils.sendPostRequest(constantMap.getString(ConstantKeys.USER_SYN_USERNAME_EXIST_URL), signParamMap(map));
        return "false".equals(response);
    }

    // 用户登录(本地不存在该用户 则同步过来)
    public static Object synUserLogin(String loginName, String password){
        if(!userSynIsOpened()) return "-2";

        TreeMap map = getParamMap();
        map.put(USERNAME_KEY, loginName);
        map.put(PASSWORD_KEY, password);

        try{
            JSONObject jsonResponse = HttpUtils.sendPostRequestForJson(constantMap.getString(ConstantKeys.USER_SYS_LOGIN_URL), signParamMap(map));
            if ("1".equals(jsonResponse.getString("ret"))) {
                // 登录成功
                JSONObject data = jsonResponse.getJSONObject("data");
                FrontUserService frontUserService = SpringContextUtils.getBean(FrontUserService.class);

                // mobile,email,real_name,sex,identify_type,identify_no,open_id
                String openId = data.getString(OPEN_ID_KEY);
                String mobile = data.getString(MOBILE_KEY);
                String email = data.getString(EMAIL_KEY);
                String realName = data.getString(REAL_NAME_KEY);
                String cardId = data.getString("identifyNo");
                String cardType = data.getString("identifyType");

                Fuser fuser = new Fuser();
                List<Fuser> list = frontUserService.findUserByProperty("floginName", loginName);
                if(CollectionUtils.isEmpty(list)){
                    // 注册到本地
                    fuser.setFloginName(loginName);
                    fuser.setFnickName(loginName);
                    if (StringUtils.isEmail(email)) {
                        fuser.setFemail(email);
                        fuser.setFisMailValidate(true);
                    }
                    if (StringUtils.isMobile(mobile)) {
                        fuser.setFtelephone(mobile);
                        fuser.setFisTelephoneBind(true);
                        fuser.setFisTelValidate(true);
                    }
                    fuser.setFregisterTime(Utils.getTimestamp());
                    fuser.setFloginPassword(Utils.MD5(password));
                    fuser.setZhgOpenId(openId);
                    if(StringUtils.hasText(realName)){
                        fuser.setFrealName(realName);
                    }
                    if(StringUtils.hasText(cardId)){
                        fuser.setFidentityNo(cardId);
                    }
                    if(StringUtils.hasText(cardType)){
                        fuser.setFidentityType(Integer.valueOf(cardType));
                    }
                    if(StringUtils.hasText(realName) && StringUtils.hasText(cardId)){
                        fuser.setFpostRealValidate(true);
                        fuser.setFpostRealValidateTime(Utils.getTimestamp());
                    }

                    frontUserService.saveRegister(fuser);
                }else{
                    // 同步信息到本地
                    fuser = list.get(0);
                    fuser.setZhgOpenId(openId);
                    fuser.setFloginPassword(Utils.MD5(password));
                    /*if (StringUtils.isMobile(mobile)) {
                        fuser.setFisTelephoneBind(true);
                        fuser.setFisTelValidate(true);
                        fuser.setFtelephone(mobile);
                    }
                    if (StringUtils.isEmail(email)) {
                        fuser.setFemail(email);
                        fuser.setFisMailValidate(true);
                    }
                    if(!fuser.getFhasRealValidate()){
                        if (StringUtils.hasText(realName)) {
                            fuser.setFrealName(realName);
                        }
                        if (StringUtils.hasText(cardId)) {
                            fuser.setFidentityNo(cardId);
                        }
                        if (StringUtils.hasText(cardType)) {
                            fuser.setFidentityType(Integer.valueOf(cardType));
                        }
                        if(StringUtils.hasText(fuser.getFidentityNo()) && StringUtils.hasText(fuser.getFrealName())){
                            fuser.setFpostRealValidate(true);
                            fuser.setFpostRealValidateTime(Utils.getTimestamp());
                        }
                    }*/
                    frontUserService.updateFuser(fuser);
                }
                return fuser;
            }else if("0".equals(jsonResponse.getString("ret"))){
                // 用户名或密码错误
                return "-1";
            }else{
                logger.error("比特家用户系统登录出错，响应内容：" + jsonResponse);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("比特家用户系统登录出错", e);
        }

        return "-2";
    }
}

















