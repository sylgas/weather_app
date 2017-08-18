package com.sylgas.weatherapp.ui.model.view;

public class ErrorMessage {
    private int message;

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public boolean isEmpty() {
        return message != 0;
    }
}
