package com.example.assignment2.roomdatabase.repository;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.assignment2.LogIn;
import com.example.assignment2.roomdatabase.dao.PainRecordDao;
import com.example.assignment2.roomdatabase.database.PainRecordDatabase;
import com.example.assignment2.roomdatabase.entity.PainRecord;
import com.example.assignment2.roomdatabase.entity.PainRecordCount;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;


public class PainRecordRepository {
    private PainRecordDao painRecordDao;
    private LiveData<List<PainRecord>> allPainRecords;
    private LiveData<List<PainRecord>> emailPainRecords;

    public PainRecordRepository(Application application) {
        PainRecordDatabase db = PainRecordDatabase.getInstance(application);
        painRecordDao = db.painRecordDao();
        allPainRecords = painRecordDao.getAll();
        emailPainRecords = painRecordDao.getByEmail(LogIn.email);
    }

    // Room executes this query on a separate thread
    public LiveData<List<PainRecord>> getAllPainRecords() {
        return allPainRecords;
    }
    // get the pain record by email
    public LiveData<List<PainRecord>> geteEmailPainRecords() {
        return emailPainRecords;
    }
    // insert function
    public void insert(final PainRecord painRecord) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDao.insert(painRecord);
            }
        });
    }
    // delete all function
    public void deleteAll() {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDao.deleteAll();
            }
        });
    }
    // update function
    public void insertOrUpdate(final PainRecord painRecord) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {

                if (painRecord == null) {
                    return;
                }

                PainRecord originPainRecord = painRecordDao.findByEmailAndCurrentDate(painRecord.email, painRecord.currentDate);
                if (originPainRecord == null) {
                    painRecordDao.insert(painRecord);
                } else {
                    painRecord.painId = originPainRecord.painId;
                    painRecordDao.updatePainRecord(painRecord);
                }
            }
        });
    }
    // get live data of location count
    public LiveData<List<PainRecordCount>> countLocation() {
        return painRecordDao.countLocation();
    }
    // get live data by email and current date
    public LiveData<PainRecord> liveDataByEmailAndCurrentDate(String email, String currentDate) {
        return painRecordDao.liveDataByEmailAndCurrentDate(email, currentDate);
    }
    // get live data by email and start date and end date
    public LiveData<List<PainRecord>> liveDataByEmail(String email, String startDate, String endDate) {
        return painRecordDao.liveDataByEmail(email, startDate, endDate);
    }
}
