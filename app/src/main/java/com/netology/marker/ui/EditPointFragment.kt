package com.netology.marker.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.netology.marker.R
import com.netology.marker.databinding.EditPointBinding
import com.netology.marker.dto.Place
import com.netology.marker.ui.MainActivity.Companion.KEY_PLACE
import com.netology.marker.utils.ParcelableArg
import com.netology.marker.viewModel.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPointFragment : Fragment() {

    companion object {
        var Bundle.latLng: Parcelable? by ParcelableArg


    }

    private val viewModel by activityViewModels<PlaceViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = EditPointBinding.inflate(inflater, container, false)
        val place = arguments?.getParcelable<Place>(KEY_PLACE)

        binding.placeName.requestFocus()

        place?.let {
            binding.apply {
                placeName.setText(it.name)
                placeDescription.setText(it.description)
            }

            binding.placeCoordinates.setText(it.coordinates.toString())
        }

        binding.cancelBtn.setOnClickListener {
            //    val bundle = Bundle()
            //    bundle.putString(KEY_CANCEL, "cancel")
            findNavController().navigateUp()
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
                    place!!.coordinates,
                    name,
                    description
                )
                viewModel.save()
            }

            findNavController().navigateUp()
        }

        return binding.root
    }

}

