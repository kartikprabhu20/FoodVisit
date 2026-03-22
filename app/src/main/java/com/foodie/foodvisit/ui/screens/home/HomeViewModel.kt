package com.foodie.foodvisit.ui.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodie.foodvisit.AppPreferenceManager
import com.foodie.foodvisit.Utils
import com.foodie.foodvisit.model.RestaurantInfo
import com.foodie.foodvisit.service.RestaurantService
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
class HomeViewModel @Inject constructor(
    private val service: RestaurantService,
    private val prefs: AppPreferenceManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var lastLoadedLocation: String? = null

    init {
        loadRestaurants()
    }

    fun loadRestaurants() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            if (!Utils.isOnline(context)) {
                _uiState.update { it.copy(isLoading = false, isOffline = true) }
                return@launch
            }

            val locationId = prefs.getLocation().toIntOrNull() ?: 4
            val apiKey = Utils.getRestaurantApiKey()

            try {
                val response = withContext(Dispatchers.IO) {
                    service.restaurantResult(apiKey, locationId, "city").execute()
                }
                val restaurants: List<RestaurantInfo> = response.body()
                    ?.restaurants
                    ?.mapNotNull { it.restaurantInfo }
                    ?: emptyList()
                lastLoadedLocation = prefs.getLocation()
                _uiState.update {
                    it.copy(isLoading = false, restaurants = restaurants, isOffline = false)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun refreshIfLocationChanged() {
        if (lastLoadedLocation != prefs.getLocation()) {
            loadRestaurants()
        }
    }
}
