package com.nourelden515.clotheingsuggester.data.source.remote

import com.nourelden515.clotheingsuggester.BuildConfig
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import io.reactivex.rxjava3.core.Single

class RemoteDataSourceImpl : RemoteDataSource {
    override fun getWeatherData(
        lat: Float, lon: Float
    ): Single<WeatherResponse> {
        return WeatherAPI.weatherService
            .getWeatherData(BuildConfig.API_KEY, "${lat},${lon}")
    }
}