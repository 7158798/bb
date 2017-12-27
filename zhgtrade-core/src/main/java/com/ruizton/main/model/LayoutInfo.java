package com.ruizton.main.model;

import java.util.Date;

/**
 * Created by liuge on 2016/9/22.
 */
public class LayoutInfo {
    int id;
    int frameId;
    String frameName;
    int articleId;
    String title;
    int rank;
    int status;
    Date createdTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrameId() {
        return frameId;
    }

    public void setFrameId(int frameId) {
        this.frameId = frameId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrameName() {
        return frameName;
    }

    public void setFrameName(String frameName) {
        this.frameName = frameName;
    }

    @Override
    public String toString() {
        return "LayoutInfo{" +
                "id=" + id +
                ", frameId=" + frameId +
                ", articleId=" + articleId +
                ", rank=" + rank +
                ", status=" + status +
                ", createdTime=" + createdTime +
                '}';
    }
}
