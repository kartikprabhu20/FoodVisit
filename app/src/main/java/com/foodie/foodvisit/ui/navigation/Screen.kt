package com.foodie.foodvisit.ui.navigation

sealed class Screen(val route: String) {
    object Home     : Screen("home")
    object ToVisit  : Screen("to_visit")
    object Maps     : Screen("maps")
    object AboutUs  : Screen("about_us")
    object Settings : Screen("settings")

    // restaurantJson is URL-encoded Gson JSON of a Restaurant
    object Detail : Screen("detail/{restaurantJson}") {
        fun createRoute(encodedJson: String) = "detail/$encodedJson"
    }
}
