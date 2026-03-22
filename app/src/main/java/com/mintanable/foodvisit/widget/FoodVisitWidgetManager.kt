package com.mintanable.foodvisit.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mintanable.foodvisit.R
import com.mintanable.foodvisit.model.Restaurant
import java.lang.reflect.Type

class FoodVisitWidgetManager(private val context: Context) {

    companion object {
        const val PREFERENCES_NAME = "Restaurant_widgets"
        const val RESTAURANT_KEY = "Restaurant_key"
    }

    private val gson = Gson()
    private val listType: Type = object : TypeToken<List<Restaurant>>() {}.type
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getJson(restaurants: List<Restaurant>?): String = gson.toJson(restaurants, listType)

    fun getRestaurants(): List<Restaurant>? {
        val restaurantList = sharedPreferences.getString(RESTAURANT_KEY, null) ?: return null
        return gson.fromJson(restaurantList, listType)
    }

    fun updateRestaurants(restaurants: List<Restaurant>) {
        sharedPreferences.edit()
            .putString(RESTAURANT_KEY, gson.toJson(restaurants, listType))
            .commit()
        updateWidget()
    }

    private fun updateWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, FoodVisitWidget::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list)
    }

    fun getInfo(json: String?, pos: Int): Restaurant {
        val list: List<Restaurant> = gson.fromJson(json, listType)
        return list[pos]
    }
}
