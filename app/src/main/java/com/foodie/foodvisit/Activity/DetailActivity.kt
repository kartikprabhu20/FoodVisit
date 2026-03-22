package com.foodie.foodvisit.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.foodie.foodvisit.model.Restaurant
import com.foodie.foodvisit.ui.screens.detail.DetailScreen
import com.foodie.foodvisit.ui.theme.FoodVisitTheme
import com.foodie.foodvisit.widget.FoodVisitWidget
import com.foodie.foodvisit.widget.FoodVisitWidgetManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val restaurant = resolveRestaurant()

        setContent {
            FoodVisitTheme {
                DetailScreen(
                    restaurant = restaurant,
                    onNavigateUp = { finish() }
                )
            }
        }
    }

    /**
     * Resolves the [Restaurant] from the intent.
     * Supports two paths:
     * - Widget deep link: RESTAURANT_LIST (JSON) + POSITION extras
     * - App-internal: parcelable "restaurant" extra
     */
    private fun resolveRestaurant(): Restaurant? {
        val extras = intent.extras ?: return null
        return if (extras.containsKey(FoodVisitWidget.RESTAURANT_LIST)) {
            val list = extras.getString(FoodVisitWidget.RESTAURANT_LIST)
            val pos = extras.getInt(FoodVisitWidget.POSITION, 0)
            FoodVisitWidgetManager(applicationContext).getInfo(list, pos)
        } else {
            @Suppress("DEPRECATION")
            extras.getParcelable("restaurant")
        }
    }
}
