package com.mintanable.foodvisit.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mintanable.core.model.Restaurant
import com.mintanable.foodvisit.ui.components.RestaurantCard
import com.mintanable.foodvisit.ui.preview.sampleRestaurantInfo
import com.mintanable.foodvisit.ui.preview.sampleRestaurantInfo2
import com.mintanable.foodvisit.ui.preview.sampleRestaurantInfo3
import com.mintanable.foodvisit.ui.theme.FoodVisitTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    onOpenDrawer: () -> Unit,
    onRestaurantClick: (Restaurant) -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.refreshIfLocationChanged()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    HomeContent(
        uiState = uiState,
        onOpenDrawer = onOpenDrawer,
        onRestaurantClick = onRestaurantClick,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onOpenDrawer: () -> Unit,
    onRestaurantClick: (Restaurant) -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var columnCount by remember { mutableIntStateOf(3) }
    var zoomScale by remember { mutableFloatStateOf(1f) }

    val targetCardHeight = when (columnCount) {
        1 -> 200.dp
        2 -> 150.dp
        else -> 120.dp
    }
    val cardHeight by animateDpAsState(
        targetValue = targetCardHeight,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f),
        label = "cardHeight"
    )

    LaunchedEffect(uiState.isOffline) {
        if (uiState.isOffline) snackbarHostState.showSnackbar("No internet connection")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FoodVisit") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Open drawer")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        zoomScale *= zoom

                        when {
                            zoomScale > 1.35f && columnCount > 1 -> {
                                columnCount--
                                zoomScale = 1f
                            }
                            zoomScale < 0.74f && columnCount < 3 -> {
                                columnCount++
                                zoomScale = 1f
                            }
                        }

                        if (columnCount == 1) zoomScale = zoomScale.coerceAtMost(1.35f)
                        if (columnCount == 3) zoomScale = zoomScale.coerceAtLeast(0.74f)
                    }
                }
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(columnCount),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        uiState.restaurants,
                        key = { it.id ?: it.hashCode().toString() }
                    ) { info ->
                        RestaurantCard(
                            restaurant = info,
                            onClick = { onRestaurantClick(Restaurant(info)) },
                            modifier = Modifier
                                .height(cardHeight)
                                .animateItem(),
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeContentLoadingPreview() {
    FoodVisitTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                HomeContent(
                    uiState = HomeUiState(isLoading = true),
                    onOpenDrawer = {},
                    onRestaurantClick = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeContentLoadedPreview() {
    FoodVisitTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                HomeContent(
                    uiState = HomeUiState(
                        restaurants = listOf(
                            sampleRestaurantInfo,
                            sampleRestaurantInfo2,
                            sampleRestaurantInfo3
                        )
                    ),
                    onOpenDrawer = {},
                    onRestaurantClick = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeContentOfflinePreview() {
    FoodVisitTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                HomeContent(
                    uiState = HomeUiState(isOffline = true),
                    onOpenDrawer = {},
                    onRestaurantClick = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}
