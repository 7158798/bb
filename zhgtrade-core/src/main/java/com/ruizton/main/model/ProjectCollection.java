package com.ruizton.main.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by sunpeng on 2016/7/5.
 */
@Entity
@Table(name = "project_collection")
public class ProjectCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int uid;
    @Column(name = "coin_id")
    private int cid;
    private Timestamp createTime;

    @Transient
    private String fname;
    @Transient
    private String furl;

    public  ProjectCollection(){}

    public ProjectCollection(int uid, int cid, Timestamp createTime) {
        this.uid = uid;
        this.cid = cid;
        this.createTime = createTime;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFurl() {
        return furl;
    }

    public void setFurl(String furl) {
        this.furl = furl;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ProjectCollection{" +
                "id=" + id +
                ", uid=" + uid +
                ", cid=" + cid +
                ", fname='" + fname + '\'' +
                ", furl='" + furl + '\'' +
                '}';
    }
}
