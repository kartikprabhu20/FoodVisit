package com.mintanable.foodvisit.ui.screens.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintanable.core.data.repository.PlacesRepository
import com.mintanable.core.model.RestaurantInfo
import com.mintanable.foodvisit.widget.FoodVisitWidgetManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: PlacesRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun init(restaurantInfo: RestaurantInfo) {
        viewModelScope.launch {
            val wishlisted = repository.isWishlisted(restaurantInfo.id ?: return@launch)
            _uiState.update { it.copy(isWishlisted = wishlisted) }
        }
    }

    fun toggleWishlist(restaurantInfo: RestaurantInfo) {
        viewModelScope.launch {
            val newState = !_uiState.value.isWishlisted
            repository.setWishlisted(restaurantInfo, newState)
            _uiState.update { it.copy(isWishlisted = newState) }
            // Keep the home-screen widget in sync after every wishlist change.
            val wishlistedList = repository.getWishlistedRestaurantsOnce()
            FoodVisitWidgetManager(context).updateRestaurants(wishlistedList)
        }
    }
}
