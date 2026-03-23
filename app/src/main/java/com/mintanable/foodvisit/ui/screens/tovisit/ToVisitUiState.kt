package com.mintanable.foodvisit.ui.screens.tovisit

import com.mintanable.core.model.RestaurantInfo

data class ToVisitUiState(
    val restaurants: List<RestaurantInfo> = emptyList(),
    val isLoading: Boolean = false
)
