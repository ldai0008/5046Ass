package com.example.assignment2.roomdatabase.entity;

import androidx.room.Entity;

@Entity
public class PainRecordCount {
    // location of the pain record
    // count of the pain record by location
    public String location;
    public int countNumber;
}
