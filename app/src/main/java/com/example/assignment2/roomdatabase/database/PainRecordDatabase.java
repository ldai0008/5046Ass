package com.example.assignment2.roomdatabase.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.assignment2.roomdatabase.dao.PainRecordDao;
import com.example.assignment2.roomdatabase.entity.PainRecord;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PainRecord.class}, version = 1, exportSchema = false)
public abstract  class PainRecordDatabase extends RoomDatabase {
    public abstract PainRecordDao painRecordDao();
    private static PainRecordDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized PainRecordDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    PainRecordDatabase.class, "PainRecordDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}
