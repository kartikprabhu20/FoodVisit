package com.mintanable.foodvisit.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.foodie.mintanable.R
import com.foodie.mintanable.model.Restaurant

internal class FoodVisitRemoteViewFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    companion object {
        private const val POSITION = "restaurantPosition"
    }

    private val foodVisitWidgetManager = FoodVisitWidgetManager(context)
    private var restaurantList: List<Restaurant> = emptyList()

    override fun onCreate() {}

    override fun onDataSetChanged() {
        restaurantList = foodVisitWidgetManager.getRestaurants() ?: emptyList()
    }

    override fun onDestroy() {}

    override fun getCount(): Int = restaurantList.size

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.wigdet_cell)
        val restaurant = restaurantList[position]
        views.setTextViewText(R.id.widget_restaurant_name, restaurant.restaurantInfo?.name)

        val extras = Bundle().apply { putInt(POSITION, position) }
        val fillInIntent = Intent().apply { putExtras(extras) }
        views.setOnClickFillInIntent(R.id.widget_restaurant_name, fillInIntent)
        return views
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = false
}
