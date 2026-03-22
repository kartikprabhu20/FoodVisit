package com.mintanable.foodvisit.ui.screens.home

import com.mintanable.foodvisit.model.RestaurantInfo

data class HomeUiState(
    val restaurants: List<RestaurantInfo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isOffline: Boolean = false
)
