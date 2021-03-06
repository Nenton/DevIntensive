package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.softdesign.devintensive.data.storage.models.DaoMaster;
import com.softdesign.devintensive.data.storage.models.DaoSession;

import org.greenrobot.greendao.database.Database;

public class DevIntensiveApplication extends Application{

    private static Context sContext;
    public static SharedPreferences sSharedPreferences;
    private static DaoSession sDaoSession;

    /**
     * Create shared preferences
     */
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
       DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,ConstantManager.NAME_BD);
        Database database = helper.getWritableDb();
        sDaoSession = new DaoMaster(database).newSession();

        Stetho.initializeWithDefaults(this);

    }

    /**
     * @return This shared preferences
     */
    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static Context getContext() {
        return sContext;
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }
}
