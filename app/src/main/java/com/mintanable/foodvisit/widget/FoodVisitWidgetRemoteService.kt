package com.mintanable.foodvisit.widget

import android.content.Intent
import android.widget.RemoteViewsService

class FoodVisitWidgetRemoteService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        FoodVisitRemoteViewFactory(applicationContext)
}
