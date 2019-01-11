package com.example.uguu_uploader.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.uguu_uploader.model.User;

@Database(entities = {User.class}, version = 3, exportSchema = false)
public abstract class UguuDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    private static volatile UguuDatabase INSTANCE;

    public static UguuDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UguuDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            UguuDatabase.class,
                            "uguu_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
