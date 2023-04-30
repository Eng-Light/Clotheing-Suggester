package com.nourelden515.clotheingsuggester.data.source.remote

import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import io.reactivex.rxjava3.core.Single

interface RemoteDataSource {
    fun getWeatherData(
        lat: Float, lon: Float
    ): Single<WeatherResponse>
}