package com.netology.marker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.netology.marker.R
import com.netology.marker.databinding.NewPointBinding
import com.netology.marker.ui.MainActivity.Companion.KEY_CANCEL
import com.netology.marker.ui.MainActivity.Companion.KEY_MARKER_LATLNG
import com.netology.marker.viewModel.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPointFragment : Fragment() {

    private val viewModel: PlaceViewModel by viewModels()

    private lateinit var binding: NewPointBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = NewPointBinding.inflate(inflater, container, false)

        val coords = arguments?.getParcelable<LatLng>(KEY_MARKER_LATLNG)!!

        binding.placeName.requestFocus()
        binding.placeCoordinates.text = coords.toString()

        binding.cancelBtn.setOnClickListener {
                val bundle = Bundle()
                bundle.putBoolean(KEY_CANCEL, true)
            findNavController().navigate(R.id.action_newPointFragment_to_mapFragment, bundle)
        }

        binding.saveBtn.setOnClickListener {
            val name = binding.placeName.text.toString()
            val description = binding.placeDescription.text.toString()

            if (binding.placeName.text.isNullOrBlank()) {
                Toast.makeText(
                    activity,
                    this.getString(R.string.error_blank_name),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.changePlace(
                    coords,
                    name,
                    description
                )
                viewModel.save()

                findNavController().navigateUp()
            }
        }
        return binding.root
    }
}

