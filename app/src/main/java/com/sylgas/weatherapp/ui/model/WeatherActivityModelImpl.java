package com.sylgas.weatherapp.ui.model;

import com.sylgas.weatherapp.model.Coordinates;
import com.sylgas.weatherapp.model.ForecastResponse;
import com.sylgas.weatherapp.model.Weather;
import com.sylgas.weatherapp.network.service.WeatherDataService;
import com.sylgas.weatherapp.notification.listener.RequestListener;
import com.sylgas.weatherapp.ui.model.observable.ObservableErrorMessage;
import com.sylgas.weatherapp.ui.model.observable.ObservableForecast;

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
        observableErrorMessage.setMessage(0);
    }

    private void handleForecastResponse(ForecastResponse forecastResponse) {
        final Weather[] weathers = forecastResponse.getWeathers();
        if (weathers != null && weathers.length > 0) {
            final Weather weather = weathers[0];
            observableForecast.setTitle(weather.getTitle());
            observableForecast.setDescription(weather.getDescription());
            observableForecast.notifyChange();
        }
    }

    private void handleErrorResponse(Throwable e) {
        observableErrorMessage.setMessage(0);
    }

    private class WeatherForecastChangeListener implements RequestListener<ForecastResponse> {
        @Override
        public void onResult(ForecastResponse result) {
            handleForecastResponse(result);
        }

        @Override
        public void onError(Throwable e) {
            handleErrorResponse(e);
        }
    }
}
