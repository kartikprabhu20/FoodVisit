package com.foodie.foodvisit.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.foodie.foodvisit.ui.navigation.AppNavigation
import com.foodie.foodvisit.ui.theme.FoodVisitTheme
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
