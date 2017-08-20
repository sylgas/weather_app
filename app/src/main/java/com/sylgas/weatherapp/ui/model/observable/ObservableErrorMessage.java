package com.sylgas.weatherapp.ui.model.observable;

import android.support.annotation.NonNull;

import com.sylgas.weatherapp.notification.listener.ChangeListener;
import com.sylgas.weatherapp.ui.model.view.ErrorMessage;

public class ObservableErrorMessage extends ErrorMessage implements Observable {
    @NonNull
    private final ChangeListener<ErrorMessage> changeListener;

    public ObservableErrorMessage(@NonNull ChangeListener<ErrorMessage> changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void notifyChange() {
        changeListener.onChange(this);
    }
}
