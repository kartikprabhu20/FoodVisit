package com.mintanable.core.model

import com.google.gson.annotations.SerializedName

data class Restaurant(
    @SerializedName("restaurant") val restaurantInfo: RestaurantInfo? = null
)
