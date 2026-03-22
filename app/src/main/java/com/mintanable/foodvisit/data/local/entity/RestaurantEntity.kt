package com.mintanable.foodvisit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Single source of truth for all restaurant data.
 *
 * - [cityId] partitions the cache by city so [getRestaurantsByCity] is efficient.
 * - [isWishlisted] replaces the old ContentProvider "To Visit" list.
 *   It is preserved across cache refreshes via [RestaurantDao.upsertPreservingWishlist].
 * - [cachedAt] epoch-ms timestamp used for cache staleness checks.
 */
@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey val id: String,

    // Identity
    val name: String?,
    val url: String?,

    // Images
    val featuredImage: String?,     // full photo URL built from Places photo reference
    val thumb: String?,             // same URL (Places doesn't distinguish thumb)

    // Classification
    val cuisines: String?,          // derived from types[], comma-separated
    val priceRange: Int?,           // 0–4 mapped from Places priceLevel string
    val currency: String?,          // null — no equivalent in Places API

    // Cost (no equivalent in Places API)
    val averageCostForTwo: Int?,

    // Features — mapped from Places reservable / delivery booleans
    val hasTableBooking: Int?,      // 1 = true
    val hasOnlineDelivery: Int?,    // 1 = true
    val isDeliveringNow: Int?,      // null — no real-time equivalent

    // Misc Zomato fields (no equivalent, kept for model parity)
    val deeplink: String?,
    val photosUrl: String?,
    val menuUrl: String?,
    val eventsUrl: String?,

    // Location (flattened from Place.addressComponents + location)
    val address: String?,           // formattedAddress
    val locality: String?,          // sublocality_level_1
    val city: String?,              // locality component
    val zipcode: String?,           // postal_code component
    val latitude: String?,
    val longitude: String?,

    // Rating (flattened from Place.rating + userRatingCount)
    val aggregateRating: String?,
    val ratingText: String?,        // derived: ≥4.5→Excellent, ≥4.0→Very Good, etc.
    val ratingColor: String?,       // null
    val votes: String?,             // userRatingCount.toString()

    // Cache metadata
    val cityId: String,
    val isWishlisted: Boolean = false,
    val cachedAt: Long = System.currentTimeMillis()
)
