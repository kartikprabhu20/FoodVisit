package com.mintanable.foodvisit.ui.screens.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.TableRestaurant
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mintanable.core.model.Restaurant
import com.mintanable.foodvisit.ui.preview.sampleRestaurant
import com.mintanable.foodvisit.ui.theme.FoodVisitTheme

@Composable
fun DetailScreen(
    restaurant: Restaurant?,
    onNavigateUp: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val info = restaurant?.restaurantInfo

    LaunchedEffect(info) {
        if (info != null) viewModel.init(info)
    }

    DetailContent(
        restaurant = restaurant,
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        onToggleWishlist = { if (info != null) viewModel.toggleWishlist(info) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun DetailContent(
    restaurant: Restaurant?,
    uiState: DetailUiState,
    onNavigateUp: () -> Unit,
    onToggleWishlist: () -> Unit
) {
    val info = restaurant?.restaurantInfo
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            if (info != null) {
                FloatingActionButton(onClick = onToggleWishlist) {
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
                .padding(bottom = innerPadding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            // Hero image with gradient overlay + restaurant name
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                AsyncImage(
                    model = info?.featuredImage,
                    contentDescription = info?.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                                startY = 120f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = info?.name.orEmpty(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                    // Price range stars
                    val priceRange = info?.priceRange ?: 0
                    if (priceRange > 0) {
                        Spacer(Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(priceRange.coerceAtMost(4)) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            repeat((4 - priceRange).coerceAtLeast(0)) {
                                Icon(
                                    Icons.Default.StarBorder,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.6f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Cuisine chips
            val cuisines = info?.cuisines
            if (!cuisines.isNullOrBlank()) {
                val chips = cuisines.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                FlowRow(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    chips.forEach { cuisine ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(cuisine, style = MaterialTheme.typography.labelMedium) },
                            icon = {
                                Icon(
                                    Icons.Default.Restaurant,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        )
                    }
                }
            } else {
                Spacer(Modifier.height(12.dp))
            }

            // Location card
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Location", style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(Modifier.height(8.dp))
                    val address = listOfNotNull(
                        info?.location?.address,
                        info?.location?.locality,
                        info?.location?.city,
                        info?.location?.zipcode
                    ).filter { it.isNotBlank() }.joinToString(", ")
                    if (address.isNotBlank()) {
                        Text(
                            text = address,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    val lat = info?.location?.latitude?.toDoubleOrNull()
                    val lon = info?.location?.longitude?.toDoubleOrNull()
                    if (lat != null && lon != null) {
                        Spacer(Modifier.height(4.dp))
                        TextButton(
                            onClick = {
                                val uri = Uri.parse("geo:$lat,$lon?q=${Uri.encode(info?.name ?: "")}")
                                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                            }
                        ) {
                            Icon(
                                Icons.Default.Map,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("View on Map")
                        }
                    }
                }
            }

            // Rating card
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Rating row
                    val rating = info?.userRating?.aggregateRating
                    val ratingText = info?.userRating?.ratingText
                    val votes = info?.userRating?.votes
                    if (!rating.isNullOrBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = rating,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (!ratingText.isNullOrBlank()) {
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = ratingText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            if (!votes.isNullOrBlank()) {
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "($votes votes)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                    // Table booking row
                    FeatureRow(
                        icon = Icons.Default.TableRestaurant,
                        label = "Table Booking",
                        available = info?.hasTableBooking == 1
                    )
                    Spacer(Modifier.height(8.dp))
                    // Delivery row
                    FeatureRow(
                        icon = Icons.Default.DeliveryDining,
                        label = "Online Delivery",
                        available = info?.hasOnlineDelivery == 1
                    )
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun FeatureRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    available: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = if (available) Icons.Default.CheckCircle else Icons.Default.StarBorder,
            contentDescription = null,
            tint = if (available) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = if (available) "Yes" else "No",
            style = MaterialTheme.typography.bodySmall,
            color = if (available) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailContentPreview() {
    FoodVisitTheme {
        DetailContent(
            restaurant = sampleRestaurant,
            uiState = DetailUiState(isWishlisted = false),
            onNavigateUp = {},
            onToggleWishlist = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailContentWishlistedPreview() {
    FoodVisitTheme {
        DetailContent(
            restaurant = sampleRestaurant,
            uiState = DetailUiState(isWishlisted = true),
            onNavigateUp = {},
            onToggleWishlist = {}
        )
    }
}
