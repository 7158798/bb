package com.ruizton.main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/12/27
 */
@Entity(name = "new_year_red_wrapper")
public class NewYearRedWrapper implements Serializable {
    private int id;
    private int userId;
    private double amount;
    private short type;         // 1七嘴八舌聊天|2新闻评论|3交易
    private Date createTime;
    private Date updateTime;
    private int version;

    public final static short CHAT_TYPE = 1;                  // 七嘴八舌聊天
    public final static short NEWS_COMMENT_TYPE = 2;          // 新闻评论
    public final static short TRADE_TYPE = 3;                 // 交易

    public final static String NEW_YEAR_RED_WRAPPER_QUEUE = "new.year.red.wrapper.queue";

    public NewYearRedWrapper() {
    }

    public NewYearRedWrapper(int userId, double amount, short type, Date createTime) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.createTime = createTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column
    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Version
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
