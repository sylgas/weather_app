package com.sylgas.weatherapp.notification.notifier;

import android.support.annotation.NonNull;

import com.sylgas.weatherapp.notification.listener.ChangeListener;

import java.util.LinkedList;
import java.util.List;

public class NotifierImpl<T> implements Notifier<T> {

    private final List<ChangeListener<T>> listeners = new LinkedList<>();

    @Override
    public void registerListener(@NonNull ChangeListener<T> listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterListener(@NonNull ChangeListener<T> listener) {
        listeners.remove(listener);
    }

    @Override
    public void onChange(@NonNull T changedData) {
        for (ChangeListener<T> listener : listeners) {
            listener.onChange(changedData);
        }
    }
}
