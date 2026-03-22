package com.mintanable.foodvisit.data.mapper

import com.mintanable.foodvisit.BuildConfig
import com.mintanable.foodvisit.data.local.entity.RestaurantEntity
import com.mintanable.foodvisit.data.remote.dto.AddressComponentDto
import com.mintanable.foodvisit.data.remote.dto.PlaceDto
import com.mintanable.foodvisit.model.Location
import com.mintanable.foodvisit.model.Restaurant
import com.mintanable.foodvisit.model.RestaurantInfo
import com.mintanable.foodvisit.model.UserRating

object PlaceMapper {

    // ── PlaceDto → RestaurantEntity ──────────────────────────────────────────

    fun toEntity(dto: PlaceDto, cityId: String): RestaurantEntity {
        val components = dto.addressComponents ?: emptyList()
        return RestaurantEntity(
            id             = dto.id,
            name           = dto.displayName?.text,
            url            = null,
            featuredImage  = dto.photos?.firstOrNull()?.name?.toPhotoUrl(),
            thumb          = dto.photos?.firstOrNull()?.name?.toPhotoUrl(),
            cuisines       = dto.types?.toCuisineString(),
            priceRange     = dto.priceLevel?.toPriceRange(),
            currency       = null,
            averageCostForTwo = null,
            hasTableBooking  = dto.reservable.toInt(),
            hasOnlineDelivery = dto.delivery.toInt(),
            isDeliveringNow  = null,
            deeplink       = null,
            photosUrl      = null,
            menuUrl        = null,
            eventsUrl      = null,
            address        = dto.formattedAddress,
            locality       = components.firstOfType("sublocality_level_1"),
            city           = components.firstOfType("locality"),
            zipcode        = components.firstOfType("postal_code"),
            latitude       = dto.location?.latitude?.toString(),
            longitude      = dto.location?.longitude?.toString(),
            aggregateRating = dto.rating?.toString(),
            ratingText     = dto.rating?.toRatingText(),
            ratingColor    = null,
            votes          = dto.userRatingCount?.toString(),
            cityId         = cityId,
            isWishlisted   = false,
            cachedAt       = System.currentTimeMillis()
        )
    }

    /** Reverse-maps a [RestaurantInfo] back to an entity (for wishlist persistence fallback). */
    fun toEntity(info: RestaurantInfo, cityId: String = ""): RestaurantEntity =
        RestaurantEntity(
            id             = info.id ?: "",
            name           = info.name,
            url            = info.url,
            featuredImage  = info.featuredImage,
            thumb          = info.thumb,
            cuisines       = info.cuisines,
            priceRange     = info.priceRange,
            currency       = info.currency,
            averageCostForTwo = info.averageCostForTwo,
            hasTableBooking  = info.hasTableBooking,
            hasOnlineDelivery = info.hasOnlineDelivery,
            isDeliveringNow  = info.isDeliveringNow,
            deeplink       = info.deeplink,
            photosUrl      = info.photosUrl,
            menuUrl        = info.menuUrl,
            eventsUrl      = info.eventsUrl,
            address        = info.location?.address,
            locality       = info.location?.locality,
            city           = info.location?.city,
            zipcode        = info.location?.zipcode,
            latitude       = info.location?.latitude,
            longitude      = info.location?.longitude,
            aggregateRating = info.userRating?.aggregateRating,
            ratingText     = info.userRating?.ratingText,
            ratingColor    = info.userRating?.ratingColor,
            votes          = info.userRating?.votes,
            cityId         = cityId,
            isWishlisted   = false,
            cachedAt       = System.currentTimeMillis()
        )

    // ── RestaurantEntity → domain models ────────────────────────────────────

    fun toRestaurantInfo(entity: RestaurantEntity): RestaurantInfo =
        RestaurantInfo(
            id            = entity.id,
            name          = entity.name,
            url           = entity.url,
            location      = Location(
                address          = entity.address,
                locality         = entity.locality,
                city             = entity.city,
                zipcode          = entity.zipcode,
                latitude         = entity.latitude,
                longitude        = entity.longitude
            ),
            cuisines          = entity.cuisines,
            priceRange        = entity.priceRange,
            currency          = entity.currency,
            averageCostForTwo = entity.averageCostForTwo,
            thumb             = entity.thumb,
            featuredImage     = entity.featuredImage,
            photosUrl         = entity.photosUrl,
            menuUrl           = entity.menuUrl,
            eventsUrl         = entity.eventsUrl,
            deeplink          = entity.deeplink,
            hasTableBooking   = entity.hasTableBooking,
            hasOnlineDelivery = entity.hasOnlineDelivery,
            isDeliveringNow   = entity.isDeliveringNow,
            userRating        = UserRating(
                aggregateRating = entity.aggregateRating,
                ratingText      = entity.ratingText,
                ratingColor     = entity.ratingColor,
                votes           = entity.votes
            )
        )

    fun toRestaurant(entity: RestaurantEntity): Restaurant =
        Restaurant(restaurantInfo = toRestaurantInfo(entity))

    // ── Private helpers ──────────────────────────────────────────────────────

    /** Builds the Places API photo URL from a photo resource name. */
    private fun String.toPhotoUrl(): String =
        "https://places.googleapis.com/v1/$this/media" +
        "?maxWidthPx=800&key=${BuildConfig.GOOGLE_MAPS_KEY}"

    private val GENERIC_TYPES = setOf(
        "restaurant", "food", "point_of_interest", "establishment",
        "store", "meal_delivery", "meal_takeaway"
    )

    /**
     * Converts a Places types list to a human-readable cuisine string.
     * e.g. ["italian_restaurant", "pizza_restaurant", "food"] → "Italian, Pizza"
     */
    private fun List<String>.toCuisineString(): String? {
        val specific = filter { it !in GENERIC_TYPES }
            .map { type ->
                type.removeSuffix("_restaurant")
                    .removeSuffix("_food")
                    .replace('_', ' ')
                    .split(' ')
                    .joinToString(" ") { word ->
                        word.replaceFirstChar { it.uppercaseChar() }
                    }
            }
            .distinct()
        return specific.joinToString(", ").takeIf { it.isNotEmpty() }
    }

    private fun String.toPriceRange(): Int = when (this) {
        "PRICE_LEVEL_FREE"          -> 0
        "PRICE_LEVEL_INEXPENSIVE"   -> 1
        "PRICE_LEVEL_MODERATE"      -> 2
        "PRICE_LEVEL_EXPENSIVE"     -> 3
        "PRICE_LEVEL_VERY_EXPENSIVE"-> 4
        else                        -> 2
    }

    private fun Double.toRatingText(): String = when {
        this >= 4.5 -> "Excellent"
        this >= 4.0 -> "Very Good"
        this >= 3.5 -> "Good"
        this >= 3.0 -> "Average"
        else        -> "Poor"
    }

    private fun Boolean?.toInt(): Int? = when (this) {
        true  -> 1
        false -> 0
        null  -> null
    }

    private fun List<AddressComponentDto>.firstOfType(type: String): String? =
        firstOrNull { it.types?.contains(type) == true }?.longText
}
