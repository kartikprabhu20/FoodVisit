package com.mintanable.foodvisit.ui.screens.settings

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.mintanable.foodvisit.AppPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: AppPreferenceManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val storedLocation = prefs.getLocation()
    private val initialCityName = CITY_OPTIONS.firstOrNull { it.first == storedLocation }?.second ?: storedLocation

    private val _uiState = MutableStateFlow(
        SettingsUiState(
            selectedCityId = storedLocation,
            searchQuery = initialCityName
        )
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun selectCity(cityId: String) {
        prefs.setLocation(cityId)
        val displayName = CITY_OPTIONS.firstOrNull { it.first == cityId }?.second ?: cityId
        _uiState.update { it.copy(selectedCityId = cityId, searchQuery = displayName) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun selectCityByName(name: String) {
        val cityId = CITY_OPTIONS.firstOrNull { it.second.equals(name, ignoreCase = true) }?.first ?: name
        prefs.setLocation(cityId)
        _uiState.update { it.copy(selectedCityId = cityId, searchQuery = name) }
    }

    @SuppressLint("MissingPermission")
    fun useCurrentLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(locationLoading = true, locationError = null) }
            try {
                val fusedClient = LocationServices.getFusedLocationProviderClient(context)
                val location = suspendCancellableCoroutine { cont ->
                    fusedClient.lastLocation
                        .addOnSuccessListener { loc -> cont.resume(loc) }
                        .addOnFailureListener { e -> cont.resumeWithException(e) }
                }
                if (location != null) {
                    @Suppress("DEPRECATION")
                    val addresses = Geocoder(context, Locale.getDefault())
                        .getFromLocation(location.latitude, location.longitude, 1)
                    val cityName = addresses?.firstOrNull()?.locality
                        ?: addresses?.firstOrNull()?.adminArea
                    if (cityName != null) {
                        _uiState.update { it.copy(searchQuery = cityName, locationLoading = false) }
                    } else {
                        _uiState.update { it.copy(locationLoading = false, locationError = "Could not determine city name") }
                    }
                } else {
                    _uiState.update { it.copy(locationLoading = false, locationError = "Location unavailable. Try moving outdoors.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(locationLoading = false, locationError = "Location error: ${e.message}") }
            }
        }
    }

    fun clearLocationError() {
        _uiState.update { it.copy(locationError = null) }
    }
}
