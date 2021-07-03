package com.example.assignment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.example.assignment2.TimeReceiver")) {
            Toast.makeText(context, "Enter daily record!", Toast.LENGTH_LONG).show();
        }
    }
}
