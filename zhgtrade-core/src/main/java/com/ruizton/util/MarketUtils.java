package com.ruizton.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-05-17 13:31
 */
public abstract class MarketUtils {

    /**
     * 是否开启交易
     * @param value
     * @return
     */
    public static boolean openTrade(String value) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        try {
            //Date类befor和after方法判断方式是大于或者小于，所以在本来区间上向左向右各增加1秒
            Date beforeDate = df.parse(value.trim().split("-")[0]);
            beforeDate.setTime(beforeDate.getTime() - 1000);
            Date afterDate = df.parse(value.trim().split("-")[1]);
            afterDate.setTime(afterDate.getTime() + 1000);
            Date time = df.parse(df.format(new Date()));
            if ( time.after(beforeDate) && time.before(afterDate)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("输入的时间区间不能进行格式化，时间格式为：HH:mm-HH:mm");
        }
        //抛出异常，返回false
        return false;
    }

}
