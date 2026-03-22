package com.foodie.foodvisit.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserRating(
    @SerializedName("aggregate_rating") @Expose val aggregateRating: String? = null,
    @SerializedName("rating_text") @Expose val ratingText: String? = null,
    @SerializedName("rating_color") @Expose val ratingColor: String? = null,
    @SerializedName("votes") @Expose val votes: String? = null
) : Parcelable
