package com.sylgas.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public class WindForecast {
    @SerializedName("speed")
    private final double speed;

    @SerializedName("deg")
    private final double degrees;

    public WindForecast(double speed, double degrees) {
        this.speed = speed;
        this.degrees = degrees;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDegrees() {
        return degrees;
    }
}
