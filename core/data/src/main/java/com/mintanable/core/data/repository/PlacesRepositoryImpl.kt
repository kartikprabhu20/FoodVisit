package com.mintanable.core.data.repository

import com.mintanable.core.data.local.dao.RestaurantDao
import com.mintanable.core.data.mapper.PlaceMapper
import com.mintanable.core.common.LoadingStatus
import com.mintanable.core.data.repository.PlacesRepository.Companion.CACHE_TTL_MS
import com.mintanable.core.model.Restaurant
import com.mintanable.core.model.RestaurantInfo
import com.mintanable.core.network.PlacesRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepositoryImpl @Inject constructor(
    private val remoteDataSource: PlacesRemoteDataSource,
    private val dao: RestaurantDao,
    private val mapper: PlaceMapper
) : PlacesRepository {

    companion object {
        /** Maps the legacy Zomato city IDs stored in SharedPreferences to Places API city names. */
        private val CITY_ID_TO_NAME = mapOf(
            "4" to "Bangalore",
            "1" to "Delhi",
            "3" to "Chennai",
            "2" to "Kolkata"
        )
    }

    override fun getRestaurants(cityId: String): Flow<LoadingStatus<List<RestaurantInfo>>> =
        channelFlow {
            send(LoadingStatus.Loading)

            val cachedCount = dao.getCachedCount(cityId)
            val lastCachedAt = dao.getLastCachedAt(cityId) ?: 0L
            val isStale = System.currentTimeMillis() - lastCachedAt > CACHE_TTL_MS

            if (cachedCount == 0 || isStale) {
                val cityName = CITY_ID_TO_NAME[cityId] ?: cityId
                remoteDataSource.searchRestaurants(cityName)
                    .onSuccess { response ->
                        val entities = response.places
                            ?.map { mapper.toEntity(it, cityId) }
                            ?: emptyList()
                        dao.upsertPreservingWishlist(entities)
                    }
                    .onFailure { error ->
                        if (cachedCount == 0) {
                            send(LoadingStatus.Error(error.message ?: "Network error", error))
                            return@channelFlow
                        }
                        // Stale cache exists — fall through and serve it below
                    }
            }

            dao.getRestaurantsByCity(cityId)
                .map { entities -> LoadingStatus.Success(entities.map { mapper.toRestaurantInfo(it) }) }
                .collect { send(it) }
        }.flowOn(Dispatchers.IO)

    override fun getWishlistedRestaurants(): Flow<List<RestaurantInfo>> =
        dao.getWishlistedRestaurants()
            .map { entities -> entities.map { mapper.toRestaurantInfo(it) } }
            .flowOn(Dispatchers.IO)

    override suspend fun setWishlisted(restaurantInfo: RestaurantInfo, wishlisted: Boolean) {
        withContext(Dispatchers.IO) {
            val id = restaurantInfo.id ?: return@withContext

            // If wishlisting and the restaurant is not in the DB (cache cleared), insert it.
            if (wishlisted && dao.getById(id) == null) {
                dao.insertIfAbsent(mapper.toEntity(restaurantInfo))
            }

            dao.setWishlisted(id, wishlisted)
        }
    }

    override suspend fun isWishlisted(id: String): Boolean =
        withContext(Dispatchers.IO) { dao.isWishlisted(id) ?: false }

    override suspend fun getWishlistedRestaurantsOnce(): List<Restaurant> =
        withContext(Dispatchers.IO) {
            dao.getWishlistedRestaurantsOnce().map { mapper.toRestaurant(it) }
        }
}
