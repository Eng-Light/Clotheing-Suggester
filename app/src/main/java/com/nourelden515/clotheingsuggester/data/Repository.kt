package com.nourelden515.clotheingsuggester.data

import android.graphics.drawable.Drawable
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import io.reactivex.rxjava3.core.Single

interface Repository {
    fun getWeatherData(
        lat: Float, lon: Float
    ): Single<WeatherResponse>

    fun saveLatLon(lat: Float, lon: Float)
    fun getLatLon(): Pair<Float?, Float?>
    fun clearLatLon()
    fun saveImageIndex(newImageIndex: Int)
    fun getImageIndex(): Int
    fun saveToday(today: Int)
    fun getLastViewedDay(): Int

    /**
     * returns outfit list either summer or winter depending on given temperature
     */
    fun getOutfits(temp: Int): List<Drawable>
    fun getSummerOutfits(): List<Drawable>
    fun getWinterOutfits(): List<Drawable>
}