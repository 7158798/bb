package com.ruizton.main.dto;

import java.util.Date;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2017/1/18
 */
public class UserDto {
    private int userId;
    private Date timestamp;

    public UserDto() {
    }

    public UserDto(int userId, Date timestamp) {
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
