package com.mintanable.foodvisit.di

import com.mintanable.foodvisit.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppApiKeysModule {

    @Provides
    @Singleton
    @Named("placesApiKey")
    fun providePlacesApiKey(): String = BuildConfig.GOOGLE_MAPS_KEY
}
