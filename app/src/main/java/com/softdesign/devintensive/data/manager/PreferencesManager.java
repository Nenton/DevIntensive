package com.softdesign.devintensive.data.manager;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {

    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_EMAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_ABOUT_KEY,
            ConstantManager.USER_RATING_VALUE,
            ConstantManager.USER_CODE_LINES_VALUE,
            ConstantManager.USER_PROJECT_VALUE,
            ConstantManager.USER_PHOTO_KEY,
            ConstantManager.USER_AVATAR_KEY,
            ConstantManager.USER_FULL_NAME_KEY
    };


    private SharedPreferences mSharedPreferences;

    /**
     * Get shared preferences
     */
    public PreferencesManager() {
        this.mSharedPreferences = DevIntensiveApplication.getSharedPreferences();
    }

    /**
     * @param userFields All fields which must be saved
     */
    public void saveUserProfileData(List<String> userFields) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < userFields.size(); i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }

    /**
     * Load data all fields user
     *
     * @return Saved data all fields user
     */
    public List<String> loadUserProfileData() {
        List<String> list = new ArrayList<>();
        list.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, ConstantManager.FIRST_FIELD_PHONE));
        list.add(mSharedPreferences.getString(ConstantManager.USER_EMAIL_KEY, ConstantManager.FIRST_FIELD_EMAIL));
        list.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, ConstantManager.FIRST_FIELD_VK));
        list.add(mSharedPreferences.getString(ConstantManager.USER_ABOUT_KEY, ConstantManager.FIRST_FIELD_ABOUT));
        list.add(mSharedPreferences.getString(ConstantManager.USER_RATING_VALUE, ConstantManager.FIRST_FIELD_ABOUT));
        list.add(mSharedPreferences.getString(ConstantManager.USER_CODE_LINES_VALUE, ConstantManager.FIRST_FIELD_ABOUT));
        list.add(mSharedPreferences.getString(ConstantManager.USER_PROJECT_VALUE, ConstantManager.FIRST_FIELD_ABOUT));
        list.add(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, ConstantManager.FIRST_FIELD_ABOUT));
        list.add(mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY, ConstantManager.FIRST_FIELD_ABOUT));
        list.add(mSharedPreferences.getString(ConstantManager.USER_FULL_NAME_KEY, ""));
        return list;
    }

    /**
     * @param uri Path file image which must saved
     */
    public void saveUserPhoto(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    /**
     * @param uri Path file avatar image which must saved
     */
    public void saveAvatarImage(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_AVATAR_KEY, uri.toString());
        editor.apply();
    }

    /**
     * @return Saved image in storage or first
     */
    public Uri loadUserPhoto() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, ConstantManager.FIRST_USER_PHOTO));
    }

    /**
     * @return Saved avatar in storage or first
     */
    public Uri loadAvatarImage() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY, ConstantManager.FIRST_IMAGE_AVATAR));
    }

    public void saveAuthToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN_KEY, authToken);
        editor.apply();
    }

    public String getAuthToken() {
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, ConstantManager.NULL_STRING);
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, ConstantManager.NULL_STRING);
    }

    public String getEmail() {
        return mSharedPreferences.getString(ConstantManager.USER_EMAIL_KEY, "");
    }

    public String getFullName(){
        return mSharedPreferences.getString(ConstantManager.USER_FULL_NAME_KEY, "");
    }

}
