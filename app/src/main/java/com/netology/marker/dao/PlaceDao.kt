package com.netology.marker.dao

import androidx.room.*
import com.netology.marker.dto.Place
import com.netology.marker.entity.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Query("SELECT * FROM PlaceEntity ORDER BY id DESC")
    fun getAll() : Flow<List<PlaceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: PlaceEntity)

    @Update
    suspend fun update(place: PlaceEntity)

    @Query("DELETE FROM PlaceEntity WHERE id = :id")
    suspend fun removeById(id : Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(placeList: List<PlaceEntity>)

}