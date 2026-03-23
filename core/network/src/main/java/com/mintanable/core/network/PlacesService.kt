package com.mintanable.core.network

import com.mintanable.core.network.dto.PlacesSearchRequest
import com.mintanable.core.network.dto.PlacesSearchResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PlacesService {

    companion object {
        /** All fields we need from the Places API (New). */
        const val FIELD_MASK =
            "places.id," +
            "places.displayName," +
            "places.formattedAddress," +
            "places.addressComponents," +
            "places.location," +
            "places.rating," +
            "places.userRatingCount," +
            "places.priceLevel," +
            "places.types," +
            "places.photos," +
            "places.reservable," +
            "places.delivery"
    }

    @POST("v1/places:searchText")
    suspend fun searchText(
        @Header("X-Goog-Api-Key") apiKey: String,
        @Header("X-Goog-FieldMask") fieldMask: String = FIELD_MASK,
        @Body request: PlacesSearchRequest
    ): PlacesSearchResponse
}
