package com.mintanable.foodvisit.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mintanable.foodvisit.ui.navigation.AppNavigation
import com.mintanable.foodvisit.ui.theme.FoodVisitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodVisitTheme {
                AppNavigation()
            }
        }
    }
}
