package com.sylgas.weatherapp.notification.listener;

import android.support.annotation.NonNull;

public interface ChangeListener<T> {
    void onChange(@NonNull T changedData);
}
