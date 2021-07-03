package com.example.assignment2.weather;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("main")
    public String main;
    /**
     * Get method
     * @return the main
     */
    public String getMain() {
        return main;
    }
    /**
     * Mutator method
     *
     */
    public void setMain(String main) {
        this.main = main;
    }






}
