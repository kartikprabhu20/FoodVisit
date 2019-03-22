package com.foodie.foodvisit;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kprabhu on 11/14/17.
 */

public class PreferenceManager {

    private static final String PREF_FILE = "com.foodie.foodvisitr";
    private static final String LOCATION = "LOCATION";
    private static PreferenceManager sInstance;
    private SharedPreferences sharedPref;

    private PreferenceManager() {
        //singleton
        sharedPref = FoodVisitApp.getAppContext().getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    public static PreferenceManager getInstance() {
        if (sInstance == null) {
            sInstance = new PreferenceManager();
        }
        return sInstance;
    }


    public void setLocation(String location) {
        sharedPref.edit().putString(LOCATION, location).apply();
    }

    public String getLocation() {
        return sharedPref.getString(LOCATION, "4");
    }

    public void clear() {
        sharedPref.edit().clear().apply();
    }
}
