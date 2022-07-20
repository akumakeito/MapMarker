package com.netology.marker.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitAnimateCamera
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.model.cameraPosition
import com.netology.marker.R
import com.netology.marker.databinding.MapFragmentBinding
import com.netology.marker.dto.Place
import com.netology.marker.ui.MainActivity.Companion.KEY_CANCEL
import com.netology.marker.ui.MainActivity.Companion.KEY_MARKER_DESCRIPTION
import com.netology.marker.ui.MainActivity.Companion.KEY_MARKER_LATLNG
import com.netology.marker.ui.MainActivity.Companion.KEY_MARKER_TITLE
import com.netology.marker.viewModel.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : Fragment(), GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    private lateinit var binding: MapFragmentBinding

    private lateinit var collection: MarkerManager.Collection

    private val viewModel: PlaceViewModel by viewModels()

    private var markers = emptyList<Place>()
    private var currentMarker: Marker? = null

    private var coords: LatLng? = null

    private var isCanceled = false

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
    ): View {
        binding = MapFragmentBinding.inflate(inflater, container, false)

        binding.showAll.setOnClickListener {
            findNavController().navigate(
                R.id.action_mapFragment_to_placesListFragment
            )
        }

        viewModel.data.observe(viewLifecycleOwner) {
            markers = it
            addMarkers()

            collection.showAll()
        }

        coords = arguments?.getParcelable(KEY_MARKER_LATLNG)
        isCanceled = arguments!!.getBoolean(KEY_CANCEL)
        println("isCanceled" + isCanceled)

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

            googleMap.setOnMapLongClickListener { latLang ->

                currentMarker = googleMap.addMarker {
                    position(latLang)
                }


                binding.createNewPoint.apply {
                    visibility = View.VISIBLE
                }

                binding.createNewPoint.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putParcelable(KEY_MARKER_LATLNG, latLang)
                    findNavController().navigate(
                        R.id.action_mapFragment_to_newPointFragment,
                        bundle
                    )
                }

            }

            val markerManager = MarkerManager(googleMap)
            collection = markerManager.newCollection()

            val startTarget = LatLng(55.751999, 37.617734)


            collection.showAll()


            googleMap.setOnMapClickListener { }

            collection.setOnInfoWindowClickListener {
                val bundle = Bundle()
                bundle.putString(KEY_MARKER_DESCRIPTION, it.snippet)
                bundle.putString(KEY_MARKER_TITLE, it.title)

                findNavController().navigate(
                    R.id.aboutPlaceFragment,
                    bundle
                )
            }

            googleMap.awaitAnimateCamera(
                CameraUpdateFactory.newCameraPosition(
                    cameraPosition {
                        var target = startTarget
                        coords?.let {
                            println("target is $target, place is ${it}")
                            target = it
                        }
                        target(target)
                        zoom(15F)
                    }
                )
            )
        }
    }

    override fun onMapReady(map: GoogleMap) {
        collection.setOnMarkerClickListener { marker ->
            marker.showInfoWindow()
            true
        }

        if (isCanceled) {
            map.apply {
                currentMarker?.remove()
            }
        }


    }


    override fun onInfoWindowClick(marker: Marker) {
        Toast.makeText(
            requireContext(),
            "infoWindowClicked",
            Toast.LENGTH_LONG
        ).show()
        val name = marker.title
        val description = marker.snippet

        val bundle = Bundle()
        bundle.putString(KEY_MARKER_TITLE, name)
        bundle.putString(KEY_MARKER_DESCRIPTION, description)

        findNavController().navigate(
            R.id.aboutPlaceFragment,
            bundle
        )
    }


    private fun addMarkers() {
        for (marker in markers) {
            collection.addMarker(
                MarkerOptions()
                    .position(marker.coordinates)
                    .title(marker.name)
                    .snippet(marker.description)
            )
        }
    }


}