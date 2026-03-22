package com.mintanable.foodvisit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mintanable.foodvisit.data.local.entity.RestaurantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {

    // ── Queries ──────────────────────────────────────────────────────────────

    /** Live list of non-wishlisted restaurants for a city (home / maps search results). */
    @Query("SELECT * FROM restaurants WHERE cityId = :cityId AND isWishlisted = 0 ORDER BY aggregateRating DESC")
    fun getRestaurantsByCity(cityId: String): Flow<List<RestaurantEntity>>

    /** Live list of all wishlisted restaurants (To Visit / Maps screens). */
    @Query("SELECT * FROM restaurants WHERE isWishlisted = 1 ORDER BY name ASC")
    fun getWishlistedRestaurants(): Flow<List<RestaurantEntity>>

    /** One-shot version for widget refresh after a wishlist toggle. */
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
    suspend fun replaceAll(restaurants: List<RestaurantEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfAbsent(restaurant: RestaurantEntity)

    @Query("UPDATE restaurants SET isWishlisted = :wishlisted WHERE id = :id")
    suspend fun setWishlisted(id: String, wishlisted: Boolean)

    @Query("UPDATE restaurants SET isWishlisted = 1 WHERE id IN (:ids)")
    suspend fun restoreWishlisted(ids: List<String>)

    /**
     * Replaces all cached results for a city while preserving [isWishlisted] flags.
     *
     * Strategy:
     * 1. Snapshot currently wishlisted IDs.
     * 2. REPLACE all rows (resets isWishlisted = false for everything).
     * 3. Restore wishlisted = 1 for previously-wishlisted IDs.
     */
    @Transaction
    suspend fun upsertPreservingWishlist(restaurants: List<RestaurantEntity>) {
        val wishlistedIds = getWishlistedIds()
        replaceAll(restaurants)
        if (wishlistedIds.isNotEmpty()) {
            restoreWishlisted(wishlistedIds)
        }
    }
}
