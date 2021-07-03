package com.example.assignment2.roomdatabase.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.assignment2.roomdatabase.entity.PainRecord;
import com.example.assignment2.roomdatabase.repository.PainRecordRepository;

import java.util.List;

public class PainWeatherReportViewModel extends AndroidViewModel  {
    private PainRecordRepository pRepository;
    private LiveData<List<PainRecord>> painRecords;

    public PainWeatherReportViewModel(Application application) {
        super(application);
        pRepository = new PainRecordRepository(application);
    }

    public void liveDataByEmail(String email, String startDate, String endDate) {
        painRecords = pRepository.liveDataByEmail(email, startDate, endDate);
    }

    public LiveData<List<PainRecord>> getPainRecords() {
        return painRecords;
    }
}
