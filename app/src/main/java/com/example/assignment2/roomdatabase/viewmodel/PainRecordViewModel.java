package com.example.assignment2.roomdatabase.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.assignment2.roomdatabase.entity.PainRecord;
import com.example.assignment2.roomdatabase.entity.PainRecordCount;
import com.example.assignment2.roomdatabase.repository.PainRecordRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PainRecordViewModel extends AndroidViewModel {
    private PainRecordRepository pRepository;
    private LiveData<List<PainRecord>> allPainRecords;
    private LiveData<List<PainRecord>> emailPainRecords;

    public PainRecordViewModel(Application application) {
        super(application);
        pRepository = new PainRecordRepository(application);
        allPainRecords = pRepository.getAllPainRecords();
        emailPainRecords = pRepository.geteEmailPainRecords();
    }

    public LiveData<List<PainRecord>> getAllPainRecords() {
        return allPainRecords;
    }

    public LiveData<List<PainRecord>> getEmailPainRecords() {
        return emailPainRecords;
    }

    public void insert(PainRecord painRecord) {
        pRepository.insert(painRecord);
    }

    public void deleteAll() {
        pRepository.deleteAll();
    }

    public void insertOrUpdate(PainRecord painRecord) {
        pRepository.insertOrUpdate(painRecord);
    }

    public LiveData<List<PainRecordCount>> countLocation() {
        return pRepository.countLocation();
    }


}
