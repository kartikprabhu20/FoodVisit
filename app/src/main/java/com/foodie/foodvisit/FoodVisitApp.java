package com.foodie.foodvisit;

import android.app.Application;
import android.content.Context;

/**
 * Created by kprabhu on 11/12/17.
 */

public class FoodVisitApp extends Application{
    private static FoodVisitApp context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = (FoodVisitApp) getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }

}
