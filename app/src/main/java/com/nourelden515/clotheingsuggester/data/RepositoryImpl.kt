package com.nourelden515.clotheingsuggester.data

import android.graphics.drawable.Drawable
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import com.nourelden515.clotheingsuggester.data.source.local.LocalDataSource
import com.nourelden515.clotheingsuggester.data.source.remote.RemoteDataSource
import com.nourelden515.clotheingsuggester.utils.shared.SharedPreferencesInterface
import io.reactivex.rxjava3.core.Single

class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val sharedPreferences: SharedPreferencesInterface
) : Repository {

    override fun getWeatherData(
        lat: Float,
        lon: Float
    ): Single<WeatherResponse> {
        return remoteDataSource.getWeatherData(
            lat,
            lon
        )
    }

    override fun saveLatLon(lat: Float, lon: Float) {
        sharedPreferences.saveLatLon(lat, lon)
    }

    override fun getLatLon(): Pair<Float?, Float?> {
        return sharedPreferences.getLatLon()
    }

    override fun clearLatLon() {
        sharedPreferences.clearLatLon()
    }

    override fun saveImageIndex(newImageIndex: Int) {
        sharedPreferences.saveImageIndex(newImageIndex)
    }

    override fun getImageIndex(): Int {
        return sharedPreferences.getImageIndex()!!
    }

    override fun saveToday(today: Int) {
        sharedPreferences.saveToday(today)
    }

    override fun getLastViewedDay(): Int {
        return sharedPreferences.getLastViewedDay()!!
    }

    override fun getSummerOutfits(): List<Drawable> {
        return localDataSource.getSummerOutfits()
    }

    override fun getWinterOutfits(): List<Drawable> {
        return localDataSource.getWinterOutfits()
    }

    override fun getOutfits(temp: Int): List<Drawable> {
        return if (temp >= 20) {
            getSummerOutfits()
        } else {
            getWinterOutfits()
        }
    }


}