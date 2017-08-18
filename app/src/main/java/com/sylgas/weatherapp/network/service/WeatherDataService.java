package com.sylgas.weatherapp.network.service;

import com.sylgas.weatherapp.model.Coordinates;
import com.sylgas.weatherapp.model.ForecastResponse;
import com.sylgas.weatherapp.network.service.api.WeatherApiService;
import com.sylgas.weatherapp.notification.listener.RequestListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherDataService {
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String APP_ID = "baffcb20761d04e4ac90d0b3f0f5dac1";

    private final WeatherApiService service;
    private Call<ForecastResponse> forecastCall;

    public WeatherDataService() {
        final Retrofit adapter = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.service = adapter.create(WeatherApiService.class);
    }

    public void requestWeatherForecast(Coordinates coordinates, final RequestListener<ForecastResponse> listener) {
        if (forecastCall != null) {
            // forecast already requested
            return;
        }
        forecastCall = service.getForecastForCoordinates(APP_ID, coordinates.getLatitude(),
                coordinates.getLongitude());
        forecastCall.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                listener.onResult(response == null ? null : response.body());
                forecastCall = null;
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                listener.onError(t);
                forecastCall = null;
            }
        });
    }

    public void cancelRequest() {
        forecastCall.cancel();
        forecastCall = null;
    }
}
