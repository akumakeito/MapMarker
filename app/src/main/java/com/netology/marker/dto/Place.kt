package com.netology.marker.dto

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    val id : Long = 0L,
    val coordinates : LatLng,
    val name : String = "",
    val description : String = ""
) : Parcelable
