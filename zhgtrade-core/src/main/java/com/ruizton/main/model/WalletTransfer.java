package com.ruizton.main.model;

import com.ruizton.main.Enum.MoneyType;
import com.ruizton.main.Enum.WalletTransferStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/7/25
 */
@Entity
@Table(name = "fwallet_transfer")
public class WalletTransfer implements Serializable {
    private int id;
    private int userId;
    private int virtualCoinId;
    private String tradeNo;     // 交易流水号
    private MoneyType moneyType;      // 0虚拟币|1人民币
    private boolean active;     // true主动转账|false到账
    private String fromSystem;           // 转账系统
    private String toSystem;             // 到账系统
    private double amount;
    private WalletTransferStatus status;         // 转账状态 0已付款|1进行中|2已到账|3已退款
    private double fee;         // 手续费
    private Date createTime;
    private Date updateTime;
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

    @Column(name = "coin_id")
    public int getVirtualCoinId() {
        return virtualCoinId;
    }

    public void setVirtualCoinId(int virtualCoinId) {
        this.virtualCoinId = virtualCoinId;
    }

    @Column(name = "trade_no", length = 32)
    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "money_type")
    public MoneyType getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(MoneyType moneyType) {
        this.moneyType = moneyType;
    }

    @Column
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Column(length = 30, name = "from_system")
    public String getFromSystem() {
        return fromSystem;
    }

    public void setFromSystem(String fromSystem) {
        this.fromSystem = fromSystem;
    }

    @Column(length = 30, name = "to_system")
    public String getToSystem() {
        return toSystem;
    }

    public void setToSystem(String toSystem) {
        this.toSystem = toSystem;
    }

    @Column
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column
    @Enumerated(EnumType.ORDINAL)
    public WalletTransferStatus getStatus() {
        return status;
    }

    public void setStatus(WalletTransferStatus status) {
        this.status = status;
    }

    @Column
    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
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
        return MoneyType.RMB.equals(this.moneyType);
    }

    @Transient
    public boolean isVirtualCoin(){
        return MoneyType.Virtual_Coin.equals(this.moneyType);
    }
}
