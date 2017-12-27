package com.ruizton.main.model;

import java.util.Date;

/**
 * Created by liuge on 2016/9/2.
 */
public class InformationArticleType {
    int id;
    String className;
    int headlineId;
    String headlineTitle;
    String imgPath;
    String content;
    Date createdTime;
    int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getHeadlineId() {
        return headlineId;
    }

    public void setHeadlineId(int headlineId) {
        this.headlineId = headlineId;
    }

    public String getHeadlineTitle() {
        return headlineTitle;
    }

    public void setHeadlineTitle(String headlineTitle) {
        this.headlineTitle = headlineTitle;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InformationArticleType{" +
                "id=" + id +
                ", className='" + className + '\'' +
                ", headlineId=" + headlineId +
                ", headlineTitle='" + headlineTitle + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", content='" + content + '\'' +
                ", createdTime=" + createdTime +
                ", status=" + status +
                '}';
    }
}
