package com.sylgas.weatherapp.ui.model;

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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeatherActivityModelTest {
    private static final String FORECAST_TITLE = "FORECAST_TITLE";
    private static final String FORECAST_WEATHER_TITLE = "FORECAST_WEATHER_TITLE";
    private static final String FORECAST_WEATHER_DESCRIPTION = "FORECAST_WEATHER_DESCRIPTION";
    private static final double FORECAST_MAIN_TEMPERATURE = 10.0;
    private static final double FORECAST_MAIN_PRESSURE = 20.0;
    private static final byte FORECAST_MAIN_HUMIDITY = 25;
    private static final double FORECAST_WIND_SPEED = 30.0;
    private static final double FORECAST_WIND_DEGREES = 35.0;
    private static final Coordinates coordinates = new Coordinates(1.0, 2.0);

    @Mock
    private WeatherDataService weatherDataService;

    @Mock
    private ObservableForecast observableForecast;

    @Mock
    private ObservableErrorMessage observableErrorMessage;

    @Mock
    private ForecastResponse forecastResponse;

    private WeatherActivityModel weatherActivityModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        weatherActivityModel = new WeatherActivityModelImpl(weatherDataService,
                observableForecast,
                observableErrorMessage);
        setupForecastResponse();
    }

    @Test
    public void testOnPause() throws Exception {
        weatherActivityModel.onPause();
        verify(weatherDataService).cancelRequest();
    }

    @Test
    public void testOnRefresh() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                final Object[] args = invocation.getArguments();
                assertEquals(2, args.length);
                final RequestListener<ForecastResponse> requestListener =
                        (RequestListener<ForecastResponse>) args[1];
                assertRequestListener(requestListener);
                return null;
            }
        }).when(weatherDataService).requestWeatherForecast(eq(coordinates),
                any(RequestListener.class));
        weatherActivityModel.onRefresh(coordinates);
        verify(weatherDataService).requestWeatherForecast(eq(coordinates),
                any(RequestListener.class));
    }

    @Test
    public void testOnLocationFailure() throws Exception {
        weatherActivityModel.onLocationFailure();
        assertErrorMessage(R.string.gps_service_error);
    }

    private void setupForecastResponse() {
        when(forecastResponse.getName()).thenReturn(FORECAST_TITLE);
        when(forecastResponse.getWeathers()).thenReturn(new Weather[]{
                new Weather(FORECAST_WEATHER_TITLE, FORECAST_WEATHER_DESCRIPTION)
        });
        when(forecastResponse.getMain()).thenReturn(
                new Main(FORECAST_MAIN_TEMPERATURE, FORECAST_MAIN_PRESSURE, FORECAST_MAIN_HUMIDITY)
        );
        when(forecastResponse.getWindForecast()).thenReturn(
                new WindForecast(FORECAST_WIND_SPEED, FORECAST_WIND_DEGREES)
        );
    }

    private void assertRequestListener(RequestListener<ForecastResponse> requestListener) {
        assertNotNull(requestListener);
        requestListener.onResult(null);
        assertErrorMessage(R.string.weather_service_error);
        reset(observableErrorMessage);
        requestListener.onError();
        assertErrorMessage(R.string.weather_service_error);
        reset(observableErrorMessage);
        requestListener.onResult(forecastResponse);
        assertForecastResponse();
    }

    private void assertErrorMessage(int message) {
        verify(observableErrorMessage).setMessage(message);
        verify(observableErrorMessage).notifyChange();
    }

    private void assertForecastResponse() {
        verify(observableForecast).setTitle(FORECAST_TITLE);
        verify(observableForecast).setDescription(
                String.format("%s, %s", FORECAST_WEATHER_TITLE, FORECAST_WEATHER_DESCRIPTION)
        );
        verify(observableForecast).setTemperature(
                String.format(Locale.getDefault(), "%.0f\u00b0C", FORECAST_MAIN_TEMPERATURE)
        );
        verify(observableForecast).setPressure(
                String.format(Locale.getDefault(), "%.0f hPa", FORECAST_MAIN_PRESSURE)
        );
        verify(observableForecast).setHumidity(
                String.format(Locale.getDefault(), "%d%%", FORECAST_MAIN_HUMIDITY)
        );
        verify(observableForecast).setWindDegrees(
                String.format(Locale.getDefault(), "%.0f\u00b0", FORECAST_WIND_DEGREES)
        );
        verify(observableForecast).setWindSpeed(
                String.format(Locale.getDefault(), "%.0f km/h", FORECAST_WIND_SPEED)
        );
    }
}
