package com.example.uguu_uploader.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.OpenableColumns;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
    private Uri uri;

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

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
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
                ", uri='" + uri + '\'' +
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
        dest.writeString(uri.toString());
        dest.writeString(user);
        dest.writeLong(uploadDate);
        dest.writeByte((byte) (randomfilename ? 1 : 0));
        dest.writeString(customName);
    }

    public static final Creator<Upload> CREATOR = new Creator<Upload>() {
        @Override
        public Upload createFromParcel(Parcel source) {
            Upload u = new Upload();
            u.setId(source.readLong());
            u.setUrl(source.readString());
            u.setName(source.readString());
            u.setUri(Uri.parse(source.readString()));
            u.setUser(source.readString());
            u.setUploadDate(source.readLong());
            u.setRandomfilename(source.readByte() != 0);
            u.setCustomName(source.readString());
            return u;
        }

        @Override
        public Upload[] newArray(int size) {
            return new Upload[size];
        }
    };

    // URI queries
    public String queryName(ContentResolver cr) {
        Cursor returnCursor = cr.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    public String queryType(ContentResolver cr) {
        return cr.getType(uri);
    }

    public byte[] queryBody(ContentResolver cr) {
        try {
            InputStream is = cr.openInputStream(uri);
            if (is == null)
                return null;
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int read;
            while ((read = is.read(buffer)) != -1) {
                if (read == 0)
                    break;
                byteBuffer.write(buffer, 0, read);
            }
            return byteBuffer.toByteArray();

        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
