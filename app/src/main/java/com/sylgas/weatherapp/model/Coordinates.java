package com.sylgas.weatherapp.model;

import android.location.Location;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Coordinates {
    @SerializedName("lon")
    private final double longitude;

    @SerializedName("lat")
    private final double latitude;

    public Coordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Coordinates(@NonNull Location location) {
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
