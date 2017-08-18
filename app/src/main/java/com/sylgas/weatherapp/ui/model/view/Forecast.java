package com.sylgas.weatherapp.ui.model.view;

import android.support.annotation.Nullable;

public class Forecast {
    @Nullable
    private String title;

    @Nullable
    private String description;

    @Nullable
    private String temperature;

    @Nullable
    private String pressure;

    @Nullable
    private String humidity;

    @Nullable
    private String windSpeed;

    @Nullable
    private String windDegrees;

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(@Nullable String temperature) {
        this.temperature = temperature;
    }

    @Nullable
    public String getPressure() {
        return pressure;
    }

    public void setPressure(@Nullable String pressure) {
        this.pressure = pressure;
    }

    @Nullable
    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(@Nullable String humidity) {
        this.humidity = humidity;
    }

    @Nullable
    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(@Nullable String windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Nullable
    public String getWindDegrees() {
        return windDegrees;
    }

    public void setWindDegrees(@Nullable String windDegrees) {
        this.windDegrees = windDegrees;
    }
}
