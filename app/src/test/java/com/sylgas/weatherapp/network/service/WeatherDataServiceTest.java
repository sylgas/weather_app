package com.sylgas.weatherapp.network.service;

import com.sylgas.weatherapp.model.Coordinates;
import com.sylgas.weatherapp.model.ForecastResponse;
import com.sylgas.weatherapp.network.service.api.WeatherApiService;
import com.sylgas.weatherapp.notification.listener.RequestListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sylgas.weatherapp.network.service.WeatherDataService.APP_ID;
import static com.sylgas.weatherapp.network.service.WeatherDataService.UNITS_METRIC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class WeatherDataServiceTest {
    private final Coordinates coordinates = new Coordinates(1.0, 2.0);

    @Mock
    private WeatherApiService apiService;

    @Mock
    private RequestListener<ForecastResponse> listener;

    @Mock
    private Call<ForecastResponse> forecastResponseCall;

    @Mock
    private ForecastResponse forecastData;

    private WeatherDataService service;

    private Response<ForecastResponse> forecastResponse;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new WeatherDataService(apiService);
        forecastResponse = Response.success(forecastData);
    }

    @Test
    public void testRequestWeatherForecast() throws Exception {
        prepareApiServiceMock(forecastResponseCall);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                final Object[] args = invocation.getArguments();
                assertEquals(1, args.length);
                final Callback<ForecastResponse> callback = (Callback<ForecastResponse>) args[0];
                assertForecastCallback(callback);
                return null;
            }
        }).when(forecastResponseCall).enqueue(any(Callback.class));

        service.requestWeatherForecast(coordinates, listener);
        verify(apiService).getForecastForCoordinates(APP_ID, coordinates.getLatitude(),
                coordinates.getLongitude(), UNITS_METRIC);
        verify(forecastResponseCall).enqueue(any(Callback.class));
    }

    @Test
    public void testRequestWeatherForecastOnSecondCall() throws Exception {
        prepareApiServiceMock(forecastResponseCall);
        service.requestWeatherForecast(coordinates, listener);
        verify(forecastResponseCall).enqueue(any(Callback.class));
        service.requestWeatherForecast(coordinates, listener);
        verifyNoMoreInteractions(forecastResponseCall);
    }

    @Test
    public void testRequestWeatherForecastIfNullCall() throws Exception {
        prepareApiServiceMock(null);
        service.requestWeatherForecast(coordinates, listener);
        verify(listener).onError();
        verifyZeroInteractions(forecastResponseCall);
    }

    @Test
    public void testCancelRequest() throws Exception {
        prepareApiServiceMock(forecastResponseCall);
        service.requestWeatherForecast(coordinates, listener);
        service.cancelRequest();
        verify(forecastResponseCall).cancel();
    }

    @Test
    public void testCancelRequestIfNoCallMade() throws Exception {
        service.cancelRequest();
        verifyZeroInteractions(forecastResponseCall);
    }

    private void prepareApiServiceMock(Call<ForecastResponse> call) {
        when(apiService.getForecastForCoordinates(APP_ID, coordinates.getLatitude(),
                coordinates.getLongitude(), UNITS_METRIC)).thenReturn(call);
    }

    private void assertForecastCallback(Callback<ForecastResponse> callback) {
        assertNotNull(callback);
        callback.onResponse(forecastResponseCall, forecastResponse);
        verify(listener).onResult(forecastData);
        callback.onResponse(forecastResponseCall, null);
        verify(listener).onResult(null);
        callback.onFailure(forecastResponseCall, null);
        verify(listener).onError();
    }
}
