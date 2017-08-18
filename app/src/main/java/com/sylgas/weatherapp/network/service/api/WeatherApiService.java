package com.sylgas.weatherapp.network.service.api;

import com.sylgas.weatherapp.model.ForecastResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("weather")
    Call<ForecastResponse> getForecastForCoordinates(@Query("APPID") String id,
                                                     @Query("lat") double latitude,
                                                     @Query("lon") double longitude);
}
