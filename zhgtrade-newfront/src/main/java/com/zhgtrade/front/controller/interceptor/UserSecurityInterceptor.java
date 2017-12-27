package com.zhgtrade.front.controller.interceptor;

import com.ruizton.main.model.Fuser;
import com.ruizton.util.Constants;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * DESC: 用户未认证
 * <p/>
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin(1186270005@qq.com)
 * Date： 2016-05-10 15:05
 */
public class UserSecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Fuser fuser = (Fuser) request.getSession().getAttribute(Constants.USER_LOGIN_SESSION);
        if(!fuser.getFpostRealValidate() && !Utils.isAjax(request)){
            // 未提交实名认证
            response.sendRedirect("/user/auth.html");
            return false;
        }

        return true;
    }

    private boolean isAuthenticated(HttpServletRequest request){
        // 身份认证|绑定手机|绑定邮箱 通过
        Fuser fuser = (Fuser) request.getSession().getAttribute(Constants.USER_LOGIN_SESSION);
        return fuser.isFisTelephoneBind() && StringUtils.hasText(fuser.getFtradePassword());
    }

    private boolean letItGo(HttpServletRequest request){
        String uri = request.getRequestURI();
        return uri.equals("/account/security.html") || uri.equals("/account/bindMobile.html") || uri.equals("/account/modPassword.html") || uri.equals("/account/bindEmail.html") || uri.equals("/account/auth.html");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
