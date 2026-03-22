package com.mintanable.foodvisit.ui.screens.settings

data class SettingsUiState(
    val selectedCityId: String = "4"
)

// City id → display name mapping (matches legacy Zomato entity IDs)
val CITY_OPTIONS = listOf(
    "4"  to "Bangalore",
    "1"  to "Delhi",
    "3"  to "Chennai",
    "2"  to "Kolkata"
)
