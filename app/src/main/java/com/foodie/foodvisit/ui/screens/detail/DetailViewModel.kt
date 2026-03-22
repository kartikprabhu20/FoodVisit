package com.foodie.foodvisit.ui.screens.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodie.foodvisit.Utils
import com.foodie.foodvisit.model.RestaurantInfo
import com.foodie.foodvisit.widget.FoodVisitWidgetManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun init(restaurantInfo: RestaurantInfo) {
        viewModelScope.launch {
            val wishlisted = withContext(Dispatchers.IO) {
                Utils.isToVisit(context, restaurantInfo)
            }
            _uiState.update { it.copy(isWishlisted = wishlisted) }
        }
    }

    fun toggleWishlist(restaurantInfo: RestaurantInfo) {
        viewModelScope.launch {
            val currentlyWishlisted = _uiState.value.isWishlisted
            withContext(Dispatchers.IO) {
                if (currentlyWishlisted) {
                    Utils.removeFromToVisit(context, restaurantInfo.id)
                } else {
                    Utils.addToVisit(context, restaurantInfo)
                }
                val manager = FoodVisitWidgetManager(context)
                manager.updateRestaurants(Utils.getRestaurantsFromDB(context))
            }
            _uiState.update { it.copy(isWishlisted = !currentlyWishlisted) }
        }
    }
}
