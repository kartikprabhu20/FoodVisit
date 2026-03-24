package com.mintanable.foodvisit.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mintanable.core.model.RestaurantInfo
import com.mintanable.foodvisit.ui.preview.sampleRestaurantInfo
import com.mintanable.foodvisit.ui.theme.FoodVisitTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RestaurantCard(
    restaurant: RestaurantInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val imageModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
                with(sharedTransitionScope) {
                    Modifier
                        .fillMaxSize()
                        .sharedElement(
                            rememberSharedContentState(key = "image-${restaurant.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                }
            } else {
                Modifier.fillMaxSize()
            }

            val textModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
                with(sharedTransitionScope) {
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                        .sharedElement(
                            rememberSharedContentState(key = "title-${restaurant.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                }
            } else {
                Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp)
            }

            AsyncImage(
                model = restaurant.featuredImage,
                contentDescription = restaurant.name,
                contentScale = ContentScale.Crop,
                modifier = imageModifier
            )

            // Title overlay at top
            Text(
                text = restaurant.name.orEmpty(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = textModifier
            )

            // Rating badge at bottom-end
            val rating = restaurant.userRating?.aggregateRating
            if (!rating.isNullOrEmpty()) {
                Text(
                    text = rating,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RestaurantCardPreview() {
    FoodVisitTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                RestaurantCard(
                    restaurant = sampleRestaurantInfo,
                    onClick = {},
                    modifier = Modifier.size(width = 120.dp, height = 120.dp),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}
