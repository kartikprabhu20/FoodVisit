package com.foodie.foodvisit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.foodie.foodvisit.R
import com.foodie.foodvisit.model.Restaurant

class RestaurantAdapter(
    private var restaurants: List<Restaurant> = emptyList(),
    private val clickListener: ClickListener
) : RecyclerView.Adapter<RestaurantAdapter.RestaurantsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_card_view, parent, false)
        return RestaurantsViewHolder(view)
    }

    override fun getItemCount(): Int = restaurants.size

    override fun onBindViewHolder(holder: RestaurantsViewHolder, position: Int) {
        val restaurantInfo = restaurants[position].restaurantInfo ?: return
        holder.restaurantTitle.text = restaurantInfo.name

        val featuredImage = restaurantInfo.featuredImage
        if (featuredImage.isNullOrEmpty()) {
            holder.restaurantImage.setImageResource(R.drawable.food)
        } else {
            holder.restaurantImage.load(featuredImage) {
                placeholder(R.drawable.food)
                error(R.drawable.food)
            }
        }

        val rating = restaurantInfo.userRating?.aggregateRating
        if (!rating.isNullOrEmpty()) {
            holder.restaurantRatings.text = rating
        }
    }

    fun updateRestaurants(newRestaurants: List<Restaurant>) {
        restaurants = newRestaurants
        notifyDataSetChanged()
    }

    inner class RestaurantsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val restaurantImage: ImageView = itemView.findViewById(R.id.restaurant_image)
        val restaurantTitle: TextView = itemView.findViewById(R.id.restaurant_title)
        val restaurantRatings: TextView = itemView.findViewById(R.id.restaurant_ratings)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            clickListener.accept(layoutPosition)
        }
    }

    interface ClickListener {
        fun accept(position: Int)
    }
}
