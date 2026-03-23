package com.mintanable.foodvisit.ui.screens.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintanable.foodvisit.data.repository.PlacesRepository
import com.mintanable.core.model.RestaurantInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    repository: PlacesRepository
) : ViewModel() {

    /** Live — map markers update in real time whenever the wishlist changes. */
    val restaurants: StateFlow<List<RestaurantInfo>> = repository.getWishlistedRestaurants()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
