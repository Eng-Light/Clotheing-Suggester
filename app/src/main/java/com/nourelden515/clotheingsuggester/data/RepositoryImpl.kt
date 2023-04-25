package com.nourelden515.clotheingsuggester.data

import com.nourelden515.clotheingsuggester.data.source.RemoteDataSource
import com.nourelden515.clotheingsuggester.utils.shared.SharedPreferencesInterface
import io.reactivex.rxjava3.core.Single
import okhttp3.Response

class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val sharedPreferences: SharedPreferencesInterface
) : Repository {

    override fun getWeatherData(
        lat: Float,
        lon: Float
    ): Single<Response> {
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


}