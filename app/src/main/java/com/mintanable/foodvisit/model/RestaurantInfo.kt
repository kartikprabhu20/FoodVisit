package com.mintanable.foodvisit.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantInfo(
    @SerializedName("apikey") @Expose val apikey: String? = null,
    @SerializedName("id") @Expose val id: String? = null,
    @SerializedName("name") @Expose val name: String? = null,
    @SerializedName("url") @Expose val url: String? = null,
    @SerializedName("location") @Expose val location: Location? = null,
    @SerializedName("switch_to_order_menu") @Expose val switchToOrderMenu: Int? = null,
    @SerializedName("cuisines") @Expose val cuisines: String? = null,
    @SerializedName("average_cost_for_two") @Expose val averageCostForTwo: Int? = null,
    @SerializedName("price_range") @Expose val priceRange: Int? = null,
    @SerializedName("currency") @Expose val currency: String? = null,
    @SerializedName("thumb") @Expose val thumb: String? = null,
    @SerializedName("photos_url") @Expose val photosUrl: String? = null,
    @SerializedName("menu_url") @Expose val menuUrl: String? = null,
    @SerializedName("featured_image") @Expose val featuredImage: String? = null,
    @SerializedName("user_rating") @Expose val userRating: UserRating? = null,
    @SerializedName("has_online_delivery") @Expose val hasOnlineDelivery: Int? = null,
    @SerializedName("is_delivering_now") @Expose val isDeliveringNow: Int? = null,
    @SerializedName("deeplink") @Expose val deeplink: String? = null,
    @SerializedName("has_table_booking") @Expose val hasTableBooking: Int? = null,
    @SerializedName("events_url") @Expose val eventsUrl: String? = null
) : Parcelable {
    // String representations used by legacy DB/display code
    val hasOnlineDeliveryString: String get() = hasOnlineDelivery?.toString() ?: "null"
    val hasTableBookingString: String get() = hasTableBooking?.toString() ?: "null"
}
