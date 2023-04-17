package com.nourelden515.clotheingsuggester.data.source

import com.nourelden515.clotheingsuggester.data.models.WeatherResponse

interface RemoteDataSource {
    fun getWeatherData(
        lat: Float, lon: Float,
        onSuccess: (response: WeatherResponse) -> Unit,
        onFailure: (error: Throwable) -> Unit
    )
}