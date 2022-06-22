package com.netology.marker.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.netology.marker.R
import com.netology.marker.ui.AboutPlaceFragment.Companion.description
import com.netology.marker.ui.AboutPlaceFragment.Companion.name
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
//
//        navController.navigate(
//            R.id.action_placesListFragment_to_navigation
//        )
//
//        navController.navigate(
//            R.id.action_mapFragment_to_placesListFragment
//        )
//
//        intent?.let {
//            val text = it.getStringExtra(Intent.EXTRA_TEXT)
//
//            navController.navigate(
//                R.id.action_mapFragment_to_aboutPlaceFragment,
//                Bundle().apply {
//                    name = text
//                    description = text
//                }
//            )
//        }




    }

}