package com.foodie.foodvisit

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.foodie.foodvisit.model.Restaurant
import com.foodie.foodvisit.model.Result
import com.foodie.foodvisit.service.RestaurantService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    application: Application,
    private val restaurantService: RestaurantService,
    private val preferenceManager: AppPreferenceManager
) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "RestaurantViewModel"
    }

    val restaurants: MutableLiveData<List<Restaurant>> = MutableLiveData()

    fun getRestaurants(): LiveData<List<Restaurant>> {
        if (restaurants.value == null) {
            loadRestaurants()
        }
        return restaurants
    }

    private fun loadRestaurants() {
        val apiKey = Utils.getRestaurantApiKey(getApplication())
        val locationId = preferenceManager.getLocation().toIntOrNull() ?: 4
        restaurantService.restaurantResult(apiKey, locationId, "city")
            .enqueue(object : Callback<Result> {
                override fun onResponse(call: Call<Result>, response: Response<Result>) {
                    val body = response.body()
                    if (body == null) {
                        Log.e(TAG, "onResponse: null body")
                        return
                    }
                    restaurants.postValue(body.restaurants)
                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                    if (!call.isCanceled) {
                        Toast.makeText(
                            getApplication(),
                            R.string.restaurants_retrieve_error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }
}
