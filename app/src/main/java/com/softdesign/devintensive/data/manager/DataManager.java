package com.softdesign.devintensive.data.manager;

import android.content.Context;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import retrofit2.Call;

public class DataManager {
    private static DataManager INSTANCE = null;
    private Context mContext;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;

    /**
     * Create preferencesmanager
     */
    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevIntensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);
    }

    /**
     * @return New datamanager or this
     */
    public static DataManager getInstanse(){
        if (INSTANCE==null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    /**
     * @return Current preferences manager
     */
    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public Context getContext(){
        return mContext;
    }

    // region =============Network===============

    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq){
        return mRestService.loginUser(userLoginReq);
    }

    //end region

    // region =============Database===============

//    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq){
//        return mRestService.loginUser(userLoginReq);
//    }

    //end region
}
