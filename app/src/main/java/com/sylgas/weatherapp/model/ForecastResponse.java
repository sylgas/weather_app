package com.sylgas.weatherapp.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ForecastResponse {
    @Nullable
    @SerializedName("name")
    private String name;

    @Nullable
    @SerializedName("weather")
    private Weather[] weathers;

    @Nullable
    @SerializedName("coord")
    private Coordinates coordinates;

    @Nullable
    @SerializedName("main")
    private Main main;

    @Nullable
    @SerializedName("wind")
    private WindForecast windForecast;

    @Nullable
    public Weather[] getWeathers() {
        return weathers;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Nullable
    public Main getMain() {
        return main;
    }

    @Nullable
    public WindForecast getWindForecast() {
        return windForecast;
    }
}
