package com.sylgas.weatherapp.ui.model.observable;

import android.support.annotation.NonNull;

import com.sylgas.weatherapp.notification.listener.ChangeListener;
import com.sylgas.weatherapp.ui.model.view.Forecast;

public class ObservableForecast extends Forecast implements Observable {
    @NonNull
    private final ChangeListener<Forecast> changeListener;

    public ObservableForecast(@NonNull ChangeListener<Forecast> changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void notifyChange() {
        changeListener.onChange(this);
    }
}
