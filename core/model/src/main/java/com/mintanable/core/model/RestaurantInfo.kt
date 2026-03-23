package com.mintanable.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RestaurantInfo(
    @SerializedName("apikey")  val apikey: String? = null,
    @SerializedName("id")  val id: String? = null,
    @SerializedName("name")  val name: String? = null,
    @SerializedName("url")  val url: String? = null,
    @SerializedName("location")  val location: Location? = null,
    @SerializedName("switch_to_order_menu")  val switchToOrderMenu: Int? = null,
    @SerializedName("cuisines")  val cuisines: String? = null,
    @SerializedName("average_cost_for_two")  val averageCostForTwo: Int? = null,
    @SerializedName("price_range")  val priceRange: Int? = null,
    @SerializedName("currency")  val currency: String? = null,
    @SerializedName("thumb")  val thumb: String? = null,
    @SerializedName("photos_url")  val photosUrl: String? = null,
    @SerializedName("menu_url")  val menuUrl: String? = null,
    @SerializedName("featured_image")  val featuredImage: String? = null,
    @SerializedName("user_rating")  val userRating: UserRating? = null,
    @SerializedName("has_online_delivery")  val hasOnlineDelivery: Int? = null,
    @SerializedName("is_delivering_now")  val isDeliveringNow: Int? = null,
    @SerializedName("deeplink")  val deeplink: String? = null,
    @SerializedName("has_table_booking")  val hasTableBooking: Int? = null,
    @SerializedName("events_url")  val eventsUrl: String? = null
) {
    val hasOnlineDeliveryString: String get() = hasOnlineDelivery?.toString() ?: "null"
    val hasTableBookingString: String get() = hasTableBooking?.toString() ?: "null"
}
