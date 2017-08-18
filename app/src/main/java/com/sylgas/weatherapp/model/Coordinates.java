package com.sylgas.weatherapp.model;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

public final class Coordinates {
    @SerializedName("lon")
    private final double longitude;

    @SerializedName("lat")
    private final double latitude;

    public Coordinates(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Coordinates(Location location) {
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
