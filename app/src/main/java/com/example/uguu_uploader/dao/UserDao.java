package com.example.uguu_uploader.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.uguu_uploader.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAll();

    @Query("SELECT * FROM User WHERE username = :username")
    User getByUsername(String username);

    @Query("SELECT * FROM User WHERE id = :id")
    User getById(long id);

    @Insert
    long insert(User u);

    @Delete
    void delete(User u);
}