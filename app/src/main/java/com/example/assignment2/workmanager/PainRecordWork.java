package com.example.assignment2.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.assignment2.LogIn;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PainRecordWork extends Worker {
    private static final String TAG = "PainRecordWork";

    public PainRecordWork(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Log.i(TAG, "PainRecordWork begin");
//            Thread.sleep(1000);
            updateFirebase();

            Log.i(TAG, "PainRecordWork end");

            // next time
            PainRecordWorkTool painRecordWorkTool = new PainRecordWorkTool(getApplicationContext());
            painRecordWorkTool.enqueue();

        } catch (Exception e) {

        }

        return Result.success();
    }

    private void updateFirebase() {
        if (LogIn.currPainRecord == null) {
            return;
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("painRecords").child(LogIn.currPainRecord.currentDate).setValue(LogIn.currPainRecord);
        Log.i(TAG, "updateFirebase done");
    }
}
