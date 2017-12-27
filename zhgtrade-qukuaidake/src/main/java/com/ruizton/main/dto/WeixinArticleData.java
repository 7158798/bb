package com.ruizton.main.dto;

import java.io.Serializable;

/**
 * Created by sunpeng on 2016/8/1.
 */
public class WeixinArticleData implements Serializable{

    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String author;
    private String time;
    private String url;
    private String imgUrl;
    private String intro;

    public WeixinArticleData(){}

    public WeixinArticleData(int id, String title, String author, String time, String url, String imgUrl, String intro) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.time = time;
        this.url = url;
        this.imgUrl = imgUrl;
        this.intro = intro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
