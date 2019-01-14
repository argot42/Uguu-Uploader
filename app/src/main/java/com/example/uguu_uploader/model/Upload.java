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

    @Override
    public String toString() {
        return "Upload{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", customName='" + customName + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
