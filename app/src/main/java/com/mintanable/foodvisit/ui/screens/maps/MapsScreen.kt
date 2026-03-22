package com.mintanable.foodvisit.ui.screens.maps

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.mintanable.foodvisit.model.RestaurantInfo
import com.mintanable.foodvisit.ui.preview.sampleRestaurantInfo
import com.mintanable.foodvisit.ui.theme.FoodVisitTheme

@Composable
fun MapsScreen(
    onOpenDrawer: () -> Unit,
    viewModel: MapsViewModel = hiltViewModel()
) {
    val restaurants by viewModel.restaurants.collectAsStateWithLifecycle()
    MapsContent(restaurants = restaurants, onOpenDrawer = onOpenDrawer)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapsContent(
    restaurants: List<RestaurantInfo>,
    onOpenDrawer: () -> Unit
) {
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
        if (LocalInspectionMode.current) {
            // GoogleMap cannot render in preview — show placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text("Map (${restaurants.size} markers)", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(LatLng(12.9716, 77.5946), 11f)
            }
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
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MapsContentPreview() {
    FoodVisitTheme {
        MapsContent(
            restaurants = listOf(sampleRestaurantInfo),
            onOpenDrawer = {}
        )
    }
}
