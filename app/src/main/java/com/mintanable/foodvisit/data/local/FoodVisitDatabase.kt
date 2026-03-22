package com.mintanable.foodvisit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mintanable.foodvisit.data.local.dao.RestaurantDao
import com.mintanable.foodvisit.data.local.entity.RestaurantEntity

@Database(
    entities = [RestaurantEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FoodVisitDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
}
