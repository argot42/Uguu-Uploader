package com.example.uguu_uploader.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import io.reactivex.annotations.NonNull;

@Entity
public class Upload implements Parcelable {

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


    public enum State {
        FAIL,
        SUCCESS,
        UPLOADING
    }

    public State getStatus() {
        if (url == null)
            return State.UPLOADING;
        switch(url) {
            case "":
                return State.UPLOADING;
            case "fail":
                return State.FAIL;
            default:
                return State.SUCCESS;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(url);
        dest.writeString(name);
        dest.writeString(user);
        dest.writeLong(uploadDate);
    }

    public static final Creator<Upload> CREATOR = new Creator<Upload>() {
        @Override
        public Upload createFromParcel(Parcel source) {
            Upload u = new Upload();
            u.setId(source.readLong());
            u.setUrl(source.readString());
            u.setName(source.readString());
            u.setUser(source.readString());
            u.setUploadDate(source.readLong());
            return u;
        }

        @Override
        public Upload[] newArray(int size) {
            return new Upload[size];
        }
    };
}
