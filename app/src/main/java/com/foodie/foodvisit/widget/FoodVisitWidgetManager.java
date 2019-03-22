package com.foodie.foodvisit.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.foodie.foodvisit.FoodVisitApp;
import com.foodie.foodvisit.R;
import com.foodie.foodvisit.model.Restaurant;
import com.foodie.foodvisit.model.RestaurantInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by kprabhu on 11/13/17.
 */

public class FoodVisitWidgetManager {
    public static final String PREFERENCES_NAME = "Restaurant_widgets";
    public static final String RESTAURANT_KEY = "Restaurant_key";

    private Gson gson = new Gson();

    Type listType = new TypeToken<List<Restaurant>>() {}.getType();
    private SharedPreferences sharedPreferences = FoodVisitApp.getAppContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

    public FoodVisitWidgetManager(){

    }

    public String getJson(List<Restaurant> restaurants) {
        return gson.toJson(restaurants, listType);
    }

    public List<Restaurant> getRestaurants() {
        String restaurantList = sharedPreferences.getString(RESTAURANT_KEY, null);
        if (restaurantList != null) {
            return gson.fromJson(restaurantList, listType);
        }
        return null;    }

    public void updateRestaurants(List<Restaurant> restrauntListFromDB) {
        String restarauntJson = gson.toJson(restrauntListFromDB, listType);

        sharedPreferences.edit().
                putString(RESTAURANT_KEY, restarauntJson).
                commit();

        updateWidget();
    }

    private void updateWidget() {
        Intent intent = new Intent(FoodVisitApp.getAppContext(), FoodVisitWidget.class);

        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(FoodVisitApp.getAppContext());
        ComponentName componentName = new ComponentName(FoodVisitApp.getAppContext(), FoodVisitWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        FoodVisitApp.getAppContext().sendBroadcast(intent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);

    }

    public Restaurant getInfo(String json,int pos){
        List<Restaurant> list = gson.fromJson(json, listType);
        return list.get(pos);
    }
}
