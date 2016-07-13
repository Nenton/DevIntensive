package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class DevIntensiveApplication extends Application{

    private static Context mContext;
    public static SharedPreferences sSharedPreferences;

    /**
     * Create shared preferences
     */
    @Override
    public void onCreate() {
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mContext = this;
        super.onCreate();
    }

    /**
     * @return This shared preferences
     */
    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static Context getContext() {
        return mContext;
    }
}
