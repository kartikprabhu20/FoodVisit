package com.mintanable.foodvisit.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_ADDRESS
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_BACKDROP_URI
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_CITY
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_COST
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_DESCRIPTION
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_ID
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_LAT
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_LOCALITY
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_LON
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_NAME
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_ONLINE_AVAILABLE
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_PRICERANGE
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_RATING
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_TABLE_BOOKING
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_VOTES
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.RESTAURANT_ZIPCODE
import com.mintanable.foodvisit.database.RestaurantContract.RestaurantEntry.TABLE_NAME

class RestaurantDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "restaurants.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val sql = "CREATE TABLE $TABLE_NAME (" +
                "$_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$RESTAURANT_ID TEXT UNIQUE NOT NULL," +
                "$RESTAURANT_NAME TEXT NOT NULL," +
                "$RESTAURANT_BACKDROP_URI TEXT NOT NULL," +
                "$RESTAURANT_ADDRESS TEXT NOT NULL," +
                "$RESTAURANT_ZIPCODE TEXT NOT NULL," +
                "$RESTAURANT_LOCALITY TEXT NOT NULL," +
                "$RESTAURANT_CITY TEXT NOT NULL," +
                "$RESTAURANT_LAT TEXT NOT NULL," +
                "$RESTAURANT_LON TEXT NOT NULL," +
                "$RESTAURANT_COST TEXT NOT NULL," +
                "$RESTAURANT_PRICERANGE TEXT NOT NULL," +
                "$RESTAURANT_ONLINE_AVAILABLE TEXT NOT NULL," +
                "$RESTAURANT_TABLE_BOOKING TEXT NOT NULL," +
                "$RESTAURANT_RATING TEXT NOT NULL," +
                "$RESTAURANT_DESCRIPTION TEXT NOT NULL," +
                "$RESTAURANT_VOTES TEXT NOT NULL," +
                "UNIQUE ($RESTAURANT_ID) ON CONFLICT IGNORE" +
                " );"
        sqLiteDatabase.execSQL(sql)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(sqLiteDatabase)
    }
}
