package com.sylgas.weatherapp.ui.model.observable;

import com.sylgas.weatherapp.notification.listener.ChangeListener;
import com.sylgas.weatherapp.ui.model.view.Forecast;

public class ObservableForecast extends Forecast implements Observable {
    private final ChangeListener<Forecast> changeListener;

    public ObservableForecast(ChangeListener<Forecast> changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void notifyChange() {
        changeListener.onChange(this);
    }
}
