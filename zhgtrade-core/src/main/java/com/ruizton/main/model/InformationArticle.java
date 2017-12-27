package com.ruizton.main.model;
import java.util.Date;
import java.util.Map;

/**
 * Created by liuge on 2016/9/2.
 */
public class InformationArticle {

    int id;
    String title;
    int classId;
    String className;
    int authorId;
    String imgPath;
    String content;
    Date createdTime;
    String sources;
    String originalText;
    Date roundTime;
    int readingNum;
    int commentNum;
    int status;

    String authorName;

    int articleId;
    String financingRound;
    int capital;

    public InformationArticle(){}

    public InformationArticle(int id, String title, int classId, String className, int authorId, String imgPath, String content, Date createdTime, String sources, int readingNum, int commentNum, int status, String authorName, Date roundTime) {
        this.id = id;
        this.title = title;
        this.classId = classId;
        this.className = className;
        this.authorId = authorId;
        this.imgPath = imgPath;
        this.content = content;
        this.createdTime = createdTime;
        this.sources = sources;
        this.readingNum = readingNum;
        this.commentNum = commentNum;
        this.status = status;
        this.authorName = authorName;
        this.roundTime = roundTime;
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

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
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

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public int getReadingNum() {
        return readingNum;
    }

    public void setReadingNum(int readingNum) {
        this.readingNum = readingNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getFinancingRound() {
        return financingRound;
    }

    public void setFinancingRound(String financingRound) {
        this.financingRound = financingRound;
    }

    public int getCapital() {
        return capital;
    }

    public void setCapital(int capital) {
        this.capital = capital;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public Date getRoundTime() {
        return roundTime;
    }

    public void setRoundTime(Date roundTime) {
        this.roundTime = roundTime;
    }

    @Override
    public String toString() {
        return "InformationArticle{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", classId=" + classId +
                ", authorId=" + authorId +
                ", imgPath='" + imgPath + '\'' +
                ", content='" + content + '\'' +
                ", createdTime=" + createdTime +
                ", sources='" + sources + '\'' +
                ", readingNum=" + readingNum +
                ", commentNum=" + commentNum +
                ", status=" + status +
                ", authorName='" + authorName + '\'' +
                '}';
    }
}
