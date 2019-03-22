package com.foodie.foodvisit.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.foodie.foodvisit.Activity.DetailActivity;
import com.foodie.foodvisit.Activity.MainActivity;
import com.foodie.foodvisit.FoodVisitApp;
import com.foodie.foodvisit.R;

/**
 * Created by kprabhu on 11/13/17.
 */

public class FoodVisitWidget extends AppWidgetProvider{

    public static final String POSITION = "restaurantPosition";
    public static final String RESTAURANT_LIST = "RESTAURANT_FIELD";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, new FoodVisitWidgetManager());
        }
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId, FoodVisitWidgetManager restaurantInfoWidgetManager) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_restaurant_info);

        // Create an Intent to launch ExampleActivity
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(RESTAURANT_LIST, restaurantInfoWidgetManager.getJson(restaurantInfoWidgetManager.getRestaurants()));

        TaskStackBuilder stackBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(FoodVisitApp.getAppContext());

            // Adds the back stack
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent to the top of the stack
            stackBuilder.addNextIntent(intent);

            // Gets a PendingIntent containing the entire back stack
            PendingIntent pendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setRemoteAdapter(R.id.widget_list,
                    new Intent(context, FoodVisitWidgetRemoteService.class));

            views.setPendingIntentTemplate(R.id.widget_list, pendingIntent);
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
