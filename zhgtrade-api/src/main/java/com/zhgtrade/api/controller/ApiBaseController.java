package com.zhgtrade.api.controller;

import com.ruizton.main.model.Fuser;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/7/13
 */
public class ApiBaseController {
    @Autowired
    private FrontUserService frontUserService;

    protected Fuser getFuser(HttpServletRequest request){
        Fuser fuser = (Fuser) request.getAttribute(Constants.USER_LOGIN_SESSION);
        if(null == fuser){
            Integer userId = (Integer) request.getAttribute(Constants.USER_ID);
            fuser = frontUserService.findById(userId);
            request.setAttribute(Constants.USER_LOGIN_SESSION, fuser);
        }

        return fuser;
    }

}
