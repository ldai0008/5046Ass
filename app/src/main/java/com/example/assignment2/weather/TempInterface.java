package com.example.assignment2.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TempInterface {
    // get the type, id and city elements
    @GET("weather?q=Jiaxing&units=metric&appid=f66bff2ae862a90743a9712f0dd423bb")
    Call<Root> weatherSearch(@Query("units") String UNIT,
                             @Query("appid") String APP_ID,
                             @Query("q") String keyword);
}
