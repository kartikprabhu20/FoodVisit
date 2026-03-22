package com.mintanable.foodvisit.ui.screens.maps

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodie.mintanable.Utils
import com.foodie.mintanable.model.RestaurantInfo
import com.mintanable.foodvisit.Utils
import com.mintanable.foodvisit.model.RestaurantInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _restaurants = MutableStateFlow<List<RestaurantInfo>>(emptyList())
    val restaurants: StateFlow<List<RestaurantInfo>> = _restaurants.asStateFlow()

    init {
        viewModelScope.launch {
            _restaurants.value = withContext(Dispatchers.IO) {
                Utils.getRestaurantInfoListFromDB(context)
            }
        }
    }
}
