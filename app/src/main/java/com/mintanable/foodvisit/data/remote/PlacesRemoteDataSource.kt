package com.mintanable.foodvisit.data.remote

import com.mintanable.foodvisit.BuildConfig
import com.mintanable.foodvisit.data.remote.dto.PlacesSearchRequest
import com.mintanable.foodvisit.data.remote.dto.PlacesSearchResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRemoteDataSource @Inject constructor(
    private val service: PlacesService
) {
    suspend fun searchRestaurants(cityName: String): Result<PlacesSearchResponse> =
        runCatching {
            service.searchText(
                apiKey = BuildConfig.GOOGLE_MAPS_KEY,
                fieldMask = PlacesService.FIELD_MASK,
                request = PlacesSearchRequest(textQuery = "restaurants in $cityName")
            )
        }
}
