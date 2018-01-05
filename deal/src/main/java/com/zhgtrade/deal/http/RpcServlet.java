package com.zhgtrade.deal.http;

import com.zhgtrade.deal.market.DepthCalculateQueue;
import com.zhgtrade.deal.model.FentrustData;
import main.Application;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2018/1/4
 */
public class RpcServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FentrustData fentrustData = new FentrustData();
        fentrustData.setFamount(Double.valueOf(req.getParameter("amount")) );
        fentrustData.setFstatus(Integer.valueOf(req.getParameter("status")) );
        fentrustData.setFleftCount(Double.valueOf(req.getParameter("leftCount")));
        fentrustData.setFprize(Double.valueOf(req.getParameter("prize")));
        fentrustData.setFviFid(Integer.valueOf(req.getParameter("id")));
        fentrustData.setFentrustType(Integer.valueOf(req.getParameter("type")));
        fentrustData.setDeep(4);
        Application application = Application.getInstance();
        DepthCalculateQueue depthCalculateQueue = application.getBean(DepthCalculateQueue.class);
        depthCalculateQueue.calculateDepthEntrust(fentrustData);
    }
}
