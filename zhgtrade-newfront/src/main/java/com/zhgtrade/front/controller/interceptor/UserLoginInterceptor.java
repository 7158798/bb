package com.zhgtrade.front.controller.interceptor;

import com.alibaba.fastjson.JSON;
import com.ruizton.util.Constants;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * DESC: 处理未登录用户
 * <p/>
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp(xxly68@qq.com)
 * Date： 2016-05-10 14:46
 */
public class UserLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!isLogin(request)){
            if(Utils.isAjax(request)){
                PrintWriter out = response.getWriter();
                response.setContentType("application/json;charset=utf-8");
                out.write("{\"code\":401}");
                out.flush();
                out.close();
            }else{
                // 登录跳转url
                String forwardUrl = request.getRequestURI();
                String queryStr = request.getQueryString();
                if(StringUtils.hasText(queryStr)){
                    forwardUrl += "?" + queryStr;
                }
                request.getSession().setAttribute(Constants.FORWARD_URL, forwardUrl);
                response.sendRedirect("/");
            }
            return false;
        }
        return true;
    }

    private boolean isLogin(HttpServletRequest request){
        return null != request.getSession().getAttribute(Constants.USER_LOGIN_SESSION);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(null != request.getSession().getAttribute(Constants.USER_LOGIN_SESSION) && null != request.getSession().getAttribute(Constants.FORWARD_URL)){
            request.getSession().removeAttribute(Constants.FORWARD_URL);
        }
    }
}
