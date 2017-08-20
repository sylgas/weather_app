package com.sylgas.weatherapp.ui.model;

import android.support.annotation.Nullable;

import com.sylgas.weatherapp.R;
import com.sylgas.weatherapp.model.Coordinates;
import com.sylgas.weatherapp.model.ForecastResponse;
import com.sylgas.weatherapp.model.Main;
import com.sylgas.weatherapp.model.Weather;
import com.sylgas.weatherapp.model.WindForecast;
import com.sylgas.weatherapp.network.service.WeatherDataService;
import com.sylgas.weatherapp.notification.listener.RequestListener;
import com.sylgas.weatherapp.ui.model.observable.ObservableErrorMessage;
import com.sylgas.weatherapp.ui.model.observable.ObservableForecast;

import java.util.Locale;

public class WeatherActivityModelImpl implements WeatherActivityModel {
    private final RequestListener<ForecastResponse> modelChangedListener = new WeatherForecastChangeListener();

    private final ObservableForecast observableForecast;
    private final ObservableErrorMessage observableErrorMessage;

    private final WeatherDataService weatherDataService;

    public WeatherActivityModelImpl(WeatherDataService weatherDataService,
                                    ObservableForecast observableForecast,
                                    ObservableErrorMessage observableErrorMessage) {
        this.weatherDataService = weatherDataService;
        this.observableForecast = observableForecast;
        this.observableErrorMessage = observableErrorMessage;
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
        weatherDataService.cancelRequest();
    }

    @Override
    public void onRefresh(Coordinates coordinates) {
        weatherDataService.requestWeatherForecast(coordinates, modelChangedListener);
    }

    @Override
    public void onLocationFailure() {
        observableErrorMessage.setMessage(R.string.gps_service_error);
    }

    private void handleForecastResponse(ForecastResponse forecastResponse) {
        final Weather[] weathers = forecastResponse.getWeathers();
        if (weathers != null && weathers.length > 0) {
            observableForecast.setTitle(forecastResponse.getName());
            final Weather weather = weathers[0];
            if (weather != null) {
                processWeather(weather);
            }
            final Main main = forecastResponse.getMain();
            if (main != null) {
                processMain(main);
            }
            final WindForecast windForecast = forecastResponse.getWindForecast();
            if (windForecast != null) {
                processWindForecast(windForecast);
            }
        }
        observableForecast.notifyChange();
    }

    private void processWeather(Weather weather) {
        observableForecast.setDescription(String.format("%s, %s", weather.getTitle(), weather.getDescription()));
    }

    private void processMain(Main main) {
        observableForecast.setTemperature(String.format(Locale.getDefault(),
                "%.0f\u00b0C", main.getTemperature()));
        observableForecast.setPressure(String.format(Locale.getDefault(),
                "%.0f hPa", main.getPressure()));
        observableForecast.setHumidity(String.format(Locale.getDefault(), "%d%%", main.getHumidity()));
    }

    private void processWindForecast(WindForecast windForecast) {
        observableForecast.setWindDegrees(String.format(Locale.getDefault(),
                "%.0f\u00b0", windForecast.getDegrees()));
        observableForecast.setWindSpeed(String.format(Locale.getDefault(),
                "%.0f km/h", windForecast.getSpeed()));
    }

    private void handleErrorResponse() {
        observableErrorMessage.setMessage(R.string.weather_service_error);
    }

    private class WeatherForecastChangeListener implements RequestListener<ForecastResponse> {
        @Override
        public void onResult(@Nullable ForecastResponse result) {
            if (result == null) {
                handleErrorResponse();
            } else {
                handleForecastResponse(result);
            }
        }

        @Override
        public void onError(Throwable e) {
            handleErrorResponse();
        }
    }
}
