package com.mintanable.foodvisit.ui.screens.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
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
import androidx.compose.material.icons.filled.RemoveCircleOutline
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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.mintanable.foodvisit.currencySymbolFor
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailScreen(
    restaurant: Restaurant?,
    onNavigateUp: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
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
        onToggleWishlist = { if (info != null) viewModel.toggleWishlist(info) },
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
private fun DetailContent(
    restaurant: Restaurant?,
    uiState: DetailUiState,
    onNavigateUp: () -> Unit,
    onToggleWishlist: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    val info = restaurant?.restaurantInfo
    val context = LocalContext.current

    val transition = animatedVisibilityScope?.transition
    val animationProgress by transition?.animateFloat(
        label = "GradientSync",
        transitionSpec = {
            tween(durationMillis = 500, easing = LinearOutSlowInEasing)
        }
    ) { state ->
        if (state == EnterExitState.Visible) 1f else 0f
    } ?: remember { mutableStateOf(1f) }

    val imageModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
        with(sharedTransitionScope) {
            Modifier
                .fillMaxSize()
                .sharedElement(
                    rememberSharedContentState(key = "image-${info?.id}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = animationProgress * 0.9f)
                            ),
                            startY = size.height * (1f - (animationProgress * 0.4f))//60% of heigth
                        )
                    )
                }
        }
    } else {
        Modifier.fillMaxSize()
    }

    val titleModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
        with(sharedTransitionScope) {
            Modifier
                .fillMaxWidth()
                .sharedElement(
                    rememberSharedContentState(key = "title-${info?.id}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
        }
    } else {
        Modifier.fillMaxWidth()
    }

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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {


                AsyncImage(
                    model = info?.featuredImage,
                    contentDescription = info?.name,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier
                )
                // Gradient overlay
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(
//                            Brush.verticalGradient(
//                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = gradientAlpha)),
//                                startY = gradientStartOffset
//                            )
//                        )
//                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = info?.name.orEmpty(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        modifier = titleModifier
                    )
                    val priceRange = info?.priceRange ?: 0
                    val ratingStr = info?.userRating?.aggregateRating
                    if (!ratingStr.isNullOrBlank() || priceRange > 0) {
                        Spacer(Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (!ratingStr.isNullOrBlank()) {
                                val ratingFloat = (ratingStr.toFloatOrNull() ?: 0f).coerceIn(0f, 5f)
                                AnimatedUserRating(rating = ratingFloat, ratingText = ratingStr)
                            }
                            if (priceRange > 0) {
                                AnimatedPriceCurrency(
                                    target = priceRange.coerceIn(1, 4),
                                    symbol = currencySymbolFor(info?.currency)
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
                                val uri =
                                    Uri.parse("geo:$lat,$lon?q=${Uri.encode(info?.name ?: "")}")
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

private enum class StarFill { EMPTY, HALF, FULL }

private fun computeStarFills(rating: Float): List<StarFill> {
    val full = rating.toInt().coerceIn(0, 5)
    val fraction = rating - full
    val hasHalf = fraction >= 0.5f && full < 5
    return List(5) { index ->
        when {
            index < full -> StarFill.FULL
            index == full && hasHalf -> StarFill.HALF
            else -> StarFill.EMPTY
        }
    }
}

@Composable
private fun AnimatedUserRating(rating: Float, ratingText: String) {
    val fills = remember(rating) { computeStarFills(rating) }
    val nonEmptyCount = fills.count { it != StarFill.EMPTY }
    var visibleCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(rating) {
        visibleCount = 0
        delay(500)
        repeat(nonEmptyCount) {
            delay(180L)
            visibleCount++
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            AnimatedStar(fill = if (index < visibleCount) fills[index] else StarFill.EMPTY)
        }
        Spacer(Modifier.width(4.dp))
        Text(
            text = "($ratingText)",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.85f)
        )
    }
}

@Composable
private fun AnimatedPriceCurrency(target: Int, symbol: String) {
    var filledCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(target) {
        filledCount = 0
        delay(500)
        repeat(target) {
            delay(200L)
            filledCount++
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(4) { index ->
            AnimatedCurrencyUnit(filled = index < filledCount, symbol = symbol)
        }
    }
}

@Composable
private fun AnimatedCurrencyUnit(filled: Boolean, symbol: String) {
    val scale by animateFloatAsState(
        targetValue = if (filled) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 500f),
        label = "currencyScale"
    )
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(20.dp)) {
        CurrencyIcon(symbol = symbol, faded = true)
        Box(modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale; alpha = scale }) {
            CurrencyIcon(symbol = symbol, faded = false)
        }
    }
}

@Composable
private fun CurrencyIcon(symbol: String, faded: Boolean) {
    Text(
        text = symbol,
        color = if (faded) Color.White.copy(alpha = 0.35f) else Color(0xFF80CBC4),
        fontSize = 13.sp,
        fontWeight = if (faded) FontWeight.Light else FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.width(16.dp)
    )
}

@Composable
private fun AnimatedStar(fill: StarFill) {
    val scale by animateFloatAsState(
        targetValue = if (fill != StarFill.EMPTY) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 500f),
        label = "starScale"
    )
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(20.dp)) {
        Icon(
            imageVector = Icons.Default.StarBorder,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.35f),
            modifier = Modifier.size(18.dp)
        )
        Icon(
            imageVector = if (fill == StarFill.HALF) Icons.AutoMirrored.Filled.StarHalf else Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFFFD700),
            modifier = Modifier
                .size(18.dp)
                .graphicsLayer { scaleX = scale; scaleY = scale; alpha = scale }
        )
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
            imageVector = if (available) Icons.Default.CheckCircle else Icons.Default.RemoveCircleOutline,
            contentDescription = null,
            tint = if (available) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = if (available) "Yes" else " No",
            style = MaterialTheme.typography.bodySmall,
            color = if (available) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailContentPreview() {
    FoodVisitTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                DetailContent(
                    restaurant = sampleRestaurant,
                    uiState = DetailUiState(isWishlisted = false),
                    onNavigateUp = {},
                    onToggleWishlist = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailContentWishlistedPreview() {
    FoodVisitTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                DetailContent(
                    restaurant = sampleRestaurant,
                    uiState = DetailUiState(isWishlisted = true),
                    onNavigateUp = {},
                    onToggleWishlist = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeatureRowPreview() {
    FoodVisitTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            FeatureRow(
                icon = Icons.Default.TableRestaurant,
                label = "Table Booking (Available)",
                available = true
            )
            Spacer(Modifier.height(8.dp))
            FeatureRow(
                icon = Icons.Default.DeliveryDining,
                label = "Online Delivery (Unavailable)",
                available = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimatedUserRatingPreview() {
    FoodVisitTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            // 4.2 → 4 full stars
            AnimatedUserRating(rating = 4.2f, ratingText = "4.2")
            Spacer(Modifier.height(8.dp))
            // 4.6 → 4 full + half star
            AnimatedUserRating(rating = 4.6f, ratingText = "4.6")
            Spacer(Modifier.height(8.dp))
            // 5.0 → 5 full stars
            AnimatedUserRating(rating = 5.0f, ratingText = "5.0")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimatedPriceCurrencyPreview() {
    FoodVisitTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("India (₹)", color = Color.White, fontSize = 12.sp)
            AnimatedPriceCurrency(target = 3, symbol = "₹")
            Spacer(Modifier.height(8.dp))
            Text("USA ($)", color = Color.White, fontSize = 12.sp)
            AnimatedPriceCurrency(target = 2, symbol = "$")
            Spacer(Modifier.height(8.dp))
            Text("Europe (€)", color = Color.White, fontSize = 12.sp)
            AnimatedPriceCurrency(target = 4, symbol = "€")
        }
    }
}


