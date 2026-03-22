package com.mintanable.foodvisit.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Restaurant(
    @SerializedName("restaurant") @Expose val restaurantInfo: RestaurantInfo? = null
) : Parcelable
