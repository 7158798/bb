package com.ruizton.main.dto;

import java.util.Date;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/12/20
 */
public class SubRedWrapperDTO {
    private int userId;
    private int redWrapperId;
    private String username;
    private String headImage;
    private double amount;
    private Date catchTime;
    private boolean bastOfLuck;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRedWrapperId() {
        return redWrapperId;
    }

    public void setRedWrapperId(int redWrapperId) {
        this.redWrapperId = redWrapperId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCatchTime() {
        return catchTime;
    }

    public void setCatchTime(Date catchTime) {
        this.catchTime = catchTime;
    }

    public boolean isBastOfLuck() {
        return bastOfLuck;
    }

    public void setBastOfLuck(boolean bastOfLuck) {
        this.bastOfLuck = bastOfLuck;
    }
}
