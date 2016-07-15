package com.softdesign.devintensive.data.network.interceptors;

import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.data.manager.PreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        PreferencesManager pm = DataManager.getInstanse().getPreferencesManager();

        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("X-Access-Token", pm.getAuthToken())
                .header("Request-User-ID", pm.getUserId())
                .header("User-Agent", "DevintensiveApp");

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
