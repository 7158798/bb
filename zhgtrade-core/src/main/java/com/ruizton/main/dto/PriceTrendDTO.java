package com.ruizton.main.dto;

/**
 * DESC:
 * <p/>
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp(xxly68@qq.com)
 * Date： 2016-05-18 17:16
 */
public class PriceTrendDTO {

    private Object[] xAxis;
    private Object[] yAxis;

    public PriceTrendDTO() {
    }

    public PriceTrendDTO(Object[] xAxis, Object[] yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public Object[] getxAxis() {
        return xAxis;
    }

    public void setxAxis(Object[] xAxis) {
        this.xAxis = xAxis;
    }

    public Object[] getyAxis() {
        return yAxis;
    }

    public void setyAxis(Object[] yAxis) {
        this.yAxis = yAxis;
    }
}
