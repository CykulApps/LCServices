package com.cykulapps.lcservices.utils;

import android.app.Application;

import com.cykulapps.lcservices.common.Prefs;


public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Prefs.initPrefs(this);
    }

//    @Override
//    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
//        if (!stateChanges.getFrom().getSubscribed() && stateChanges.getTo().getSubscribed()) {
//            // get player ID
//            stateChanges.getTo().getUserId();
//        }
//        Log.d("Debug",""+stateChanges);
//    }
}
