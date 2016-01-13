package com.example.android.sfcrimeheatmap.application;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

public class CMApp extends Application {

    private CMAppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        bootstrapDI();
        JodaTimeAndroid.init(this);
    }

    private void bootstrapDI() {
        appComponent = CMAppComponent.Initializer.initialize(this);
    }

    public CMAppComponent getAppComponent() {
        return appComponent;
    }
}
