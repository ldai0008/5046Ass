package com.example.assignment2.roomdatabase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.assignment2.roomdatabase.entity.PainRecord;
import com.example.assignment2.roomdatabase.entity.PainRecordCount;

import java.util.List;

@Dao
public interface PainRecordDao {
    @Query("SELECT * FROM PainRecord")
    LiveData<List<PainRecord>> getAll();

    @Query("SELECT * FROM PainRecord WHERE email = :email order by `current_date` desc")
    LiveData<List<PainRecord>> getByEmail(String email);

//    @Query("SELECT * FROM PainRecord WHERE pain_Id = :painId LIMIT 1")
//    PainRecord findByID(int painId);

    @Insert
    void insert(PainRecord painRecord);

    @Query("SELECT * FROM PainRecord WHERE email = :email AND `current_date` = :currentDate LIMIT 1")
    LiveData<PainRecord> liveDataByEmailAndCurrentDate(String email, String currentDate);

    @Query("SELECT * FROM PainRecord WHERE email = :email AND `current_date` = :currentDate LIMIT 1")
    PainRecord findByEmailAndCurrentDate(String email, String currentDate);

    @Query("SELECT * FROM PainRecord WHERE email = :email and `current_date` >= :startDate and `current_date` <= :endDate order by `current_date`")
    LiveData<List<PainRecord>> liveDataByEmail(String email, String startDate, String endDate);

    @Query("SELECT * FROM PainRecord WHERE email = :email")
    List<PainRecord> findByEmail(String email);

    @Insert
    void insertAll(PainRecord... painRecord);

    @Delete
    void delete(PainRecord painRecord);

    @Query("DELETE FROM PainRecord WHERE pain_Id = :painId")
    void deleteById(int painId);

    @Update
    void updatePainRecord(PainRecord... painRecord);

    @Query("DELETE FROM PainRecord")
    void deleteAll();

    @Query("SELECT location, count(location) as countNumber FROM PainRecord group by location")
    LiveData<List<PainRecordCount>> countLocation();

}
