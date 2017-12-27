package com.ruizton.main.Enum;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/7/15
 */
public enum UseStatus {
    Normal, Forbidden;

    public int getIndex(){
        return this.ordinal();
    }

    public static UseStatus get(int index){
        for(UseStatus status : values()){
            if(index == status.ordinal()){
                return status;
            }
        }
        return null;
    }

    public String getName(){
        if(this.equals(Normal)){
            return "正常";
        }else if(this.equals(Forbidden)){
            return "禁用";
        }
        return "";
    }
}
