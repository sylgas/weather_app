package com.sylgas.weatherapp.notification.notifier;

import android.support.annotation.NonNull;

import com.sylgas.weatherapp.notification.listener.ChangeListener;

public interface Notifier<T> extends ChangeListener<T> {
    void registerListener(@NonNull ChangeListener<T> listener);

    void unregisterListener(@NonNull ChangeListener<T> listener);
}
