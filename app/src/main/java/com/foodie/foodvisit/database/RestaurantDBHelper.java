package com.foodie.foodvisit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_ADDRESS;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_BACKDROP_URI;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_CITY;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_COST;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_DESCRIPTION;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_ID;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_LAT;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_LOCALITY;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_LON;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_NAME;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_ONLINE_AVAILABLE;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_PRICERANGE;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_RATING;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_TABLE_BOOKING;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_VOTES;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_ZIPCODE;
import static com.foodie.foodvisit.database.RestaurantContract.RestaurantEntry.TABLE_NAME;

/**
 * Created by kprabhu on 11/12/17.
 */

public class RestaurantDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "restaurants.db";
    public static final int DATABASE_VERSION = 1;

    public RestaurantDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_RESTAURANT_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RESTAURANT_ID + " TEXT UNIQUE NOT NULL," +
                RESTAURANT_NAME + " TEXT NOT NULL," +
                RESTAURANT_BACKDROP_URI + " TEXT NOT NULL," +
                RESTAURANT_ADDRESS + " TEXT NOT NULL," +
                RESTAURANT_ZIPCODE + " TEXT NOT NULL," +
                RESTAURANT_LOCALITY + " TEXT NOT NULL," +
                RESTAURANT_CITY + " TEXT NOT NULL," +
                RESTAURANT_LAT + " TEXT NOT NULL," +
                RESTAURANT_LON + " TEXT NOT NULL," +
                RESTAURANT_COST + " TEXT NOT NULL," +
                RESTAURANT_PRICERANGE + " TEXT NOT NULL," +
                RESTAURANT_ONLINE_AVAILABLE + " TEXT NOT NULL," +
                RESTAURANT_TABLE_BOOKING + " TEXT NOT NULL," +
                RESTAURANT_RATING + " TEXT NOT NULL," +
                RESTAURANT_DESCRIPTION + " TEXT NOT NULL," +
                RESTAURANT_VOTES + " TEXT NOT NULL," +

                "UNIQUE (" + RESTAURANT_ID + ") ON CONFLICT IGNORE" +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_RESTAURANT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
