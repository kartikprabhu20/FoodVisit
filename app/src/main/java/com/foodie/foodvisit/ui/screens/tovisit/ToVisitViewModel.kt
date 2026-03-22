package com.foodie.foodvisit.ui.screens.tovisit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodie.foodvisit.Utils
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
class ToVisitViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ToVisitUiState())
    val uiState: StateFlow<ToVisitUiState> = _uiState.asStateFlow()

    init {
        loadFromDb()
    }

    fun loadFromDb() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val restaurants = withContext(Dispatchers.IO) {
                Utils.getRestaurantInfoListFromDB(context)
            }
            _uiState.update { it.copy(isLoading = false, restaurants = restaurants) }
        }
    }
}
