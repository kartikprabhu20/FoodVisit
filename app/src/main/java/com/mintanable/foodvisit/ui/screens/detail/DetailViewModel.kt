package com.mintanable.foodvisit.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintanable.foodvisit.data.repository.PlacesRepository
import com.mintanable.foodvisit.model.RestaurantInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: PlacesRepository
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
        }
    }
}
