package com.mintanable.foodvisit

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.mintanable.foodvisit.database.RestaurantContract
import com.mintanable.foodvisit.model.Location
import com.mintanable.foodvisit.model.Restaurant
import com.mintanable.foodvisit.model.RestaurantInfo
import com.mintanable.foodvisit.model.UserRating

object Utils {

    private val ALL_COLUMNS = arrayOf(
        RestaurantContract.RestaurantEntry._ID,                         // 0
        RestaurantContract.RestaurantEntry.RESTAURANT_ID,               // 1
        RestaurantContract.RestaurantEntry.RESTAURANT_NAME,             // 2
        RestaurantContract.RestaurantEntry.RESTAURANT_BACKDROP_URI,     // 3
        RestaurantContract.RestaurantEntry.RESTAURANT_ADDRESS,          // 4
        RestaurantContract.RestaurantEntry.RESTAURANT_LOCALITY,         // 5
        RestaurantContract.RestaurantEntry.RESTAURANT_ZIPCODE,          // 6
        RestaurantContract.RestaurantEntry.RESTAURANT_CITY,             // 7
        RestaurantContract.RestaurantEntry.RESTAURANT_LON,              // 8
        RestaurantContract.RestaurantEntry.RESTAURANT_LAT,              // 9
        RestaurantContract.RestaurantEntry.RESTAURANT_COST,             // 10
        RestaurantContract.RestaurantEntry.RESTAURANT_PRICERANGE,       // 11
        RestaurantContract.RestaurantEntry.RESTAURANT_ONLINE_AVAILABLE, // 12
        RestaurantContract.RestaurantEntry.RESTAURANT_TABLE_BOOKING,    // 13
        RestaurantContract.RestaurantEntry.RESTAURANT_RATING,           // 14
        RestaurantContract.RestaurantEntry.RESTAURANT_DESCRIPTION,      // 15
        RestaurantContract.RestaurantEntry.RESTAURANT_VOTES             // 16
    )

    fun getRestaurantApiKey(): String = BuildConfig.ZOMATO_API_KEY

    fun isOnline(context: Context): Boolean { // context kept for ConnectivityManager
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun isToVisit(context: Context, restaurantInfo: RestaurantInfo): Boolean {
        val uri = RestaurantContract.RestaurantEntry.CONTENT_URI
        val cr: ContentResolver = context.contentResolver
        val cursor: Cursor? = cr.query(
            uri,
            arrayOf(RestaurantContract.RestaurantEntry.RESTAURANT_ID),
            "${RestaurantContract.RestaurantEntry.RESTAURANT_ID}=?",
            arrayOf(restaurantInfo.id),
            null
        )
        return cursor?.use { it.moveToFirst() } ?: false
    }

    fun removeFromToVisit(context: Context, id: String?) {
        val uri = RestaurantContract.RestaurantEntry.CONTENT_URI
        context.contentResolver.delete(
            uri,
            "${RestaurantContract.RestaurantEntry.RESTAURANT_ID} = ?",
            arrayOf(id.orEmpty())
        )
    }

    fun addToVisit(context: Context, restaurantInfo: RestaurantInfo) {
        val uri = RestaurantContract.RestaurantEntry.CONTENT_URI
        val values = ContentValues().apply {
            put(RestaurantContract.RestaurantEntry.RESTAURANT_ID, restaurantInfo.id)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_BACKDROP_URI, restaurantInfo.featuredImage)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_NAME, restaurantInfo.name)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_ADDRESS, restaurantInfo.location?.address)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_CITY, restaurantInfo.location?.city)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_LOCALITY, restaurantInfo.location?.locality)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_ZIPCODE, restaurantInfo.location?.zipcode)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_LAT, restaurantInfo.location?.latitude)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_LON, restaurantInfo.location?.longitude)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_COST, restaurantInfo.averageCostForTwo)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_PRICERANGE, restaurantInfo.priceRange)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_ONLINE_AVAILABLE, restaurantInfo.hasOnlineDeliveryString)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_TABLE_BOOKING, restaurantInfo.hasTableBookingString)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_RATING, restaurantInfo.userRating?.aggregateRating)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_DESCRIPTION, restaurantInfo.userRating?.ratingText)
            put(RestaurantContract.RestaurantEntry.RESTAURANT_VOTES, restaurantInfo.userRating?.votes)
        }
        context.contentResolver.insert(uri, values)
    }

    fun getRestaurantInfoListFromDB(context: Context): List<RestaurantInfo> {
        val uri = RestaurantContract.RestaurantEntry.CONTENT_URI
        val cursor: Cursor? = context.contentResolver.query(uri, ALL_COLUMNS, null, null, null)
            ?: return emptyList()
        return cursor!!.use { c ->
            buildList {
                while (c.moveToNext()) add(buildRestaurant(c))
            }
        }
    }

    private fun buildRestaurant(cursor: Cursor): RestaurantInfo {
        return RestaurantInfo(
            id = cursor.getString(1),
            name = cursor.getString(2),
            featuredImage = cursor.getString(3),
            location = Location(
                address = cursor.getString(4),
                locality = cursor.getString(6),
                city = cursor.getString(7),
                zipcode = cursor.getString(5),
                latitude = cursor.getString(9),
                longitude = cursor.getString(8)
            ),
            userRating = UserRating(
                aggregateRating = cursor.getString(14),
                ratingText = cursor.getString(15),
                votes = cursor.getString(16)
            ),
            averageCostForTwo = cursor.getString(10)?.toIntOrNull(),
            priceRange = cursor.getString(11)?.toIntOrNull(),
            hasOnlineDelivery = cursor.getString(12)?.toIntOrNull(),
            hasTableBooking = cursor.getString(13)?.toIntOrNull()
        )
    }

    fun hasToVisitList(context: Context): Boolean {
        val cursor: Cursor? = context.contentResolver.query(
            RestaurantContract.RestaurantEntry.CONTENT_URI, null, null, null, null
        )
        return cursor?.use { it.moveToFirst() } ?: false
    }

    fun getRestaurantsFromDB(context: Context): List<Restaurant> {
        return getRestaurantInfoListFromDB(context).map { Restaurant(it) }
    }
}
