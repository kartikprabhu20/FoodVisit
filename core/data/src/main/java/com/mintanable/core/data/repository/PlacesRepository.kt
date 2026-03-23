package com.mintanable.core.data.repository

import com.mintanable.core.common.LoadingStatus
import com.mintanable.core.model.Restaurant
import com.mintanable.core.model.RestaurantInfo
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {

    /**
     * Offline-first flow of restaurants for [cityId].
     * Emits [LoadingStatus.Loading], then [LoadingStatus.Success] from the Room cache.
     * Triggers a network refresh if the cache is empty or older than [CACHE_TTL_MS].
     * Emits [LoadingStatus.Error] only when there is no cached data at all.
     */
    fun getRestaurants(cityId: String): Flow<LoadingStatus<List<RestaurantInfo>>>

    /** Live flow of all wishlisted restaurants. Auto-updates when wishlist changes. */
    fun getWishlistedRestaurants(): Flow<List<RestaurantInfo>>

    /**
     * Toggles wishlist status. If [wishlisted] = true and the restaurant is not in the DB
     * (e.g. cache was cleared), the entity is inserted from [restaurantInfo] first.
     */
    suspend fun setWishlisted(restaurantInfo: RestaurantInfo, wishlisted: Boolean)

    /** Returns whether [id] is currently wishlisted. */
    suspend fun isWishlisted(id: String): Boolean

    /** One-shot list used for widget refresh after a wishlist change. */
    suspend fun getWishlistedRestaurantsOnce(): List<Restaurant>

    companion object {
        const val CACHE_TTL_MS = 30 * 60 * 1000L   // 30 minutes
    }
}
