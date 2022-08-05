package com.nevergarden.myna.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MynaConfigBuilder {
    static final Gson gson = new GsonBuilder().create();

    public static MynaConfig create(String config) {
        return gson.fromJson(config, MynaConfig.class);
    }
}

