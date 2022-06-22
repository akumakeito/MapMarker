package com.netology.marker.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.netology.marker.dto.Place

@Entity
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val coordinatesLat: Double,
    val coordinatesLng: Double,
    val name: String,
    val description: String
) {
    fun toDto() = Place(
        id,
        LatLng(coordinatesLat, coordinatesLng),
        name,
        description
    )
    companion object {
        fun fromDto(dto : Place) = PlaceEntity(
            dto.id,
            dto.coordinates.latitude,
            dto.coordinates.longitude,
            dto.name,
            dto.description
        )
    }
}

fun List<PlaceEntity>.toDto() : List<Place> = map(PlaceEntity::toDto)
fun List<Place>.toEntity() : List<PlaceEntity> = map(PlaceEntity::fromDto)


