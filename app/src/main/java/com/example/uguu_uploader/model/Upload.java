package com.example.uguu_uploader.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity
public class Upload {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String url;
    private String customName;
    @NonNull
    private String path;
    @NonNull
    private String user;
    private long uploadDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(long uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Upload{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", customName='" + customName + '\'' +
                ", path='" + path + '\'' +
                ", user='" + user + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }
}
