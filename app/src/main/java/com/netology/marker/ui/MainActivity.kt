package com.netology.marker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.netology.marker.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        val KEY_CANCEL = "cancel"
        val KEY_MARKER_LATLNG = "latlng"
        val KEY_MARKER_TITLE = "titleMarker"
        val KEY_MARKER_DESCRIPTION = "titleDescription"
        val KEY_PLACE = "place"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

}