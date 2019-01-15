package com.example.uguu_uploader.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity
public class Upload {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String url;
    private String name;
    @NonNull
    private String user;
    private long uploadDate;
    @Ignore
    private boolean randomfilename;
    @Ignore
    private String customName;
    @Ignore
    private String path;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(long uploadDate) {
        this.uploadDate = uploadDate;
    }

    public boolean isRandomfilename() {
        return randomfilename;
    }

    public void setRandomfilename(boolean randomfilename) {
        this.randomfilename = randomfilename;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public String toString() {
        return "Upload{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", user='" + user + '\'' +
                ", uploadDate=" + uploadDate +
                ", randomfilename=" + randomfilename +
                ", customName='" + customName + '\'' +
                '}';
    }
}
