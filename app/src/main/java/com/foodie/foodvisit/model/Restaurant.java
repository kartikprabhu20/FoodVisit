package com.foodie.foodvisit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by kprabhu on 11/12/17.
 */

public class Restaurant implements Parcelable {

    @SerializedName("restaurant")
    @Expose
    private RestaurantInfo restaurant;

    public Restaurant(Parcel in) {
        restaurant = in.readParcelable(RestaurantInfo.class.getClassLoader());
    }

    public Restaurant() {

    }

    public Restaurant(RestaurantInfo restaurantInfo) {
        this.restaurant = restaurantInfo;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(restaurant, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public RestaurantInfo getRestaurantInfo() {
        return restaurant;
    }

    public void setRestaurant(RestaurantInfo restaurant) {
        this.restaurant = restaurant;
    }

}

