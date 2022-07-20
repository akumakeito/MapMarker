package com.netology.marker.db

import android.content.Context
import androidx.room.Room
import com.netology.marker.dao.PlaceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DbModule {

    @Provides
    @Singleton
    fun provideAppDb(@ApplicationContext context: Context): AppDB = Room.databaseBuilder(
        context,
        AppDB::class.java,
        "App.db"
    ).build()

    @Provides
    fun providePlaceDao(appDB: AppDB): PlaceDao = appDB.placeDao()
}