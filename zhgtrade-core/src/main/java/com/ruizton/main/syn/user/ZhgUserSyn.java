package com.ruizton.main.syn.user;

import java.util.Date;
import java.util.Map;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/6/22
 */
public class ZhgUserSyn {
    private int type;
    private Map params;
    private Date startTime;

    public ZhgUserSyn() {
    }

    public ZhgUserSyn(int type, Map params) {
        this.type = type;
        this.params = params;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
