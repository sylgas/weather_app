package com.sylgas.weatherapp.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sylgas.weatherapp.R;
import com.sylgas.weatherapp.model.Coordinates;
import com.sylgas.weatherapp.network.service.WeatherDataService;
import com.sylgas.weatherapp.network.service.WeatherDataServiceFactory;
import com.sylgas.weatherapp.notification.listener.ChangeListener;
import com.sylgas.weatherapp.notification.notifier.Notifier;
import com.sylgas.weatherapp.notification.notifier.NotifierImpl;
import com.sylgas.weatherapp.ui.model.WeatherActivityModel;
import com.sylgas.weatherapp.ui.model.WeatherActivityModelImpl;
import com.sylgas.weatherapp.ui.model.observable.ObservableErrorMessage;
import com.sylgas.weatherapp.ui.model.observable.ObservableForecast;
import com.sylgas.weatherapp.ui.model.view.ErrorMessage;
import com.sylgas.weatherapp.ui.model.view.Forecast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class WeatherActivity extends AppCompatActivity {
    private final static int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final static int LOCATION_METERS_ACCURACY = 30;

    private FloatingActionButton refreshButton;
    private TextView title;
    private TextView description;
    private TextView temperature;
    private TextView pressure;
    private TextView humidity;
    private TextView windSpeed;
    private TextView windDegrees;

    private LocationManager locationManager;
    private WeatherActivityModel activityModel;

    @NonNull
    private final Notifier<Forecast> forecastNotifier = new NotifierImpl<>();

    @NonNull
    private final Notifier<ErrorMessage> errorMessageNotifier = new NotifierImpl<>();

    @NonNull
    private final LocationListener locationListener = new GpsLocationListener();

    @NonNull
    private final ChangeListener<Forecast> forecastChangeListener = new OnForecastChangeListener();

    @NonNull
    private final ChangeListener<ErrorMessage> errorChangeListener = new OnErrorChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        injectServices();
        injectActivityModel();
        injectViews();
        setupViews();
    }

    private void injectServices() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private void injectActivityModel() {
        final WeatherDataService weatherDataService = new WeatherDataServiceFactory().create();
        final ObservableForecast observableForecast = new ObservableForecast(forecastNotifier);
        final ObservableErrorMessage observableErrorMessage = new ObservableErrorMessage(errorMessageNotifier);
        activityModel = new WeatherActivityModelImpl(weatherDataService, observableForecast,
                observableErrorMessage);
    }

    private void injectViews() {
        title = findViewById(R.id.weather_title);
        description = findViewById(R.id.weather_subtitle);
        temperature = findViewById(R.id.weather_temperature);
        pressure = findViewById(R.id.weather_pressure);
        humidity = findViewById(R.id.weather_humidity);
        windSpeed = findViewById(R.id.wind_speed);
        windDegrees = findViewById(R.id.wind_degrees);
        refreshButton = findViewById(R.id.refresh);
    }

    private void setupViews() {
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshModel();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final int[] grantResults = {checkSelfPermission(ACCESS_FINE_LOCATION),
                    checkSelfPermission(ACCESS_COARSE_LOCATION)};
            if (!arePermissionsGranted(grantResults)) {
                requestLocationPermissions();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        forecastNotifier.registerListener(forecastChangeListener);
        errorMessageNotifier.registerListener(errorChangeListener);
        activityModel.onResume();
        refreshModel();
    }

    private void refreshModel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final int[] grantResults = {checkSelfPermission(ACCESS_FINE_LOCATION),
                    checkSelfPermission(ACCESS_COARSE_LOCATION)};
            if (!arePermissionsGranted(grantResults)) {
                return;
            }
        }

        final Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            activityModel.onRefresh(new Coordinates(location));
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestLocationPermissions() {
        final String[] locationPermissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        requestPermissions(locationPermissions, LOCATION_PERMISSION_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (arePermissionsGranted(grantResults)) {
                refreshModel();
            } else {
                activityModel.onLocationFailure();
            }
        }
    }

    private boolean arePermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (isPermissionDenied(result)) {
                return false;
            }
        }
        return true;
    }

    private boolean isPermissionDenied(int grantResult) {
        return grantResult == PackageManager.PERMISSION_DENIED;
    }

    @Override
    protected void onPause() {
        super.onPause();
        forecastNotifier.unregisterListener(forecastChangeListener);
        errorMessageNotifier.unregisterListener(errorChangeListener);
        locationManager.removeUpdates(locationListener);
        activityModel.onPause();
    }

    private void refreshView(@NonNull Forecast forecast) {
        title.setText(forecast.getTitle());
        description.setText(forecast.getDescription());
        temperature.setText(forecast.getTemperature());
        pressure.setText(forecast.getPressure());
        humidity.setText(forecast.getHumidity());
        windSpeed.setText(forecast.getWindSpeed());
        windDegrees.setText(forecast.getWindDegrees());
    }

    private void refreshView(@NonNull ErrorMessage errorMessage) {
        Toast.makeText(this, errorMessage.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private class GpsLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location.getAccuracy() < LOCATION_METERS_ACCURACY) {
                locationManager.removeUpdates(locationListener);
                activityModel.onRefresh(new Coordinates(location));
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    }

    private class OnForecastChangeListener implements ChangeListener<Forecast> {
        @Override
        public void onChange(@NonNull Forecast changedData) {
            refreshView(changedData);
        }
    }

    private class OnErrorChangeListener implements ChangeListener<ErrorMessage> {
        @Override
        public void onChange(@NonNull ErrorMessage changedData) {
            refreshView(changedData);
        }
    }
}
