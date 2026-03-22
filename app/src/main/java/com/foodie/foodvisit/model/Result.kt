package com.foodie.foodvisit.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    @SerializedName("results_found") @Expose val resultsFound: Int? = null,
    @SerializedName("results_start") @Expose val resultsStart: Int? = null,
    @SerializedName("results_shown") @Expose val resultsShown: Int? = null,
    @SerializedName("restaurants") @Expose val restaurants: List<Restaurant>? = null
) : Parcelable
