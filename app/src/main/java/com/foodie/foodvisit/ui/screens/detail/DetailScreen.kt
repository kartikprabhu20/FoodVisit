package com.foodie.foodvisit.ui.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.foodie.foodvisit.model.Restaurant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    restaurant: Restaurant?,
    onNavigateUp: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val info = restaurant?.restaurantInfo
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(info) {
        if (info != null) viewModel.init(info)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(info?.name.orEmpty()) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (info != null) {
                FloatingActionButton(onClick = { viewModel.toggleWishlist(info) }) {
                    Icon(
                        imageVector = if (uiState.isWishlisted) Icons.Default.Bookmark
                                      else Icons.Default.BookmarkBorder,
                        contentDescription = if (uiState.isWishlisted) "Remove from wishlist"
                                             else "Add to wishlist"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = info?.featuredImage,
                contentDescription = info?.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Location card
            InfoCard(title = "Location") {
                LabeledRow("Address", info?.location?.address)
                LabeledRow("Locality", info?.location?.locality)
                LabeledRow("City", info?.location?.city)
                LabeledRow("ZIP", info?.location?.zipcode)
            }

            // Details card
            InfoCard(title = "Details") {
                LabeledRow("Cuisines", info?.cuisines)
                LabeledRow("Avg cost for two", info?.averageCostForTwo?.let { "${info.currency} $it" })
                LabeledRow("Online delivery", if (info?.hasOnlineDelivery == 1) "Yes" else "No")
                LabeledRow("Table booking", if (info?.hasTableBooking == 1) "Yes" else "No")
            }

            // Reviews card
            InfoCard(title = "Reviews") {
                LabeledRow("Rating", info?.userRating?.aggregateRating)
                LabeledRow("Rating text", info?.userRating?.ratingText)
                LabeledRow("Votes", info?.userRating?.votes)
            }

            Spacer(Modifier.height(80.dp)) // FAB clearance
        }
    }
}

@Composable
private fun InfoCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun LabeledRow(label: String, value: String?) {
    if (value.isNullOrEmpty()) return
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}
