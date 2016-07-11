package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class DevIntensiveApplication extends Application{


    public static SharedPreferences sSharedPreferences;

    /**
     * Create shared preferences
     */
    @Override
    public void onCreate() {
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate();
    }

    /**
     * @return This shared preferences
     */
    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }
}
