package com.foodie.foodvisit.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.foodie.foodvisit.AppPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: AppPreferenceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState(selectedCityId = prefs.getLocation()))
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun selectCity(cityId: String) {
        prefs.setLocation(cityId)
        _uiState.update { it.copy(selectedCityId = cityId) }
    }
}
