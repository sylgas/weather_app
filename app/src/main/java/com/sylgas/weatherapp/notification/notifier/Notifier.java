package com.sylgas.weatherapp.notification.notifier;

import com.sylgas.weatherapp.notification.listener.ChangeListener;

public interface Notifier<T> extends ChangeListener<T> {
    void registerListener(ChangeListener<T> listener);

    void unregisterListener(ChangeListener<T> listener);
}
