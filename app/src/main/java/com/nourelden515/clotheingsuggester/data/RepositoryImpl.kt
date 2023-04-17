package com.nourelden515.clotheingsuggester.data

import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import com.nourelden515.clotheingsuggester.data.source.RemoteDataSource
import com.nourelden515.clotheingsuggester.utils.SharedPreferencesUtils

class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val sharedPreferences: SharedPreferencesUtils
) : Repository {

    override fun getWeatherData(
        lat: Float,
        lon: Float,
        onSuccess: (response: WeatherResponse) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        remoteDataSource.getWeatherData(
            lat,
            lon,
            onSuccess,
            onFailure
        )
    }


    override fun saveLatLon(lat: Float, lon: Float) {
        sharedPreferences.saveLatLon(lat, lon)
    }

    override fun getLatLon(): Pair<Float?, Float?> {
        return sharedPreferences.getLatLon()
    }

}