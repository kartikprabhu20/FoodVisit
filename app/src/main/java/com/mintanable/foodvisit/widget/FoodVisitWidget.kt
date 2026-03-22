package com.mintanable.foodvisit.widget

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.mintanable.foodvisit.R
import com.mintanable.foodvisit.activity.DetailActivity
import com.mintanable.foodvisit.activity.MainActivity

class FoodVisitWidget : AppWidgetProvider() {

    companion object {
        const val POSITION = "restaurantPosition"
        const val RESTAURANT_LIST = "RESTAURANT_FIELD"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, FoodVisitWidgetManager(context))
        }
    }

    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        widgetManager: FoodVisitWidgetManager
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_restaurant_info)

        val intent = Intent(context, DetailActivity::class.java).apply {
            putExtra(RESTAURANT_LIST, widgetManager.getJson(widgetManager.getRestaurants()))
        }

        val stackBuilder = TaskStackBuilder.create(context).apply {
            addParentStack(MainActivity::class.java)
            addNextIntent(intent)
        }

        val pendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        views.setRemoteAdapter(R.id.widget_list, Intent(context, FoodVisitWidgetRemoteService::class.java))
        views.setPendingIntentTemplate(R.id.widget_list, pendingIntent)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
