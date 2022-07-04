package com.netology.marker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.ktx.awaitMap
import com.netology.marker.R
import com.netology.marker.viewModel.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.map
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitAnimateCamera
import com.google.maps.android.ktx.model.cameraPosition
import com.google.maps.android.ktx.model.markerOptions
import com.google.maps.android.ktx.utils.collection.addMarker
import com.netology.marker.databinding.MapFragmentBinding
import com.netology.marker.dto.Place
import com.netology.marker.ui.NewPointFragment.Companion.KEY_CANCEL
import com.netology.marker.ui.PlacesListFragment.Companion.KEY_PLACE
import com.netology.marker.viewModel.MapViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment(), GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    companion object {
        val KEY_MARKER_LAT = "lat"
        val KEY_MARKER_LNG = "lng"
        val KEY_MARKER_LATLNG = "latlng"
        val KEY_MARKER_TITLE = "titleMarker"
        val KEY_MARKER_DESCRIPTION = "titleDescription"
    }

    private lateinit var googleMap: GoogleMap

    private lateinit var binding: MapFragmentBinding

    private lateinit var collection: MarkerManager.Collection

    private val viewModel: PlaceViewModel by viewModels()

    private val markers = viewModel.data

//    @Inject
//    lateinit var mapViewModelFactory: MapViewModel.MapViewModelFactory
//
//    private val viewModel: MapViewModel by viewModels {
//        MapViewModel.providesFactory(
//            assistedFactory = mapViewModelFactory
//        )
//    }
//
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                googleMap.apply {
                    isMyLocationEnabled = true
                    uiSettings.isMyLocationButtonEnabled = true
                }
            } else {
                // TODO: show smth
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MapFragmentBinding.inflate(inflater, container, false)

        binding.showAll.setOnClickListener {
            findNavController().navigate(
                R.id.action_mapFragment_to_placesListFragment
            )
        }

        markers.observe(viewLifecycleOwner) {
            collection.showAll()
        }


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        lifecycle.coroutineScope.launchWhenCreated {
            googleMap = mapFragment.awaitMap().apply {
                isBuildingsEnabled = true

                uiSettings.apply {
                    isZoomControlsEnabled = true
                    setAllGesturesEnabled(true)
                }
            }

            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    googleMap.apply {
                        isMyLocationEnabled = true
                        uiSettings.isMyLocationButtonEnabled = true
                    }

                    val fusedLocationProviderClient = LocationServices
                        .getFusedLocationProviderClient(requireActivity())

                    fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                        println(it)
                    }
                }

                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    //TODO: show dialog
                }

                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }

            val markerManager = MarkerManager(googleMap)
            collection = markerManager.newCollection()

            val target = LatLng(55.751999, 37.617734)
//            val markerManager = MarkerManager(googleMap)
//            collection = markerManager.newCollection()
//                val markers = viewModel.data
//                markers.map { places ->
//                    places.map { marker ->
//                        collection.addMarker {
//                            position(marker.coordinates)
//                            title(marker.name)
//                            snippet(marker.description)
//                        }
//                    }
//                }
//                addMarker {
//                    position(target)
//                    title("The Moscow Kremlin")
//                    snippet("sdhfajrgfiUWG")
//                }.apply {
//                    tag = "Any additional data"


//            println("collectionOfMarkers" + collection)
//
//
//            collection.setOnMarkerClickListener { marker ->
//                marker?.showInfoWindow()
//                true
//            }

            markers.map { places ->
                places.map { marker ->
                    collection.addMarker( MarkerOptions()
                        .position(marker.coordinates)
                        .title(marker.name)
                        .snippet(marker.description)
                    )
                }
            }

            googleMap.setOnMapClickListener {  }

            //googleMap.setOnInfoWindowClickListener(this@MapFragment)
            googleMap.awaitAnimateCamera(
                CameraUpdateFactory.newCameraPosition(
                    cameraPosition {
                        target(target)
                        zoom(15F)
                    }
                )
            )

            googleMap.setOnMapLongClickListener { latLang ->
                googleMap.addMarker {
                    position(latLang)
                }


                binding.createNewPoint.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        val bundle = Bundle()
                        bundle.putParcelable(KEY_MARKER_LATLNG, latLang)
                        //bundle.putParcelable(KEY_PLACE, Place(coordinates = latLang))
                        findNavController().navigate(
                            R.id.newPointFragment,
                            bundle
                        )
                    }
                }

                //Bundle().getString(KEY_CANCEL)
            }
        }
    }

    override fun onInfoWindowClick(marker: Marker) {
        Toast.makeText(
            requireContext(),
            "infoWindowClicked",
            Toast.LENGTH_LONG
        ).show()
//        val name = marker.title
//        val description = marker.snippet
//
//        val bundle = Bundle()
//        bundle.putString(KEY_MARKER_TITLE, name)
//        bundle.putString(KEY_MARKER_DESCRIPTION, description)
//
//        findNavController().navigate(
//            R.id.aboutPlaceFragment,
//            bundle
//        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.apply {
            setOnInfoWindowClickListener(this@MapFragment)

            println("collectionOfMarkers" + collection)


            collection.setOnMarkerClickListener { marker ->
                marker?.showInfoWindow()
                true
            }

            collection.showAll()
        }




    }


}