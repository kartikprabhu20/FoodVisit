package com.mintanable.foodvisit.ui.preview

import com.mintanable.core.model.Location
import com.mintanable.core.model.Restaurant
import com.mintanable.core.model.RestaurantInfo
import com.mintanable.core.model.UserRating

internal val sampleRestaurantInfo = RestaurantInfo(
    id = "1",
    name = "The Spice Garden",
    featuredImage = "",
    cuisines = "Indian, Chinese",
    averageCostForTwo = 600,
    currency = "₹",
    priceRange = 2,
    hasOnlineDelivery = 1,
    hasTableBooking = 0,
    location = Location(
        address = "12 MG Road",
        locality = "Indiranagar",
        city = "Bangalore",
        zipcode = "560001",
        latitude = "12.9716",
        longitude = "77.5946"
    ),
    userRating = UserRating(
        aggregateRating = "4.2",
        ratingText = "Very Good",
        votes = "1,234"
    )
)

internal val sampleRestaurantInfo2 = sampleRestaurantInfo.copy(
    id = "2",
    name = "Coastal Bites",
    cuisines = "Seafood, Coastal"
)

internal val sampleRestaurantInfo3 = sampleRestaurantInfo.copy(
    id = "3",
    name = "Mughal Dine",
    cuisines = "Mughlai, North Indian"
)

internal val sampleRestaurant = Restaurant(sampleRestaurantInfo)
