package com.mintanable.core.network

import com.mintanable.core.network.dto.PlacesSearchRequest
import com.mintanable.core.network.dto.PlacesSearchResponse
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class PlacesRemoteDataSource @Inject constructor(
    private val service: PlacesService,
    @Named("placesApiKey") private val apiKey: String
) {
    suspend fun searchRestaurants(cityName: String): Result<PlacesSearchResponse> =
        runCatching {
            service.searchText(
                apiKey = apiKey,
                fieldMask = PlacesService.FIELD_MASK,
                request = PlacesSearchRequest(textQuery = "restaurants in $cityName")
            )
        }
}
