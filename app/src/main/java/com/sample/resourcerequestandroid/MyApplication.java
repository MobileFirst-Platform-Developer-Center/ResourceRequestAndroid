package com.sample.resourcerequestandroid;

import android.app.Application;
import android.util.Log;

import com.worklight.wlclient.api.WLClient;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        WLClient.createInstance(this);
    }
}
