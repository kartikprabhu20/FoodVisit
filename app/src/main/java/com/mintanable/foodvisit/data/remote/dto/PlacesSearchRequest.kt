package com.mintanable.foodvisit.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PlacesSearchRequest(
    @SerializedName("textQuery") val textQuery: String,
    @SerializedName("includedType") val includedType: String = "restaurant",
    @SerializedName("maxResultCount") val maxResultCount: Int = 20,
    @SerializedName("languageCode") val languageCode: String = "en"
)
