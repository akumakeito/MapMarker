package com.netology.marker.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PlaceRepositoryModule {

    @Binds
    @Singleton
    fun bindRepositoryImpl(placeRepository: PlaceRepositoryImpl) :PlaceRepository
}