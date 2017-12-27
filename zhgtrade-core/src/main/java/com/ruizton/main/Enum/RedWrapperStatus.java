package com.ruizton.main.Enum;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/12/19
 */
public enum RedWrapperStatus {
    Default, Normal, Finished, Refunded, Part_Refunded;

    public int getIndex(){
        return this.ordinal();
    }
}
