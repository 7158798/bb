package com.ruizton.main.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by sunpeng on 2016/7/26.
 */
@Entity
@Table(name = "block_articletype")
public class BlockArticleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Timestamp createTime;
    private Timestamp lastUpdateTime;

    public BlockArticleType(){}

    public BlockArticleType(String name, Timestamp lastUpdateTime, Timestamp createTime) {
        this.name = name;
        this.lastUpdateTime = lastUpdateTime;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
