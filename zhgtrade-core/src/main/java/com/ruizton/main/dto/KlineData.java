package com.ruizton.main.dto;

import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * 比特家
 * K线JSON数据
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-01 14:21
 */
public class KlineData implements Serializable {

    private ObjectId _id;
    private int fviFid;         // 币ID
    private int key;            // 周期
    private String data;        // JSON数据

    public KlineData() {
    }

    public KlineData(int id, int key, String jsonString) {
        this.fviFid = id;
        this.key = key;
        this.data = jsonString;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public int getFviFid() {
        return fviFid;
    }

    public void setFviFid(int fviFid) {
        this.fviFid = fviFid;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
