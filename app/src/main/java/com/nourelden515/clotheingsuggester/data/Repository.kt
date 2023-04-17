package com.nourelden515.clotheingsuggester.data

import com.nourelden515.clotheingsuggester.data.models.WeatherResponse

interface Repository {
    fun getWeatherData(
        lat: Float, lon: Float,
        onSuccess: (response: WeatherResponse) -> Unit,
        onFailure: (error: Throwable) -> Unit
    )

    fun saveLatLon(lat: Float, lon: Float)
    fun getLatLon(): Pair<Float?, Float?>

    fun saveImageIndex(newImageIndex: Int)
    fun getImageIndex(): Int
    fun saveToday(today: Int)
    fun getLastViewedDay(): Int
}