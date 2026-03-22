package com.mintanable.foodvisit.database

import android.net.Uri
import android.provider.BaseColumns

object RestaurantContract {

    const val AUTHORITY = "com.foodie.foodvisit"
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")
    const val PATH_RESTAURANTS = "restaurants"

    object RestaurantEntry : BaseColumns {
        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESTAURANTS).build()

        const val TABLE_NAME = "restaurants"
        const val RESTAURANT_ID = "id"
        const val RESTAURANT_BACKDROP_URI = "backdrop_path"
        const val RESTAURANT_NAME = "name"
        const val RESTAURANT_ADDRESS = "address"
        const val RESTAURANT_LOCALITY = "locality"
        const val RESTAURANT_CITY = "city"
        const val RESTAURANT_ZIPCODE = "zipcode"
        const val RESTAURANT_LAT = "latitude"
        const val RESTAURANT_LON = "longitude"
        const val RESTAURANT_COST = "averageCostForTwo"
        const val RESTAURANT_PRICERANGE = "priceRange"
        const val RESTAURANT_ONLINE_AVAILABLE = "onlineAvailable"
        const val RESTAURANT_TABLE_BOOKING = "tableBooking"
        const val RESTAURANT_RATING = "rating"
        const val RESTAURANT_DESCRIPTION = "description"
        const val RESTAURANT_VOTES = "votes"
    }
}
