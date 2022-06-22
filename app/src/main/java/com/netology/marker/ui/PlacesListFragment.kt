package com.netology.marker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.netology.marker.R
import com.netology.marker.adapter.OnInteractionListener
import com.netology.marker.adapter.PlaceAdapter
import com.netology.marker.databinding.PlacesFragmentBinding
import com.netology.marker.dto.Place
import com.netology.marker.viewModel.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlacesListFragment : Fragment() {

    private val viewModel: PlaceViewModel by viewModels()
//
//    private val viewModel: PlaceViewModel by viewModels {
//        PlaceViewModel.providesFactory(
//            assistedFactory = placeViewModelFactory
//        )
//    }

    companion object {
        val KEY_ID = "id"
        val KEY_PLACE = "place"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = PlacesFragmentBinding.inflate(inflater, container, false)

        val adapter = PlaceAdapter(object : OnInteractionListener {
            override fun onEdit(place: Place) {
                println("edited value onEdit Places Fragment  state: " + lifecycle.currentState)

                viewModel.edit(place)
                val bundle = Bundle()
                bundle.putParcelable(KEY_PLACE, place)
                findNavController().navigate(R.id.action_placesListFragment_to_editPointFragment, bundle)
            }

            override fun onRemove(place: Place) {
                viewModel.removeById(place.id)
            }

            override fun onShow(place: Place) {
                val bundle = Bundle()
                bundle.putLong(KEY_ID, place.id)
                findNavController().navigate(R.id.action_placesListFragment_to_navigation, bundle)
            }
        })

        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { places ->
            adapter.submitList(places)
        }

        viewModel.edited.observe(viewLifecycleOwner,) { place ->
            println("edited value edited observe Places Fragment state: " + lifecycle.currentState)

            println("edited value placefragment" + viewModel.edited + viewModel.edited.value)

            if(place.id == 0L) {
                return@observe
            }
        }

        binding.showAll.setOnClickListener {
            findNavController().navigate(R.id.action_placesListFragment_to_navigation)
        }
        return binding.root
    }
}