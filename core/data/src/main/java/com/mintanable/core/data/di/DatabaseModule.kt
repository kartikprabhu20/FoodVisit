package com.mintanable.core.data.di

import android.content.Context
import androidx.room.Room
import com.mintanable.core.data.local.FoodVisitDatabase
import com.mintanable.core.data.local.dao.RestaurantDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFoodVisitDatabase(@ApplicationContext context: Context): FoodVisitDatabase =
        Room.databaseBuilder(context, FoodVisitDatabase::class.java, "foodvisit.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideRestaurantDao(db: FoodVisitDatabase): RestaurantDao = db.restaurantDao()
}
