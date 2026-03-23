package com.mintanable.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mintanable.core.data.local.dao.RestaurantDao
import com.mintanable.core.data.local.entity.RestaurantEntity

@Database(
    entities = [RestaurantEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FoodVisitDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
}
