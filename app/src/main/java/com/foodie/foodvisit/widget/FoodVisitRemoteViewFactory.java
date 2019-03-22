package com.foodie.foodvisit.widget;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.foodie.foodvisit.FoodVisitApp;
import com.foodie.foodvisit.R;
import com.foodie.foodvisit.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kprabhu on 11/13/17.
 */

class FoodVisitRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String POSITION = "restaurantPosition";
    private FoodVisitWidgetManager foodVisitWidgetManager = new FoodVisitWidgetManager();
    private List<Restaurant> restaurantList = new ArrayList<>();

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        restaurantList = foodVisitWidgetManager.getRestaurants();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (restaurantList == null || restaurantList.isEmpty()) {
            return 0;
        }
        return restaurantList.size();    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(FoodVisitApp.getAppContext().getPackageName(),
                R.layout.wigdet_cell);

        Restaurant restaurant = restaurantList.get(position);
        views.setTextViewText(R.id.widget_restaurant_name,restaurant.getRestaurantInfo().getName());

        Bundle extras = new Bundle();
        extras.putInt(POSITION, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        views.setOnClickFillInIntent(R.id.widget_restaurant_name, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
