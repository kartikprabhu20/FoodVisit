package com.mintanable.foodvisit.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mintanable.foodvisit.ui.screens.detail.DetailScreen
import com.mintanable.foodvisit.ui.theme.FoodVisitTheme
import com.mintanable.foodvisit.widget.FoodVisitWidget
import com.mintanable.foodvisit.widget.FoodVisitWidgetManager
import com.mintanable.foodvisit.model.Restaurant
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
        return if (extras.containsKey(FoodVisitWidget.Companion.RESTAURANT_LIST)) {
            val list = extras.getString(FoodVisitWidget.Companion.RESTAURANT_LIST)
            val pos = extras.getInt(FoodVisitWidget.Companion.POSITION, 0)
            FoodVisitWidgetManager(applicationContext).getInfo(list, pos)
        } else {
            @Suppress("DEPRECATION")
            extras.getParcelable("restaurant")
        }
    }
}
