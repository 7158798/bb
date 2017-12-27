package com.ruizton.main.model;

/**
 * Created by liuge on 2016/9/7.
 */
public class KeywordInfo {
    int id;
    int classId;
    String className;
    String keyName;
    int status;
    boolean checked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "KeywordInfo{" +
                "id=" + id +
                ", classId=" + classId +
                ", keyName='" + keyName + '\'' +
                ", status=" + status +
                '}';
    }
}
