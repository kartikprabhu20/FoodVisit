package com.foodie.foodvisit.adapter;

import android.arch.lifecycle.LiveData;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodie.foodvisit.R;
import com.foodie.foodvisit.model.Restaurant;
import com.foodie.foodvisit.model.RestaurantInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by kprabhu on 11/12/17.
 */

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestrauntsViewHolder> {

    private ClickListener clickListener;
    private List<Restaurant> restaurants = new ArrayList<>();

    public RestaurantAdapter(LiveData<List<Restaurant>> restaurants, ClickListener onRestaurantClicked ) {
        if (restaurants != null) {
            this.restaurants = restaurants.getValue();
        }
        clickListener = onRestaurantClicked;
    }

    @Override
    public RestrauntsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_card_view,parent,false);

        return new RestrauntsViewHolder(view);
    }

    @Override
    public int getItemCount() {

        if (restaurants != null) {
            return restaurants.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(RestrauntsViewHolder holder, int position) {
        if (holder != null) {
            Restaurant restaurant = restaurants.get(position);
            RestaurantInfo restaurantInfo = restaurant.getRestaurantInfo();

            holder.restaurantTitle.setText(restaurantInfo.getName());
            String featuredImage = restaurantInfo.getFeaturedImage();
            if (featuredImage.isEmpty()) {
                holder.restaurantImage.setImageResource(R.drawable.food);
                return;
            }
            Picasso.with(holder.itemView.getContext())
                    .load(featuredImage)
                    .error(R.drawable.food)
                    .placeholder(R.drawable.food)
                    .into(holder.restaurantImage);

            if (!TextUtils.isEmpty(restaurantInfo.getUserRating().getAggregateRating()))
                holder.restaurantRatings.setText(restaurantInfo.getUserRating().getAggregateRating());

        }
    }

    public void updateRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    public class RestrauntsViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.restaurant_image)
        ImageView restaurantImage;

        @BindView(R.id.restaurant_title)
        TextView restaurantTitle;

        @BindView(R.id.restaurant_ratings)
        TextView restaurantRatings;

        public RestrauntsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            clickListener.accept(getLayoutPosition());
        }
    }

    public interface ClickListener{
        void accept(int position);
    }
}
