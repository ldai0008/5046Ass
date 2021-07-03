package com.example.assignment2.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.assignment2.roomdatabase.entity.PainRecord;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PainRecordWorkTool {
    public static final Integer EXECUTE_HOUR = 22;
    public static final Integer EXECUTE_MINUTE = 0;
    public static final String DATA_KEY = "PAIN_RECORD";
    private Context context;

    public PainRecordWorkTool(Context context) {
        this.context = context;
    }

    public void enqueue() {
        enqueue(EXECUTE_HOUR, 0);
    }

    public void enqueue(int hour) {
        enqueue(hour, 0);
    }

    public void enqueue(int hour, int minute) {
        WorkRequest workRequest = new OneTimeWorkRequest.Builder(PainRecordWork.class)
                .setInitialDelay(getDelayTimes(hour, minute), TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(workRequest);
        Log.i("PainRecordWorkTool", "PainRecord enqueue");
    }

    /**
     * get delay time
     * @param hour
     * @param minute
     * @return
     */
    private long getDelayTimes(int hour, int minute) {
        long delayTimes = 0;
        long currTimes = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        //  current time is larger the calendar time, then the day goes next day
        if (currTimes > calendar.getTimeInMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        delayTimes = calendar.getTimeInMillis() - currTimes;

        return delayTimes;
    }
}
