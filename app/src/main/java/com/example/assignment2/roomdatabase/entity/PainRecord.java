package com.example.assignment2.roomdatabase.entity;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PainRecord {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pain_id")
    public int painId;

    @ColumnInfo(name = "pain_level")
    @NonNull
    public int painLevel;

    @ColumnInfo(name = "location")
    @NonNull
    public String location;

    @ColumnInfo(name = "mood_level")
    @NonNull
    public String moodLevel;

    @ColumnInfo(name = "step_taken")
    public int stepTaken;

    @ColumnInfo(name = "current_date")
    public String currentDate;

    @ColumnInfo(name = "temperature")
    public double temperature;

    @ColumnInfo(name = "humidity")
    public int humidity;

    @ColumnInfo(name = "pressure")
    public int pressure;

    @ColumnInfo(name = "email")
    public String email;

    public PainRecord(int painLevel, @NonNull String location, @NonNull String moodLevel, int stepTaken, String currentDate, double temperature, int humidity, int pressure, String email) {
        this.painLevel = painLevel;
        this.location = location;
        this.moodLevel = moodLevel;
        this.stepTaken = stepTaken;
        this.currentDate = currentDate;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.email = email;
    }

}
