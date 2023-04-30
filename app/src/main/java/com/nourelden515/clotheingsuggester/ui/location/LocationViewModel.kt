package com.nourelden515.clotheingsuggester.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nourelden515.clotheingsuggester.base.BaseViewModel
import com.nourelden515.clotheingsuggester.data.Repository

class LocationViewModel(private val repository: Repository) : BaseViewModel() {
    override val tag: String
        get() = "LocationViewModel"

    private val _location = MutableLiveData<Pair<Float?, Float?>>()
    val location: LiveData<Pair<Float?, Float?>>
        get() = _location

    val locationValid = MutableLiveData<Boolean>()

    fun getLatLon() {
        _location.value = repository.getLatLon()
        locationValid.value = (true)
    }

    fun onCompleteNavigation() {
        locationValid.value = false
    }

    fun saveLatLon(lat: Float, lon: Float) {
        repository.saveLatLon(lat, lon)
    }
}