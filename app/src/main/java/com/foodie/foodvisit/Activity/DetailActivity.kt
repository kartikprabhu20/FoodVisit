package com.foodie.foodvisit.Activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import coil.load
import com.foodie.foodvisit.BuildConfig
import com.foodie.foodvisit.R
import com.foodie.foodvisit.Utils
import com.foodie.foodvisit.model.Restaurant
import com.foodie.foodvisit.model.RestaurantInfo
import com.foodie.foodvisit.widget.FoodVisitWidget
import com.foodie.foodvisit.widget.FoodVisitWidgetManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var toolbar: Toolbar
    private lateinit var backDropImage: ImageView
    private lateinit var tvCostForTwo: TextView
    private lateinit var tvPriceRange: TextView
    private lateinit var tvOnlineAvailable: TextView
    private lateinit var tvHasTableBooking: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvLocality: TextView
    private lateinit var tvCity: TextView
    private lateinit var tvZipcode: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvDescribte: TextView
    private lateinit var tvVotes: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var adview: AdView

    private lateinit var restaurant: Restaurant
    private lateinit var restaurantInfo: RestaurantInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        toolbar = findViewById(R.id.toolbar_details_activity)
        backDropImage = findViewById(R.id.backdrop)
        tvCostForTwo = findViewById(R.id.cost_for_two)
        tvPriceRange = findViewById(R.id.price_range)
        tvOnlineAvailable = findViewById(R.id.online_available)
        tvHasTableBooking = findViewById(R.id.has_table_booking)
        tvAddress = findViewById(R.id.address)
        tvLocality = findViewById(R.id.locality)
        tvCity = findViewById(R.id.city)
        tvZipcode = findViewById(R.id.zipcode)
        tvRating = findViewById(R.id.rating)
        tvDescribte = findViewById(R.id.describte)
        tvVotes = findViewById(R.id.votes)
        fab = findViewById(R.id.fab_fav)
        adview = findViewById(R.id.adView)

        restaurant = getRestaurant()
        restaurantInfo = restaurant.restaurantInfo ?: run {
            finish()
            return
        }

        fab.setOnClickListener(this)
        initialiseFab()
        initialiseViews()
        initialiseAdView()
    }

    private fun initialiseFab() {
        if (Utils.isToVisit(this, restaurantInfo)) {
            fab.setImageResource(R.drawable.favorite_added)
        } else {
            fab.setImageResource(R.drawable.favorite_removed)
        }
    }

    private fun initialiseAdView() {
        if ("free".equals(BuildConfig.FLAVOR, ignoreCase = true)) {
            adview.loadAd(AdRequest.Builder().build())
        } else {
            adview.visibility = View.GONE
        }
    }

    private fun initialiseViews() {
        val name = restaurantInfo.name ?: ""
        val backDropImagePath = restaurantInfo.featuredImage?.takeIf { it.isNotEmpty() }

        val location = restaurantInfo.location
        val userRatings = restaurantInfo.userRating

        val costForTwo = restaurantInfo.averageCostForTwo?.toString() ?: ""
        val priceRange = restaurantInfo.priceRange?.toString() ?: ""
        val onlineAvailable = if ("1".equals(restaurantInfo.hasOnlineDeliveryString, ignoreCase = true))
            getString(R.string.available_yes) else getString(R.string.available_no)
        val hasTableBooking = if ("1".equals(restaurantInfo.hasTableBookingString, ignoreCase = true))
            getString(R.string.available_yes) else getString(R.string.available_no)

        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        collapsingToolbar.title = name

        if (backDropImagePath != null) {
            backDropImage.load(backDropImagePath) {
                placeholder(R.drawable.food)
                error(R.mipmap.ic_launcher)
            }
        } else {
            backDropImage.setImageResource(R.drawable.food)
        }

        tvCostForTwo.text = costForTwo
        tvHasTableBooking.text = hasTableBooking
        tvOnlineAvailable.text = onlineAvailable
        tvPriceRange.text = priceRange

        tvAddress.text = location?.address ?: ""
        tvLocality.text = location?.locality ?: ""
        tvCity.text = location?.city ?: ""
        tvZipcode.text = location?.zipcode ?: ""

        tvRating.text = userRatings?.aggregateRating ?: ""
        tvDescribte.text = userRatings?.ratingText ?: ""
        tvVotes.text = userRatings?.votes ?: ""
    }

    override fun onClick(view: View) {
        setToVisit()
    }

    private fun setToVisit() {
        if (Utils.isToVisit(this, restaurantInfo)) {
            Utils.removeFromToVisit(this, restaurantInfo.id)
            fab.setImageResource(R.drawable.favorite_removed)
        } else {
            Utils.addToVisit(this, restaurantInfo)
            fab.setImageResource(R.drawable.favorite_added)
        }
        FoodVisitWidgetManager(applicationContext)
            .updateRestaurants(Utils.getRestaurantsFromDB(applicationContext))
    }

    private fun getRestaurant(): Restaurant {
        val bundle = intent.extras ?: throw IllegalStateException("No extras in intent")

        if (bundle.containsKey(FoodVisitWidget.RESTAURANT_LIST)) {
            val list = bundle.getString(FoodVisitWidget.RESTAURANT_LIST)
            val pos = bundle.getInt(FoodVisitWidget.POSITION)
            return FoodVisitWidgetManager(applicationContext).getInfo(list, pos)
        }

        return bundle.getParcelable("restaurant")
            ?: throw IllegalStateException("No restaurant in extras")
    }
}
