package com.ruizton.main.Enum;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/7/13
 */
public enum TransferOrderStatus {
    ALL, PAID, SUCCESSED, CANCELED;

    public String getName(){
        if(this.equals(TransferOrderStatus.PAID)){
            return "已支付";
        }else if(this.equals(TransferOrderStatus.SUCCESSED)){
            return "已到账";
        }else if(this.equals(TransferOrderStatus.CANCELED)){
            return "已取消";
        }
        return "";
    }
}
