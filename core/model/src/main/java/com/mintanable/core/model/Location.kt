package com.mintanable.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("address") val address: String? = null,
    @SerializedName("locality") val locality: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("city_id") val cityId: Int? = null,
    @SerializedName("latitude") val latitude: String? = null,
    @SerializedName("longitude") val longitude: String? = null,
    @SerializedName("zipcode") val zipcode: String? = null,
    @SerializedName("country_id") val countryId: Int? = null,
    @SerializedName("locality_verbose") val localityVerbose: String? = null
)
