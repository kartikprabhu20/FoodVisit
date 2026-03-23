package com.mintanable.core.network.dto

import com.google.gson.annotations.SerializedName

data class PlacesSearchResponse(
    @SerializedName("places") val places: List<PlaceDto>? = null
)

data class PlaceDto(
    @SerializedName("id") val id: String,
    @SerializedName("displayName") val displayName: DisplayNameDto?,
    @SerializedName("formattedAddress") val formattedAddress: String?,
    @SerializedName("location") val location: LatLngDto?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("userRatingCount") val userRatingCount: Int?,
    @SerializedName("priceLevel") val priceLevel: String?,
    @SerializedName("types") val types: List<String>?,
    @SerializedName("photos") val photos: List<PhotoDto>?,
    @SerializedName("reservable") val reservable: Boolean?,
    @SerializedName("delivery") val delivery: Boolean?,
    @SerializedName("addressComponents") val addressComponents: List<AddressComponentDto>?
)

data class DisplayNameDto(
    @SerializedName("text") val text: String?,
    @SerializedName("languageCode") val languageCode: String?
)

data class LatLngDto(
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?
)

data class PhotoDto(
    @SerializedName("name") val name: String?,
    @SerializedName("widthPx") val widthPx: Int?,
    @SerializedName("heightPx") val heightPx: Int?
)

data class AddressComponentDto(
    @SerializedName("longText") val longText: String?,
    @SerializedName("shortText") val shortText: String?,
    @SerializedName("types") val types: List<String>?
)
