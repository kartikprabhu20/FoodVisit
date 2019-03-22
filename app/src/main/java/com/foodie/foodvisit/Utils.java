package com.foodie.foodvisit;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.foodie.foodvisit.Activity.DetailActivity;
import com.foodie.foodvisit.database.RestaurantContract;
import com.foodie.foodvisit.model.Location;
import com.foodie.foodvisit.model.Restaurant;
import com.foodie.foodvisit.model.RestaurantInfo;
import com.foodie.foodvisit.model.UserRating;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by kprabhu on 11/12/17.
 */

public class Utils {

    private static final String TAG = "Utils";
    private static final String ZOMATO_KEY = "zomato-key";

    private static final String[] ALL_COLUMNS = {
            RestaurantContract.RestaurantEntry._ID,                                   // 0
            RestaurantContract.RestaurantEntry.RESTAURANT_ID,                         // 1
            RestaurantContract.RestaurantEntry.RESTAURANT_NAME,                       // 2
            RestaurantContract.RestaurantEntry.RESTAURANT_BACKDROP_URI,               // 3
            RestaurantContract.RestaurantEntry.RESTAURANT_ADDRESS,                    // 4
            RestaurantContract.RestaurantEntry.RESTAURANT_LOCALITY,                   // 5
            RestaurantContract.RestaurantEntry.RESTAURANT_ZIPCODE,                    // 7
            RestaurantContract.RestaurantEntry.RESTAURANT_CITY,                       // 6
            RestaurantContract.RestaurantEntry.RESTAURANT_LON,                        // 9
            RestaurantContract.RestaurantEntry.RESTAURANT_LAT,                       // 8
            RestaurantContract.RestaurantEntry.RESTAURANT_COST,                      // 13
            RestaurantContract.RestaurantEntry.RESTAURANT_PRICERANGE,                 // 14
            RestaurantContract.RestaurantEntry.RESTAURANT_ONLINE_AVAILABLE,            // 15
            RestaurantContract.RestaurantEntry.RESTAURANT_TABLE_BOOKING,             // 16
            RestaurantContract.RestaurantEntry.RESTAURANT_RATING,                     // 10
            RestaurantContract.RestaurantEntry.RESTAURANT_DESCRIPTION,                 // 11
            RestaurantContract.RestaurantEntry.RESTAURANT_VOTES                        // 12

    };

    public static String getRestaurantApiKey() {
        try {
            Context appContext = FoodVisitApp.getAppContext();
            ApplicationInfo ai = appContext.getPackageManager().getApplicationInfo(appContext.getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString(ZOMATO_KEY);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        return null;
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) FoodVisitApp.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static boolean isToVist(Context context, RestaurantInfo restaurantInfo) {
        Uri uri = RestaurantContract.RestaurantEntry.CONTENT_URI;

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = null;
        try {
            String where = RestaurantContract.RestaurantEntry.RESTAURANT_ID + "=?";
            cursor = cr.query(uri, new String[]{RestaurantContract.RestaurantEntry.RESTAURANT_ID}, where, new String[]{restaurantInfo.getId()}, null);
            if (cursor != null && cursor.moveToFirst())
                return true;
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    public static void removeFromToVist(Context context, String id) {
        Uri uri = RestaurantContract.RestaurantEntry.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();

        resolver.delete(uri, RestaurantContract.RestaurantEntry.RESTAURANT_ID + " = ? ",
                new String[]{id + ""});
    }

    public static void addToVist(Context context, RestaurantInfo restaurantInfo) {
        Uri uri = RestaurantContract.RestaurantEntry.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_ID, restaurantInfo.getId());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_BACKDROP_URI, restaurantInfo.getFeaturedImage());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_NAME, restaurantInfo.getName());

        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_ADDRESS, restaurantInfo.getLocation().getAddress());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_CITY, restaurantInfo.getLocation().getCity());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_LOCALITY, restaurantInfo.getLocation().getLocality());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_ZIPCODE, restaurantInfo.getLocation().getZipcode());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_LAT, restaurantInfo.getLocation().getLatitude());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_LON, restaurantInfo.getLocation().getLongitude());

        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_COST, restaurantInfo.getAverageCostForTwo());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_PRICERANGE, restaurantInfo.getPriceRange());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_ONLINE_AVAILABLE, restaurantInfo.getHasOnlineDelivery());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_TABLE_BOOKING, restaurantInfo.getHasTableBooking());

        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_RATING, restaurantInfo.getUserRating().getAggregateRating());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_DESCRIPTION, restaurantInfo.getUserRating().getRatingText());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_VOTES, restaurantInfo.getUserRating().getVotes());

        resolver.insert(uri, values);
    }

    public static List<RestaurantInfo> getRestrauntInfoListFromDB(Context context) {
        Uri uri = RestaurantContract.RestaurantEntry.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        List<RestaurantInfo> restaurantInfoList = new ArrayList<>();

        cursor = resolver.query(uri,ALL_COLUMNS,null, null, null);

        if (cursor == null)
            return null;

        while (cursor.moveToNext())
            restaurantInfoList.add(buildRestaurant(cursor));

        return restaurantInfoList;
    }

    private static RestaurantInfo buildRestaurant(Cursor cursor) {
        return new RestaurantInfo(cursor.getString(1),cursor.getString(2),cursor.getString(3),
                new Location(cursor.getString(4),cursor.getString(6),cursor.getString(7),cursor.getString(5),cursor.getString(8),cursor.getString(9)),
                new UserRating(cursor.getString(14),cursor.getString(15),cursor.getString(16)),
                cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13));
    }

    public static boolean hasToVisitList(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = cr.query(RestaurantContract.RestaurantEntry.CONTENT_URI, null, null, null, null);
            if (cursor != null && cursor.moveToFirst())
                return true;
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }

    public static List<Restaurant> getRestrauntFromDB(Context appContext) {
        List<Restaurant> restaurantList = new ArrayList<>();
        List<RestaurantInfo> restaurantInfos = getRestrauntInfoListFromDB(appContext);

        if(restaurantInfos.size()>0) {
            for (RestaurantInfo restaurant : restaurantInfos) {
                Restaurant rest = new Restaurant();
                rest.setRestaurant(restaurant);
                restaurantList.add(rest);
            }
        }
        return restaurantList;
    }
}
