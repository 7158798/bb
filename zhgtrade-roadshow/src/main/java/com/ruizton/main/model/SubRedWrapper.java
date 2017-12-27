package com.ruizton.main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 子红包记录
 *
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/12/19
 */
@Entity(name = "sub_red_wrapper")
public class SubRedWrapper {
    private int id;
    private int redWrapperId;              // 父级红包
    private int userId;                        // 领取用户
    private double amount;                      // 红包金额
    private Date createTime;
    private Date updateTime;
    private int version;

    public SubRedWrapper() {
    }

    public SubRedWrapper(int redWrapperId, int userId, double amount, Date createTime, Date updateTime) {
        this.redWrapperId = redWrapperId;
        this.userId = userId;
        this.amount = amount;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "red_wrapper_id")
    public int getRedWrapperId() {
        return redWrapperId;
    }

    public void setRedWrapperId(int redWrapperId) {
        this.redWrapperId = redWrapperId;
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

    @Column
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
