package com.ruizton.main.Enum;

/**
 * DESC: 网上充值类型
 * <p/>
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin(1186270005@qq.com)
 * Date： 2016-05-23 19:29
 */
public enum ChargePayTypeEnum {

    Default, _95epay_EBank, Wechat_Scan_Code;

    public static ChargePayTypeEnum get(int payType){
        for(ChargePayTypeEnum type : values()){
            if(payType == type.ordinal()){
                return type;
            }
        }
        return ChargePayTypeEnum.Default;
    }

    public int getIndex(){
        return this.ordinal();
    }

    public String getName(){
        String name = "线下充值";
        switch (this.ordinal()){
            case 1:
                name = "双乾网银支付";
            break;
            case 2:
                name = "微信扫码支付";
            break;
        }
        return name;
    }

}
