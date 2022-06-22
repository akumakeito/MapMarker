package com.netology.marker.repository

import com.netology.marker.dao.PlaceDao
import com.netology.marker.dto.Place
import com.netology.marker.entity.PlaceEntity
import com.netology.marker.entity.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeDao: PlaceDao
) : PlaceRepository {

    override val data: Flow<List<Place>> = placeDao.getAll()
        .map(List<PlaceEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        placeDao.getAll()
    }

    override suspend fun save(place: Place) {
        if (place.id == 0L) {
            placeDao.insert(PlaceEntity.fromDto(place))
        } else {
            placeDao.update(PlaceEntity.fromDto(place))
        }
    }

    override suspend fun removeById(id: Long) {
        placeDao.removeById(id)
    }
}