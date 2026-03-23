package com.mintanable.foodvisit.data.repository

import com.mintanable.foodvisit.data.model.Resource
import com.mintanable.core.model.Restaurant
import com.mintanable.core.model.RestaurantInfo
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {

    /**
     * Offline-first flow of restaurants for [cityId].
     * Emits [Resource.Loading], then [Resource.Success] from the Room cache.
     * Triggers a network refresh if the cache is empty or older than [CACHE_TTL_MS].
     * Emits [Resource.Error] only when there is no cached data at all.
     */
    fun getRestaurants(cityId: String): Flow<Resource<List<RestaurantInfo>>>

    /** Live flow of all wishlisted restaurants. Auto-updates when wishlist changes. */
    fun getWishlistedRestaurants(): Flow<List<RestaurantInfo>>

    /**
     * Toggles wishlist status. If [wishlisted] = true and the restaurant is not in the DB
     * (e.g. cache was cleared), the entity is inserted from [restaurantInfo] first.
     * Also triggers a widget data refresh.
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
