package com.foodie.foodvisit.service

import com.foodie.foodvisit.model.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RestaurantService {
    @GET("/api/v2.1//search")
    fun restaurantResult(
        @Header("user-key") key: String?,
        @Query("entity_id") id: Int,
        @Query("entity_type") type: String
    ): Call<Result>
}
