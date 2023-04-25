package com.nourelden515.clotheingsuggester.data

import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.Response

interface Repository {
    fun getWeatherData(
        lat: Float, lon: Float,
        observable: (Observable<Response>) -> Unit
    )

    fun saveLatLon(lat: Float, lon: Float)
    fun getLatLon(): Pair<Float?, Float?>
    fun clearLatLon()
    fun saveImageIndex(newImageIndex: Int)
    fun getImageIndex(): Int
    fun saveToday(today: Int)
    fun getLastViewedDay(): Int
}