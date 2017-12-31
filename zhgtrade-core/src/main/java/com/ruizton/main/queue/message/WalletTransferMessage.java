package com.ruizton.main.queue.message;

import com.ruizton.main.model.WalletTransfer;

import java.util.Date;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/7/27
 */
public class WalletTransferMessage {
    private WalletTransfer walletTransfer;
    private Date earliestSendTime;

    public WalletTransferMessage() {
    }

    public WalletTransferMessage(WalletTransfer walletTransfer) {
        this.walletTransfer = walletTransfer;
    }

    public WalletTransfer getWalletTransfer() {
        return walletTransfer;
    }

    public void setWalletTransfer(WalletTransfer walletTransfer) {
        this.walletTransfer = walletTransfer;
    }

    public Date getEarliestSendTime() {
        return earliestSendTime;
    }

    public void setEarliestSendTime(Date earliestSendTime) {
        this.earliestSendTime = earliestSendTime;
    }
}
