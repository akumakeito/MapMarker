package com.netology.marker.repository

import com.netology.marker.dto.Place
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    val data : Flow<List<Place>>
    suspend fun getAll()
    suspend fun save(place: Place)
    suspend fun removeById(id : Long)
}