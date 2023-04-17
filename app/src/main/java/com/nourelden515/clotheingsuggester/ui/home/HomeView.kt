package com.nourelden515.clotheingsuggester.ui.home

import com.nourelden515.clotheingsuggester.data.models.WeatherResponse

interface HomeView {
    fun showWeatherData(weatherData: WeatherResponse)
    fun showError(error: Throwable)
    fun getImageData(imageIndex: Int, lastViewedDay: Int)
}