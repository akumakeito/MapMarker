package com.netology.marker.utils

import android.os.Bundle
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object StringArg : ReadWriteProperty<Bundle, String?> {
    override fun getValue(thisRef: Bundle, property: KProperty<*>): String? =
        thisRef.getString(property.name)


    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) =
        thisRef.putString(property.name, value)

}

object ParcelableArg :ReadWriteProperty<Bundle, Parcelable?> {
    override fun getValue(thisRef: Bundle, property: KProperty<*>): Parcelable? =
        thisRef.getParcelable(property.name)

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Parcelable?) {
        thisRef.putParcelable(property.name, value)
    }
}

object LongArg : ReadWriteProperty<Bundle, Long> {
    override fun getValue(thisRef: Bundle, property: KProperty<*>): Long =
        thisRef.getLong(property.name)


    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Long) =
        thisRef.putLong(property.name, value)

}
