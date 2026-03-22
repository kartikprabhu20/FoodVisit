package com.mintanable.foodvisit.di

import android.content.Context
import com.mintanable.foodvisit.database.RestaurantDBHelper
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
    fun provideRestaurantDBHelper(@ApplicationContext context: Context): RestaurantDBHelper =
        RestaurantDBHelper(context)
}
