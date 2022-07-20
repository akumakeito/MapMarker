package com.netology.marker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.netology.marker.databinding.AboutPlaceCardBinding
import com.netology.marker.ui.MainActivity.Companion.KEY_MARKER_DESCRIPTION
import com.netology.marker.ui.MainActivity.Companion.KEY_MARKER_TITLE

import com.netology.marker.utils.StringArg
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AboutPlaceFragment : Fragment() {

    companion object {
        var Bundle.name: String? by StringArg
        var Bundle.description: String? by StringArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = AboutPlaceCardBinding.inflate(inflater, container, false)

        arguments?.name?.let(binding.name::setText)
        arguments?.description?.let(binding.description::setText)

        binding.apply {
            name.setText(arguments?.getString(KEY_MARKER_TITLE))
            description.setText(arguments?.getString(KEY_MARKER_DESCRIPTION))
        }

        return binding.root
    }

}