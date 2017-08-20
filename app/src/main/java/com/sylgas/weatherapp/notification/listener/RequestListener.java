package com.sylgas.weatherapp.notification.listener;

import android.support.annotation.Nullable;

public interface RequestListener<T> {
    void onResult(@Nullable T result);

    void onError();
}
