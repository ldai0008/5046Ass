package com.example.assignment2.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Root {
    @SerializedName("weather")
    public List<Weather> weather;
    @SerializedName("main")
    public Main main;
    /**
     * Get method
     * @return the weather
     */
    public List<Weather> getWeather() {
        return weather;
    }
    /**
     * Mutator method
     *
     */
    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }
    /**
     * Get method
     * @return the main
     */
    public Main getMain() {
        return main;
    }
    /**
     * Mutator method
     *
     */
    public void setMain(Main main) {
        this.main = main;
    }
}
