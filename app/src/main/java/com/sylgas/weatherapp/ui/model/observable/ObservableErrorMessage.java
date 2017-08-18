package com.sylgas.weatherapp.ui.model.observable;

import com.sylgas.weatherapp.notification.listener.ChangeListener;
import com.sylgas.weatherapp.ui.model.view.ErrorMessage;

public class ObservableErrorMessage extends ErrorMessage implements Observable {
    private final ChangeListener<ErrorMessage> changeListener;

    public ObservableErrorMessage(ChangeListener<ErrorMessage> changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void notifyChange() {
        changeListener.onChange(this);
    }
}
