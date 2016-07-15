package com.softdesign.devintensive.data.manager;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {

    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_EMAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_GIT_KEY,
            ConstantManager.USER_ABOUT_KEY
    };
    private static final String[] USER_VALUES = {
            ConstantManager.USER_RATING_VALUE,
            ConstantManager.USER_CODE_LINES_VALUE,
            ConstantManager.USER_PROJECT_VALUE
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
        for (int i = 0; i < USER_FIELDS.length; i++) {
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
        list.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY, ConstantManager.FIRST_FIELD_GIT));
        list.add(mSharedPreferences.getString(ConstantManager.USER_ABOUT_KEY, ConstantManager.FIRST_FIELD_ABOUT));
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
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, "null");
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "null");
    }

    public void saveUserProfileValues(int[] userValues) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_VALUES.length; i++) {
            editor.putString(USER_VALUES[i], String.valueOf(userValues[i]));
        }
        editor.apply();
    }

    public List<String> loadUserProfileValues() {
        List<String> listValues = new ArrayList<>();
        listValues.add(mSharedPreferences.getString(ConstantManager.USER_RATING_VALUE, "0"));
        listValues.add(mSharedPreferences.getString(ConstantManager.USER_CODE_LINES_VALUE, "0"));
        listValues.add(mSharedPreferences.getString(ConstantManager.USER_PROJECT_VALUE, "0"));
        return listValues;
    }

    public void saveFirstSecondNameUser(String firstName,String secondName){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_FIRST_NAME_KEY,firstName);
        editor.putString(ConstantManager.USER_SECOND_NAME_KEY,secondName);
        editor.apply();
    }

    public List<String> loadFirstSecondNameUser(){
        List<String> list = new ArrayList<>();
        list.add(mSharedPreferences.getString(ConstantManager.USER_FIRST_NAME_KEY,"null"));
        list.add(mSharedPreferences.getString(ConstantManager.USER_SECOND_NAME_KEY,"null"));
        return list;
    }

    public void saveEmailAuthActivity(String email) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.LOAD_EMAIL_AUTH_KEY,email);
        editor.apply();
    }

    public String loadEmailAuthActivity() {
        return mSharedPreferences.getString(ConstantManager.LOAD_EMAIL_AUTH_KEY,"");
    }
}
