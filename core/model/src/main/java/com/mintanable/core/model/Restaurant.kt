package com.mintanable.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Restaurant(
    @SerializedName("restaurant") @Expose val restaurantInfo: RestaurantInfo? = null
)
