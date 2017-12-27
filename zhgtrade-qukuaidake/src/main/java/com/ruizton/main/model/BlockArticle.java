package com.ruizton.main.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by sunpeng on 2016/7/25.
 */
@Entity
@Table(name = "block_article")
public class BlockArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator")
    private Fadmin creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editor")
    private Fadmin editor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleType")
    private BlockArticleType blockArticleType;
    private String title;
    private String keyword;
    private String description;
    private String imgUrl;
    private String content;
    private Timestamp createTime;
    private Timestamp lastUpdateTime;
    private String  docUrl;

    public BlockArticle(){}

    public BlockArticle(Fadmin creator, Fadmin editor, BlockArticleType blockArticleType, String title, String keyword, String description, String content, Timestamp createTime, String docUrl) {
        this.creator = creator;
        this.editor = editor;
        this.blockArticleType = blockArticleType;
        this.title = title;
        this.keyword = keyword;
        this.description = description;
        this.content = content;
        this.createTime = createTime;
        this.docUrl = docUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fadmin getCreator() {
        return creator;
    }

    public void setCreator(Fadmin creator) {
        this.creator = creator;
    }

    public Fadmin getEditor() {
        return editor;
    }

    public void setEditor(Fadmin editor) {
        this.editor = editor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public BlockArticleType getBlockArticleType() {
        return blockArticleType;
    }

    public void setBlockArticleType(BlockArticleType blockArticleType) {
        this.blockArticleType = blockArticleType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
