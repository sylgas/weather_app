package com.sylgas.weatherapp.ui.model;

import com.sylgas.weatherapp.model.Coordinates;

public interface WeatherActivityModel extends ActivityModel {
    void onRefresh(Coordinates coordinates);

    void onLocationFailure();
}
