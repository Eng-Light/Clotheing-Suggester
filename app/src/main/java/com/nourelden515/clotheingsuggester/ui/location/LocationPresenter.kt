package com.nourelden515.clotheingsuggester.ui.location

import com.nourelden515.clotheingsuggester.data.RepositoryImpl

class LocationPresenter(
    private val repository: RepositoryImpl,
    private val locationView: LocationView
) {
    fun saveLatLon(lat: Float, lon: Float) {
        repository.saveLatLon(lat, lon)
    }
    /**
     * You Can Use
     * @see locationView
     * As a reference to LocationView
     */
}