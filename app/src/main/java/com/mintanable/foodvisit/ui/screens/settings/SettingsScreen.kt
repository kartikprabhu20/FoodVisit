package com.mintanable.foodvisit.ui.screens.settings

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mintanable.foodvisit.ui.theme.FoodVisitTheme

@Composable
fun SettingsScreen(
    onOpenDrawer: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsContent(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onCitySelected = viewModel::selectCityByName,
        onUseCurrentLocation = viewModel::useCurrentLocation,
        onClearLocationError = viewModel::clearLocationError
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    onNavigateUp: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onCitySelected: (String) -> Unit,
    onUseCurrentLocation: () -> Unit,
    onClearLocationError: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.locationError) {
        if (uiState.locationError != null) {
            snackbarHostState.showSnackbar(uiState.locationError)
            onClearLocationError()
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) onUseCurrentLocation()
    }

    // Determine current selected city display name
    val selectedDisplayName = CITY_OPTIONS.firstOrNull { it.first == uiState.selectedCityId }?.second
        ?: uiState.selectedCityId

    // Filter predefined city suggestions based on search query
    val filteredCities = if (uiState.searchQuery.isBlank()) {
        CITY_OPTIONS
    } else {
        CITY_OPTIONS.filter { (_, name) ->
            name.contains(uiState.searchQuery, ignoreCase = true)
        }
    }

    // Show "Search for X" option when query is non-empty and doesn't match a predefined city exactly
    val showCustomOption = uiState.searchQuery.isNotBlank() &&
            CITY_OPTIONS.none { it.second.equals(uiState.searchQuery, ignoreCase = true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Select City",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Search bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search city…") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            // Use current location row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                    .padding(vertical = 10.dp)
            ) {
                if (uiState.locationLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Icon(
                        Icons.Default.MyLocation,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Use Current Location",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (uiState.locationLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // City suggestions list
            LazyColumn {
                // Custom city option when the typed query doesn't match a predefined city
                if (showCustomOption) {
                    item {
                        CityRow(
                            name = "Search \"${uiState.searchQuery}\"",
                            isSelected = false,
                            onClick = { onCitySelected(uiState.searchQuery) }
                        )
                    }
                }
                items(filteredCities) { (cityId, cityName) ->
                    CityRow(
                        name = cityName,
                        isSelected = uiState.selectedCityId == cityId ||
                                selectedDisplayName.equals(cityName, ignoreCase = true),
                        onClick = { onCitySelected(cityName) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CityRow(name: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsContentPreview() {
    FoodVisitTheme {
        SettingsContent(
            uiState = SettingsUiState(selectedCityId = "4", searchQuery = "Bangalore"),
            onNavigateUp = {},
            onSearchQueryChanged = {},
            onCitySelected = {},
            onUseCurrentLocation = {},
            onClearLocationError = {}
        )
    }
}
