package com.example.uguu_uploader.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.uguu_uploader.model.Upload;

import java.util.List;

@Dao
public interface UploadDao {
    @Query("SELECT * FROM Upload")
    List<Upload> getAll();

    @Query("SELECT * FROM Upload WHERE id = :id")
    Upload getById(long id);

    @Insert
    long insert(Upload u);

    @Update
    long update(Upload u);

    @Delete
    void delete(Upload u);
}
