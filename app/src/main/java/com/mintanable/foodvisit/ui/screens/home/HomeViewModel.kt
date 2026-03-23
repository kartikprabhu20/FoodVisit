package com.mintanable.foodvisit.ui.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintanable.foodvisit.AppPreferenceManager
import com.mintanable.foodvisit.Utils
import com.mintanable.core.common.Resource
import com.mintanable.core.data.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PlacesRepository,
    private val prefs: AppPreferenceManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var collectJob: Job? = null
    private var lastLoadedCityId: String? = null

    init {
        loadRestaurants()
    }

    fun loadRestaurants() {
        val cityId = prefs.getLocation()
        lastLoadedCityId = cityId

        collectJob?.cancel()
        collectJob = viewModelScope.launch {
            repository.getRestaurants(cityId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _uiState.update {
                        it.copy(isLoading = true, error = null)
                    }
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            restaurants = resource.data,
                            error = null,
                            isOffline = !Utils.isOnline(context)
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(isLoading = false, error = resource.message, isOffline = true)
                    }
                }
            }
        }
    }

    fun refreshIfLocationChanged() {
        if (lastLoadedCityId != prefs.getLocation()) loadRestaurants()
    }
}
