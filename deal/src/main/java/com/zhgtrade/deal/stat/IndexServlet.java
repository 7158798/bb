package com.zhgtrade.deal.stat;

import com.alibaba.druid.util.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-06-12 10:48
 */
public class IndexServlet extends HttpServlet {

    private String resourcePath = "pages";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        process(uri, resp);
    }

    private void process(String uri, HttpServletResponse response) throws IOException {
        if (uri.equals("/")) {
            response.sendRedirect("/index.html");
            return;
        }
        if (uri.contains(".html")) {
            response.setContentType("text/html; charset=utf-8");
        }
        String filePath = getFilePath(uri);
        String text = Utils.readFromResource(filePath);
        if (text == null) {
            response.sendRedirect("/index.html");
            return;
        }
        response.getWriter().write(text);
    }

    protected String getFilePath(String fileName) {
        return resourcePath + fileName;
    }
}
