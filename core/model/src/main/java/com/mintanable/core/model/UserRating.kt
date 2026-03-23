package com.mintanable.core.model

import com.google.gson.annotations.SerializedName

data class UserRating(
    @SerializedName("aggregate_rating") val aggregateRating: String? = null,
    @SerializedName("rating_text") val ratingText: String? = null,
    @SerializedName("rating_color") val ratingColor: String? = null,
    @SerializedName("votes") val votes: String? = null
)
