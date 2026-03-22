package com.mintanable.foodvisit.di

import com.mintanable.foodvisit.data.repository.PlacesRepository
import com.mintanable.foodvisit.data.repository.PlacesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPlacesRepository(impl: PlacesRepositoryImpl): PlacesRepository
}
