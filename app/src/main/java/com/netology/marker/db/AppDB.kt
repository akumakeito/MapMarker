package com.netology.marker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.netology.marker.dao.PlaceDao
import com.netology.marker.entity.PlaceEntity

@Database(entities = [PlaceEntity::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
}