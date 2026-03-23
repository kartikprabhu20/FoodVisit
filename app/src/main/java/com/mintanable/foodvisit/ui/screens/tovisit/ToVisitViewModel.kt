package com.mintanable.foodvisit.ui.screens.tovisit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintanable.core.data.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ToVisitViewModel @Inject constructor(
    repository: PlacesRepository
) : ViewModel() {

    /** Live — auto-updates whenever the wishlist changes in Room. */
    val uiState: StateFlow<ToVisitUiState> = repository.getWishlistedRestaurants()
        .map { ToVisitUiState(restaurants = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ToVisitUiState()
        )
}
