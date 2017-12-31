package com.zhgtrade.front.controller;

import com.ruizton.main.model.Fuser;
import com.ruizton.util.Constants;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/11/1
 */
public class ApiBaseController extends BaseController {

    protected Object forSuccessResult(Object data){
        return forSuccessResult(data, 1);
    }

    protected Object forSuccessResult(Object data, int pageCount){
        Map map = new HashMap<>();
        map.put("code", 200);
        map.put("data", data);
        map.put("pageCount", pageCount);
        return map;
    }

    protected Object forFailureResult(Object data, int code, String message){
        Map map = new HashMap<>();
        map.put("code", code);
        map.put("data", data);
        map.put("message", message);
        return map;
    }

    protected Object forFailureResult(int code){
        return forFailureResult(null, code, null);
    }

    /**
     * api异常
     *
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected Object exceptionHandler(Exception e){
        e.printStackTrace();
        return forFailureResult("", 500, "系统异常");
    }

    /**
     * 登录用户
     *
     * @param session
     * @return
     */
    protected Fuser getLoginUser(HttpSession session){
        return (Fuser) session.getAttribute(Constants.USER_LOGIN_SESSION);
    }

}
