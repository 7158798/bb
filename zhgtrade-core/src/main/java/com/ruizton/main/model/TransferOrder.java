package com.ruizton.main.model;

import com.ruizton.main.Enum.MoneyType;
import com.ruizton.main.Enum.TransferOrderStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/7/13
 */
public class TransferOrder {
    private int id;
    private int userId;
    private MoneyType moneyType;            // 0人民币 1虚拟币
    private int symbol;                     // 币id(虚拟币)
    private double amount;                  // 金额
    private double fee;                     // 手续费
    private String toAppId;                 // 到账系统id
    private TransferOrderStatus status;     // 转账状态
    private Date createTime;
    private Date updateTime;                // 到账时间/取消时间
    private int version;

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

    @Column(name = "money_type")
    @Enumerated(EnumType.ORDINAL)
    public MoneyType getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(MoneyType moneyType) {
        this.moneyType = moneyType;
    }

    @Column
    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

    @Column
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column
    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    @Column(name = "to_app_id", length = 128)
    public String getToAppId() {
        return toAppId;
    }

    public void setToAppId(String toAppId) {
        this.toAppId = toAppId;
    }

    @Column
    @Enumerated(EnumType.ORDINAL)
    public TransferOrderStatus getStatus() {
        return status;
    }

    public void setStatus(TransferOrderStatus status) {
        this.status = status;
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

    public String getMoneyTypeName(){
        return this.moneyType.getName();
    }

    public String getStatusName(){
        return this.status.getName();
    }
}
