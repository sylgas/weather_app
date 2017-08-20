package com.sylgas.weatherapp.network.service;

import android.support.annotation.NonNull;

import com.sylgas.weatherapp.network.service.api.WeatherApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherDataServiceFactory {
    private static final String WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/";

    @NonNull
    public WeatherDataService create() {
        final Retrofit adapter = new Retrofit.Builder()
                .baseUrl(WEATHER_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final WeatherApiService apiService = adapter.create(WeatherApiService.class);
        return new WeatherDataService(apiService);
    }
}
