package com.foodie.foodvisit.ui.screens.maps

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(
    onOpenDrawer: () -> Unit,
    viewModel: MapsViewModel = hiltViewModel()
) {
    val restaurants by viewModel.restaurants.collectAsStateWithLifecycle()

    // Default camera position: Bangalore
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(12.9716, 77.5946), 11f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Maps") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Open drawer")
                    }
                }
            )
        }
    ) { innerPadding ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            cameraPositionState = cameraPositionState
        ) {
            restaurants.forEach { info ->
                val lat = info.location?.latitude?.toDoubleOrNull()
                val lon = info.location?.longitude?.toDoubleOrNull()
                if (lat != null && lon != null) {
                    Marker(
                        state = MarkerState(position = LatLng(lat, lon)),
                        title = info.name
                    )
                }
            }
        }
    }
}
