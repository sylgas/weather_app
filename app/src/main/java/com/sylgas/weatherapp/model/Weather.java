package com.sylgas.weatherapp.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @Nullable
    @SerializedName("main")
    private final String title;

    @Nullable
    @SerializedName("description")
    private final String description;

    public Weather(@Nullable String title, @Nullable String description) {
        this.title = title;
        this.description = description;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }
}
