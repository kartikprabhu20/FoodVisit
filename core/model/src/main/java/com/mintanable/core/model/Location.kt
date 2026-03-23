package com.mintanable.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("address") @Expose val address: String? = null,
    @SerializedName("locality") @Expose val locality: String? = null,
    @SerializedName("city") @Expose val city: String? = null,
    @SerializedName("city_id") @Expose val cityId: Int? = null,
    @SerializedName("latitude") @Expose val latitude: String? = null,
    @SerializedName("longitude") @Expose val longitude: String? = null,
    @SerializedName("zipcode") @Expose val zipcode: String? = null,
    @SerializedName("country_id") @Expose val countryId: Int? = null,
    @SerializedName("locality_verbose") @Expose val localityVerbose: String? = null
)
