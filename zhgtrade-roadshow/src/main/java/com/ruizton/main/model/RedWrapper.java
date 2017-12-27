package com.ruizton.main.model;

import com.ruizton.main.Enum.RedWrapperStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

/**
 * 大红包
 *
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/12/19
 */
@Entity(name = "red_wrapper")
public class RedWrapper implements Serializable {
    private int id;
    private int userId;                // 红包发送人
    private String title;               // 红包标题
    private double amount;              // 金额
    private double refundAmount;        // 退还金额
    private int count;                  // 子红包个数
    private RedWrapperStatus status;    // 红包状态
    private int coinType;               // 币种 0人民币|虚拟币id
    private short type;                 // 红包类型 1拼手气|2普通
    private int roadShowId;             // 路演id
    private Date createTime;
    private Date updateTime;
    private int version;

    public final static String CACHE_RED_WRAPPER_PREFIX = "cache:redWrapper:";
    public final static String CACHE_RED_WRAPPER_COUNT = ":count";
    public final static String CACHE_RED_WRAPPER_SUB = ":sub";
    public final static String CACHE_RED_WRAPPER_USERS = ":users";
    public final static String CACHE_RED_WRAPPER_USERS_SUB = ":uSub";
    public final static String CACHE_RED_WRAPPER_EXPIRE = ":expire";
    public final static int EXPIRE_SECONDES = 3 * 24 * 60 * 60;

    public RedWrapper() {
    }

    public RedWrapper(int userId, String title, double amount, int count, RedWrapperStatus status, int coinType, short type, Date createTime) {
        this.userId = userId;
        this.title = title;
        this.amount = amount;
        this.count = count;
        this.status = status;
        this.coinType = coinType;
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

    @Column(length = 125)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column(name = "refund_amount")
    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    @Column
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Column
    @Enumerated(EnumType.ORDINAL)
    public RedWrapperStatus getStatus() {
        return status;
    }

    public void setStatus(RedWrapperStatus status) {
        this.status = status;
    }

    @Column(name = "coin_type")
    public int getCoinType() {
        return coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    @Column
    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    @Column(name = "road_show_id")
    public int getRoadShowId() {
        return roadShowId;
    }

    public void setRoadShowId(int roadShowId) {
        this.roadShowId = roadShowId;
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

    @Transient
    public boolean isRMB(){
        return 0 == this.coinType;
    }

    @Transient
    public boolean isExpire(){
        return System.currentTimeMillis() - this.createTime.getTime() > EXPIRE_SECONDES * 1000;
    }
}
