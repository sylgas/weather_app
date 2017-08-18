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

    @Nullable
    @SerializedName("icon")
    private final String icon;

    public Weather(@Nullable String title, @Nullable String description, @Nullable String icon) {
        this.title = title;
        this.description = description;
        this.icon = icon;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getIcon() {
        return icon;
    }
}
