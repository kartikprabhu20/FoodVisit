package com.mintanable.foodvisit.ui.screens.tovisit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mintanable.foodvisit.model.Restaurant
import com.mintanable.foodvisit.ui.components.RestaurantCard
import com.mintanable.foodvisit.ui.preview.sampleRestaurantInfo
import com.mintanable.foodvisit.ui.preview.sampleRestaurantInfo2
import com.mintanable.foodvisit.ui.preview.sampleRestaurantInfo3
import com.mintanable.foodvisit.ui.theme.FoodVisitTheme

@Composable
fun ToVisitScreen(
    onOpenDrawer: () -> Unit,
    onRestaurantClick: (Restaurant) -> Unit,
    viewModel: ToVisitViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.loadFromDb()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    ToVisitContent(
        uiState = uiState,
        onOpenDrawer = onOpenDrawer,
        onRestaurantClick = onRestaurantClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToVisitContent(
    uiState: ToVisitUiState,
    onOpenDrawer: () -> Unit,
    onRestaurantClick: (Restaurant) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To Visit") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Open drawer")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                uiState.restaurants.isEmpty() -> Text(
                    text = "No restaurants saved yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.restaurants) { info ->
                        RestaurantCard(
                            restaurant = info,
                            onClick = { onRestaurantClick(Restaurant(info)) },
                            modifier = Modifier.height(120.dp)
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
private fun ToVisitEmptyPreview() {
    FoodVisitTheme {
        ToVisitContent(
            uiState = ToVisitUiState(),
            onOpenDrawer = {},
            onRestaurantClick = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ToVisitPopulatedPreview() {
    FoodVisitTheme {
        ToVisitContent(
            uiState = ToVisitUiState(
                restaurants = listOf(
                    sampleRestaurantInfo,
                    sampleRestaurantInfo2,
                    sampleRestaurantInfo3
                )
            ),
            onOpenDrawer = {},
            onRestaurantClick = {}
        )
    }
}
