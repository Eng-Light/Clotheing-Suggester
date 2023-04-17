package com.nourelden515.clotheingsuggester.ui.home

import com.nourelden515.clotheingsuggester.data.RepositoryImpl
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse

class HomePresenter(
    private val repository: RepositoryImpl,
    private val homeView: HomeView
) {
    fun getWeatherData(lat: Double, lon: Double) {
        repository.getWeatherData(
            lat.toFloat(),
            lon.toFloat(),
            ::onGetWeatherDataSuccess,
            ::onGetWeatherDataFailure
        )
    }

    private fun onGetWeatherDataSuccess(response: WeatherResponse) {
        homeView.showWeatherData(response)
    }

    private fun onGetWeatherDataFailure(error: Throwable) {
        homeView.showError(error)
    }

    fun saveImageData(newImageIndex: Int, today: Int) {
        repository.saveImageIndex(newImageIndex)
        repository.saveToday(today)
    }

    fun getImageData() {
        homeView.getImageData(repository.getImageIndex(), repository.getLastViewedDay())
    }

}