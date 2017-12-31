package com.ruizton.main.Enum;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/7/13
 */
public enum MoneyType {
    Virtual_Coin, RMB;

    public String getName(){
        if(this.equals(MoneyType.RMB)){
            return "人民币";
        }else if(this.equals(MoneyType.Virtual_Coin)){
            return "虚拟币";
        }
        return "";
    }

    public short getIndex(){
        return (short) this.ordinal();
    }

    public static MoneyType get(int index){
        for(MoneyType type : values()){
            if(index == type.ordinal()){
                return type;
            }
        }

        return null;
    }
}
