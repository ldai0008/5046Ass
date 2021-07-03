package com.example.assignment2.roomdatabase.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.assignment2.LogIn;
import com.example.assignment2.roomdatabase.entity.PainRecord;
import com.example.assignment2.roomdatabase.repository.PainRecordRepository;

import java.text.SimpleDateFormat;

public class MainViewModel extends AndroidViewModel {
    private PainRecordRepository pRepository;
    private LiveData<PainRecord> currPainRecord;

    public MainViewModel(Application application) {
        super(application);
        pRepository = new PainRecordRepository(application);
    }
    // get live data by current date
    public void liveDataByCurrentDate() {
        currPainRecord = pRepository.liveDataByEmailAndCurrentDate(LogIn.email, new SimpleDateFormat("yyyyMMdd")
                .format(System.currentTimeMillis()));
    }

    public LiveData<PainRecord> getCurrPainRecord() {
        return currPainRecord;
    }
}
