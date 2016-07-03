package com.softdesign.devintensive.data.manager;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {

    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY, ConstantManager.USER_EMAIL_KEY, ConstantManager.USER_VK_KEY, ConstantManager.USER_GIT_KEY, ConstantManager.USER_ABOUT_KEY};
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
    public void saveUserProfileData (List<String> userFields){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }

    /**
     * Load data all fields user
     * @return Saved data all fields user
     */
    public List<String> loadUserProfileData (){
        List<String> list = new ArrayList<>();
        list.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY,ConstantManager.FIRST_FIELD_PHONE));
        list.add(mSharedPreferences.getString(ConstantManager.USER_EMAIL_KEY,ConstantManager.FIRST_FIELD_EMAIL));
        list.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY,ConstantManager.FIRST_FIELD_VK));
        list.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY,ConstantManager.FIRST_FIELD_GIT));
        list.add(mSharedPreferences.getString(ConstantManager.USER_ABOUT_KEY,ConstantManager.FIRST_FIELD_ABOUT));
        return list;
    }

    /**
     * @param uri Path file image which must saved
     */
    public void saveUserPhoto(Uri uri){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY,uri.toString());
    }

    /**
     * @return Saved image in storage or first
     */
    public Uri loadUserPhoto(){
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, ConstantManager.FIRST_IMAGE_AVATAR));
    }
}
