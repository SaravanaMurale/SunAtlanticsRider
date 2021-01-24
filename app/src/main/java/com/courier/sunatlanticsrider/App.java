package com.courier.sunatlanticsrider;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static Context getInstance() {
        return sInstance;
    }
}
