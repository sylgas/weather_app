package com.sylgas.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public final class WindForecast {
    @SerializedName("speed")
    private double speed;

    @SerializedName("deg")
    private double degrees;

    public double getSpeed() {
        return speed;
    }

    public double getDegrees() {
        return degrees;
    }
}
