package com.sylgas.weatherapp.network.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sylgas.weatherapp.model.Coordinates;
import com.sylgas.weatherapp.model.ForecastResponse;
import com.sylgas.weatherapp.network.service.api.WeatherApiService;
import com.sylgas.weatherapp.notification.listener.RequestListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDataService {
    static final String APP_ID = "baffcb20761d04e4ac90d0b3f0f5dac1";
    static final String UNITS_METRIC = "metric";

    @NonNull
    private final WeatherApiService service;

    @Nullable
    private Call<ForecastResponse> forecastCall;

    WeatherDataService(@NonNull WeatherApiService service) {
        this.service = service;
    }

    public void requestWeatherForecast(@NonNull Coordinates coordinates,
                                       @NonNull RequestListener<ForecastResponse> listener) {
        if (forecastCall != null) {
            // forecast already requested
            return;
        }
        forecastCall = createForecastCall(coordinates);
        enqueueForecastRequest(listener);
    }

    @Nullable
    private Call<ForecastResponse> createForecastCall(@NonNull Coordinates coordinates) {
        return service.getForecastForCoordinates(APP_ID, coordinates.getLatitude(),
                coordinates.getLongitude(), UNITS_METRIC);
    }

    private void enqueueForecastRequest(@NonNull final RequestListener<ForecastResponse> listener) {
        if (forecastCall == null) {
            listener.onError();
            return;
        }
        forecastCall.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                listener.onResult(response == null ? null : response.body());
                forecastCall = null;
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                listener.onError();
                forecastCall = null;
            }
        });
    }

    public void cancelRequest() {
        if (forecastCall != null) {
            forecastCall.cancel();
            forecastCall = null;
        }
    }
}
