package com.example.assignment2.roomdatabase.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.assignment2.LogIn;
import com.example.assignment2.roomdatabase.entity.PainRecord;
import com.example.assignment2.roomdatabase.entity.PainRecordCount;
import com.example.assignment2.roomdatabase.repository.PainRecordRepository;

import java.text.SimpleDateFormat;
import java.util.List;

public class PainRecordCountViewModel extends AndroidViewModel {
    private PainRecordRepository pRepository;
    private LiveData<List<PainRecordCount>> painRecordCounts;
    private LiveData<PainRecord> painRecord;

    public PainRecordCountViewModel(Application application) {
        super(application);
        pRepository = new PainRecordRepository(application);
        painRecordCounts = pRepository.countLocation();
        painRecord = pRepository.liveDataByEmailAndCurrentDate(LogIn.email, new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis()));
    }

    public LiveData<List<PainRecordCount>> getPainRecordCounts() {
        return painRecordCounts;
    }

    public LiveData<PainRecord> getPainRecord() {
        return painRecord;
    }
}
