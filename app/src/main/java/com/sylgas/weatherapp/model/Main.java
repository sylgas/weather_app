package com.sylgas.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public final class Main {
    @SerializedName("temp")
    private final double temperature;

    @SerializedName("pressure")
    private final double pressure;

    @SerializedName("humidity")
    private final byte humidity;

    public Main(float temperature, short pressure, byte humidity) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public byte getHumidity() {
        return humidity;
    }
}
