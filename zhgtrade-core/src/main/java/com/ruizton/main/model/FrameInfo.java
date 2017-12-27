package com.ruizton.main.model;

import java.util.Date;

/**
 * Created by liuge on 2016/9/22.
 */
public class FrameInfo {
    int id;
    String frameName;
    int status;
    Date createdTime;
    boolean checked;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrameName() {
        return frameName;
    }

    public void setFrameName(String frameName) {
        this.frameName = frameName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "FrameInfo{" +
                "id=" + id +
                ", frameName='" + frameName + '\'' +
                ", status=" + status +
                ", createdTime=" + createdTime +
                '}';
    }
}
