package com.mintanable.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mintanable.core.data.local.entity.RestaurantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {

    // ── Queries ──────────────────────────────────────────────────────────────

    @Query("SELECT * FROM restaurants WHERE cityId = :cityId AND isWishlisted = 0 ORDER BY aggregateRating DESC")
    fun getRestaurantsByCity(cityId: String): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE isWishlisted = 1 ORDER BY name ASC")
    fun getWishlistedRestaurants(): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE isWishlisted = 1 ORDER BY name ASC")
    suspend fun getWishlistedRestaurantsOnce(): List<RestaurantEntity>

    @Query("SELECT id FROM restaurants WHERE isWishlisted = 1")
    suspend fun getWishlistedIds(): List<String>

    @Query("SELECT isWishlisted FROM restaurants WHERE id = :id")
    suspend fun isWishlisted(id: String): Boolean?

    @Query("SELECT * FROM restaurants WHERE id = :id")
    suspend fun getById(id: String): RestaurantEntity?

    @Query("SELECT COUNT(*) FROM restaurants WHERE cityId = :cityId AND isWishlisted = 0")
    suspend fun getCachedCount(cityId: String): Int

    @Query("SELECT MAX(cachedAt) FROM restaurants WHERE cityId = :cityId AND isWishlisted = 0")
    suspend fun getLastCachedAt(cityId: String): Long?

    // ── Writes ───────────────────────────────────────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceAll(restaurants: List<RestaurantEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfAbsent(restaurant: RestaurantEntity): Long

    @Query("UPDATE restaurants SET isWishlisted = :wishlisted WHERE id = :id")
    suspend fun setWishlisted(id: String, wishlisted: Boolean): Int

    @Query("UPDATE restaurants SET isWishlisted = 1 WHERE id IN (:ids)")
    suspend fun restoreWishlisted(ids: List<String>): Int

    /**
     * Replaces all cached results for a city while preserving [isWishlisted] flags.
     */
    @Transaction
    suspend fun upsertPreservingWishlist(restaurants: List<RestaurantEntity>): Int {
        val wishlistedIds = getWishlistedIds()
        replaceAll(restaurants)
        if (wishlistedIds.isNotEmpty()) {
            restoreWishlisted(wishlistedIds)
        }
        return restaurants.size
    }
}
