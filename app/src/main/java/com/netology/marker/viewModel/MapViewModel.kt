package com.netology.marker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.netology.marker.dto.Place
import com.netology.marker.repository.PlaceRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class MapViewModel @AssistedInject
constructor(
    private val repository: PlaceRepository
) : ViewModel() {

    @AssistedFactory
    interface MapViewModelFactory{
        fun create() : MapViewModel
    }

    companion object {
        fun providesFactory(
            assistedFactory: MapViewModelFactory,
        ) : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create() as T
            }
        }
    }

    val data = repository.data.asLiveData()

}