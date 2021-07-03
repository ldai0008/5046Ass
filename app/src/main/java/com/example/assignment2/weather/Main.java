package com.example.assignment2.weather;

import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    public double temp;
    @SerializedName("pressure")
    public int pressure;
    @SerializedName("humidity")
    public int humidity;
    /**
     * Get method
     * @return the temperature
     */
    public double getTemp() {
        return temp;
    }
    /**
     * Mutator method
     * @param   temp  the new temperature
     */
    public void setTemp(double temp) {
        this.temp = temp;
    }
    /**
     * Get method
     * @return the pressure
     */
    public int getPressure() {
        return pressure;
    }
    /**
     * Mutator method
     * @param   pressure the new pressure
     */
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }
    /**
     * Get method
     * @return the humidity
     */
    public int getHumidity() {
        return humidity;
    }
    /**
     * Mutator method
     * @param   humidity the new humidity
     */
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }



}
