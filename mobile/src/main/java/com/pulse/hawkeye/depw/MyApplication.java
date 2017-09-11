package com.pulse.hawkeye.depw;

import android.app.Application;

import com.estimote.coresdk.common.config.EstimoteSDK;



public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        EstimoteSDK.initialize(getApplicationContext(), "<#App ID#>", "<#App Token#>");
    }
}
