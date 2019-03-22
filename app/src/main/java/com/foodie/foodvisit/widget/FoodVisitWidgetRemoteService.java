package com.foodie.foodvisit.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by kprabhu on 11/13/17.
 */

public class FoodVisitWidgetRemoteService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FoodVisitRemoteViewFactory();
    }
}
