package com.softdesign.devintensive.data.manager;

public class DataManager {
    private static DataManager INSTANCE = null;
    private PreferencesManager mPreferencesManager;

    /**
     * Create preferencesmanager
     */
    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
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
}
