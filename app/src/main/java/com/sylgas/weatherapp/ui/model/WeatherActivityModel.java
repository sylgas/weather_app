package com.sylgas.weatherapp.ui.model;

import android.support.annotation.NonNull;

import com.sylgas.weatherapp.model.Coordinates;

public interface WeatherActivityModel extends ActivityModel {
    void onRefresh(@NonNull Coordinates coordinates);

    void onLocationFailure();
}
